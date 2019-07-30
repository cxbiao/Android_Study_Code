package com.bryan.studycodes.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bryan.studycodes.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Author：Cxb on 2016/9/12 17:05
 */
public class FrescoSingleActivity extends TitleBaseActivity {


    private static final String url2="http://img5.imgtn.bdimg.com/it/u=543126909,1030891202&fm=206&gp=0.jpg";
    //private static final String url3="http://cdn.duitang.com/uploads/blog/201403/03/20140303061128_hviXc.thumb.700_0.jpeg";
    private static final String url3="http://dfgcdn.duitang.com/uploads/blog/201403/03/20140303061128_hviXc.thumb.700_0.jpeg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco_single);
        setHeaderTitle("单张图片");

        simpleFresco();
        progressive();
        cornerround();
        circle();

    }



    //入门级使用fresco
    private void simpleFresco(){
        Uri uri= Uri.parse(url3);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.iv_img1);
        draweeView.setImageURI(uri);

        //同样在DataSubscriber中获取硬盘缓存
//        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(new SimpleCacheKey(url2));
//        if (resource != null && resource.getFile() != null) {
//            File f=resource.getFile();
//        }

        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FrescoSingleActivity.this,FrescoPhotoActivity.class);
                intent.putExtra("url",url3);
                startActivity(intent);
            }
        });
    }

    //渐进式加载网络图片
    private void progressive(){
        Uri uri=Uri.parse(url3);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.iv_img2);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)  //点击重新加载
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .build();
        //下方显示一个蓝色进度条
        draweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
        draweeView.setController(controller);



    }


    //圆角
    private void cornerround(){
        Uri uri= Uri.parse(url2);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.iv_img3);
        draweeView.setImageURI(uri);


    }

    //圆形
    private void circle() {
        Uri uri= Uri.parse(url2);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.iv_img4);
        draweeView.setImageURI(uri);
    }


}
