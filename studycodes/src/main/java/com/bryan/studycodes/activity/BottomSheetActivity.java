package com.bryan.studycodes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.BottomAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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

        View contentView=View.inflate(this,R.layout.dialog_bottom,null);
        RecyclerView recyclerView= (RecyclerView) contentView.findViewById(R.id.recyclerView);
        BottomAdapter adapter=new BottomAdapter(this,data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);  //禁止recyclerview在NestedScrollView中滚动
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        final BottomSheetDialog dialog=new BottomSheetDialog(this);
        dialog.setContentView(contentView);
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
