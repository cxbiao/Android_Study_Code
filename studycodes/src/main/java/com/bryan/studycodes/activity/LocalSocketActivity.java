package com.bryan.studycodes.activity;

import android.net.Credentials;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bryan.studycodes.R;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author：Cxb on 2016/6/2 11:42
 *
 * MediaRecorder的捕获流可以输出到localsocket
 */
public class LocalSocketActivity extends TitleBaseActivity {

    private static final String TAG = "LocalSocketActivity";
    private static final String SOCKET_NAME="H264";
    private static final int BUFFER_SIZE = 500000;
    private LocalSocket client;
    private LocalSocket server;
    private LocalServerSocket lss;

    private boolean running ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localsocket);
        initSocket();
    }

    private void initSocket() {

        try {
            client = new LocalSocket();
            lss = new LocalServerSocket(SOCKET_NAME);

            //LocalServerSocket和LocalSocketAddress的name要相同
            client.connect(new LocalSocketAddress(SOCKET_NAME));
            client.setReceiveBufferSize(BUFFER_SIZE);
            client.setSendBufferSize(BUFFER_SIZE);

            server = lss.accept();
            Credentials cre= server.getPeerCredentials();
            Log.e(TAG,cre.getUid()+"");
            server.setReceiveBufferSize(BUFFER_SIZE);
            server.setSendBufferSize(BUFFER_SIZE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void startServer(View v) {
        v.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os;
                InputStream is;
                int i=0;
                try {
                    is = server.getInputStream();
                    os = server.getOutputStream();
                    int len;
                    byte[] data;
                    while (true) {
                        data = new byte[1024];
                        len=is.read(data);
                        Log.e(TAG, "server:receive-size--" + len + " --- " + new String(data,0,len) + " ---");

                        byte[] writeBytes = ("Hello Client:" + i).getBytes();
                        os.write(writeBytes);
                        Log.e(TAG, "server:send-size--" + writeBytes.length + " --- " + new String(writeBytes) + " ---");
                        os.flush();
                        i++;
                    }

                    //不能关闭流，流关闭则套接字也关闭
                    //  os.close();
                    //  server.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void clientSend(View v) {

        if(running) return;
        running=true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is;
                OutputStream os;
                int i=0;
                try {
                    is = client.getInputStream();
                    os= client.getOutputStream();
                    byte[] data;
                    int len;

                    while (running) {
                        byte[] writeBytes=("LocalSocket:" + i).getBytes();
                        os.write(writeBytes);
                        Log.e(TAG, "client:send-size--" + writeBytes.length + " --- " + new String(writeBytes) + " ---");
                        os.flush();

                        i++;
                        data = new byte[1024];
                        len=is.read(data);
                        Log.e(TAG, "client:receive-size--" + len + " --- " + new String(data,0,len) + " ---");
                        Thread.sleep(1000);

                    }

                   // is.close();
                   // client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopClient(View v) {
        running = false;

    }


}
