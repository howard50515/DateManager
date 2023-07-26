package com.example.datemanager;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

public class MatterView extends LinearLayout {
    public Context context;
    public String txtTime, txtTopic;
    public MatterInfo matterInfo;
    public MyScrollView scrollView;
    public RelativeLayout detailInfoView;
    public TextView txtViewTime, txtViewTopic, txtViewDateTime, txtViewRemind, txtViewRemark;
    public Button btMoreOption;
    public boolean isDetailViewActivated;

    public MatterView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MatterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MatterView);
        getValues(typedArray);
        initView();
    }

    private void getValues(TypedArray typedArray) {
        txtTime = typedArray.getString(R.styleable.MatterView_txtTime);
        txtTopic = typedArray.getString(R.styleable.MatterView_txtTopic);
        typedArray.recycle();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        inflate(context, R.layout.component_matterview, this);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        detailInfoView = findViewById(R.id.detailInfoView);
        txtViewTime = findViewById(R.id.txtTime);
        txtViewTopic = findViewById(R.id.txtTopic);
        txtViewDateTime = findViewById(R.id.txtDateTime);
        txtViewRemind = findViewById(R.id.txtRemind);
        txtViewRemark = findViewById(R.id.txtRemark);
        btMoreOption = findViewById(R.id.btMoreOption);
        txtViewTime.setText(txtTime);
        txtViewTopic.setText(txtTopic);
        this.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        if (ViewManager.getInstance().moreOptionView.isActivate)
                            ViewManager.getInstance().moreOptionView.closeView();
                        MatterView.this.setBackgroundColor(getResources().getColor(R.color.light_gray, MatterView.this.getContext().getTheme()));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > MatterView.this.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > MatterView.this.getHeight() || motionEvent.getY() < 0;

                        if (!isOutside)
                            MatterView.this.setBackgroundColor(getResources().getColor(R.color.light_gray, MatterView.this.getContext().getTheme()));
                        else
                            MatterView.this.setBackgroundColor(getResources().getColor(R.color.white, MatterView.this.getContext().getTheme()));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        MatterView.this.setBackgroundColor(getResources().getColor(R.color.white, MatterView.this.getContext().getTheme()));
                        break;
                    case MotionEvent.ACTION_UP:
                        MatterView.this.setBackgroundColor(getResources().getColor(R.color.white, MatterView.this.getContext().getTheme()));
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        if (!isOutside) {
                            isDetailViewActivated = !isDetailViewActivated;
                            if (isDetailViewActivated)
                                AnimationManager.expand(MatterView.this.detailInfoView, 10);
                            else
                                AnimationManager.collapse(MatterView.this.detailInfoView, 5);
                        }
                        break;
                }

                return true;
            }
        });

        btMoreOption.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btMoreOption.setBackgroundResource(R.drawable.background_rectangle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btMoreOption.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btMoreOption.getHeight() || motionEvent.getY() < 0;

                        if (!isOutside)
                            btMoreOption.setBackgroundResource(R.drawable.background_rectangle_focus);
                        else
                            btMoreOption.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        btMoreOption.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        if (!isOutside) {
                            int y = MatterView.this.getBottom() + 350 > ((ViewGroup) MatterView.this.getParent()).getHeight() ?
                                    (int) (motionEvent.getRawY() - 500) : (int) motionEvent.getRawY();
                            ViewManager.getInstance().moreOptionView.showView((Activity) context, (int) motionEvent.getRawX(), y, matterInfo);
                        }
                        break;
                }

                return true;
            }
        });
    }

    public void closeDetailInfoView(){
        if (isDetailViewActivated)
            detailInfoView.setVisibility(GONE);
        isDetailViewActivated = false;
    }

    @SuppressLint("SetTextI18n")
    public void setMatterInfo(MatterInfo matterInfo){
        this.matterInfo = matterInfo;
        setTxtTopic(matterInfo.topic);
        setTxtTime(matterInfo.getTimeStart());
        txtViewDateTime.setText(matterInfo.getDateStart() + " -> " + matterInfo.getDateEnd());
        setRemindInfo(matterInfo.remindInfo);
        txtViewRemark.setText(matterInfo.remark.equals("") ? "無備註" : matterInfo.remark);
    }

    private void setTxtTime(String txtTime){
        this.txtTime = txtTime;
        this.txtViewTime.setText(txtTime);
    }

    private void setTxtTopic(String txtTopic){
        this.txtTopic = txtTopic;
        this.txtViewTopic.setText(txtTopic);
    }

    private void setRemindInfo(RemindInfo remindInfo){
        String strRemind = "";
        if (remindInfo.firstRemind == -1){
            strRemind = "無提醒";
        }
        else {
            switch (remindInfo.mode) {
                case GENERAL:
                    if (remindInfo.firstRemind == 0)
                        strRemind = "在活動開始時提醒一次";
                    else
                        strRemind = String.format(Locale.TAIWAN,
                            "在活動開始前%d分鐘提醒一次",
                            remindInfo.firstRemind);
                    break;
                case REPEAT:
                    strRemind = String.format(Locale.TAIWAN,
                            "在活動開始前%d分鐘\n每過%d分鐘提醒一次",
                            remindInfo.firstRemind,
                            remindInfo.remindInterval);
                    break;
            }
        }

        txtViewRemind.setText(strRemind);
    }
}
