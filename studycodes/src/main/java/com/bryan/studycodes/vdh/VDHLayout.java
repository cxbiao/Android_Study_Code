/*
 * Copyright (c) 2015.
 * @Author：Cxb
 */

package com.bryan.studycodes.vdh;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 *

 onViewDragStateChanged

 当ViewDragHelper状态发生变化时回调（IDLE,DRAGGING,SETTING[自动滚动时]）

 onViewPositionChanged

 当captureview的位置发生改变时回调

 onViewCaptured

 当captureview被捕获时回调

 onViewReleased 已用

 onEdgeTouched

 当触摸到边界时回调。

 onEdgeLock

 true的时候会锁住当前的边界，false则unLock。

 onEdgeDragStarted 已用

 getOrderedChildIndex

 改变同一个坐标（x,y）去寻找captureView位置的方法。（具体在：findTopChildUnder方法中）

 getViewHorizontalDragRange 已用
 getViewVerticalDragRange 已用
 tryCaptureView 已用
 clampViewPositionHorizontal 已用
 clampViewPositionVertical 已用

 */
public class VDHLayout extends LinearLayout {
    private ViewDragHelper mViewDragHelper;

    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();
    public VDHLayout(Context context) {
        super(context);
        init();
    }



    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VDHLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        /**
         * 创建实例需要3个参数，第一个就是当前的ViewGroup，第二个sensitivity，主要用于设置touchSlop:
         */
        mViewDragHelper=ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            //tryCaptureView如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return (child==mEdgeTrackerView)?false:true;
            }

            //控制水平方向移动的位置
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int leftBound=getPaddingLeft();
                int rightBound=getWidth()-getPaddingRight()-child.getWidth();
                int newLeft=Math.min(Math.max(left,leftBound),rightBound);
                return newLeft;
            }

            //控制竖起方向移动的位置
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                //只能水平移动  return child.getTop();
                return top;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView)
                {
                    mViewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
            }


            //对于可以消耗事件的view ，需要重写getViewHorizontalDragRange和getViewVerticalDragRange
            //并返回大于0的数才可以捕获
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }
        });
        //开启左边界捕捉
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll()
    {
        if(mViewDragHelper.continueSettling(true))
        {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView=getChildAt(0);
        mAutoBackView=getChildAt(1);
        mEdgeTrackerView=getChildAt(2);
    }
}
