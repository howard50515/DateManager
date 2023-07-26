package com.example.datemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DateView extends LinearLayout {
    Context context;
    public String txtDate = "", txtReminder = "";
    public TextView txtViewDate, txtViewReminder, dragText;

    public ArrayList<DateInfo> data = new ArrayList<>();

    public DateView(Context context) {
        super(context);
        initView();
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DateView);
        getValues(typedArray);
        initView();
    }

    private void getValues(TypedArray typedArray){
        txtDate = typedArray.getString(R.styleable.DateView_txtDate);
        txtReminder = typedArray.getString(R.styleable.DateView_txtReminder);
        typedArray.recycle();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(){
        inflate(context, R.layout.component_dateview, this);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        txtViewDate = findViewById(R.id.dateText);
        txtViewReminder = findViewById(R.id.reminderText);
        dragText = findViewById(R.id.dragText);
        txtViewDate.setText(txtDate);
        txtViewReminder.setText(txtReminder);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        DateView.this.setBackgroundColor(getResources().getColor(R.color.light_gray, DateView.this.getContext().getTheme()));
                        break;
                    case MotionEvent.ACTION_UP:
                        DateView.this.setBackgroundColor(getResources().getColor(R.color.white, DateView.this.getContext().getTheme()));
                        //ViewManager.getInstance().dateInfoView.showView((Activity) context, data, 7, Integer.parseInt(txtDate));
                        break;
                }
                return true;
            }
        });
    }
}
