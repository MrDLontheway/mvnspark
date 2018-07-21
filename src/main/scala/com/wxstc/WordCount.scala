package com.wxstc

import org.apache.spark.{SparkContext, SparkConf}

object WordCount {
  def main(args: Array[String]) {
    //创建SparkConf()并设置App名称
    val conf = new SparkConf().setAppName("WordCount-Debug").setMaster("local[2]")//.setMaster("spark://hadoop01:7077")//.setJars(Array("C:\\Users\\L\\Desktop\\mayun\\mvnspark\\target\\mvn-spark-1.0-SNAPSHOT.jar"))
    //创建SparkContext，该对象是提交spark App的入口
    val sc = new SparkContext(conf)
    //使用sc创建RDD并执行相应的transformation和action
    //获取基站数据
//    val rdd1 = sc.parallelize(Seq(0,2,1),1)
//    val rdd1 = sc.textFile("hdfs://hadoop01:9000/wordcount").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_, 1).sortBy(_._2, false)
    val rdd1 = sc.textFile("D:\\txt").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_, 1).sortBy(_._2, false)

    rdd1.cache()
    rdd1.collect()
      //.saveAsTextFile(args(1))
    rdd1.toDebugString
    rdd1.unpersist(true)
    println(rdd1.collect().toBuffer)
    //停止sc，结束该任务
    sc.stop()
  }
}