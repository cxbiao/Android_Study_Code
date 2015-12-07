package com.bryan.studycodes.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by bryan on 2015-11-29.
 */
public class ImageResizer {
    private static final String TAG = "ImageResizer";
    public ImageResizer(){

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
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(path,options);
    }

    public Bitmap decodeSampleBitmapFromFileDes(FileDescriptor fd,int reqWidth,int reqHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        if(reqWidth==0 || reqHeight==0){
            return 1;
        }
        int height=options.outHeight;
        int width=options.outWidth;
        Log.d(TAG, "origin,w="+width+" h="+height);
        int inSampleSize=1;
        if(height>reqHeight || width>reqWidth){
            int halfHeight=height/2;
            int halfWidth=width/2;

            while ((halfHeight/inSampleSize)>=reqHeight
                    && (halfWidth/inSampleSize)>=reqWidth){
                inSampleSize*=2;
            }
        }
        Log.d(TAG, "sampleSize:"+inSampleSize);
        return inSampleSize;
    }
}
