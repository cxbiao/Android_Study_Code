package com.bryan.studycodes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import java.util.LinkedList;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2015/10/22.
 * ActivityLifecycleCallbacks  API 14
 *
 */
public class MyApplication extends Application  implements Application.ActivityLifecycleCallbacks{

   // private RefWatcher refWatcher;
//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
       // refWatcher = LeakCanary.install(this);


        //fresco硬盘缓存默认在手机内存，现配置到sd中，/Android/data/packagename/cache/...
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                    .setBaseDirectoryPath(getExternalCacheDir())
                    .build();

        //使用okhttp，不再使用ImagePipelineConfig.newBuilder
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(this,okHttpClient)
                    .setMainDiskCacheConfig(diskCacheConfig)
                   .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())  //开启渐进式加载
                    .build();

        Fresco.initialize(this,config);


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle arg1) {
        if (null != mExistedActivitys && null != activity) {
            // 把新的 activity 添加到最前面，和系统的 activity 堆栈保持一致
            mExistedActivitys.offerFirst(activity);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (null != mExistedActivitys && null != activity) {
            mExistedActivitys.remove(activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    public void exitAllActivity() {
        if (null != mExistedActivitys) {
            // 先暂停监听（省得同时在2个地方操作列表）
            unregisterActivityLifecycleCallbacks( this );

            // 弹出的时候从头开始弹，和系统的 activity 堆栈保持一致
            for (Activity info : mExistedActivitys) {
                if (null == info ) {
                    continue;
                }
                try {
                    info.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mExistedActivitys.clear();
            // 退出完之后再添加监听
            registerActivityLifecycleCallbacks( this );
        }
    }


    private LinkedList<Activity> mExistedActivitys = new LinkedList<Activity>();
}
