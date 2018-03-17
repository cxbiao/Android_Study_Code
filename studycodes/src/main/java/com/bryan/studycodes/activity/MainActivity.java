package com.bryan.studycodes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bryan.studycodes.R;
import com.bryan.studycodes.adapter.MainAdapter;
import com.bryan.studycodes.largeimage.LargeImageSample;
import com.bryan.studycodes.vdh.LeftDrawerLayoutActivity;
import com.bryan.studycodes.vdh.VDHActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity {

    public static final String TAG="MainActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    @BindView(R.id.navigation)
    NavigationView navigationView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;

    MainAdapter mAdapter;
    private List<String> titles =new ArrayList<>();
    private Class[] clazz=new Class[]{LargeImageSample.class,GridHeaderActivity.class,ProgressBarActivity.class
    ,MeasureActivity.class,MoveActivity.class,MessengerActivity.class,BookManagerActivity.class,ImageLoaderActivity.class
    ,CustomCameraActivity.class,LetterActivity.class,VDHActivity.class, LeftDrawerLayoutActivity.class,LocalSocketActivity.class,BottomSheetActivity.class
    ,FrescoActivity.class,WebViewActivity.class,BluetoothActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initTitle();

        mAdapter=new MainAdapter(this, titles);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new MainAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getBaseContext(), clazz[position]));

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });



    }

    private Handler mHandler=new Handler();



    public void  initView(){

        setSupportActionBar(toolbar);


        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open ,R.string.close);
        drawerToggle.syncState();
       //drawerLayout.addDrawerListener(drawerToggle);


        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT"))
                {

                    float leftScale = 1 - 0.3f * scale;

                    mMenu.setScaleX(leftScale);
                    mMenu.setScaleY(leftScale);
                    mMenu.setAlpha( 0.6f + 0.4f * (1 - scale));
                    mContent.setTranslationX(mMenu.getMeasuredWidth() * (1 - scale));
                    mContent.setPivotX( 0);
                    mContent.setPivotY(mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    mContent.setScaleX(rightScale);
                    mContent.setScaleY( rightScale);
                } else
                {
                    mContent.setTranslationX(-mMenu.getMeasuredWidth() * slideOffset);
                    mContent.setPivotX( mContent.getMeasuredWidth());
                    mContent.setPivotY(mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    mContent.setScaleX( rightScale);
                    mContent.setScaleY(rightScale);
                }
            }


        });
        navigationView= (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navItem1:
                        Snackbar.make(fabBtn, "item1", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navItem2:
                        Snackbar.make(fabBtn, "item2", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navItem3:
                        Snackbar.make(fabBtn, "item3", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.navItem4:
                        Snackbar.make(fabBtn, "item4", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "这是一个snackbar...", Snackbar.LENGTH_SHORT)
                        .setAction("点击", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("action;click");
                            }
                        }).show();
            }
        });


    }


    private void initTitle() {
        titles.add("巨图展示");
        titles.add("带header的gridview");
        titles.add("各种进度条");
        titles.add("测量");
        titles.add("滑动");
        titles.add("messenger");
        titles.add("AIDL");
        titles.add("ImageLoader");
        titles.add("自定义相机");
        titles.add("SortLetter");
        titles.add("ViewDragHelper");
        titles.add("LeftDrawer");
        titles.add("LocalSocket");
        titles.add("BottomSheet");
        titles.add("Fresco");
        titles.add("h5原生获取位置与图片上传");
        titles.add("蓝牙");


    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    protected void setStatusBar() {
//        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawerLayout), getResources().getColor(R.color.colorPrimary), 0);
//    }



}
