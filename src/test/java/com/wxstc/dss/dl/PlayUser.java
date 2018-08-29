package com.wxstc.dss.dl;

import com.wxstc.util.JDBCUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class PlayUser implements Serializable{
    public String id;
    public String nickeName;
    public int age;
    public String province;
    public Date addTime;
    public static String[] provinces = new String[]{
        "河南省",
                "广东省",
                "内蒙古自治区",
                "黑龙江省",
                "新疆维吾尔自治区",
                "湖北省",
                "辽宁省",
                "山东省",
                "陕西省",
                "上海市",
                "贵州省",
                "重庆市",
                "西藏自治区",
                "安徽省",
                "福建省",
                "湖南省",
                "海南省",
                "江苏省",
                "青海省",
                "广西壮族自治区",
                "宁夏回族自治区",
                "江西省",
                "浙江省",
                "河北省",
                "山西省",
                "香港特别行政区",
                "台湾省",
                "澳门特别行政区",
                "甘肃省",
                "四川省",
                "云南省",
                "北京市",
                "天津市",
                "吉林省"
    };
    public static Random rand = new Random();

    public PlayUser(){//构造  随机生成用户信息
        super();
        this.id = UUID.randomUUID().toString().toLowerCase().replace("-","");
        int nicklength = rand.nextInt(6)+1;
        this.nickeName = "";
        for (int i=0;i<=nicklength;i++){
            this.nickeName += getRandomChar();
        }
        this.age = rand.nextInt(21)+10;//用户为10-30岁 玩家分布
        this.province = provinces[rand.nextInt(provinces.length)];//随机大省
        this.addTime = randomDate("2013-07-01","2018-08-20");
    }

    @Override
    public String toString() {
        return this.id+","+
                this.nickeName+","+
                this.age+","+
                this.province;
    }

    public static void main(String args[]) throws IOException, SQLException {
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);
        int i = 0;
        PreparedStatement statement = connection.prepareStatement("INSERT INTO dss_playuser(id,nickeName,age,province,addTime) VALUES (?,?,?,?,?)");
        while (i<1000){
            PlayUser playUser = new PlayUser();
            statement.setString(1,playUser.id);
            statement.setString(2,playUser.nickeName);
            statement.setInt(3,playUser.age);
            statement.setString(4,playUser.province);
            statement.setDate(5,new java.sql.Date(playUser.addTime.getTime()));
            statement.addBatch();
            i++;
        }
        statement.executeBatch();
        connection.commit();
        JDBCUtils.close();
//        String PATH = args[0];
//        int FILENUMBER = Integer.valueOf(args[1]);
//        int ONECENUMBER = Integer.valueOf(args[2]);
//        for (int i=0;i<FILENUMBER;i++){
//            File file = new File(PATH + "/" + new Date().getTime() + i + ".log");
//            FileWriter fw = new FileWriter(new File(PATH+"/"+new Date().getTime()+i+".log"));
//            for (int j=0;j<ONECENUMBER;j++){
//                fw.write(new PlayUser().toString()+"\n");
//            }
//        }
    }

    //随机生成常见汉字
    public static String getRandomChar() {
        String str = "";
        int highCode;
        int lowCode;

        Random random = new Random();

        highCode = (176 + Math.abs(random.nextInt(39))); //B0 + 0~39(16~55) 一级汉字所占区
        lowCode = (161 + Math.abs(random.nextInt(93))); //A1 + 0~93 每区有94个汉字

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highCode)).byteValue();
        b[1] = (Integer.valueOf(lowCode)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取随机日期
     * @param beginDate 起始日期，格式为：yyyy-MM-dd
     * @param endDate 结束日期，格式为：yyyy-MM-dd
     * @return
     */
    public static Date randomDate(String beginDate,String endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);  // 构造开始日期
            Date end = format.parse(endDate);  // 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if(start.getTime() >= end.getTime()){
                return null;
            }

            long date = random(start.getTime(),end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }
}
