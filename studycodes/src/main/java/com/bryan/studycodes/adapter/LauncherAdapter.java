package com.bryan.studycodes.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bryan.studycodes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：Cxb on 2016/2/15 14:46
 */
public class LauncherAdapter extends RecyclerView.Adapter<LauncherAdapter.MyHolder> {


    private Context context;
    private List<ResolveInfo> data = new ArrayList<>();

    public LauncherAdapter(Context context,List<ResolveInfo> data) {
        this.context=context;
        this.data=data;
    }


    public void setData(List<ResolveInfo> data){
        this.data=data;
        notifyDataSetChanged();
    }

    public void addData(ResolveInfo data){
        if(data==null) return;
        this.data.add(0,data);
        notifyItemInserted(0);
    }

    public void addData(List<ResolveInfo> datas){
        if(datas==null || datas.size()==0) return;
        this.data.addAll(datas);
        notifyItemRangeInserted(this.data.size(),datas.size());
    }

    public void remove(int index){
        this.data.remove(index);
        notifyItemRemoved(index);
    }

    public ResolveInfo getItem(int index){
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
        MyHolder holder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_launcher, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        ResolveInfo resolveInfo=data.get(position);

        holder.iv.setImageDrawable(resolveInfo.loadIcon(context.getPackageManager()));

        String tx= (String) resolveInfo.loadLabel(context.getPackageManager());
        holder.tv.setText(tx);

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

        private TextView tv;
        private ImageView iv;

        public MyHolder(View itemView) {
            super(itemView);
            tv= itemView.findViewById(R.id.tv);
            iv= itemView.findViewById(R.id.iv);
        }
    }
}
