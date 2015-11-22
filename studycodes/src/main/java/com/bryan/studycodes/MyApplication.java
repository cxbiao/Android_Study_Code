package com.bryan.studycodes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.LinkedList;

/**
 * Created by Administrator on 2015/10/22.
 * ActivityLifecycleCallbacks  API 14
 *
 */
public class MyApplication extends Application  implements Application.ActivityLifecycleCallbacks{

    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        refWatcher = LeakCanary.install(this);
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle arg1) {
        if (null != mExistedActivitys && null != activity) {
            // 把新的 activity 添加到最前面，和系统的 activity 堆栈保持一致
            mExistedActivitys.offerFirst(new ActivityInfo(activity,ActivityInfo.STATE_CREATE));
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (null != mExistedActivitys && null != activity) {
            ActivityInfo info = findActivityInfo(activity);
            if (null != info) {
                mExistedActivitys.remove(info);
            }
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
            for (ActivityInfo info : mExistedActivitys) {
                if (null == info || null == info.mActivity) {
                    continue;
                }

                try {
                    info.mActivity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mExistedActivitys.clear();
            // 退出完之后再添加监听
            registerActivityLifecycleCallbacks( this );
        }
    }

    private ActivityInfo findActivityInfo(Activity activity) {
        if (null == activity || null == mExistedActivitys) {
            return null;
        }

        for (ActivityInfo info : mExistedActivitys) {
            if (null == info) {
                continue;
            }

            if (activity.equals(info.mActivity)) {
                return info;
            }
        }

        return null;
    }

    class ActivityInfo {
        private final static int STATE_NONE = 0;
        private final static int STATE_CREATE = 1;

        Activity mActivity;
        int mState;

        ActivityInfo() {
            mActivity = null;
            mState = STATE_NONE;
        }

        ActivityInfo(Activity activity, int state) {
            mActivity = activity;
            mState = state;
        }
    }


    private LinkedList<ActivityInfo> mExistedActivitys = new LinkedList<ActivityInfo>();
}
