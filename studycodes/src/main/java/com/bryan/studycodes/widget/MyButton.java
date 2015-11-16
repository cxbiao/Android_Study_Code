package com.bryan.studycodes.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.Button;
import android.widget.Scroller;

/**
 * Created by bryan on 2015-11-15.
 */
public class MyButton extends Button {

    private static final String TAG="MyButton";
    private static final int DURATION=1000;
    private Scroller mScroller;
    private VelocityTracker velocityTracker;
    public MyButton(Context context) {
        super(context);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init(){
        mScroller=new Scroller(getContext());
        velocityTracker=VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        //计算1000ms内的滑动距离
        velocityTracker.computeCurrentVelocity(1000);
       float xSpeed= velocityTracker.getXVelocity();
        float ySpeed=velocityTracker.getYVelocity();
        Log.e(TAG, "xSpeed:" + xSpeed + ",ySpeed:" + ySpeed);
        velocityTracker.getYVelocity();
        return super.onTouchEvent(event);
    }


    public void smoothScrollTo(int destX,int destY){
        int scollX=getScrollX();
        int scollY=getScrollY();
        int deltaX=destX-scollX;
        int deltaY=destY-scollY;
        //1000ms内慢慢移动
        mScroller.startScroll(scollX, scollY, deltaX, deltaY, DURATION);
        invalidate();
    }

    public void smoothScrollBy(int deltaX,int deltaY){
        int scollX=getScrollX();
        int scollY=getScrollY();
        //1000ms内慢慢移动
        mScroller.startScroll(scollX,scollY,deltaX,deltaY,DURATION);
        invalidate();
    }

    @Override
    public void computeScroll() {
       if(mScroller.computeScrollOffset()){
           scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
           postInvalidate();
       }
    }


}
