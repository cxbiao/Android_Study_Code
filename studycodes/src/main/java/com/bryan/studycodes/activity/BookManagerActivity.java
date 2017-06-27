package com.bryan.studycodes.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.bryan.aidl.Book;
import com.bryan.aidl.BookManageService;
import com.bryan.aidl.IBookManager;
import com.bryan.aidl.IOnNewBookArrivedListener;
import com.bryan.studycodes.R;

import java.util.List;

/**
 * Created by bryan on 2015-11-29.
 */
public class BookManagerActivity extends TitleBaseActivity {
    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED=1;

    private IBookManager mBookManager;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG, "receive new book:"+msg.obj);
                    break;

            }
        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener=new IOnNewBookArrivedListener.Stub(){

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.i(TAG,Thread.currentThread().getName());
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };

    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBookManager=IBookManager.Stub.asInterface(service);
            try {
                //设置死亡代理，断线重连
                mBookManager.asBinder().linkToDeath(mBinderDeath,0);
                List<Book> list=mBookManager.getBookList();
                Log.i(TAG, "query book list,list type;" + list.getClass().getCanonicalName());
                Log.i(TAG, "query book list: " + list.toString());
                Book newBook=new Book(3,"Android源码设计模式");
                mBookManager.addBook(newBook);
                Log.i(TAG, "add book:" + newBook);
                List<Book> newList=mBookManager.getBookList();
                Log.i(TAG, "query book list: " + newList.toString());
                mBookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //此处可以重连服务，运行在UI线程
            //mBookManager=null;
            Log.i(TAG, "onServiceDisconnected,binderDied:"+Thread.currentThread().getName());
        }
    };

    private IBinder.DeathRecipient mBinderDeath=new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            //binder线程池
            Log.i(TAG, "IBinder.DeathRecipient,binderDied:"+Thread.currentThread().getName());
            mBookManager.asBinder().unlinkToDeath(this, 0);
            mBookManager=null;
            bindService(new Intent(BookManagerActivity.this,BookManageService.class),conn, Context.BIND_AUTO_CREATE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent=new Intent(this, BookManageService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);

        setHeaderTitle("AIDL");
    }

    @Override
    protected void onDestroy() {
        if(mBookManager!=null && mBookManager.asBinder().isBinderAlive()){
            Log.i(TAG, "unregister listener:"+mOnNewBookArrivedListener);
            try {
                mBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(conn);
        super.onDestroy();

    }
}
