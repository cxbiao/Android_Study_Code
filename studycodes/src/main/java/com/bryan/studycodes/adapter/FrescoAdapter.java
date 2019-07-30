package com.bryan.studycodes.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bryan.studycodes.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Author：Cxb on 2016/2/15 14:46
 */
public class FrescoAdapter extends RecyclerView.Adapter<FrescoAdapter.MyHolder> {


    private Context context;
    private String[] data;

    public FrescoAdapter(Context context, String[]data) {
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
        MyHolder holder=new MyHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item_image, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        holder.image.setAspectRatio(1f);

        holder.image.setImageURI(Uri.parse(data[position]));
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
        return data.length;
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView image;

        public MyHolder(View itemView) {
            super(itemView);
            image= (SimpleDraweeView) itemView.findViewById(R.id.image);
        }
    }
}
