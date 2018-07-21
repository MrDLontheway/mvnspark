package com.wxstc.lagou

import java.util.Properties

import com.wxstc.bean.{LaGouCommany, LaGouJob}
import com.wxstc.util.{BeanUtil, JDBCUtils, JsonUtils}
import org.apache.log4j.Level
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.types._

import scala.collection.mutable.Set

object LaGou {
  def main(args: Array[String]): Unit = {
//    LoggerLevels.setStreamingLogLevels(Level.WARN)
    val beanUtil = new BeanUtil
    val conf = new SparkConf().setAppName("LaGou")//.setMaster("spark://hadoop01:7077")
      //.setJars(Array("C:\\Users\\L\\Desktop\\mayun\\mvnspark\\target\\mvn-spark-1.0-SNAPSHOT.jar"))//.setMaster("local[*]")
      //.setMaster("spark://hadoop01:7077")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "root")
    prop.put("sqlUrl","jdbc:mysql://hadoop01:3306/superclass?useUnicode=true&characterEncoding=UTF-8")
    //拉钩公司信息分析
    //companyAnalysis(sc,sqlContext,prop)

    //拉钩职位分析
    jobAnalysis(sc,sqlContext,prop)
    sc.stop()
  }


  def companyAnalysis(sc: SparkContext,sqlContext: SQLContext,prop: Properties): Unit ={
    val rdd = sc.textFile("hdfs://hadoop01:9000/lagou/company")
      .map(x => {
        val company = JsonUtils.jsonToPojo(x, classOf[LaGouCommany])
        if (!company.isEmpty) {
          company.get
        }
      })
    //数据清洗
    val rdd1 = rdd.filter(_.isInstanceOf[LaGouCommany]).map(x=>{
      val company = x.asInstanceOf[LaGouCommany]
      if(company.commany_area==null)
        company.commany_area="暂无"
      if(company.address==null)
        company.address="暂无"
      if (company.rongzi==null)
        company.rongzi="暂无"
      if (company.commany_member==null)
        company.commany_member="暂无"
      println(company.toString)
      company
    })

    rdd1.persist()
    //所有company数量
    //val companyNumber = rdd1.count()
    //println("--------------------------------------------------"+companyNumber)
    //------------------------开始company业务分析
    //根据不同省份分类
    val rdd2 = rdd1.groupBy(_.address).map(x => {
      (x._1, x._2.size)
    }).map(x=>{
      Row(x._1,1,x._2)
    })

    //根据领域分类
    val rdd3 = rdd1.flatMap(x => {
      if (x.commany_area == null) {
        Array("null")
      }else {
        x.commany_area=x.commany_area.replace(" ",",")
        x.commany_area=x.commany_area.replace("、",",")
        x.commany_area.split(",").flatMap(_.split(" ").map(_.trim))
      }
    }
    ).map((_, 1)).reduceByKey(_ + _).map(x=>{
      Row(x._1,2,x._2)
    })
    //根据融资情况分类
    val rdd4 = rdd1.groupBy(_.rongzi.replace(" ","")).map(x => {
      (x._1, x._2.size)
    }).map(x=>{
      Row(x._1,3,x._2)
    })
    //根据公司规模情况分类
    val rdd5 = rdd1.groupBy(_.commany_member).map(x => {
      (x._1, x._2.size)
    }).map(x=>{
      Row(x._1,4,x._2)
    })

    rdd1.unpersist()

    val result = rdd2++rdd3++rdd4++rdd5
    //val partionsnum = rdd1.groupBy(_.address).count()
    val schema  = StructType(StructField("name", StringType, true)::StructField("type", IntegerType, true)::StructField("value", IntegerType, true)::Nil)
    val result_df = sqlContext.createDataFrame(result,schema)
    result_df.write.mode("overwrite").jdbc(prop.getProperty("sqlUrl"), "superclass.lagou_companycate", prop)
  }

  def jobAnalysis(sc: SparkContext,sqlContext: SQLContext,prop: Properties): Unit ={
    val rdd_job = sc.textFile("hdfs://hadoop01:9000/lagou/job")
      .map(x => {
        val job = JsonUtils.jsonToPojo(x, classOf[LaGouJob])
        if (!job.isEmpty) {
          job.get
        }
      })

    val job_data = rdd_job.filter(_.isInstanceOf[LaGouJob]).map(x=>{
      val job = x.asInstanceOf[LaGouJob]
      //      if(job.address==null)
      //        job.address="暂无"
      //      if(job.commany==null)
      //        job.commany="暂无"
      //      if (job.commany_jianjie==null)
      //        job.commany_jianjie="暂无"
      job
    })

    job_data.persist()
    //val companyNumber = job_data.count()
    //println("job--------------------------------------------------"+companyNumber)
    //------------------------开始job业务分析
    val job_rdd1 = job_data.map(x=>{
      (x.address,1)
    }).reduceByKey(_+_).map((x=>{
            Row(x._1,1,x._2)
          }))

    val job_rdd2 = job_data.map(x=>{
      (x.experience,1)
    }).reduceByKey(_+_).map(x=>{
      Row(x._1,2,x._2)
    })

    val job_rdd3 = job_data.map(x=>{
      (x.salary,1)
    }).reduceByKey(_+_).map(x=>{
      Row(x._1,3,x._2)
    })

    val job_rdd4 = job_data.map(x=>{
      (x.job_type,1)
    }).reduceByKey(_+_).map(x=>{
      Row(x._1,4,x._2)
    })

    val job_rdd5 = job_data.map(x=>{
      (x.education,1)
    }).reduceByKey(_+_).map(x=>{
      Row(x._1,5,x._2)
    })

    job_data.unpersist()
  //数据聚合
    val result = job_rdd1++job_rdd2++job_rdd3++job_rdd4++job_rdd5
    val schema  = StructType(StructField("name", StringType, true)::StructField("type", IntegerType, true)::StructField("value", IntegerType, true)::Nil)
    val result_df = sqlContext.createDataFrame(result,schema)
    result_df.write.mode("overwrite").jdbc(prop.getProperty("sqlUrl"), "superclass.lagou_job", prop)
  }
}

