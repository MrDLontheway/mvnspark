package com.wxstc.ml
import org.apache.spark.ml.linalg.Matrices
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils

object VectorLearning {
  def main(args: Array[String]): Unit = {
    MatrixRowLearning(args);
  }

  def vectorLearning(args: Array[String]) {

    //Vector 向量
    //LabeledPoint  向量标签
    //Matrix 矩阵
    val vd = Vectors.dense(2, 0, 6)
    println(vd(2))
    println(vd)

    //数据个数，序号，value
    val vs = Vectors.sparse(4, Array(0, 1, 2, 3), Array(9, 5, 2, 7))
    println(vs(2))
    println(vs)

    val vs2 = Vectors.sparse(4, Array(0, 2, 1, 3), Array(9, 5, 2, 7))
    println(vs2(2))
    println(vs2)
  }

  def labeledPointLearning(args: Array[String]) {

    val vd = Vectors.dense(2, 0, 6)
    val pos = LabeledPoint(1, vd) //对密集向量建立标记点
    println(pos.features)
    println(pos.label)
    println(pos)

    val vs = Vectors.sparse(4, Array(0, 1, 2, 3), Array(9, 5, 2, 7))
    val neg = LabeledPoint(2, vs) //对稀疏向量建立标记点
    println(neg.features)
    println(neg.label)
    println(neg)

  }

  //从文本中读取带标签的数据
  def LabeledPointLoadlibSVMFile()={
    val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass().getSimpleName().filter(!_.equals('$')))
    //  println(this.getClass().getSimpleName().filter(!_.equals('$')))
    //设置环境变量
    val sc = new SparkContext(conf)

    val mu = MLUtils.loadLibSVMFile(sc, "C:\\Users\\L\\Documents\\GitHub\\SparkLearning\\file\\data\\mllib\\input\\basic\\sample_libsvm_data.txt") //读取文件
    mu.foreach(println) //打印内容

    sc.stop
  }

  //创建分布式矩阵
  def MatrixLearning(): Unit ={
    val mx = Matrices.dense(2, 3, Array(1, 2, 3, 4, 5, 6)) //创建一个分布式矩阵
    println(mx) //打印结果

    val arr=(1 to 6).toArray.map(_.toDouble)
    val mx2 = Matrices.dense(2, 3, arr) //创建一个分布式矩阵
    println(mx2) //打印结果

    val arr3=(1 to 20).toArray.map(_.toDouble)
    val mx3 = Matrices.dense(4, 5, arr3) //创建一个分布式矩阵
    println(mx3) //打印结果
//    println(mx3.index(0,0))
//    println(mx3.index(1,1))
//    println(mx3.index(2,2))
    println(mx3.numRows)
    println(mx3.numCols)
  }


  def MatrixRowLearning(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[*]").setAppName(this.getClass().getSimpleName().filter(!_.equals('$')))
    val sc = new SparkContext(conf)

    println("First:Matrix ")
    val rdd = sc.textFile("C:\\Users\\L\\Documents\\GitHub\\SparkLearning\\file\\data\\mllib\\input\\basic\\MatrixRow.txt") //创建RDD文件路径
      .map(_.split(' ') //按“ ”分割
      .map(_.toDouble)) //转成Double类型
      .map(line => Vectors.dense(line)) //转成Vector格式
    val rm = new RowMatrix(rdd) //读入行矩阵
    //    for(i <- rm){
    //      println(i)
    //    }
    //error
    //疑问：如何打印行矩阵所有值，如何定位？
    println(rm.numRows()) //打印列数
    println(rm.numCols()) //打印行数
    rm.rows.foreach(println)

//    println("Second:index Row Matrix ")
//    val rdd2 = sc.textFile("C:\\Users\\L\\Documents\\GitHub\\SparkLearning\\file\\data\\mllib\\input\\basic\\MatrixRow.txt") //创建RDD文件路径
//      .map(_.split(' ') //按“ ”分割
//      .map(_.toDouble)) //转成Double类型
//      .map(line => Vectors.dense(line)) //转化成向量存储
//      .map((vd) => new IndexedRow(vd.size, vd)) //转化格式
//    val irm = new IndexedRowMatrix(rdd2) //建立索引行矩阵实例
//    println(irm.getClass) //打印类型
//    irm.rows.foreach(println) //打印内容数据
//    //如何定位？
//
//    println("Third: Coordinate Row Matrix ")
//    val rdd3 = sc.textFile("C:\\Users\\L\\Documents\\GitHub\\SparkLearning\\file\\data\\mllib\\input\\basic\\MatrixRow.txt") //创建RDD文件路径
//      .map(_.split(' ') //按“ ”分割
//      .map(_.toDouble)) //转成Double类型
//      .map(vue => (vue(0).toLong, vue(1).toLong, vue(2))) //转化成坐标格式
//      .map(vue2 => new MatrixEntry(vue2 _1, vue2 _2, vue2 _3)) //转化成坐标矩阵格式
//    val crm = new CoordinateMatrix(rdd3) //实例化坐标矩阵
//    crm.entries.foreach(println) //打印数据
//    println(crm.numCols())
//    println(crm.numCols())
//    //    Return approximate number of distinct elements in the RDD.
//    println(crm.entries.countApproxDistinct())
//
//
//    println("Fourth: Block Matrix :null")
//    //块矩阵待完善

    sc.stop
  }
}
