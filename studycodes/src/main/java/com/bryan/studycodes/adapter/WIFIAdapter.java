package com.bryan.studycodes.adapter;

import android.net.wifi.p2p.WifiP2pDevice;
import androidx.annotation.Nullable;

import com.bryan.studycodes.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Bryan on 2018/3/24.
 */

public class WIFIAdapter extends BaseQuickAdapter<WifiP2pDevice,BaseViewHolder> {

    public WIFIAdapter(int layoutResId, @Nullable List<WifiP2pDevice> data) {
        super(layoutResId, data);
    }


    public WIFIAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiP2pDevice item) {
        helper.setText(R.id.tv_name,"设备名称:"+item.deviceName);
        helper.setText(R.id.tv_mac,"设备MAC地址:"+item.deviceAddress);

    }
}
