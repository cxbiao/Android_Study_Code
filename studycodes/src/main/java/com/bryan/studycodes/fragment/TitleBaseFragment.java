package com.bryan.studycodes.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bryan.studycodes.R;


public abstract class TitleBaseFragment extends Fragment {

    protected RelativeLayout mTitleHeaderBar;
    protected FrameLayout mContentContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup view  = (ViewGroup) inflater.inflate(getFrameLayoutId(), null);
        FrameLayout contentContainer = (FrameLayout) view.findViewById(R.id.frame_content);

        mTitleHeaderBar = (RelativeLayout) view.findViewById(R.id.frame_title_header);


        if(enableDefaultBack()){
            mTitleHeaderBar.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        mContentContainer = contentContainer;
        View contentView = createView(inflater, view, savedInstanceState);
        contentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        contentContainer.addView(contentView);
        return view;
    }


    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected boolean enableDefaultBack() {
        return true;
    }


    protected int getFrameLayoutId() {
        return R.layout.content_frame_with_title_header;
    }

    protected void setHeaderTitle(String title) {
        ((TextView)mTitleHeaderBar.findViewById(R.id.tv_title)).setText(title);
    }

    public RelativeLayout getTitleHeaderBar() {
        return mTitleHeaderBar;
    }

}
