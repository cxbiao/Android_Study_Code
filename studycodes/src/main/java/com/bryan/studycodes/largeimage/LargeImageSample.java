package com.bryan.studycodes.largeimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bryan.studycodes.R;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/10/22.
 */
public class LargeImageSample extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_largeimage_demo);

        image= (ImageView) findViewById(R.id.image);

        try {
            InputStream inputStream=getAssets().open("tangyan.jpg");

            //获得图片宽高
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(inputStream, null, options);
            int width=options.outWidth;
            int height=options.outHeight;

            //设置展示区域
            BitmapRegionDecoder bitmapRegionDecoder=BitmapRegionDecoder.newInstance(inputStream, false);
            options.inPreferredConfig= Bitmap.Config.RGB_565;
            Bitmap bitmap=bitmapRegionDecoder.decodeRegion(new Rect(width / 2 - 100, height / 2 - 100, width / 2 + 100, height / 2 + 100), options);
            width=bitmap.getWidth();  //200
            height=bitmap.getHeight(); //200
            image.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
