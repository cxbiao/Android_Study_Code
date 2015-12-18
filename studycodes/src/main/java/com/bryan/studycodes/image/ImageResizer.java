package com.bryan.studycodes.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by bryan on 2015-11-29.
 */
public class ImageResizer {
    private static final String TAG = "ImageResizer";
    private static final double RATIO=2.5;
    private Context context;
    public ImageResizer(Context context){
        this.context=context;
    }
    public Bitmap decodeSampleBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public Bitmap decodeSampleBitmapFromFile(String path,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(path, options);
    }


    public Bitmap decodeSampleBitmapFromFileDes(FileDescriptor fd,int reqWidth,int reqHeight) throws IOException {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        if(isLargeImage(options)){
            return getRegionBmp(fd,options,reqWidth,reqHeight);
        }else{
            options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
            options.inJustDecodeBounds=false;
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        }

    }

    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int height=options.outHeight;
        int width=options.outWidth;
        int sampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio;
            final int widthRatio;
            if (reqHeight == 0) {
                sampleSize = (int) Math.floor((float) width / (float) reqWidth);
            } else if (reqWidth == 0) {
                sampleSize = (int) Math.floor((float) height / (float) reqHeight);
            } else {
                heightRatio = (int) Math.floor((float) height / (float) reqHeight);
                widthRatio = (int) Math.floor((float) width / (float) reqWidth);
                sampleSize =Math.max(heightRatio, widthRatio);
            }
        }
        Log.d(TAG, "sampleSize:" + sampleSize);
        return sampleSize;
    }


    public boolean isLargeImage(BitmapFactory.Options options){
        double height=options.outHeight;
        double width=options.outWidth;
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        int screenWidth=displayMetrics.widthPixels;
        int screenHeight=displayMetrics.heightPixels;
        if(height>width){
            if((height/width)>=RATIO && height>screenHeight){
                return true;
            }
        }else{
            if((width/height)>=RATIO && width>screenWidth){
                return true;
            }
        }
        return false;
    }

    public  Bitmap getRegionBmp(FileDescriptor fd,BitmapFactory.Options options,int reqWidth,int reqHeight) throws IOException {
        BitmapRegionDecoder bitmapRegionDecoder=BitmapRegionDecoder.newInstance(fd,false);
        return bitmapRegionDecoder.decodeRegion(new Rect(0,0,Math.min(reqWidth,options.outWidth),Math.min(reqHeight,options.outHeight)),null);
    }


}
