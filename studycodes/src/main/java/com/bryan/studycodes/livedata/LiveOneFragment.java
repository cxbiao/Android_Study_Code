package com.bryan.studycodes.livedata;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.bryan.studycodes.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LiveOneFragment extends Fragment {
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_data)
    TextView tvData;
    Unbinder unbinder;

    private MyViewModel mVm1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_live, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mVm1 = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
//        mVm1.getLiveData().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                Log.i("LiveActivityOne","onChanged:"+s);
//                tvData.setText(s);
//            }
//        });
        MyStockData.getInstance().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Log.i("LiveActivityOne","onChanged:"+s);
                tvData.setText(s);
            }
        });


    }

    @OnClick(R.id.btn_send)
    public void send() {
       // mVm1.getLiveData().setValue("LiveOneFragment");
        MyStockData.getInstance().setValue("LiveOneFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
