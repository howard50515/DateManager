package com.example.datemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
    public View view;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
