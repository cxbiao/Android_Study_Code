package com.bryan.studycodes.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bryan.studycodes.R;
import com.bryan.studycodes.utils.ScreenUtils;

/**
 * Author：Cxb on 2016/2/15 10:23
 */
public abstract class TitleBaseActivity extends BaseActivity {

    protected RelativeLayout mTitleHeaderBar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        FrameLayout contentView= findViewById(Window.ID_ANDROID_CONTENT);
        View childView=contentView.getChildAt(0);
        FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) childView.getLayoutParams();
        layoutParams.topMargin+= ScreenUtils.dip2px(this,48);

        View titleBar= LayoutInflater.from(this).inflate(R.layout.header_bar,contentView,false);
        contentView.addView(titleBar,0);

        mTitleHeaderBar=(RelativeLayout)titleBar;//titleBar.findViewById(R.id.frame_title_header);
        if(mTitleHeaderBar!=null && enableDefaultBack()){
            mTitleHeaderBar.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    protected boolean enableDefaultBack() {
        return true;
    }


    protected void setHeaderTitle(String title) {
        ((TextView)mTitleHeaderBar.findViewById(R.id.tv_title)).setText(title);
    }

    public RelativeLayout getTitleHeaderBar() {
        return mTitleHeaderBar;
    }


}
