package com.bryan.studycodes.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bryan.studycodes.R;
import com.bryan.studycodes.utils.SystemBarTintManager;

/**
 * Author：Cxb on 2016/2/15 10:23
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#9900FF"));
        // set a custom navigation bar resource
        tintManager.setNavigationBarTintColor(Color.parseColor("#FF0000"));
        // set a custom status bar drawable
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimaryDark));


    }


}
