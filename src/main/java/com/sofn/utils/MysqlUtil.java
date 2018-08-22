package com.sofn.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * hive的元数据库数据
 */
public class MysqlUtil {
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://192.168.21.101:3306/sofn_db?useUnicode=true&characterEncoding=utf8";
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
        Connection conn = getConn();

//        try {
//            FileReader reader = new FileReader("C:\\Users\\czx\\Desktop\\in\\fieldInfo.txt");
//            BufferedReader reader1 = new BufferedReader(reader);
//            String s = null;
//            int id=10000;
//            while ((s=reader1.readLine())!=null){
//                String[] arr = s.split("\t");
//                id++;
//                int tableId=Integer.parseInt(arr[1]);
//                String desc = arr[2];
//                String name=arr[3];;
//
//                String sql = "insert into sofn_field_info(id,tableId,comment,name) values(?,?,?,?);";
//                PreparedStatement ps = conn.prepareStatement(sql);
//                ps.setInt(1,id);
//                ps.setInt(2,tableId);
//                ps.setString(3,desc);
//                ps.setString(4,name);
//                ps.execute();
//            }
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        try {
//            FileReader reader = new FileReader("C:\\Users\\czx\\Desktop\\in\\new 1.txt");
//            BufferedReader reader1 = new BufferedReader(reader);
//            String s = null;
//            int id = 10000;
//            Map<String,String> map = new HashMap<>();
//            while ((s = reader1.readLine()) != null) {
//                String[] arr = s.split("\t");
//                if(arr.length<6){
//                    continue;
//                }
//                map.put(arr[0].trim().toLowerCase(),arr[5].trim());
//                System.out.println(arr[0].toLowerCase().trim()+":"+arr[5].trim());
//            }
//
//            String sql = "update sofn_field_info set comment=? where comment='' and name=?";
//            for(Map.Entry<String,String> entry: map.entrySet()){
//                PreparedStatement ps = conn.prepareStatement(sql);
//                ps.setString(1,entry.getValue());
//                ps.setString(2,entry.getKey());
//                ps.executeUpdate();
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }
}
