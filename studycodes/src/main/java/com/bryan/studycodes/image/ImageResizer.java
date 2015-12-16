package com.bryan.studycodes.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

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
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(path, options);
    }

    public Bitmap decodeSampleBitmapFromStream(InputStream is,int reqWidth,int reqHeight) throws IOException {
        MarkableInputStream markStream = new MarkableInputStream(is);
        is = markStream;
        long mark = markStream.savePosition(65536); // TODO fix this crap.
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        markStream.reset(mark);
        return BitmapFactory.decodeStream(is, null, options);
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
        Log.d(TAG, "sampleSize:"+sampleSize);
        return sampleSize;
    }
}
