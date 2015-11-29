package com.bryan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bryan.studycodes.R;
import com.bryan.studycodes.image.ImageLoader;

/**
 * Created by bryan on 2015-11-29.
 */
public class ImageAdapter extends BaseAdapter {

    private String[] imageUrls;
    private Context context;
    private int mItemHeight;
    private boolean mIsGridViewIdle=true;

    public ImageAdapter(Context context,String[] imageUrls){
        this.context=context;
        this.imageUrls=imageUrls;
    }

    public boolean ismIsGridViewIdle() {
        return mIsGridViewIdle;
    }

    public void setmIsGridViewIdle(boolean mIsGridViewIdle) {
        this.mIsGridViewIdle = mIsGridViewIdle;
        notifyDataSetChanged();
    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public String getItem(int position) {
        return imageUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.activity_image_item,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ImageView iv=viewHolder.imageView;
        String url= getItem(position);
        if(mIsGridViewIdle){
            ImageLoader.getInstance(context).bindBitmap(url,iv,R.mipmap.ic_launcher,mItemHeight,mItemHeight);
       }

        return convertView;
    }

    class  ViewHolder {
        ImageView imageView;
    }
}
