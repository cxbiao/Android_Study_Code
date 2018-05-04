package com.bryan.studycodes.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.bryan.studycodes.Constant;
import com.bryan.studycodes.utils.KLog;

/**
 * Created by bryan on 2015-11-22.
 */
public class MessengerService extends Service {

    private Handler msgHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.MSG_FROM_CLIENT:
                  Messenger client=msg.replyTo;  //得到客户端的messengenr
                  KLog.e("messenger","receiver from client:"+msg.getData().getString("msg"));
                  if(client!=null) {
                      try {
                          Message replymsg=Message.obtain(null,Constant.MSG_FROM_SERVER);
                          Bundle bundle=new Bundle();
                          bundle.putString("reply","你的消息服务器已收到，将很快处理!");
                          replymsg.setData(bundle);
                          client.send(replymsg);
                      } catch (RemoteException e) {
                          e.printStackTrace();
                      }
                  }
                  break;
                default:
                    break;
            }
        }
    };
    private Messenger messenger=new Messenger(msgHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
