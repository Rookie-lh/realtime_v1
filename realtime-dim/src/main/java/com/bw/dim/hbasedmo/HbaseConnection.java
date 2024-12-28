package com.bw.dim.hbasedmo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HbaseConnection {


    public static Connection connection = null;

    static {
        //        创建连接配置对象
        Configuration conf = new Configuration();

//        添加配置参数
        conf.set("hbase.zookeeper.quorum","hadoop102,hadoop103,hadoop104");


//        创建连接
        try {
//            可以空参
            connection =  ConnectionFactory.createConnection(conf);
        }catch (IOException e){
            e.printStackTrace();
        }

//        创建异步连接
//        CompletableFuture<AsyncConnection> asyncConnection = ConnectionFactory.createAsyncConnection(conf);


    }

    public static void closeConnection() throws IOException {
        if(connection != null){
            connection.close();
        }
    }



    public static void main(String[] args) throws IOException {



        HbaseConnection.closeConnection();
    }
}
