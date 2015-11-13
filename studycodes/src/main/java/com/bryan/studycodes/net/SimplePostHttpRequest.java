package com.bryan.studycodes.net;
import com.bryan.studycodes.net.callback.RequestCallback;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SimplePostHttpRequest extends SimpleHttpRequest {


    public SimplePostHttpRequest(String url, Map<String, String> params, RequestCallback callBack) {
        super(url, params, callBack);
    }

    @Override
    protected void initConnection() throws Exception {
        if(params==null || params.isEmpty()){
            throw new IllegalArgumentException("the parmas cannot be null");
        }
        URL netUrl = new URL(url);
        conn = (HttpURLConnection) netUrl.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(TIME_OUT);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


    }

    @Override
    protected void buildRequestBody() throws Exception {

        OutputStream os = conn.getOutputStream();
        String postStr = appendParams(params);
        os.write(postStr.getBytes());

    }


}
