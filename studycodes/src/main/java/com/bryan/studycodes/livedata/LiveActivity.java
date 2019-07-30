package com.bryan.studycodes.livedata;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bryan.studycodes.R;
import com.bryan.studycodes.activity.TitleBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiveActivity extends TitleBaseActivity {

    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_data)
    TextView tvData;
    private MyViewModel mVm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livedata);
        ButterKnife.bind(this);
        setHeaderTitle("LiveData");
        mVm = ViewModelProviders.of(this).get(MyViewModel.class);
        mVm.getLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvData.setText(s);
            }
        });

    }

    @OnClick(R.id.btn_send)
    public void send() {
        mVm.getLiveData().setValue("LiveActivity");
    }
}
