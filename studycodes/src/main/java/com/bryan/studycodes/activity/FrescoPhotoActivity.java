package com.bryan.studycodes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bryan.studycodes.R;
import com.bryan.studycodes.widget.FrescoPhotoView;

/**
 * Author：Cxb on 2016/9/13 14:37
 */
public class FrescoPhotoActivity extends TitleBaseActivity {

    private FrescoPhotoView photoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_photo);

        photoView= (FrescoPhotoView) findViewById(R.id.photoView);
        setHeaderTitle("大图");

        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        photoView.setImageUri(url,null);


        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FrescoPhotoActivity.this, "111", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
