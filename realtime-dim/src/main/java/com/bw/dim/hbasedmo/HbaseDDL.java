package com.bw.dim.hbasedmo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HbaseDDL {


    //    利用伪单例调用连接
    public static Connection connection = HbaseConnection.connection;


    /**
     * 创建命名空间
     * @param nameSpace
     */

    public static void createNameSpace(String nameSpace){
    //  创建Admin
    //  等方法写完 统一抛出
    //   admin连接是轻量级  线程不安全   不推荐池化和缓存连接
        Admin admin = connection.getAdmin();

    //    调用方法创建命名空间

    }


    public static void main(String[] args) {



    }
}
