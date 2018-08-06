package com.wxstc.streaming

//import com.wxstc.util.LoggerLevels
import com.wxstc.util.JDBCUtils
import org.apache.log4j.Level
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingWordCount {
  val updateFunc = (iterator: Iterator[(String, Seq[Int], Option[Int])]) => {
    iterator.flatMap{case(x,y,z)=> Some(y.sum + z.getOrElse(0)).map(n=>(x, n))}
  }
  def main(args: Array[String]): Unit = {
//    LoggerLevels.setStreamingLogLevels(Level.WARN)
    val conf = new SparkConf().setAppName("StreamingWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(5))
    ssc.checkpoint("F:\\_vm\\ck")
    //socket 接收数据 127.0.0.1
    val ds = ssc.socketTextStream("127.0.0.1",8888)
    val resDS = ds.flatMap(_.split(" ")).map((_,1))//.reduceByKey(_+_)
    val result = resDS.updateStateByKey(updateFunc, new HashPartitioner(ssc.sparkContext.defaultParallelism), true)
    resDS.print()
    println(resDS)
    ssc.start()
    ssc.awaitTermination()
  }
}
