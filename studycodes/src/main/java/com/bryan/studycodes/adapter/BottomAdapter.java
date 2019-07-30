package com.bryan.studycodes.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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
public class BottomAdapter extends RecyclerView.Adapter<BottomAdapter.MyHolder> {


    private Context context;
    private List<String> data = new ArrayList<>();

    public BottomAdapter(Context context, List data) {
        this.context=context;
        this.data = data;
    }


    public interface OnItemClickLitener
    {
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
        MyHolder holder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_bottom, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
         String title=data.get(position);
         holder.tv_title.setText(title);
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
