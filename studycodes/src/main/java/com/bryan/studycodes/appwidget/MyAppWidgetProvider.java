package com.bryan.studycodes.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bryan.studycodes.R;

/**
 * Created by bryan on 2015-11-07.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    public static final String TAG="MyAppWidgetProvider";
    public static final String CLICK_ACTION="com.bryan.studycodes.action.CLICK";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG,"onReceive:action="+intent.getAction());
        //这里判断是自己的action，做自己的事情，比如小部件被单击了要干什么，这里是做一个动画效果
        if(intent.getAction().equals(CLICK_ACTION)){
            Toast.makeText(context,"clicked  it",Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap src= BitmapFactory.decodeResource(context.getResources(),R.mipmap.app);
                    AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
                    for(int i=0;i<37;i++){
                        float degree=(i*10)%360;
                        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.app_img, rotateBitmap(context, src, degree));
                        remoteViews.setTextViewText(R.id.time_tv, degree + "");

                        Intent intent=new Intent(CLICK_ACTION);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, intent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.app_img,pendingIntent);
                        appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                        SystemClock.sleep(30);

                    }
                }
            }).start();
        }
    }

    /**
     * 每次桌面部件更新时都调用一次该方法
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG,"onUpdate");
        final  int counter=appWidgetIds.length;
        Log.i(TAG, "counter=" + counter);
        for (int i=0;i<counter;i++){
            int appWidgetId=appWidgetIds[i];
            onWidgetUpdate(context,appWidgetManager,appWidgetId);
        }
    }

    /**
     * 桌面小部件更新
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    private void onWidgetUpdate(Context context,AppWidgetManager appWidgetManager,int appWidgetId){
        Log.i(TAG, "appWidgetId=" + appWidgetId);
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent intent=new Intent(CLICK_ACTION);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.app_img,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private Bitmap rotateBitmap(Context context,Bitmap src,float degree){
        Matrix matrix=new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bitmap=Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,true);
        return  bitmap;
    }
}
