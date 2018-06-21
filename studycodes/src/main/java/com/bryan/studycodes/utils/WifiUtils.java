package com.bryan.studycodes.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WifiUtils {


    private WifiManager mWifiManager;

    private static class WifiHolder {
        private static WifiUtils mInstance=new WifiUtils();
    }

    private WifiUtils(){

    }

    public static WifiUtils getIntance(){
        return WifiHolder.mInstance;
    }


    public void init(Context context){
        mWifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean isWifiEnabled(){
        return mWifiManager.isWifiEnabled();
    }


    public boolean isWifiApEnabled() {
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            return (Boolean) method.invoke(mWifiManager);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    //wifi和热点不能同时打开
    public void openWifi() {
        if(!isWifiEnabled()) {
           if(isWifiApEnabled()){
               closeWifiAp();
           }
           mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if(isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }


    //android 8.0不支持反射打开热点
    public void openWifiAp(String apName, String password) {
        try {
            if (mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(false);
            }
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration apConfig = new WifiConfiguration();
            //android 4.4不用设置 allowedKeyManagement默认即可
            Field f=WifiConfiguration.KeyMgmt.class.getField("WPA2_PSK");
            int type=f.getInt(null);
            apConfig.allowedKeyManagement.set(type);
            apConfig.preSharedKey = password;
            apConfig.SSID = apName;
            method.invoke(mWifiManager, apConfig, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeWifiAp() {
        if (isWifiApEnabled()) {
            try {
                Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                WifiConfiguration config = (WifiConfiguration) method.invoke(mWifiManager);
                Method method2 = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(mWifiManager, config, false);
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public int getWifiState() {
        return mWifiManager.getWifiState();
    }

    //获取WIFI热点的状态：
    public int getWifiApState() {
        try {
            Method method = mWifiManager.getClass().getMethod("getWifiApState");
            return  (Integer) method.invoke(mWifiManager);
        } catch (Exception e) {
           e.printStackTrace();
           return -1;
        }
    }




}
