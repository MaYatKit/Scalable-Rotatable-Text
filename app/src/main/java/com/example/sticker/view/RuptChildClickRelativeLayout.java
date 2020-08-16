package com.example.sticker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class RuptChildClickRelativeLayout extends RelativeLayout {


    private boolean mIsNeedInterceptTouch;

    private RuptChildClickRelativeLayout.IIsNeedInterceptListener mInterceptListener;


    public RuptChildClickRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RuptChildClickRelativeLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsNeedInterceptTouch) {
            return mInterceptListener == null || mInterceptListener.needInterceptTouchEvent(this, ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isNeedInterceptTouch() {
        return mIsNeedInterceptTouch;
    }

    public void setNeedInterceptTouch(boolean needInterceptTouch) {
        mIsNeedInterceptTouch = needInterceptTouch;
    }


    public void setIsNeedInterceptListener(RuptChildClickRelativeLayout.IIsNeedInterceptListener listener) {
        mInterceptListener = listener;
    }

    public interface IIsNeedInterceptListener {
        boolean needInterceptTouchEvent(RuptChildClickRelativeLayout parent, MotionEvent ev);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
