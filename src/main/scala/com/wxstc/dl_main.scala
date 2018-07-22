package com.wxstc

import com.wxstc.lagou.LagoujobHbase

/**
  * 任务提交主入口
  */
object dl_main {
  def main(args: Array[String]): Unit = {
//    Superclass_school.getSchoolDF()
//    Superclass_school2.getSchoolDF()
    LagoujobHbase.lagoujobToHbase()
  }

}
