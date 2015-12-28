/*
 * Copyright (c) 2015.
 * @Author：Cxb
 */

package com.bryan.studycodes.widget.sidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bryan.studycodes.R;

import java.util.Arrays;
import java.util.List;


public class SortedLetterBar extends View {

	public static String[] INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z"};

	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	private List<String> indexStrings;
	private int choose = -1;
	private Paint paint = new Paint();
	private int normalColor;
	private int pressedColor;

	private Rect indexBounds=new Rect();


	public SortedLetterBar(Context context) {
		this(context, null);
	}

	public SortedLetterBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SortedLetterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SortedLetterBar);
		normalColor=ta.getColor(R.styleable.SortedLetterBar_normalColor,Color.TRANSPARENT);
		pressedColor=ta.getColor(R.styleable.SortedLetterBar_pressedColor,Color.parseColor("#a1000000"));
		ta.recycle();
		indexStrings = Arrays.asList(INDEX_STRING);

		paint.getTextBounds("A",0,1,indexBounds);

	}


	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / indexStrings.size();

		for (int i = 0; i < indexStrings.size(); i++) {
			paint.setColor(Color.rgb(33, 65, 98));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(24);
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}

			float xPos = width / 2 - paint.measureText(indexStrings.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(indexStrings.get(i), xPos, yPos, paint);
			paint.reset();
		}

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode=MeasureSpec.getMode(widthMeasureSpec);
		int heightMode=MeasureSpec.getMode(heightMeasureSpec);
		int width=MeasureSpec.getSize(widthMeasureSpec);
		int height=MeasureSpec.getSize(heightMeasureSpec);
		if(widthMode==MeasureSpec.AT_MOST || widthMode==MeasureSpec.UNSPECIFIED ){
			 width=indexBounds.width()+30;
		}
		if(heightMode==MeasureSpec.AT_MOST || heightMode==MeasureSpec.UNSPECIFIED){
			height=(indexBounds.height()+30)*indexStrings.size();
		}
		setMeasuredDimension(width,height);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * indexStrings.size());

		switch (action) {
			case MotionEvent.ACTION_UP:
				setBackgroundColor(normalColor);
				if (listener != null) {
					listener.onTouchingLetterUp(indexStrings.get(c));
				}
				choose = -1;
				invalidate();
				break;
			default:
				setBackgroundColor(pressedColor);
				if (oldChoose != c) {
					if (c >= 0 && c < indexStrings.size()) {
						if (listener != null) {
							listener.onTouchingLetterChanged(indexStrings.get(c));
						}

						choose = c;
						invalidate();
					}
				}

				break;
		}
		return true;
	}

	public void setIndexText(List<String> indexStrings) {
		this.indexStrings = indexStrings;
		invalidate();
	}


	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
		void onTouchingLetterUp(String s);
	}

}