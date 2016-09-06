package com.bryan.studycodes.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.BottomAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Cxb on 2016/9/6 09:57
 */
public class BottomSheetActivity extends AppCompatActivity {

    private List<String> data=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        for (int i=0;i<30;i++){
            data.add("item:"+i);
        }
    }

    /**
     *
     *
     * 设置 BottomSheetBehavior 没有设置 PeekHeight 或者 PeekHeight为0 的时候. 底部的BottomSheetBehavior 视图滑动不出来.
     * google的bug
     *
     * 网上的方法还是无效
     * @param v
     */
    public  void sheetB(View v){
        final BottomSheetBehavior  behavior=BottomSheetBehavior.from(findViewById(R.id.scrollView));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            public boolean hasRequest;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                if (!hasRequest && behavior.getPeekHeight() == 0 && slideOffset > 0) {
                    hasRequest = true;
                    bottomSheet.setTranslationY(0);
                }
            }
        });

        if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public  void sheetD(View v){
        RecyclerView recyclerView=new RecyclerView(this);
        BottomAdapter adapter=new BottomAdapter(this,data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setContentView(recyclerView);
        dialog.show();

        adapter.setOnItemClickLitener(new BottomAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }


}