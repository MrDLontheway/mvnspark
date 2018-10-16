package com.wxstc.streaming

import java.text.SimpleDateFormat
import java.util.Date

import com.wxstc.bean.Danmaku
import com.wxstc.redis.JedisSingle
import com.wxstc.util.{IKUtils, JsonUtils}
import org.apache.log4j.Level
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

object DyDanMu {
  val updateFunc = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    iter.flatMap { case (x, y, z) => Some(y.sum + z.getOrElse(0)).map(i => (x, i)) }
  }


  def main(args: Array[String]): Unit = {
//    LoggerLevels.setStreamingLogLevels(Level.WARN)
    val Array(zkQuorum, group, topics, numThreads) = args
    val conf = new SparkConf().setAppName("dy_danmuCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))
    //TODO
    ssc.checkpoint("F:\\_vm\\ckdy")
    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val data = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap, StorageLevel.MEMORY_AND_DISK_SER)
    //data.print()
    //将json数据转成bean 然后进行数据过滤  ik分词 计数
    val words = data.map(x => {
      JsonUtils.jsonToPojo(x._2, classOf[Danmaku])
    })
      .filter(!_.isEmpty)
      .map(_.get.getContent).flatMap(IKUtils.ikAny(_)).filter(_.length > 2).map((_, 1))

    val wordCounts_shi = words.reduceByKeyAndWindow((v1: Int, v2: Int) => v1 + v2, Seconds(60 * 1), Seconds(5)).transform(
      rdd => {
        val rddn = rdd.sortBy(_._2, false)
        if (rddn.take(10).size > 0) {
          val jedis = JedisSingle.jedisPool.getResource
          jedis.set("dy_danmu_wordsWindow", JsonUtils.objectToJson(rddn.take(10)))
          jedis.close()
        }
        rddn
      }
    )
    //与以往数据迭代聚合
    val wordCounts = words.updateStateByKey(updateFunc, new HashPartitioner(ssc.sparkContext.defaultParallelism), true)
    //对数据结果排序
    val sortResult = wordCounts.transform(rdd => {
      val rddn = rdd.sortBy(_._2, false)
      if (rddn.take(10).size > 0) {
        val jedis = JedisSingle.jedisPool.getResource
        jedis.set("dy_danmu_words", JsonUtils.objectToJson(rddn.take(10)))
        jedis.close()
      }
      rddn
    })
    //sortResult.print()

    //对添加的直播间活跃度进行弹幕总计数 排序
    val live_danmuCountshi = data.map(x => {
      (x._1, 1)
    }).reduceByKeyAndWindow((v1: Int, v2: Int) => v1 + v2, Seconds(60 * 1), Seconds(5)).transform(
      rdd => {
        val rddn = rdd.sortBy(_._2, false)
        if (rddn.take(10).size > 0) {
          val jedis = JedisSingle.jedisPool.getResource
          jedis.set("dy_danmu_roomWindow", JsonUtils.objectToJson(rddn.take(10)))
          jedis.close()
        }
        rddn
      }
    )
    val live_danmuCount = data.map(x => {
      (x._1, 1)
    }).updateStateByKey(updateFunc, new HashPartitioner(ssc.sparkContext.defaultParallelism), true).transform(rdd => {
      val rddn = rdd.sortBy(_._2, false)
      val result = rddn.take(10).toMap
      if (result.size > 0) {
        val jedis = JedisSingle.jedisPool.getResource
        jedis.set("dy_danmu_room", JsonUtils.objectToJson(rddn.take(10)))
        jedis.close()
      }
      rddn
    })

    val user_liveshi = data.map(x => {
      JsonUtils.jsonToPojo(x._2, classOf[Danmaku])
    })
      .filter(!_.isEmpty)
      .map(x => {
        (x.get.getSnick, 1)
      }).reduceByKeyAndWindow((v1: Int, v2: Int) => v1 + v2, Seconds(60 * 1), Seconds(5)).transform(
      rdd => {
        val rddn = rdd.sortBy(_._2, false)
        if (rddn.take(10).size > 0) {
          val jedis = JedisSingle.jedisPool.getResource
          jedis.set("dy_danmu_liveUserWindow", JsonUtils.objectToJson(rddn.take(10)))
          jedis.close()
        }
        rddn
      }
    )
    //对用户活跃度 （弹幕总数）排序
    val user_live = data.map(x => {
      JsonUtils.jsonToPojo(x._2, classOf[Danmaku])
    })
      .filter(!_.isEmpty)
      .map(x => {
        (x.get.getSnick, 1)
      }).updateStateByKey(updateFunc, new HashPartitioner(ssc.sparkContext.defaultParallelism), true).transform(rdd => {
      val rddn = rdd.sortBy(_._2, false)
      val result = rddn.take(10).toMap
      if (result.size > 0) {
        val jedis = JedisSingle.jedisPool.getResource
        jedis.set("dy_danmu_liveUser", JsonUtils.objectToJson(rddn.take(10)))
        jedis.close()
      }
      rddn
    })

    var da: DStream[(String, (Int, Int))] = data.map(x => {
      JsonUtils.jsonToPojo(x._2, classOf[Danmaku])
    })
      .filter(!_.isEmpty)
      .map(x => {
        (x.get.getSnick, (1,1))
      })
         .updateStateByKey[Tuple2[Int,Int]]((values: Seq[Tuple2[Int, Int]], state: Option[Tuple2[Int, Int]]) => {

      val now: Date = new Date()
      val dateFormat: SimpleDateFormat = new SimpleDateFormat("HH:mm")
      val date = dateFormat.format(now)


      var newValue = state.getOrElse(Tuple2[Int, Int](0, 0))
//      if(date.equals("17:37")){
//        newValue = Tuple2[Int, Int](0, 0)
//      }
      var up = newValue._1
      var down = newValue._2
      for (value <- values) {
        up += value._1
        down += value._2
      }
      date match {
        case "17:37" => Option[Tuple2[Int,Int]](0, 0)
        case _ => Option[Tuple2[Int,Int]](up, down)
      }
    })


    sortResult.print(1)
    live_danmuCount.print(1)
    user_live.print(1)
    wordCounts_shi.print(1)
    user_liveshi.print(1)
    live_danmuCountshi.print(1)
    ssc.start()
    ssc.awaitTermination()
  }
}
