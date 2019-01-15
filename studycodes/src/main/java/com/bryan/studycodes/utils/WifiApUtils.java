package com.bryan.studycodes.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class WifiApUtils {

    public static boolean isWifiApEnabled(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            return (Boolean) method.invoke(wifiManager);
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //android 8.0不支持反射打开热点
    public static void openWifiAp(Context context,String apName, String password) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration apConfig = new WifiConfiguration();
            //android 4.4不用设置 allowedKeyManagement默认即可
            Field f=WifiConfiguration.KeyMgmt.class.getField("WPA2_PSK");
            int type=f.getInt(null);
            apConfig.allowedKeyManagement.set(type);
            apConfig.preSharedKey = password;
            apConfig.SSID = apName;
            method.invoke(wifiManager, apConfig, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void closeWifiAp(Context context) {
        if (isWifiApEnabled(context)) {
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //获取WIFI热点的状态：
    public static int getWifiApState(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            return  (Integer) method.invoke(wifiManager);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取开启Ap热点后设备本身的IP地址
     *
     * @param context 上下文
     * @return IP地址
     */
    public static String getHotspotIpAddress(Context context) {
        WifiManager wifimanager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifimanager == null ? null : wifimanager.getDhcpInfo();
        if (dhcpInfo != null) {
            int address = dhcpInfo.serverAddress;
            return ((address & 0xFF)
                    + "." + ((address >> 8) & 0xFF)
                    + "." + ((address >> 16) & 0xFF)
                    + "." + ((address >> 24) & 0xFF));
        }
        return "";
    }
}
