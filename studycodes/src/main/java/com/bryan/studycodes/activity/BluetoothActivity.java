package com.bryan.studycodes.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.MyBluetoothAdapter;
import com.bryan.studycodes.utils.ClsUtils;
import com.bryan.studycodes.utils.KLog;
import com.bryan.studycodes.utils.ToastUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Bryan on 2018/3/3.
 */

public class BluetoothActivity extends TitleBaseActivity {
    @BindView(R.id.btn_system_open)
    Button btnSystemOpen;
    @BindView(R.id.btn_code_open)
    Button btnCodeOpen;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private BluetoothAdapter bluetoothAdapter;

    private MyBluetoothAdapter mAdapter;

    private List<BluetoothDevice> bluetoothDeviceList;

    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String name = "my_uuid_bluetooth";

    private static final int FROM_CLIENT = 11;
    private static final int FROM_SERVER = 12;

    private Map<String, ClientBte> clientPool = new HashMap<>();
    private AcceptThread acceptThread;
    private List<CommunicateThread> threadList = new ArrayList<>();
    private Map<String,CommunicateThread> serverPool=new HashMap<>();
    private Set<String> lostBluetoothClient=new HashSet<>();
    private Set<String> lostBluetoothServer=new HashSet<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ButterKnife.bind(this);
        setHeaderTitle("蓝牙");


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(receiver, filter);


        mAdapter = new MyBluetoothAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new MyBluetoothAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                Toast.makeText(getApplicationContext(), mAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                BluetoothDevice device = mAdapter.getItem(position);
                ClientBte clientBte;
                if (clientPool.containsKey(device.getAddress())) {
                    clientBte = clientPool.get(device.getAddress());
                    clientBte.writeFile(new File(Environment.getExternalStorageDirectory(),"qq.jpg"));

                } else {
                    clientBte = new ClientBte(device);
                    if(clientBte.isConnected()) {
                        clientPool.put(device.getAddress(), clientBte);
                    }else {
                        Toast.makeText(getApplicationContext(), "无法连接"+mAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    clientBte.writeCmd("from client " + Build.MODEL + " : " + new Random().nextInt(1000));

                }

                //自动配对,传输数据则不用显示配对，会自动进行
//                try {
//                    if(device.getBondState()==BluetoothDevice.BOND_NONE){
//                        ClsUtils.createBond(BluetoothDevice.class,device);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        acceptThread = new AcceptThread();
        acceptThread.start();

        //获得已配对的设备，包括曾经配对的,必须是打开状态,一开始打开不行，有延迟
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        bluetoothDeviceList = new ArrayList<>(devices);
        mAdapter.setData(bluetoothDeviceList);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 88) {
            KLog.e("ok");
        }
    }

    @OnClick(R.id.btn_system_open)
    public void systemOpenBluetooth() {
        //会出现一个系统对话框，不美观
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 88);
    }

    @OnClick(R.id.btn_code_open)
    public void codeOpenBluetooth() {
        bluetoothAdapter.enable(); //打开蓝牙
        //bluetoothAdapter.disable();  //关闭蓝牙

    }

    @OnClick(R.id.btn_scan)
    public void scan() {

        tvStatus.setText("正在扫描...");


        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
                int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.STATE_OFF);
                switch (state){
                    case BluetoothAdapter.STATE_ON:
                        ToastUtils.show(getApplicationContext(),"蓝牙已打开");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        ToastUtils.show(getApplicationContext(),"正在打开蓝牙");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        ToastUtils.show(getApplicationContext(),"正在关闭蓝牙");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        ToastUtils.show(getApplicationContext(),"蓝牙已关闭");
                        break;
                }


            }else if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mAdapter.addData(device);
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_NONE:
                        //点了取消
                        tvStatus.setText("未配对..." + device.getName());
                        KLog.e(device.getName() + ":" + "未配对");
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        tvStatus.setText("配对中..." + device.getName());
                        KLog.e(device.getName() + ":" + "配对中");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        tvStatus.setText("已配对..." + device.getName());
                        KLog.e(device.getName() + ":" + "已配对");
                        break;
                    default:
                        break;
                }
            } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                tvStatus.setText("配对请求..." + device.getName());
                KLog.e("配对请求..." + device.getName());

                try {

                    //自动配对
                    //1.确认配对
                    ClsUtils.setPairingConfirmation(BluetoothDevice.class, device, true);
                    //2.终止有序广播
                    KLog.e("isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:" + isInitialStickyBroadcast());
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    // boolean ret = ClsUtils.setPin(BluetoothDevice.class, device, "1234");

                    //  KLog.e(ret);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                KLog.e("蓝牙已连接:"+device.getName());
                ToastUtils.show(getApplicationContext(),"蓝牙已连接:"+device.getName());

            }else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //断线之后，点击重连
                if(clientPool.containsKey(device.getAddress())){
                    lostBluetoothClient.add(device.getAddress());
                    clientPool.remove(device.getAddress());
                }
                if(serverPool.containsKey(device.getAddress())){
                    lostBluetoothServer.add(device.getAddress());
                }
                KLog.e("蓝牙已断开:"+device.getName());
                ToastUtils.show(getApplicationContext(),"蓝牙已断开:"+device.getName());
            }

        }

    };


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String ret = msg.obj.toString();
            tvStatus.append(ret + "\n");
        }
    };

    private class ClientBte {

        private BluetoothSocket socket;
        private CommunicateThread thread;


        public ClientBte(BluetoothDevice device) {
            try {
                socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                thread = new CommunicateThread(socket,FROM_CLIENT);
                threadList.add(thread);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
                KLog.e(e.getMessage());
            }
        }

        public boolean isConnected(){
            return socket.isConnected();
        }

        public void writeCmd(String data) {
            if(thread==null) return;
            thread.writeCmd(data);
        }

        public void writeFile(File file) {
            if(thread==null) return;
            thread.writeFile(file);
        }



    }

    private class AcceptThread extends Thread {

        private BluetoothServerSocket bluetoothServerSocket;
        private boolean isRunning = true;

        @Override
        public void run() {
            try {
                bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(name, uuid);
                while (true) {
                    if (!isRunning) {
                        return;
                    }
                    BluetoothSocket socket = bluetoothServerSocket.accept();
                    KLog.e("new socket coming");
                    CommunicateThread thread = new CommunicateThread(socket,FROM_SERVER);
                    serverPool.put(socket.getRemoteDevice().getAddress(),thread);
                    threadList.add(thread);
                    thread.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
                KLog.i(e.getMessage());
            }

        }

        public void cancel() {
            try {
                isRunning = false;
                if (bluetoothServerSocket != null)
                    bluetoothServerSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CommunicateThread extends Thread {

        private BluetoothSocket socket;
        private DataOutputStream os;
        private DataInputStream is;
        private boolean isRunning = true;
        private int type;

        public CommunicateThread(BluetoothSocket socket,int type) {
            try {
                this.socket = socket;
                this.type=type;
                os = new DataOutputStream(socket.getOutputStream());
                is = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                KLog.i(e.getMessage());
            }

        }

        public void writeCmd(String data) {
            try {
                if (os == null) return;
                os.writeChar('C');
                os.writeUTF(data);

            } catch (Exception e) {
                e.printStackTrace();
                KLog.i(e.getMessage());
            }
        }

        public void writeFile(File file) {
            try {
                if (os == null) return;
                os.writeChar('F');
                os.writeLong(file.length());
                os.writeUTF(file.getName());
                FileInputStream fis = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = fis.read(bytes, 0, bytes.length)) != -1) {
                    os.write(bytes, 0, len);
                }
                fis.close();

            } catch (Exception e) {
                e.printStackTrace();
                KLog.i(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (true) {

                BluetoothDevice device=socket.getRemoteDevice();
                if(type==FROM_CLIENT && lostBluetoothClient.contains(device.getAddress())){
                    isRunning=false;
                    lostBluetoothClient.remove(device.getAddress());
                }
                if(type==FROM_SERVER && lostBluetoothServer.contains(device.getAddress())){
                    isRunning=false;
                    lostBluetoothServer.remove(device.getAddress());
                }

                if (!isRunning) {
                    return;
                }
                try {
                    if (is == null) return;
                    char cmd = is.readChar();
                    if ('C' == cmd) {
                        Message msg = new Message();
                        msg.obj = is.readUTF();
                        mHandler.sendMessage(msg);
                        if(type==FROM_SERVER)
                        writeCmd("from server " + Build.MODEL + " : " + new Random().nextInt(1000));

                    } else if ('F' == cmd) {
                        long length = is.readLong();
                        String fname = is.readUTF();
                        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fname);

                        byte[] bytes = new byte[1024];
                        int len;
                        int totalSize = 0;
                        while ((len = is.read(bytes, 0, bytes.length)) != -1) {
                            fos.write(bytes, 0, len);
                            totalSize += len;
                            if (totalSize >= length) break;

                        }
                        fos.close();
                        Message msg = new Message();
                        msg.obj = fname + " 文件传输完毕";
                        mHandler.sendMessage(msg);
                        if(type==FROM_SERVER)
                        writeFile(new File(Environment.getExternalStorageDirectory(), "kfc.png"));

                    }

                    KLog.e("CommunicateThread");

                } catch (Exception e) {
                    e.printStackTrace();
                    KLog.i(e.getMessage());
                }
            }

        }


        private void cancel() {
            try {
                isRunning = false;
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (socket != null) {
                    socket.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        for (CommunicateThread thread : threadList) {
            thread.cancel();
            thread = null;
        }
        acceptThread.cancel();
        acceptThread = null;

    }
}
