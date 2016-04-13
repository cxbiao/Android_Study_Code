package com.bryan.studycodes.activity;

import android.support.v7.app.AppCompatActivity;

import com.bryan.studycodes.R;
import com.bryan.studycodes.utils.StatusBarCompat;

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


}
