package com.wxstc.superclass

import java.io.Serializable
import java.util.Properties

import com.wxstc.util.{JDBCUtils, JsonUtils}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.types._

object SuperClass {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SuperClass").setMaster("local[*]")
    //"spark://hadoop01:7077")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
//    val rdd = sc.textFile("F:\\SuperClassUserData\\input2")
    val rdd = sc.textFile("hdfs://hadoop01:9000/superclass")
      .map(x => {
      val user = JsonUtils.jsonToPojo(x, classOf[SuperClassUserApi])
      if (!user.isEmpty) {
        user.get.data
      }
    })
    //数据清洗
    val rdd1 = rdd.filter(_.isInstanceOf[SuperClassUser]).map(_.asInstanceOf[SuperClassUser])
    val rows = List(
      StructField("avatarUrl", StringType, true),
      StructField("backgroundImage", StringType, true),
      StructField("studentId", IntegerType, true),
      StructField("bigAvatarUrl", StringType, true),
      StructField("bornDate", LongType, true),
      StructField("certificationType", LongType, true),
      StructField("eachFriendNum", LongType, true),
      StructField("fansNum", LongType, true),
      StructField("friendNum", LongType, true),
      StructField("fullAvatarUrl", StringType, true),
      StructField("gender", IntegerType, true),
      StructField("hasBbsBool", BooleanType, true),
      StructField("isCelebrity", IntegerType, true),
      StructField("isHideByMe", IntegerType, true),
      StructField("nickName", StringType, true),
      StructField("pinyinStr", StringType, true),
      StructField("publishType", IntegerType, true),
      StructField("purviewValue", IntegerType, true),
      StructField("rate", IntegerType, true),
      StructField("schoolName", StringType, true),
      StructField("showRate", BooleanType, true),
      StructField("signature", StringType, true),
      StructField("studentType", IntegerType, true),
      StructField("todayVisit", LongType, true),
      StructField("totalVisit", LongType, true),
      StructField("vipLevel", IntegerType, true)
    )
    //通过StructType直接指定每个字段的schema
    val schema = StructType(rows)
    //将RDD映射到rowRDD
    rows.map(_.name)
    val rowRDD = rdd1.map(p => Row(
      p.avatarUrl,
      p.backgroundImage,
      p.studentId,
      p.bigAvatarUrl,
      p.bornDate,
      p.certificationType,
      p.eachFriendNum,
      p.fansNum,
      p.friendNum,
      p.fullAvatarUrl,
      p.gender,
      p.hasBbsBool,
      p.isCelebrity,
      p.isHideByMe,
      p.nickName,
      p.pinyinStr,
      p.publishType,
      p.purviewValue,
      p.rate,
      p.schoolName,
      p.showRate,
      p.signature,
      p.studentType,
      p.todayVisit,
      p.totalVisit,
      p.vipLevel
    ))
    //将schema信息应用到rowRDD上
    val personDataFrame = sqlContext.createDataFrame(rowRDD, schema)
    //    import sqlContext.implicits._
    //    val userDF = rdd1.toDF
    personDataFrame.registerTempTable("superclass_user")

    //计算每个学校用户人数
    val group_school = sqlContext.sql("select count(1) as number,schoolName from superclass_user GROUP BY schoolName")

    //计算平台总用户数
/*    val allCount = sqlContext.sql("select count(1) as allCount from superclass_user")
    //女用户数
    val girlCount = sqlContext.sql("select count(1) as nan from superclass_user where gender=0")
    //男用户数
    val manCount = sqlContext.sql("select count(1) as nan from superclass_user where gender=1")
    allCount.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/usercount/allCount.text")
    girlCount.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/usercount/girlCount.text")
    manCount.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/usercount/manCount.text")
    //val userCount = new DataFrame()

    //group_school.show()

    //计算用户活跃度  1级为僵尸用户  1-5 一般用户  5级以上为高级用户
    val jiangs = sqlContext.sql("select count(1) as jiangshi from superclass_user where rate=1")
    jiangs.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/userRate/jiangshi.text")
    val yiban = sqlContext.sql("select count(1) as jiangshi from superclass_user where rate<=5")
    yiban.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/userRate/yiban.text")
    val gaoji = sqlContext.sql("select count(1) as jiangshi from superclass_user where rate>=5")
    gaoji.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/userRate/gaoji.text")
    // 总访问 1-50 一般活跃  50-100 比较活跃  大于100 为非常活跃用户
    val ybhuoyue = sqlContext.sql("select count(1) as jiangshi from superclass_user where totalVisit>=0 and totalVisit<50")
    ybhuoyue.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/userLive/ybhuoyue.text")
    val bjhuoyue = sqlContext.sql("select count(1) as jiangshi from superclass_user where totalVisit>=50 and totalVisit<100")
    bjhuoyue.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/userLive/bjhuoyue.text")
    val fchuoyue = sqlContext.sql("select count(1) as jiangshi from superclass_user where totalVisit>=100")
    fchuoyue.toJSON.saveAsTextFile("hdfs://hadoop01:9000/superclass/userLive/fchuoyue.text")*/

    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "root")
    //将数据追加到数据库
    group_school.write.mode("overwrite").jdbc("jdbc:mysql://localhost:3306/superclass", "superclass.school_count", prop)
    //停止sc，结束该任务
    sc.stop()
  }

  def saveCountToMQ(allcount: Long, girlCount: Long, manCount: Long): Unit = {
    val conn = JDBCUtils.getConnection
    JDBCUtils.delete("user_distribution")
    val res = conn.prepareStatement("insert into user_distribution(all_count,girl_count,man_count) values(?,?,?)")
    res.setLong(1, allcount)
    res.setLong(2, girlCount)
    res.setLong(3, manCount)
    res.execute()
    JDBCUtils.close()
  }
}

//case class SuperClassUser{
//  var avatarUrl: String = null
//  var backgroundImage: String = null
//  var studentId = 0
//  var bigAvatarUrl: String = null
//  var bornDate = 0L
//  var certificationType = 0L
//  var eachFriendNum = 0L
//  var fansNum = 0L
//  var friendNum = 0L
//  var fullAvatarUrl: String = null
//  var gender = 0
//  var hasBbsBool = false
//  var isCelebrity = 0
//  var isHideByMe = 0
//  var nickName: String = null
//  var pinyinStr: String = null
//  var publishType = 0
//  var purviewValue = 0
//  var rate = 0
//  var schoolName: String = null
//  var showRate = false
//  var signature: String = null
//  var studentType = 0
//  var todayVisit = 0L
//  var totalVisit = 0L
//  var vipLevel = 0
//}
