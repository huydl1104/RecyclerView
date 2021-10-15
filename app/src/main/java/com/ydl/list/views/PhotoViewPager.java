package com.ydl.list.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ydl.list.layout.NestedViewPager;


/**
 *  ViewPager 中使用 PhotoView 时出现 pointerIndex out of range 异常
 */
public final class PhotoViewPager extends NestedViewPager {

    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 当 PhotoView 和 ViewPager 组合时 ，用双指进行放大时 是没有问题的，但是用双指进行缩小的时候，程序就会崩掉
        // 并且抛出 java.lang.IllegalArgumentException: pointerIndex out of range
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }
}