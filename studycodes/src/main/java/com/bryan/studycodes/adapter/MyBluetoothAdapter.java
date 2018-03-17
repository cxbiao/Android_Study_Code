package com.bryan.studycodes.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bryan.studycodes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Cxb on 2016/2/15 14:46
 */
public class MyBluetoothAdapter extends RecyclerView.Adapter<MyBluetoothAdapter.MyHolder> {


    private Context context;
    private List<BluetoothDevice> data = new ArrayList<>();

    public MyBluetoothAdapter(Context context) {
        this.context=context;
    }


    public void setData(List<BluetoothDevice> data){
        this.data=data;
        notifyDataSetChanged();
    }

    public void addData(BluetoothDevice device){
        if(device==null) return;
        this.data.add(0,device);
        notifyItemInserted(0);
    }

    public void addData(List<BluetoothDevice> devices){
        if(devices==null || devices.size()==0) return;
        this.data.addAll(devices);
        notifyItemRangeInserted(this.data.size(),devices.size());
    }

    public void remove(int index){
        this.data.remove(index);
        notifyItemRemoved(index);
    }

    public BluetoothDevice getItem(int index){
        return data.get(index);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_bluetooth, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
         BluetoothDevice device=data.get(position);


         StringBuilder sb=new StringBuilder();
         sb.append(device.getName()+":"+device.getAddress()+";");
         switch (device.getBondState()){
             case BluetoothDevice.BOND_NONE:
                 sb.append("未配对");
                 break;
             case BluetoothDevice.BOND_BONDING:
                 sb.append("正在配对");
                 break;
             case BluetoothDevice.BOND_BONDED:
                 sb.append("已配对");
                 break;
         }
         holder.tv_title.setText(sb.toString());
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //获得正确的下标位置
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;

        public MyHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
