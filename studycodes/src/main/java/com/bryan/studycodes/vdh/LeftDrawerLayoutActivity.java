/*
 * Copyright (c) 2015.
 * @Author：Cxb
 */

package com.bryan.studycodes.vdh;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bryan.studycodes.R;


public class LeftDrawerLayoutActivity extends AppCompatActivity {

    private LeftMenuFragment mMenuFragment;
    private LeftDrawerLayout mLeftDrawerLayout;
    private TextView mContentTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_drawer_layout);

        mLeftDrawerLayout = (LeftDrawerLayout) findViewById(R.id.id_drawerlayout);
        mContentTv = (TextView) findViewById(R.id.id_content_tv);

        FragmentManager fm = getSupportFragmentManager();
        mMenuFragment = (LeftMenuFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment = new LeftMenuFragment()).commit();
        }

        mMenuFragment.setOnMenuItemSelectedListener(new LeftMenuFragment.OnMenuItemSelectedListener() {
            @Override
            public void menuItemSelected(String title) {
                mLeftDrawerLayout.closeDrawer();
                mContentTv.setText(title);
            }
        });


    }

    public void back(View v){
        mLeftDrawerLayout.closeDrawer();
    }


}
