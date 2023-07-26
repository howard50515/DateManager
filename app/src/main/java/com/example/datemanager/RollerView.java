package com.example.datemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class RollerView extends RelativeLayout {
    public Context context;
    public TextView[] rollerTexts = new TextView[5];
    private ScrollView scrollView;
    public int scrollViewId;
    public String[] display;
    public int displayIndex;
    private ViewTreeObserver observer;

    public interface OnTextChangedListener{
        void onTextChanged();
    }
    public OnTextChangedListener listener = null;

    public RollerView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public RollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RollerView);
        getValues(typedArray);
        initView();
    }

    public void setOnTextChangedListener(OnTextChangedListener listener){
        this.listener = listener;
    }

    public String getCurrentText(){
        return rollerTexts[2].getText().toString();
    }

    public void setIndex(String value){
        for (int i = 0;i < display.length;i++){
            if (display[i].equals(value)){
                displayIndex = i;
                break;
            }
        }

        redisplay();
    }

    private void getValues(TypedArray typedArray){
        DisplayArray mode = DisplayArray.values()[typedArray.getInt(R.styleable.RollerView_displayArray, 0)];
        switch (mode){
            case HOURS:
                display = getResources().getStringArray(R.array.hours);
                break;
            case MINUTES:
                display = getResources().getStringArray(R.array.minutes);
                break;
        }
        scrollViewId = typedArray.getResourceId(R.styleable.RollerView_scrollView, 0);
        typedArray.recycle();
    }

    private void getScrollView(){
        if (scrollView == null){
            ViewParent temp = getParent();
            ViewGroup parent = null;
            if (temp instanceof ViewGroup)
                parent = (ViewGroup) temp;
            while (parent != null && scrollView == null){
                scrollView = parent.findViewById(scrollViewId);
                temp = parent.getParent();
                if (temp instanceof ViewGroup)
                    parent = (ViewGroup) temp;
                else
                    parent = null;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(){
        inflate(context, R.layout.component_rollerview, this);
        observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RollerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                getScrollView();
            }
        });
        rollerTexts[0] = findViewById(R.id.rollerText0);
        rollerTexts[1] = findViewById(R.id.rollerText1);
        rollerTexts[2] = findViewById(R.id.rollerText2);
        rollerTexts[3] = findViewById(R.id.rollerText3);
        rollerTexts[4] = findViewById(R.id.rollerText4);
        redisplay();
        this.setOnTouchListener(new OnTouchListener() {
            private float lastY, middleTextY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        lastY = motionEvent.getY();
                        middleTextY = rollerTexts[2].getY();
                        if (scrollView != null)
                            scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float diffY = motionEvent.getY() - lastY;
                        rollText(diffY, middleTextY);
                        lastY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (scrollView != null)
                            scrollView.requestDisallowInterceptTouchEvent(false);
                        resetTextY(middleTextY);
                        break;
                }

                return true;
            }
        });
    }

    private void setYAndScale(float diffY, float middleTextY){
        for (int i = 0;i<5;i++){
            rollerTexts[i].setY(rollerTexts[i].getY() + diffY);
            rollerTexts[i].setScaleX(1.5f - Math.abs(rollerTexts[i].getY() - middleTextY)/150);
            rollerTexts[i].setScaleY(1.5f - Math.abs(rollerTexts[i].getY() - middleTextY)/150);
        }
    }

    private void rollText(float diffY, float middleTextY){
        if (Math.abs(rollerTexts[2].getY() - middleTextY) > 60){
            if (rollerTexts[2].getY() < middleTextY){
                displayIndex++;
                if (displayIndex >= display.length)
                    displayIndex -= display.length;
            } else {
                displayIndex--;
                if (displayIndex < 0)
                    displayIndex += display.length;
            }
            redisplay();
            rollTextY(middleTextY);
            if (listener != null)
                listener.onTextChanged();
        }

        setYAndScale(diffY, middleTextY);
    }

    private void redisplay(){
        int index = displayIndex - 2;
        if (index < 0)
            index += display.length;
        for (int i = 0;i < 5;i++){
            rollerTexts[i].setText(display[index]);
            index++;
            if (index >= display.length)
                index -= display.length;
        }
    }

    private void rollTextY(float middleTextY) {
        float diffY = rollerTexts[2].getY() - rollerTexts[3].getY();
        int multiplier = rollerTexts[2].getY() > middleTextY ? 1 : -1;
        for (int i = 0;i < 5;i++){
            rollerTexts[i].setY(rollerTexts[i].getY() + multiplier * diffY);
        }
    }

    private void resetTextY(float middleTextY){
        float diffY = middleTextY - rollerTexts[2].getY();

        setYAndScale(diffY, middleTextY);
    }

    public enum DisplayArray{
        HOURS, MINUTES
    }
}
