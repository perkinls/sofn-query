package com.sofn.utils;

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
}
