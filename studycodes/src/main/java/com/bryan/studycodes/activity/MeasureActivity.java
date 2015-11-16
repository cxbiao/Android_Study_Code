package com.bryan.studycodes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.bryan.studycodes.R;

/**
 * Created by bryan on 2015-11-08.
 * 获取控件大小的4种方法
 */
public class MeasureActivity extends AppCompatActivity {

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        tv1 = (TextView) findViewById(R.id.tv1);



       // measure2();
         measure3();
       // measure4();
    }


    /**
     * 方法一
     *
     * @param hasFocus  会执行多次
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int width = tv1.getMeasuredWidth();
            int height = tv1.getMeasuredHeight();
            System.out.println(height);
        }
    }

    /**
     * 方法二
     */
    private void measure2() {
        tv1.post(new Runnable() {
            @Override
            public void run() {
                int width = tv1.getMeasuredWidth();
                int height = tv1.getMeasuredHeight();
                System.out.println(height);
            }
        });
    }

    /**
     * 方法三
     */

    private void measure3() {
        tv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = tv1.getMeasuredWidth();
                int height = tv1.getHeight();
            }
        });
    }

    /**
     * 方法四
     */
    private void measure4() {
        //(不太好，此方法总是获取wrap_content时的大小)
        int widthSpec = View.MeasureSpec.makeMeasureSpec(1 << 30 - 1, View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(1 << 30 - 1, View.MeasureSpec.AT_MOST);
        // tv1.measure(widthSpec,heightSpec);
        tv1.measure(0, 0);  //这样也可以
        int width = tv1.getMeasuredWidth();
        int height = tv1.getMeasuredHeight();
    }


}
