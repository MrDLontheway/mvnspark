package com.wxstc.mypartitioner

import org.apache.spark.Partitioner

import scala.util.Random

class MyPartitioner(numParts:Int) extends Partitioner{
  override def numPartitions = numParts
  val rand = new Random()
  override def getPartition(key: Any):Int = {
    return rand.nextInt(numParts);
  }
}
