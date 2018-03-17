package com.bryan.studycodes.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.LauncherAdapter;
import com.bryan.studycodes.utils.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 2018/3/15.
 */

public class LauncherActivity extends Activity {



    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private LauncherAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
        KLog.e("onCreate");
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList= getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_ALL);



        mAdapter=new LauncherAdapter(this,resolveInfoList);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(mAdapter);




        mAdapter.setOnItemClickLitener(new LauncherAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ResolveInfo resolveInfo=mAdapter.getItem(position);
                Intent app=new Intent();
                app.setComponent(new ComponentName(resolveInfo.activityInfo.packageName,resolveInfo.activityInfo.name));
                startActivity(app);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });



    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        KLog.e("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        KLog.e("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        KLog.e("onStop");
    }

    @Override
    public void onBackPressed() {
        return;
    }



}
