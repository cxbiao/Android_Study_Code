package com.bryan.studycodes.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.WIFIAdapter;
import com.bryan.studycodes.utils.KLog;
import com.bryan.studycodes.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Bryan on 2018/3/24.
 */

public class WiFiDirectActivity extends TitleBaseActivity {
    @BindView(R.id.tv_receive)
    TextView tvReceive;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private WIFIAdapter mAdapter;
    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;

    private ServerSocket ss;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tvReceive.append(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_direct);
        ButterKnife.bind(this);
        setHeaderTitle("WIFI DIRECT");


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION); //设备发现的状态，开始或者停止
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION); //检测WIFI P2P是否可用
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION); //周围WIFI P2P设备发生新增，删除，更新等
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);  //WIFI P2P连接状态
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);  //WIFI 设备的状态发生变化
        registerReceiver(wifiReceiver, intentFilter);

        wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                ToastUtils.show(getApplicationContext(), "onChannelDisconnected");
                KLog.e("onChannelDisconnected");
            }
        });
        searchPeers();
        mAdapter = new WIFIAdapter(R.layout.recycler_item_wifi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //ToastUtils.show(getApplicationContext(),position+"");
                connectDevice(mAdapter.getItem(position));

            }
        });



    }

    //查找周围的WIFI DIRECT设备
    private void searchPeers() {
        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                ToastUtils.show(getApplicationContext(), "discoverPeers:onSuccess");
                KLog.e("discoverPeers:onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                ToastUtils.show(getApplicationContext(), "discoverPeers:onFailure:" + reason);
                KLog.e("discoverPeers:onFailure:" + reason);
            }
        });
    }

    private void connectDevice(WifiP2pDevice device) {
        WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = device.deviceAddress;
        wifiP2pManager.connect(channel, wifiP2pConfig, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                ToastUtils.show(getApplicationContext(), "连接成功");
                KLog.e("connect success");
            }

            @Override
            public void onFailure(int reason) {
                ToastUtils.show(getApplicationContext(), "连接失败:" + reason);
                KLog.e("connect fail");
            }
        });

    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 0);
                if (state == WifiP2pManager.WIFI_P2P_DISCOVERY_STARTED) {
                    KLog.e("p2p discovery has started");
                } else if (state == WifiP2pManager.WIFI_P2P_DISCOVERY_STOPPED) {
                    KLog.e("p2p discovery has stopped");
                }

            } else if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, 0);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    KLog.e("Wi-Fi p2p is enabled");
                } else if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                    KLog.e("Wi-Fi p2p is disabled");
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(intent.getAction())) {

                KLog.e("WIFI PEERS FIND");
                WifiP2pDeviceList wifiP2pDeviceList = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);
                mAdapter.setNewData(new ArrayList<>(wifiP2pDeviceList.getDeviceList()));
//                wifiP2pManager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
//                    @Override
//                    public void onPeersAvailable(WifiP2pDeviceList peers) {
//
//                    }
//                });

            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(intent.getAction())) {

                KLog.e("WIFI P2P 连接状态发生了变化");

                NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                //1 .一旦连接成功，随机一方会创建组，即成为server,另一方则为client
                //2. 此后只有这个组没有变化，就算连接断开，无论谁先再连接也不会改变server和 client的归属,除非把组删除，则会回归到第一步

                //另外可以通过主动createGroup来创建组，即成为server
                if(networkInfo.isConnected()){
                    KLog.e("已连接");
                    WifiP2pInfo wifiP2pInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                    ToastUtils.show(getApplicationContext(),wifiP2pInfo.groupOwnerAddress.getHostAddress()+":"+wifiP2pInfo.isGroupOwner);
                    begingTransact(wifiP2pInfo);

                }else {
                    //断开之后
                    KLog.e("未连接");
                }

                //  这个方法也能得到ip
//                wifiP2pManager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
//                    @Override
//                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
//                        if (info.groupOwnerAddress != null) {
//                            String addr = info.groupOwnerAddress.getHostAddress();
//                            KLog.e("addr:" + addr);
//                        }
//
//
//                    }
//                });


            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(intent.getAction())) {
                KLog.e("WIFI P2P 设备的状态发生变化");

            }
        }
    };



    private void begingTransact(final WifiP2pInfo wifiP2pInfo){

        if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
            startServer();
        }else if(wifiP2pInfo.groupFormed ){
            //客户端延时启动
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startClient(wifiP2pInfo.groupOwnerAddress.getHostAddress());
                }
            },1000);

        }
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ss = new ServerSocket(5555);
                    Socket s = ss.accept();
                    InputStream is = s.getInputStream();
                    OutputStream os = s.getOutputStream();
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    len = is.read(bytes, 0, bytes.length);
                    String ret=new String(bytes, 0, len, "UTF-8");
                    KLog.e(ret);
                    Message msg=Message.obtain();
                    msg.obj=ret;
                    mHandler.sendMessage(msg);
                    os.write("from server :  I come from china !".getBytes("UTF-8"));
                    is.close();
                    os.close();
                    ss.close();
                } catch (IOException e) {
                    KLog.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void startClient(final String ip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(ip, 5555);
                    InputStream is = s.getInputStream();
                    OutputStream os = s.getOutputStream();
                    os.write("from client: where do you come from ?".getBytes("UTF-8"));
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    len = is.read(bytes, 0, bytes.length);
                    String ret=new String(bytes, 0, len, "UTF-8");
                    KLog.e(ret);
                    Message msg=Message.obtain();
                    msg.obj=ret;
                    mHandler.sendMessage(msg);
                    is.close();
                    os.close();
                    s.close();


                } catch (IOException e) {
                    KLog.e(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
        if(ss!=null && ss.isBound()){
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
