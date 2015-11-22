package com.bryan.studycodes.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bryan.studycodes.Constant;
import com.bryan.studycodes.R;
import com.bryan.studycodes.messenger.MessengerService;
import com.bryan.studycodes.utils.KLog;

/**
 * Created by bryan on 2015-11-22.
 */
public class MessengerActivity extends AppCompatActivity {



    private Messenger replyMessenger=new Messenger(new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.MSG_FROM_SERVER:
                    KLog.e("messenger","receive from server:"+msg.getData().getString("reply"));
                    break;
                default:
                    break;
            }
        }
    });
    private Messenger messenger;

    private ServiceConnection mConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
              messenger=new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent=new Intent(this, MessengerService.class);
        bindService(intent,mConn, Context.BIND_AUTO_CREATE);
    }

    public void bind(View v){
        Message msg=Message.obtain(null, Constant.MSG_FROM_CLIENT);
        msg.replyTo=replyMessenger;
        Bundle bundle=new Bundle();
        bundle.putString("msg","this is client");
        msg.setData(bundle);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }
}
