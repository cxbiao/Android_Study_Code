package com.bryan.studycodes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bryan.studycodes.R;

/**
 * Author：Cxb on 2016/9/12 17:02
 */
public class FrescoActivity extends TitleBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);

        setHeaderTitle("Fresco探究");
    }

    public void single(View v){
       startActivity(new Intent(this,FrescoSingleActivity.class));
    }

    public void list(View v){
        startActivity(new Intent(this,FrescoListActivity.class));
    }
}
