package com.example.datemanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class RemindModeView extends RelativeLayout {
    public Context context;
    public RemindMode remindMode;
    public LinearLayout generalModeView, repeatModeView;
    public RemindInfoView generalInfoView, repeatInfoView1, repeatInfoView2;

    public RemindModeView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public RemindModeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RemindModeView);
        getValues(typedArray);
        initView();
    }

    private void getValues(TypedArray typedArray) {
        remindMode = RemindMode.values()[typedArray.getInt(R.styleable.RemindModeView_remindMode, 0)];
        typedArray.recycle();
    }

    private void initView() {
        inflate(context, R.layout.component_remindmodeview, this);
        generalModeView = findViewById(R.id.generalModeView);
        repeatModeView = findViewById(R.id.repeatModeView);
        generalInfoView = findViewById(R.id.generalInfoView);
        repeatInfoView1 = findViewById(R.id.repeatInfoView1);
        repeatInfoView2 = findViewById(R.id.repeatInfoView2);

        selectMode(remindMode);
    }

    public void selectMode(RemindMode mode){
        switch (mode){
            case GENERAL:
                AnimationManager.expand(generalModeView, 5);
                repeatModeView.setVisibility(GONE);
                break;
            case REPEAT:
                generalModeView.setVisibility(GONE);
                AnimationManager.expand(repeatModeView, 5);
                break;
        }
        remindMode = mode;
    }

    public RemindInfo getRemindInfo(){
        RemindInfo remindInfo;

        switch (remindMode){
            case GENERAL:
                remindInfo = new RemindInfo(generalInfoView.getNumber(), -1);
                break;
            case REPEAT:
                remindInfo = new RemindInfo(repeatInfoView1.getNumber(), repeatInfoView2.getNumber());
                break;
            default:
                remindInfo = new RemindInfo(-1, -1);
                break;
        }

        remindInfo.mode = remindMode;

        return remindInfo;
    }

    public void resetView(){
        generalInfoView.inputNumber.setText("");
        repeatInfoView1.inputNumber.setText("");
        repeatInfoView2.inputNumber.setText("");
    }
}
