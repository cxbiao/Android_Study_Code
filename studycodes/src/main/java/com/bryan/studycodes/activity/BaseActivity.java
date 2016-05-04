package com.bryan.studycodes.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.bryan.studycodes.R;
import com.bryan.studycodes.utils.StatusBarCompat;
import com.zhy.m.permission.MPermissions;

/**
 * Author：Cxb on 2016/2/15 10:23
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
