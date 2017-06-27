package com.bryan.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by bryan on 2015-11-29.
 */
public class BookManageService extends Service{
    private static final String TAG = "BookManageService";
    private AtomicBoolean mIsServiceDestroyed=new AtomicBoolean(false);
    /**
     * aidl方法是在Binder线程池中执行的，会存在线程并发问题，所以用CopyOnWriteArrayList来进行线程自动同步
     */
    private CopyOnWriteArrayList<Book> mBookList=new CopyOnWriteArrayList<>();
    /**
     * RemoteCallbackList专门用来保存AIDL回调，当客户端进程终止后，它能够自动移除客户端注册的listner，此类内部自动实现了线程同步
     */
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList=new RemoteCallbackList<>();

    /**
     * 服务器方法远行在binder线程池中,些时客户端挂起，如果有耗时操作，客户端要开启子线程
     */
    private Binder mBinder=new IBookManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.i(TAG, Thread.currentThread().getName());
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i(TAG,Thread.currentThread().getName());
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
           mListenerList.register(listener);
            int num=mListenerList.beginBroadcast();
            mListenerList.finishBroadcast();
            Log.i(TAG, "registerListener,size:"+num);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
           mListenerList.unregister(listener);
            Log.i(TAG, "unregisterListener");
            int num=mListenerList.beginBroadcast();
             mListenerList.finishBroadcast();
            Log.i(TAG, "registerListener,size:"+num);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2,"iOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    /**
     * listener.onNewBookArrived 运行在binder线程池中，只不过是客户端的线程池,如果客户端的onNewBookArrived耗时的话，此时服务端要启子线程
     * @param book
     * @throws RemoteException
     */
    private  void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        int num=mListenerList.beginBroadcast();
        for (int i = 0; i <num; i++) {
            IOnNewBookArrivedListener listener=mListenerList.getBroadcastItem(i);
            if(listener!=null){
                listener.onNewBookArrived(book);
                Log.i(TAG, "onNewBookArrived,notify listener:" + listener);
            }

        }
        mListenerList.finishBroadcast();
    }

    private  class  ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId=mBookList.size()+1;
                Book newBook=new Book(bookId,"new book#"+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
