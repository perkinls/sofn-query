package com.sofn.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * hive的元数据库数据
 */
public class HiveDBUtil {
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://192.168.21.101:3306/hive_db";
    private static final String user = "root";
    private static final String password = "1234";
    private static Connection conn = null;

    public static Connection getConn(){
        try {
            if(null == conn){
                Class.forName(driver);
                conn = DriverManager.getConnection(url,user,password);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args){
        System.out.println(getConn());
    }
}
