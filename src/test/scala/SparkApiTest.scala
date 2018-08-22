import java.text.SimpleDateFormat
import java.util.Date

import com.wxstc.lagou.LagoujobHbase.spark
import com.wxstc.mypartitioner.MyPartitioner
import com.wxstc.util.JDBCUtils
import org.apache.spark.{HashPartitioner, SparkConf, SparkException}
import org.apache.spark.sql.{SparkSession, functions}
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object SparkApiTest {
  //创建SparkConf()并设置App名称 .setMaster("local[3]")
  val conf = new SparkConf().setAppName("apitest").setMaster("local[*]")//.setMaster("spark://47.94.196.195:7077")
  //.set("spark.executor.memory", "512m")//.setJars(Array("C:\\Users\\L\\Desktop\\mayun\\mvnspark\\target\\mvn-spark-1.0-SNAPSHOT.jar"))
  //创建SparkContext，该对象是提交spark App的入口
  val spark = SparkSession.builder().config(conf)
    .config("spark.task.maxFailures",2).getOrCreate()
  def main2(args: Array[String]) {
    System.setProperty("HADOOP_USER_NAME", "root")
    val myBroad = Array(1,2,3,4,5,6,7)
    //sc.broadcast()
//    val rdd1 = spark.sparkContext.parallelize(
//      Seq(User(1,"tom",10,"2016-05-01"),
//          User(2,"jerry",12,"2014-02-09"),
//          User(3,"jack",15,"2016-05-28")))
//    spark.sparkContext.setLogLevel("WARN")
    val rdd2 = spark.sparkContext.parallelize(Seq(
      Order(112387,"iphone 7","2011-01-05",BigDecimal.apply(4388),0.8,10),
      Order(8873,"iphone x","2017-04-02",BigDecimal.apply(8888),0.75,2),
      Order(99087,"mi 5","2016-09-01",BigDecimal.apply(1500),0.6,3),
      Order(7996,"watch","2016-05-01",BigDecimal.apply(200),0.4,1)
    ))//.map(x=>{(x.create_user,x)})
    val userinfo = spark.sparkContext.textFile("D:\\_zhauy\\spark-testdata\\input\\user")
    val format = new SimpleDateFormat("yyyy-MM-dd")
    val rdduser = userinfo.map(x=>{
      val arr = x.split(",")
      User(arr(0).toInt,arr(1),arr(2).toInt,arr(3))
    })//.map(x=>{(x.id,x)})
//    val res = rdduser.distinct().leftOuterJoin(rdd2)
//
//    val result = res.map(x=>{
//      (x._1,x._2._1.age,x._2._1.name,x._2._1.birthday,if(x._2._2.isEmpty) null else x._2._2.get.order_no,
//        if(x._2._2.isEmpty) null else x._2._2.get.order_name,
//        if(x._2._2.isEmpty) null else x._2._2.get.create_time,
//        if(x._2._2.isEmpty) null else x._2._2.get.price,
//        if(x._2._2.isEmpty) null else x._2._2.get.zheco
//      )
//    })
//
//    println(result.collect().toBuffer)
//    println("执行时间:"+(new Date().getTime-spark.sparkContext.startTime)/1000+"s")


    val numType= Array(
      Tuple2(1,"String"),
      Tuple2(1,"String1"),
      Tuple2(1,"String2"),
      Tuple2(1,"String3"),
      Tuple2(1,"String4"),
      Tuple2(1,"String5"),
      Tuple2(1,"String6"),
      Tuple2(1,"String7"),
      Tuple2(1,"String8"),
      Tuple2(1,"String9"),
      Tuple2(1,"String10"),
      Tuple2(1,"String11"),
      Tuple2(1,"String12"),
      Tuple2(1,"String13"),
      Tuple2(1,"String14"),
      Tuple2(1,"String15"),
      Tuple2(1,"String16"),
      Tuple2(1,"String17"),
      Tuple2(2,"int"),
      Tuple2(3,"byte"),
      Tuple2(4,"bollean"),
      Tuple2(5,"float"),
      Tuple2(1,"34"),
      Tuple2(1,"45"),
      Tuple2(2,"47"),
      Tuple2(3,"75"),
      Tuple2(4,"95"),
      Tuple2(5,"16"),
      Tuple2(1,"85")
    ).toBuffer
    for(i <- 1 until 5000){
      numType.append((9,"heiheia"))
    }
    val DBName=Array(
      Tuple2(1,"Spark"),
      Tuple2(2,"Hadoop"),
      Tuple2(3,"Kylin"),
      Tuple2(4,"Flink")
    )

    val DBName2=Array(
      Tuple2(1,"Spark2"),
      Tuple2(2,"Hadoop2"),
      Tuple2(3,"Kylin2"),
      Tuple2(4,"Flink2"),
      Tuple2(99,"lalalla2"),
      Tuple2(5,"heiheia"),
        Tuple2(6,"aaa"),
        Tuple2(7,"cccc")
    )



    val rdda = spark.sparkContext.parallelize(numType,3)
    val rddn = spark.sparkContext.parallelize(DBName,3)
    val rddn2 = spark.sparkContext.parallelize(DBName2,3)
    val re = rdda.cogroup(rdda,rddn2)//.map((_._2))

    val tmp = rddn.join(rddn2)
    println(tmp.collect().toBuffer)

//    try {
//      rddn2.foreachPartition(it=>{
//        if(Thread.currentThread().getName.equals("Executor task launch worker-1")){
//          throw new NullPointerException("aa")
//        }
//        println(Thread.currentThread().getName+"==========")
//        val conn = JDBCUtils.getConnection
//        while (it.hasNext){
//          val i = it.next()
//        }
//        conn.close()
//      })
//    }catch {
//      case e:Exception =>{}
//    }
//    val timepoint = new Date().getTime
//    val rdda2 = rdda.partitionBy(new HashPartitioner(3));
//    rdda2.collect();
//    val timepoint2 = new Date().getTime
//    println("repartion times:"+(timepoint2-timepoint)/1000+"s")

//    rdda.foreachPartition(it=>{
//      println(it.size)
//      it
//    })
    val r = new Random()
    val restmp = rdda.map(x=>{(x._1+","+r.nextInt(10),x._2)}).groupByKey(10)
//      .map(x=>{
//      (x._1.split(",")(0),x._2)
//    })
//    val restmp = rdda.groupByKey(10);

    restmp.foreachPartition(it=>{
      println(it.size)
    })

    println(restmp.count())
    println((new Date().getTime-spark.sparkContext.startTime)/1000+"s")


    /*rdda.map(x=>{
      println(Thread.currentThread().getName+"=========="+x)
      x
    }).collect()*/


//    rdda.repartitionAndSortWithinPartitions(new MyPartitioner(4))
//val myres = rdda.map(x=>{(x._1,1)}).reduceByKey(_+_)
//val myres = rdda.groupBy(_._1).map(x=>{(x._1,x._2.size)})
    val rand = new Random()
    val res2 = new ArrayBuffer[Any]()
    val res = new ArrayBuffer[Any]()
    val resbro = spark.sparkContext.broadcast(res)
    /*val tm1 = rdda.map(x=>{
      res2.append(x)
      resbro.value.append(x)
      println(Thread.currentThread().getName+"=========="+resbro.value.size)
      (rand.nextInt(4)+""+x._1,x._2)
    }).collect()*/
    println(res.size)
    println(res2.size)

//    println("shuffle------------------")
//    val myres = tm1.groupBy(_._1).map(x=>{
//      (x._1.substring(1,x._1.length),x._2.size)
//    }).reduceByKey(_+_)
//    myres.saveAsTextFile("D:\\_zhauy\\spark-testdata\\output")

//    println((new Date().getTime-spark.sparkContext.startTime)/1000+"s")
//    val res = rdda.repartitionAndSortWithinPartitions(new MyPartitioner(4))
    //println(res.collect().toBuffer)

    //28s
/*    import spark.implicits._
    rdduser.toDF()createOrReplaceTempView("user")
    val ds = rdd2.toDS()
    ds.createOrReplaceTempView("order")
    spark.sql(
      """
        |select distinct(id),name,age,birthday,order.*
        |from user left join order on user.id = order.create_user
      """.stripMargin).show()

//    df.show(10)

    println("执行时间:"+(new Date().getTime-spark.sparkContext.startTime)/1000+"s")
    println(rdduser.collect().toBuffer)*/
    spark.sparkContext.stop()
  }

  def testsql(): Unit ={
    import spark.implicits._
    val rdd2 = spark.sparkContext.parallelize(Seq(
      Order(112387,"iphone 7","2011-01-05",BigDecimal.apply(4388),0.8,10),
      Order(8873,"iphone x","2017-04-02",BigDecimal.apply(8888),0.75,2),
      Order(99087,"mi 5","2016-09-01",BigDecimal.apply(1500),0.6,3),
      Order(7996,"watch","2016-05-01",BigDecimal.apply(200),0.4,1)
    ))

    val rdd1 = spark.sparkContext.parallelize(
          Seq(User(1,"tom",10,"2016-05-01"),
              User(2,"jerry",12,"2014-02-09"),
              User(3,"jack",15,"2016-05-28"),
              User(7,"lala",15,"2016-05-28")
          ))

    val d2df = rdd2.toDF()
    val d1df = rdd1.toDF()

    d1df.createOrReplaceTempView("user")
    d2df.createOrReplaceTempView("order")

    val tmp1 = spark.sql(
      """
        |select id
        |from user
      """.stripMargin)
    tmp1.persist().show()
    spark.sqlContext.cacheTable("user")
    val res = spark.sql(
      """
        |select *
        |from order
      """.stripMargin)
    val tmpd = functions.broadcast(tmp1)
    val rr = res.col("create_user")===tmpd.col("id")
    println(rr)
    res.join(tmpd,rr,"leftsemi").show()
//
//    val tmp2 = spark.sql(
//      """
//        |select *
//        |from user
//      """.stripMargin).join(res).show()
  }

  def main(args: Array[String]): Unit = {
//    main2(Array())
      testsql();
  }
  case class User(id:Int,
                  name:String,
                  age:Int,
                  birthday:String
                 )

  case class Order(order_no:Int,
                   order_name:String,
                  create_time:String,
                   price:BigDecimal,
                   zheco:Double,
                   create_user:Int
                 )
}
