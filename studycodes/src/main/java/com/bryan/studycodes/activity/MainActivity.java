package com.bryan.studycodes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.bryan.studycodes.R;
import com.bryan.studycodes.largeimage.LargeImageSample;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG="MainActivity";
    @Bind(R.id.listview)
    ListView listview;

    private List<String> title=new ArrayList<>();
    private Class[] clazz=new Class[]{LargeImageSample.class,GridHeaderActivity.class,ProgressBarActivity.class
    ,MeasureActivity.class,MoveActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initTitle();
        BaseAdapter adapter=new ArrayAdapter<String>(this,R.layout.activity_main_item,R.id.item_tv,title);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(), clazz[position]));
            }
        });


    }

    private void initTitle() {
        title.add("巨图展示");
        title.add("带header的gridview");
        title.add("各种进度条");
        title.add("测量");
        title.add("滑动");
    }


}
