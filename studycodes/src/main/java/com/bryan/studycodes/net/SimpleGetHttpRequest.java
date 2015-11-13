package com.bryan.studycodes.net;

import android.text.TextUtils;

import com.bryan.studycodes.net.callback.RequestCallback;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class SimpleGetHttpRequest  extends  SimpleHttpRequest{

    public SimpleGetHttpRequest(String url, Map<String, String> params, RequestCallback callBack) {
        super(url, params, callBack);
    }

    @Override
    protected void initConnection() throws Exception {
        String getStr = appendParams(params);
        if(!TextUtils.isEmpty(getStr)){
            url+="?"+getStr;
        }
        URL netUrl = new URL(url);
        conn = (HttpURLConnection) netUrl.openConnection();
        conn.setUseCaches(false);
        conn.setConnectTimeout(TIME_OUT);
        conn.setRequestMethod("GET");
    }

    @Override
    protected void buildRequestBody() throws Exception {

    }



}
