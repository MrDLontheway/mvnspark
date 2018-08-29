package com.wxstc.dss.dl;

import com.wxstc.util.JDBCUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable{
    public String orderNo;
    public String userId;
    public BigDecimal pay;
    public Date payTime;
    public String productId;

    public static List<String> ids = new ArrayList<>();
    public static List<String> product_ids = new ArrayList<>();

    static {
        Connection connection = JDBCUtils.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from dss_playuser");
            ResultSet set = statement.executeQuery();
            while (set.next()){
                String id = set.getString(1);
                ids.add(id);
//                String nickName = set.getString(2);
//                String age = set.getInt(3);
//                set.getString(4);
//                set.getDate(5);
            }
            PreparedStatement statement2 = connection.prepareStatement("SELECT * from dss_product");
            ResultSet set2 = statement2.executeQuery();
            while (set2.next()){
                String id = set2.getString(1);
                product_ids.add(id);
//                String nickName = set.getString(2);
//                String age = set.getInt(3);
//                set.getString(4);
//                set.getDate(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Order(){
        super();
        this.productId = product_ids.get(PlayUser.rand.nextInt(product_ids.size()));
        this.payTime = PlayUser.randomDate("2013-07-01","2018-08-20");
        this.userId = ids.get(PlayUser.rand.nextInt(ids.size()));
        this.orderNo = UUID.randomUUID().toString().toLowerCase().replace("-","");
        this.pay = new BigDecimal(PlayUser.rand.nextDouble()*PlayUser.rand.nextInt(10000));
    }

    @Override
    public String toString() {
        return this.orderNo+","+this.userId+","+this.pay+","+this.payTime+","+this.productId;
    }

    public static void main(String args[]) throws IOException, SQLException {
//        System.out.println(new Order());
        String PATH = args[0];
        int FILENUMBER = Integer.valueOf(args[1]);
        int ONECENUMBER = Integer.valueOf(args[2]);
        for (int i=0;i<FILENUMBER;i++){
            File file = new File(PATH + "/" + new Date().getTime() + i + "order.log");
            FileWriter fw = new FileWriter(file);
            for (int j=0;j<ONECENUMBER;j++){
                fw.write(new Order().toString()+"\n");
            }
        }

    }
}
