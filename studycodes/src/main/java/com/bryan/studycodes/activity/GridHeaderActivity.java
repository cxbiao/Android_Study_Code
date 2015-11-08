package com.bryan.studycodes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.bryan.studycodes.R;
import com.bryan.studycodes.widget.GridViewWithHeaderAndFooter;

/**
 * Created by Administrator on 2015/10/29.
 */
public class GridHeaderActivity extends AppCompatActivity {


    GridViewWithHeaderAndFooter gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        gridView= (GridViewWithHeaderAndFooter) findViewById(R.id.gridview);

        View headview=View.inflate(this,R.layout.activity_grid_header,null);
        View footerView=View.inflate(this,R.layout.activity_grid_footer,null);
        gridView.addHeaderView(headview);
        gridView.addFooterView(footerView);
        gridView.setAdapter(new MyAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),position+"",Toast.LENGTH_SHORT).show();
            }
        });
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           if(convertView==null){
               convertView=View.inflate(GridHeaderActivity.this,R.layout.activity_grid_item,null);
           }
            return  convertView;
        }
    }


}
