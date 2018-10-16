package com

import org.apache.spark.SparkConf

object YarnTest {
  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")
    //创建SparkConf()并设置App名称 .setMaster("local[3]")
//    val conf = new SparkConf().setAppName("yarntst").setMaster("yarn-client")
    val res = test1()
    println(res)
  }

  def test1(): Int ={
    val i = 0;
    if(i==0){
      10
    }
    20
  }
}
