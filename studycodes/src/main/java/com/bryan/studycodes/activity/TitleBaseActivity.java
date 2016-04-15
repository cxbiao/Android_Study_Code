package com.bryan.studycodes.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bryan.studycodes.R;

/**
 * Author：Cxb on 2016/2/15 10:23
 */
public abstract class TitleBaseActivity extends BaseActivity {

    protected RelativeLayout mTitleHeaderBar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mTitleHeaderBar=(RelativeLayout)findViewById(R.id.frame_title_header);
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
