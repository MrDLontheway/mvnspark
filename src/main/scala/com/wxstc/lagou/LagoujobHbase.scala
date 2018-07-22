package com.wxstc.lagou

import com.wxstc.bean.LaGouJob
import com.wxstc.util.JsonUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object LagoujobHbase {
  val conf = new SparkConf().setAppName("order").setMaster("local[*]")
  //设置笛卡尔乘积 spark 2.1.1 之后已经解决了这个问题
  conf.set("spark.sql.crossJoin.enabled", "true")
  conf.set("spark.dynamicAllocation.enabled", "false")
  val spark = SparkSession.builder().config(conf)//.enableHiveSupport()
    .config("es.nodes", "47.94.196.195")
    //HTTP默认端口为9200
    .config("es.port", "9200")
    .config("es.index.auto.create", "true")
    .config("pushdown", "true")
    .config("es.nodes.wan.only", "true")
    .config("es.mapping.date.rich", "false")
    //.config("dfs.client.use.datanode.hostname", "true")
    .getOrCreate()
  //创建ucParser对象，用来调用碰撞方法
  //val ucParser = new ParseUc
  spark.sparkContext.setLogLevel("WARN")

  def lagoujobToHbase()={
    val lgjob = spark.sparkContext.textFile("hdfs://localhost:9000/lagoujob")
    println(lgjob.count())

    val data = lgjob.map(x=>{
      val row = JsonUtils.jsonToPojo(x,classOf[LaGouJob])
      if (!row.isEmpty) {
        row.get
      }
      row
    }).filter(_.isInstanceOf[LaGouJob]).map(x=>{
      val row = x.asInstanceOf[LaGouJob]
      row
      LaGouJobC(row.id,row.job_name,row.salary,row.address,
        row.experience,row.education,row.job_type,row.label,
        row.date,row.job_desc,row.commany,row.commany_jianjie,row.job_youhuo)
    })
//    val schema = StructType(
//      Seq(
//        StructField("name",StringType,true)
//        ,StructField("age",IntegerType,true)
//      )
//    )
    import spark.implicits._
    val df = data.toDF()
    println(df.head(10))
    //    val df = data.toDF("id",
//      "job_name",
//      "salary",
//      "address",
//      "experience",
//      "education",
//      "job_type",
//      "label",
//      "date",
//      "job_desc",
//      "commany",
//      "commany_jianji",
//      "job_youhuo")
//    println(df.head(10))
//
  }

  def main(args: Array[String]): Unit = {
    //    Superclass_school.getSchoolDF()
    //    Superclass_school2.getSchoolDF()
    LagoujobHbase.lagoujobToHbase()
  }
  case class LaGouJobC(
                        id:Long,
                        job_name:String,
                        salary:String,
                        address:String,
                        experience:String,
                        education:String,
                        job_type:String,
                        label:String,
                        date:String,
                        job_desc:String,
                        commany:String,
                        commany_jianjie:String,
                        job_youhuo:String
                      )
}
