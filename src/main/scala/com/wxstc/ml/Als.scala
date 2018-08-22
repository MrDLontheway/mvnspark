package com.wxstc.ml

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object Als {

  //参数含义
  //input表示数据路径
  //kryo表示是否使用kryo序列化
  //numIterations迭代次数
  //lambda正则化参数
  //numUserBlocks用户的分块数
  //numProductBlocks物品的分块数
  //implicitPrefs这个参数没用过，但是通过后面的可以推断出来了，是否开启隐藏的分值参数阈值，预测在那个级别才建议推荐，这里是5分制度的，详细看后面代码
  case class Params(
                     input: String = null,
                     output: String = null,
                     kryo: Boolean = false,
                     numIterations: Int = 20,
                     lambda: Double = 1.0,
                     rank: Int = 10,
                     numUserBlocks: Int = -1,
                     numProductBlocks: Int = -1,
                     implicitPrefs: Boolean = false)

  def main(args: Array[String]): Unit = {
    val params = Params("",
      "",
      false,
      10
    )
    val conf = new SparkConf().setAppName(s"MovieLensALS").setMaster("local[*]")
    //.set("spark.sql.warehouse.dir","E:/ideaWorkspace/ScalaSparkMl/spark-warehouse")
    // 如果是在云搭建集群可以考虑使用
    if (params.kryo) {
      //conf.registerKryoClasses(Array(classOf[mutable.BitSet], classOf[Rating]))
      //.set("spark.kryoserializer.buffer", "8m")
    }
    val sc = new SparkContext(conf)
    //设置log基本，生产也建议使用WARN
    Logger.getRootLogger.setLevel(Level.WARN)

    //得到因此的级别
    val implicitPrefs = params.implicitPrefs
    val yuandata =ArrayBuffer[String]()
    val rand = new Random()
    for (i<- 0 until 10000){
      val score = if(rand.nextInt(2)==0)rand.nextDouble() else -rand.nextDouble()
      yuandata.append(rand.nextInt(10000)+"|"+rand.nextInt(10000)+"|"+ score)
    }
    //元数据整理 缓存
    val ratings = sc.parallelize(yuandata).map(line=>{
      val args1 = line.split("|")
      Rating(args1(0).toInt,args1(1).toInt,args1(2).toDouble)
    }).cache()
    //    val rdd1 = sc.textFile(params.input).map(line=>{
    //
    //    })
    val allcount = ratings.count()
    val usercount = ratings.map(_.user).distinct().count()
    val productcount = ratings.map(_.product).distinct().count()
    println(s"usercount:$usercount")
    println(s"productcount:$productcount")
    //按80%训练，20%验证分割样本
    val splits = ratings.randomSplit(Array(0.8, 0.2))
    sc.broadcast()
    //读取数据，并通过是否设置了分值阈值来修正评分
    //官方推荐是，只有哦大于3级别的时候才值得推荐
    //且下面的代码，implicitPrefs，直接就是默认5 Must see，按道理会根据自己对分数阈值的预估，rating减去相应的值，比如fields(2).toDouble - 2.5
    //5 -> 2.5, 4 -> 1.5, 3 -> 0.5, 2 -> -0.5, 1 -> -1.5
    //现在是5分值的映射关系，如果是其他分值的映射关系有该怎么做？还不确定，个人建议别使用这个了。
    //经过下面代码推断出，如果implicitPrefs=true或者flase，true的意思是，预测的分数要大于2.5（自己设置），才能推荐给用户，小了，没有意义
    //它引入implicitPrefs的整体含义为，只有用户对物品的满意达到一定的值，才推荐，不然推荐不喜欢的没有意思，所以在构建样本的时候，会减去相应的值fields(2).toDouble - 2.5（自己设置）
    //这种理论是可以的，但是还有一个理论，不给用户推荐比给用户推荐错了还要严重（有人提出过），不推荐产生的效果还要严重，还有反向推荐，
    //我把implicitPrefs叫做分值阈值
    //把训练样本缓存起来，加快运算速度
    val training = splits(0).cache()

    val test = if (params.implicitPrefs) {
      /*
       * 0 means "don't know" and positive values mean "confident that the prediction should be 1".
       * Negative values means "confident that the prediction should be 0".
       * We have in this case used some kind of weighted RMSE. The weight is the absolute value of
       * the confidence. The error is the difference between prediction and either 1 or 0,
       * depending on whether r is positive or negative.
       */
      splits(1).map(x => UserComments(x.user, x.product, if (x.rating > 0) 1.0 else 0.0))
    } else {
      splits(1)
    }.cache()

    //释放缓存
    ratings.unpersist()

    //setRank设置随机因子，就是隐藏的属性
    //setIterations设置最大迭代次数
    //setLambda设置正则化参数
    //setImplicitPrefs 是否开启分值阈值
    //setUserBlocks设置用户的块数量，并行化计算,当特别大的时候需要设置
    //setProductBlocks设置物品的块数量
    val model = new ALS()
      .setRank(params.rank)
      .setIterations(params.numIterations)
      .setLambda(params.lambda)
      .setImplicitPrefs(params.implicitPrefs)
      .setUserBlocks(params.numUserBlocks)
      .setProductBlocks(params.numProductBlocks)
    model.run(training)

    //训练的样本和测试的样本的分值全部是减了2.5分的
    //测试样本的分值如果大于0为1，else 0，表示分值大于2.5才预测为Ok

    //计算rmse
    //val rmse = computeRmse(model, test, params.implicitPrefs)

    //println(s"Test RMSE = $rmse.")


  }

  /** Compute RMSE (Root Mean Squared Error). */
  /*def computeRmse(model: MatrixFactorizationModel, data: RDD[Rating], implicitPrefs: Boolean)
  : Double = {

    //内部方法含义如下
    // 如果已经开启了implicitPref那么，预测的分值大于0的为1，小于0的为0，没有开启的话，就是用原始分值
    //min(r,1.0)求预测分值和1.0那个小，求小值，然后max(x,0.0)求大值， 意思就是把预测分值大于0的为1，小于0 的为0
    //这样构建之后预测的预测值和测试样本的样本分值才一直，才能进行加权rmse计算
    def mapPredictedRating(r: Double): Double = {
      if (implicitPrefs) math.max(math.min(r, 1.0), 0.0) else r
    }

    //根据模型预测，用户对物品的分值，predict的参数为RDD[(Int, Int)]
    val predictions: RDD[Rating] = model.predict(data.map(x => (x.user, x.product)))

    //mapPredictedRating把预测的分值映射为1或者0
    //join连接原始的分数,连接的key为x.user, x.product
    //values方法表示只保留预测值，真实值
    val predictionsAndRatings = predictions.map{ x =>
      ((x.user, x.product), mapPredictedRating(x.rating))
    }//.join(data.map(x => ((x.user, x.product), x.rating))).values

    //最后计算预测与真实值的平均误差平方和
    //这是先每个的平方求出来，然后再求平均值，最后开方
    math.sqrt(predictionsAndRatings.map(x => (x._1 - x._2) * (x._1 - x._2)).mean())
  }*/

  case class UserComments(userid: Int, productid: Int, score: Double)

}
