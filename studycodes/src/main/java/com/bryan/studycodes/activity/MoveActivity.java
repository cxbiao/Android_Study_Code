package com.bryan.studycodes.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bryan.studycodes.R;
import com.bryan.studycodes.widget.MyButton;

/**
 * Created by bryan on 2015-11-15.
 *
 * mScrollX的值总是等View左边缘和View内容左边缘在水平方向的距离
 * 而mScrollY的值总是等View上边缘和View内容上边缘在竖起方向的距离
 * View边缘指View的位置，由四个顶点组成，而View内容边缘是指View中的内容的边缘,
 * scrollTo和scrollBy只能改变View内容的位置而不能改变View在布局中的位置
 *
 * 当View左边缘在View内容左边缘的右边时,mScrollX为正值,反之为负值
 * 当View上边缘在View内容上边缘的下边，mScrollY为正值，反之为负值
 * 即从左向右滑动，mScrollX为负值，反之为正值
 * 从上往下滑动,mScrollY为负值，反之为正值
 */
public class MoveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        findViewById(R.id.ll_move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 v.scrollTo(100, 100);   //向左上滑动

            }
        });
    }

    /**
     *内容滑动
     * @param v
     */
    public void move(View v){
        v.scrollBy(20, 20);
        //v.offsetLeftAndRight(20); //改变view的位置
       // v.offsetTopAndBottom(20);

    }


    public void scrollMove(View v){
        //((MyButton)v).smoothScrollTo(-150,0);//水平向右移动150px
       ((MyButton)v).smoothScrollBy(50, 10);
    }
}
