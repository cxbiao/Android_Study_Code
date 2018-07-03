package com.bryan.studycodes.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.chrisbanes.photoview.PhotoView;


/**
 * 自定义的View，我们就要处理好下面这几个函数，
 * 这样才能保证引用计数的正确性，否则可能就会引起内存泄露。
 * 其实就是要在View移除屏幕或进入屏幕去维护好引用计数了。
 * onAttachedToWindow()
 * onDetacherFromWindow()
 * onStartTemporaryDetach()
 * onFinishTemporaryDetach()
 * onTouchEvent()
 */
public class FrescoPhotoView extends PhotoView {

    private DraweeHolder<GenericDraweeHierarchy> mDraweeHolder;

    public FrescoPhotoView(Context context) {
        this(context, null);
    }

    public FrescoPhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public FrescoPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        selfInit();
    }

    private void selfInit() {
        if (mDraweeHolder == null) {
            final GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setProgressBarImage(new ProgressBarDrawable()).build();

            mDraweeHolder = DraweeHolder.create(hierarchy, getContext());
            mDraweeHolder.getTopLevelDrawable().setCallback(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDraweeHolder.onDetach();
        mDraweeHolder.getTopLevelDrawable().setCallback(null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDraweeHolder.onAttach();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        if (dr == mDraweeHolder.getTopLevelDrawable()) {
            return true;
        }
        return  super.verifyDrawable(dr);
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        mDraweeHolder.onDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        mDraweeHolder.onAttach();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDraweeHolder.onTouchEvent(event) || super.onTouchEvent(event);
    }


    public void setImageUri(String uri, ResizeOptions options) {

        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                .setResizeOptions(options)
                .setAutoRotateEnabled(true)
                .build();
        final ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        final AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeHolder.getController())
                .setImageRequest(imageRequest)
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);

                        CloseableReference<CloseableImage> imageCloseableReference = null;
                        try {
                            imageCloseableReference = dataSource.getResult();
                            if (imageCloseableReference != null) {
                                final CloseableImage image = imageCloseableReference.get();
                                if (image != null && image instanceof CloseableStaticBitmap) {
                                    CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                    final Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                    if (bitmap != null) {
                                        setImageBitmap(bitmap);
                                    }
                                }
                            }
                        } finally {
                            dataSource.close();
                            CloseableReference.closeSafely(imageCloseableReference);
                        }
                    }
                })
                .setTapToRetryEnabled(true)
                .build();
        mDraweeHolder.setController(controller);
       //setImageDrawable(mDraweeHolder.getTopLevelDrawable());
    }
}
