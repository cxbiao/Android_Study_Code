package com.bryan.studycodes.activity;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bryan.studycodes.R;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author：Cxb on 2016/6/2 11:42
 */
public class LocalSocketActivity extends TitleBaseActivity {

    private static final String TAG = "LocalSocketActivity";
    private LocalSocket receiver;
    private LocalSocket sender;
    private LocalServerSocket lss;

    private int BUFFER_SIZE = 500000;

    private boolean running ;

    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localsocket);
        initSocket();
    }

    private void initSocket() {

        try {
            receiver = new LocalSocket();
            lss = new LocalServerSocket("H264");

            //LocalServerSocket和LocalSocketAddress的name要相同
            receiver.connect(new LocalSocketAddress("H264"));
            receiver.setReceiveBufferSize(BUFFER_SIZE);
            receiver.setSendBufferSize(BUFFER_SIZE);

            sender = lss.accept();
            sender.setReceiveBufferSize(BUFFER_SIZE);
            sender.setSendBufferSize(BUFFER_SIZE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void send(View v) {

        if(running) return;
        running=true;
        //发送线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os;
                try {
                    os = sender.getOutputStream();
                    while (running) {
                        byte[] data = ("LocalSocket:" + i).getBytes();
                        os.write(data);
                        Log.e(TAG, "sender:size--" + data.length + " --- " + new String(data) + " ---");
                        os.flush();
                        Thread.sleep(500);
                        i++;
                    }

                  //  os.close();
                  //  sender.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        //接收线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is;
                try {
                    is = receiver.getInputStream();

                    byte[] data;
                    int len;

                    while (running) {
                        data = new byte[1024];
                        len=is.read(data);
                        Log.e(TAG, "receiver:size--" + len + " --- " + new String(data,0,len) + " ---");
                        Thread.sleep(500);

                    }

                   // is.close();
                   // receiver.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop(View v) {
        running = false;

    }


}
