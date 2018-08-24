package com.sofn.utils;

import java.util.Date;

public class StringUtils {
    public static boolean isEmpty(String str){
        if(null == str){
            return true;
        }
        if(str.equals("null")||str.equals("*")){
            return true;
        }
        if(str.trim().equals("")){
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        long l1 = System.currentTimeMillis()-1000;
        Date date1 = new Date();
        Date date2 = new Date(l1);
        System.out.println(date1.before(date2));
        System.out.println(date1.getDate());
        date1.setDate(date1.getDate()+1);
        System.out.println(date1.getDate());
     }
}
