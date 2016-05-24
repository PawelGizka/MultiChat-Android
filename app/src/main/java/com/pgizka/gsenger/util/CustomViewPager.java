package com.pgizka.gsenger.util;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Custom implementation of view pager. Standard view pager does not allow to turn of swapping.
 * This implementation only intercept touch events.
 */
public class CustomViewPager extends ViewPager {

    private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            try {
                return super.onTouchEvent(event);
            } catch (Exception e) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setSwipingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
