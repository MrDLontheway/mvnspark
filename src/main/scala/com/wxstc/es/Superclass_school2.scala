package com.wxstc.es

import com.wxstc.util.JDBCUtils
import org.apache.spark.SparkConf
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.sql.SparkSession

object Superclass_school2 {
  val conf = new SparkConf().setAppName("order2").setMaster("local[*]")
  //设置笛卡尔乘积 spark 2.1.1 之后已经解决了这个问题
  conf.set("spark.sql.crossJoin.enabled", "true")
  conf.set("spark.dynamicAllocation.enabled", "false")
  val spark = SparkSession.builder().config(conf)//.enableHiveSupport()
    .config("es.nodes", "10.100.61.61")
    //HTTP默认端口为9200
    .config("es.port", "9200")
    .config("es.index.auto.create", "true")
    .config("pushdown", "true")
    .config("es.nodes.wan.only", "true")
    .config("es.mapping.date.rich", "false")
    .getOrCreate()
  //创建ucParser对象，用来调用碰撞方法
  //val ucParser = new ParseUc
  spark.sparkContext.setLogLevel("WARN")

  import org.elasticsearch.spark._
  import spark.implicits._
  import spark.sql
  def getSchoolDF()={
    val school = new JdbcRDD(spark.sparkContext,JDBCUtils.getConnection,"select * from school_count where ? = ?",
      1,1,3,x=>{
        val id = x.getInt("id")
        val number  = x.getLong("number")
        val schoolName = x.getString("schoolName")
        val province  = x.getString("province")
        val level = x.getString("level")
        val schoolnature  = x.getString("schoolnature")
        val guanwang = x.getString("guanwang")
        SchoolInfo(id,number,schoolName,province,level,schoolnature,guanwang)
      })
    //school.saveToEs("test/school")
    school.toDF().createOrReplaceTempView("school")
    val df2 = sql(
      """
        |select * from school where level = '本科'
      """.stripMargin)
    println(df2.show(10))
//    val df = sql(
//      """
//        |select VEHICLE_CODE_ID,
//        |       PRODUCT_CODE,
//        |       MODEL_YEAR,
//        |       model_designator
//        |from ldd.TM_MV_VEHICLE_CODE_example
//      """.stripMargin)//.createOrReplaceTempView("tmvc")W
//    df.show(10)

//    val schoolDF = sql(
//      """
//          |select csddh.number,
//          |       csddh.quantity,
//          |       csddh.create_time,
//          |       csddh.date,
//          |       csddh.unit,
//          |       csddh.material,
//          |       cbm.material_code,
//          |       cbm.material_name,
//          |       cbm.supplier_code,
//          |       cbm.supplier_name
//          |from ldd.c2b_sub_demand_detail_hive csddh
//          |inner join (
//          |select *
//          |from ldd.c2b_bom_mapping
//          |where head_material_code=''
//          |) cbm
//          |on csddh.material = cbm.parent_material_code
//        """.stripMargin)
  }
  case class SchoolInfo(id:Int,number:Long,schoolName:String,province:String,level:String,schoolnature:String,guanwang:String)
  def main(args: Array[String]): Unit = {
    Superclass_school.getSchoolDF()
  }
}
