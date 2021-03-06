package com.bryan.studycodes.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.ImageAdapter;
import com.bryan.studycodes.image.ImageLoader;
import com.bryan.studycodes.utils.PermissionCode;
import com.bryan.studycodes.vdh.RevealBackgroundView;
import com.bryan.studycodes.vdh.SwipeBackLayout;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;
import com.zhy.m.permission.ShowRequestPermissionRationale;

/**
 * Created by bryan on 2015-11-29.
 * 带滑动返回
 */
public class ImageLoaderActivity extends TitleBaseActivity implements AbsListView.OnScrollListener,RevealBackgroundView.OnStateChangeListener {
    public final static String[] imageUrls = new String[]{
            "http://news.xinhuanet.com/photo/2015-09/23/128257164_14429624216661n.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383291_8239.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_9329.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383290_1042.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383275_3977.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383265_8550.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_3954.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_4787.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383264_8243.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383248_3693.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383243_5120.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_3127.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_9576.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383242_1721.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383219_5806.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383214_7794.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383213_4418.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383213_3557.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383210_8779.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383172_4577.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383166_3407.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383166_2224.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383166_7301.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383165_7197.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383150_8410.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383131_3736.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383130_5094.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383130_7393.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383129_8813.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383100_3554.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383093_7894.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383092_2432.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383092_3071.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383091_3119.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383059_6589.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383059_8814.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383059_2237.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383058_4330.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406383038_3602.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382942_3079.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382942_8125.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382942_4881.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382941_4559.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382941_3845.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382924_8955.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382923_2141.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382923_8437.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382922_6166.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382922_4843.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382905_5804.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382904_3362.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382904_2312.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382904_4960.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382900_2418.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382881_4490.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382881_5935.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382880_3865.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382880_4662.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382879_2553.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382862_5375.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382862_1748.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382861_7618.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382861_8606.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382861_8949.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382841_9821.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382840_6603.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382840_2405.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382840_6354.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382839_5779.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382810_7578.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382810_2436.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382809_3883.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382809_6269.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382808_4179.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382790_8326.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382789_7174.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382789_5170.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382789_4118.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382788_9532.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382767_3184.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382767_4772.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382766_4924.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382766_5762.jpg",
            "http://img.my.csdn.net/uploads/201407/26/1406382765_7341.jpg"
    };

    GridView gridView;
    ImageAdapter mAdapter;

    ImageView img_http;


    SwipeBackLayout swipeBackLayout;

    RevealBackgroundView mRevealBackgroundView;

    LinearLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);
        gridView = (GridView) findViewById(R.id.gridview);
        img_http= (ImageView) findViewById(R.id.img_http);
        swipeBackLayout= (SwipeBackLayout) findViewById(R.id.swipelayout);
        mRevealBackgroundView= (RevealBackgroundView) findViewById(R.id.revealView);
        mContentView= (LinearLayout) findViewById(R.id.contentView);

       //String file="file://"+ Environment.getExternalStorageDirectory().getAbsolutePath()+"/qq中国.jpg";
       String file="assets://qm.jpg";

       ImageLoader.getInstance(this).bindBitmap(file,img_http,R.mipmap.ic_launcher);
       // ImageLoader.getInstance(this).bindBitmap("http://imgphoto.gmw.cn/attachement/jpg/site2/20120511/001d0918b5731116c4ff3d.jpg",img_http,R.mipmap.ic_launcher);

       // mAdapter = new ImageAdapter(this, imageUrls);
      //  gridView.setAdapter(mAdapter);
      //  gridView.setOnScrollListener(this);
        swipeBackLayout.setCallBack(new SwipeBackLayout.CallBack() {
            @Override
            public void onFinish() {
                finish();
            }
        });
        setupRevealBackground();

        setHeaderTitle("图片加载");

    }


    public  void onPic(View v){
        if(!MPermissions.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionCode.REQUEST_SDCARD)){
            MPermissions.requestPermissions(this,PermissionCode.REQUEST_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

    }


    @ShowRequestPermissionRationale(PermissionCode.REQUEST_SDCARD)
    public void whyNeedCard(){
        new  AlertDialog.Builder(this)
                .setMessage("需要SD卡访问权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MPermissions.requestPermissions(ImageLoaderActivity.this,PermissionCode.REQUEST_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "用户拒绝授权!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }

    @PermissionGrant(PermissionCode.REQUEST_SDCARD)
    public void requestSdcardSuccess() {
        Toast.makeText(this, "授权成功!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK).setType("image/*");
        startActivityForResult(intent,3);
    }

    @PermissionDenied(PermissionCode.REQUEST_SDCARD)
    public void requestSdcardFailed() {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            Toast.makeText(getBaseContext(), "始终拒绝!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "用户拒绝授权!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==3){
            Uri uri=data.getData();
            String stringuri=uri.toString();
            System.out.println(stringuri);
            ImageLoader.getInstance(this).bindBitmap(stringuri, img_http, R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE || scrollState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            mAdapter.setmIsGridViewIdle(true);
        } else {
            mAdapter.setmIsGridViewIdle(false);
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void setupRevealBackground() {
        mRevealBackgroundView.setOnStateChangeListener(this);
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight=this.getWindowManager().getDefaultDisplay().getHeight();
        final int[] startingLocation = {screenWidth,screenHeight};
        mRevealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRevealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRevealBackgroundView.startFromLocation(startingLocation);
                return true;
            }
        });

    }

    @Override
    public void onStateChange(int state) {

        if (RevealBackgroundView.STATE_FINISHED == state) {
            mContentView.setVisibility(View.VISIBLE);
        }else {
            mContentView.setVisibility(View.INVISIBLE);
        }
    }
}
