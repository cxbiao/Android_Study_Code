package com.bryan.studycodes.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtils {



    public static boolean isWifiEnabled(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }


    //wifi和热点不能同时打开
    public static void openWifi(Context context) {
        if(!isWifiEnabled(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
           if(WifiApUtils.isWifiApEnabled(context)){
               WifiApUtils.closeWifiAp(context);
           }
            wifiManager.setWifiEnabled(true);
        }
    }

    public static void closeWifi(Context context) {
        if(isWifiEnabled(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
        }
    }


    public static int getWifiState(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getWifiState();
    }


    /**
     * 获取连接的Wifi热点的IP地址
     *
     * @param context 上下文
     * @return IP地址
     */
    public static String getHotspotIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        if (wifiinfo != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            if (dhcpInfo != null) {
                int address = dhcpInfo.gateway;
                return ((address & 0xFF)
                        + "." + ((address >> 8) & 0xFF)
                        + "." + ((address >> 16) & 0xFF)
                        + "." + ((address >> 24) & 0xFF));
            }
        }
        return "";
    }

    /**
     * 获取连接Wifi后设备本身的IP地址
     *
     * @param context 上下文
     * @return IP地址
     */
    public static String getLocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiinfo = wifiManager == null ? null : wifiManager.getConnectionInfo();
        if (wifiinfo != null) {
            int ipAddress = wifiinfo.getIpAddress();
            return ((ipAddress & 0xFF)
                    + "." + ((ipAddress >> 8) & 0xFF)
                    + "." + ((ipAddress >> 16) & 0xFF)
                    + "." + ((ipAddress >> 24) & 0xFF));
        }
        return "";
    }





}
