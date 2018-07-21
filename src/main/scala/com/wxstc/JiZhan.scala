package com.wxstc

import java.text.SimpleDateFormat

import com.wxstc.util.JDBCUtils
import org.apache.spark.{SparkConf, SparkContext}

object JiZhan {
  def main(args: Array[String]) {
    val df = new SimpleDateFormat("YYYYMMddHHmmss")

    //创建SparkConf()并设置App名称
    val conf = new SparkConf().setAppName("JZ").setMaster("local[2]")
    //[2] 本地启动线程数
    //创建SparkContext，该对象是提交spark App的入口
    val sc = new SparkContext(conf)
    //使用sc创建RDD并执行相应的transformation和action
    //获取基站数据
    val rddJizhan = sc.textFile("F:\\jizhan\\loc_info.txt").map(_.split(",")).map((x => (x(0), (x(1), x(2)))))
    val rdd1 = sc.textFile("F:\\jizhan\\user_data").map(_.split(",")).map(x => {
      //（手机号_基站ID，时间，事件类型）
      (x(0) + "_" + x(2), df.parse(x(1)).getTime, x(3))
      //按 手机号_基站ID 分组
    }).groupBy(_._1)
      .mapValues(_.map(
        //建立连接基站的时间设置为负的
        x => if (x._3.toInt == 0) x._2.toLong else -x._2.toLong
      )).mapValues(_.sum)
      .groupBy(_._1.split("_")(0)).map { case (k, v) => {
      //分组后二次排序
      (k, v.toList.sortBy(_._2).reverse(0))
    }
    }.map(t => (t._1, t._2._1.split("_")(1), t._2._2))
    //获取用户访问数据
    val user = sc.textFile("F:\\jizhan\\user_data").map(_.split(","))
      .map(x => if (x(3).toInt == 0) (x(0) + "_" + x(2), df.parse(x(1)).getTime, x(3)) else (x(0) + x(2), -df.parse(x(1)).getTime, x(3)))
      .groupBy(x => x._1)
      .collect()
    val res = rdd1.map(x => (x._2, (x._1, x._3))).join(rddJizhan).map(x => (x._2._1._1, x._1, x._2._2._1, x._2._2._2))
    res.foreachPartition(it=>{
      val conn = JDBCUtils.getConnection()
      while(it.hasNext){
        val raw = it.next()
        println(raw)
//        val res = conn.prepareStatement("insert into user(phone,lon,lat,lac) values(?,?,?,?)")
//        res.setString(1,raw._1)
//        res.setString(2,raw._3)
//        res.setString(3,raw._4)
//        res.setString(4,raw._2)
//        res.execute()
      }
      JDBCUtils.close()
    })
    //停止sc，结束该任务
    sc.stop()
  }
}
