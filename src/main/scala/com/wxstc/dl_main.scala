package com.wxstc

import com.wxstc.es.{Superclass_school, Superclass_school2}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * 任务提交主入口
  */
object dl_main {
  def main(args: Array[String]): Unit = {
    Superclass_school.getSchoolDF()
    Superclass_school2.getSchoolDF()
  }

}
