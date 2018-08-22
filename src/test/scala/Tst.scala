import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.util.Random

object Tst {
  val conf = new SparkConf().setAppName("apitest").setMaster("local[*]")//.setMaster("spark://47.94.196.195:7077")
  //.set("spark.executor.memory", "512m")//.setJars(Array("C:\\Users\\L\\Desktop\\mayun\\mvnspark\\target\\mvn-spark-1.0-SNAPSHOT.jar"))
  //创建SparkContext，该对象是提交spark App的入口
  val spark = SparkSession.builder().config(conf)
    .config("spark.task.maxFailures",2).getOrCreate()

  val rdd = spark.sparkContext.parallelize(Array(
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
    Tuple2(3,"1aaa")
  ))

  val rdd2 = spark.sparkContext.parallelize(Array(
    Tuple2(1,"heihei"),
    Tuple2(1,"heihei"),
    Tuple2(1,"heihei"),
    Tuple2(2,"String3"),
    Tuple2(4,"String4"),
    Tuple2(5,"String5"),
    Tuple2(3,"String6"),
    Tuple2(1,"String7"),
    Tuple2(1,"String8"),
    Tuple2(1,"String9"),
    Tuple2(1,"String10")
  ))


  rdd.persist()

  println(rdd.count())
  Thread.sleep(1000000000)

  def main(args: Array[String]): Unit = {
    val rand = new Random()
    for(i <- 0 until(100)){
      println(rand.nextDouble())
    }
  }
}
