package com.wxstc.mypartitioner

import org.apache.spark.Partitioner

class MyPartitioner(numParts:Int) extends Partitioner{
  override def numPartitions = numParts

  override def getPartition(key: Any):Int = {
    return 1;
  }
}
