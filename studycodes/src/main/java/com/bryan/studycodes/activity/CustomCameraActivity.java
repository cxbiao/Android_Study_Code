package com.bryan.studycodes.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.bryan.studycodes.R;
import com.bryan.studycodes.utils.KLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by bryan on 2015-12-05.
 */
public class CustomCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{


    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private Camera.PictureCallback mPictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            int length=data.length;
            //横屏数据
            File file=new File("/sdcard/temp.jpg");
            try {
                FileOutputStream fos=new FileOutputStream(file);
                fos.write(data);
                fos.close();
                setStartPrieview(mCamera,mHolder);
                Toast.makeText(getBaseContext(),"拍照成功!",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        mSurfaceView= (SurfaceView) findViewById(R.id.surfaceView);
        mHolder=mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });


    }

    public void capture(View v){
        Camera.Parameters  parameters=mCamera.getParameters();
        Camera.Size size= mCamera.getParameters().getPictureSize();
        List<Camera.Size> listSize= parameters.getSupportedPictureSizes();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPictureSize(1840, 3264);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            }
        });
    }

    private Camera getCamera(){
        Camera  camera;
        try {
            camera=Camera.open();
            if(mHolder!=null){
                setStartPrieview(mCamera,mHolder);
            }

        }catch (Exception ex){
            camera=null;
            ex.printStackTrace();
        }
        return camera;

    }

    private void setStartPrieview(Camera camera,SurfaceHolder holder){
        try {
            camera.setPreviewDisplay(holder);
            //将camera预览角度进行调整，默认横屏
           camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera(){
        if(mCamera!=null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }

    }

    @Override
    protected void onResume() {
        KLog.e("onResume");
        super.onResume();
        if(mCamera==null){
            mCamera=getCamera();
            List<Camera.Size> sizes=mCamera.getParameters().getSupportedPreviewSizes();
            if(mHolder!=null){
                mCamera.getParameters().setPreviewSize(sizes.get(0).width,sizes.get(0).height);
                setStartPrieview(mCamera, mHolder);

            }
        }
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                KLog.e("onPreviewFrame");
                Camera.Size size = camera.getParameters().getPreviewSize();
                int length = data.length;
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream("/sdcard/preview.jpg");
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, out);
                    out.close();
                    byte[] bytes = out.toByteArray();
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Matrix m = new Matrix();
                    m.setRotate(90, bmp.getWidth() / 2, bmp.getHeight() / 2);
                    Bitmap dest = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),bmp.getConfig());
                    Canvas canvas = new Canvas(dest);
                    canvas.drawBitmap(bmp, m, null);
                    dest.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onPause() {
        KLog.e("onPause");
        super.onPause();
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        KLog.e("surfaceCreated");
       setStartPrieview(mCamera,mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        KLog.e("surfaceChanged");
//        if(mCamera!=null){
//            mCamera.stopPreview();
//            setStartPrieview(mCamera, mHolder);
//        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        KLog.e("surfaceDestroyed");
       releaseCamera();
    }
}
