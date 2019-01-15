package com.bryan.studycodes.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MD5 {

    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return byte2hex(digest.digest(content.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String getMD5(File file){
        InputStream inputStream=null;
        try {
            int num;
            byte[] buffer = new byte[1024];
            inputStream = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while ((num = inputStream.read(buffer)) > 0) {
                md5.update(buffer, 0, num);
            }
            inputStream.close();
            return byte2hex(md5.digest());
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String byte2hex(byte[] bytes){
        StringBuilder sb=new StringBuilder();
        for(byte b:bytes){
            String low=Integer.toHexString(b & 0x0f);
            String high=Integer.toHexString(b>>4 & 0x0f);
            sb.append(high).append(low);
        }
        return sb.toString().toUpperCase();
    }



}
