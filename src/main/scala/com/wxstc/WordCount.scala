package com.wxstc

import com.wxstc.bean.LaGouJob
import com.wxstc.es.Superclass_school.spark
import com.wxstc.util.JsonUtils
import org.apache.spark.{SparkConf, SparkContext}
//
object WordCount {
  def main(args: Array[String]) {
    System.setProperty("HADOOP_USER_NAME", "root")
    //创建SparkConf()并设置App名称 .setMaster("local[3]")
    val conf = new SparkConf().setAppName("lagoujob")//.setMaster("spark://47.94.196.195:7077")
      .set("spark.executor.memory", "512m")//.setJars(Array("C:\\Users\\L\\Desktop\\mayun\\mvnspark\\target\\mvn-spark-1.0-SNAPSHOT.jar"))
    //创建SparkContext，该对象是提交spark App的入口
    val sc = new SparkContext(conf)
    //使用sc创建RDD并执行相应的transformation和action
    //获取基站数据
    //val rdd1 = sc.parallelize(Seq(0,2,1,5,6,8),3)
    val rdd1 = sc.textFile("hdfs://47.94.196.195:9000/lagoujob")//.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_, 1).sortBy(_._2, false)
    //val rdd_job = sc.textFile("hdfs://hadoop01:9000/lagou/job")
    val rdd2 = rdd1.map(x => {
        val job = JsonUtils.jsonToPojo(x, classOf[LaGouJob])
        if (!job.isEmpty) {
          job.get
        }
      })

    val job_data = rdd2.filter(_.isInstanceOf[LaGouJob]).map(x=>{
      val job = x.asInstanceOf[LaGouJob]
      //      if(job.address==null)
      //        job.address="暂无"
      //      if(job.commany==null)
      //        job.commany="暂无"
      //      if (job.commany_jianjie==null)
      //        job.commany_jianjie="暂无"
      job.id+","+job.job_name+","+job.salary+","+job.address+","+job.experience+","+job.education+","+job.job_type+","+
      job.label+","+job.date+","+job.job_desc+","+job.commany+","+job.commany_jianjie+","+job.job_youhuo
    })

    //println(job_data.collect().toBuffer)
    job_data.saveAsTextFile("hdfs://47.94.196.195:9000/lagoujobtxt/")
    //    val rdd1 = sc.textFile("D:\\txt").flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_, 1).sortBy(_._2, false)
    /*var b = scala.collection.mutable.Map(1->"a",2->"b")
    var bro = sc.broadcast(b)
    b += (3->"c")

    val mydata1 =  spark.sparkContext.parallelize(Seq((1->"heiheia"),(2->"lallal"),(3->"lelelleee"),(4,"ooooo")),3)
    val mydata =  spark.sparkContext.parallelize(Seq((1->"heiheia"),(2->"lallal"),(3->"lelelleee"),(9999999,"ooooo")),3)
d
    val res2 = mydata1.zipWithUniqueId()
    val res = mydata1.join(mydata,3)*/


    //println(res2.collect().toBuffer)
    //sc.wait(100000)
    //rdd1.cache()
    //rdd1.collect()
      //.saveAsTextFile(args(1))
    //rdd1.toDebugString
    //rdd1.unpersist(true)
    //println(rdd1.collect().toBuffer)
    //停止sc，结束该任务
    //sc.stop()
  }

}