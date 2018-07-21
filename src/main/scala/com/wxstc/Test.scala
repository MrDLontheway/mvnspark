package com.wxstc

import java.io.{FileOutputStream, ObjectOutputStream, StringReader}

import com.wxstc.bean.Danmaku
import com.wxstc.es.Sc2
import com.wxstc.es.Superclass_school.SchoolInfo
import com.wxstc.util.JsonUtils
import org.apache.lucene.analysis.{Analyzer, TokenStream}
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.wltea.analyzer.core.{IKSegmenter, Lexeme}
import org.wltea.analyzer.lucene.IKAnalyzer

import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer
object Test {
  def main(args: Array[String]): Unit = {
//    val text: String = "北京时间3月8日0时42分，马来西亚航空公司一架波音777型客机执行从马来西亚吉隆坡飞往北京（MH370）航班任务，机上共搭乘239人，包括150多名中国乘客。1时20分，飞机与地面失去联系。马来西亚航空公司发布声明称，其正与搜寻救援机构合作，以确定飞机的位置。"
//    val res = ikAny(text)
//    println(res)
//    val b = (1,1,"2","3","4","5","6")
//    val a = SchoolInfo(1,1,"2","3","4","5","6")

    val as = ArrayBuffer[Any]()
    val bs = ArrayBuffer[Any]()
    val cs = ArrayBuffer[Sc2]()
    for(i<- 0 until 100000){
      as.append(SchoolInfo(1,1,"2","3","4","5","6"))
      bs.append((1,1,"2","3","4","5","6"))
      cs.append(new Sc2(1,1,"2","3","4","5","6"))
    }

    serialize(as,"C:\\Users\\L\\Desktop\\work\\1.log")
    serialize(bs,"C:\\Users\\L\\Desktop\\work\\2.log")
    serialize(cs,"C:\\Users\\L\\Desktop\\work\\3.log")
//    var s = "{\"uid\":9252245,\"snick\":\"祭影呀祭影\",\"content\":\"站着说话不腰疼 敢平a吗真的是\",\"date\":1517889625734,\"rid\":885443}";
//    s = "我"
    //val res = JsonUtils.jsonToPojo(s,classOf[Danmaku])
//    print(s.length)

  }

  def serialize[T](o: T,path:String) {
    val bos = new FileOutputStream(path)//基于磁盘文件流的序列化
    val oos = new ObjectOutputStream(bos)
    oos.writeObject(o)
    oos.close()
  }

  def ikAny(text:String): ArrayBuffer[String] ={
    val stringReader: StringReader = new StringReader(text)
    val ik: IKSegmenter = new IKSegmenter(stringReader, true)
    var result = ArrayBuffer[String]()
    var wordLexeme: Lexeme = null
    while (true) {
      wordLexeme =ik.next()
      if(wordLexeme==null) break
      val s = wordLexeme.getLexemeText
      result+=s
    }
    stringReader.close()
    result
  }
}
