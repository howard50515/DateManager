package com.example.datemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AnimatedTextView extends androidx.appcompat.widget.AppCompatTextView {
    public Drawable background_unfocus, background_focus;

    public AnimatedTextView(Context context) {
        super(context);
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimatedTextView);
        getValues(typedArray);
        initView();
    }

    private void getValues(TypedArray typedArray) {
        background_unfocus = typedArray.getDrawable(R.styleable.AnimatedTextView_background_unfocus);
        background_focus = typedArray.getDrawable(R.styleable.AnimatedTextView_background_focus);
        typedArray.recycle();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(){
        this.setOnTouchListener(new OnTouchListener() {
            public boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        AnimatedTextView.this.setBackground(background_focus);
                        if (onActionDownListener != null)
                            onActionDownListener.onActionDown();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > AnimatedTextView.this.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > AnimatedTextView.this.getHeight() || motionEvent.getY() < 0;

                        if (!isOutside)
                            AnimatedTextView.this.setBackground(background_focus);
                        else
                            AnimatedTextView.this.setBackground(background_unfocus);
                        if (onActionMoveListener != null)
                            onActionMoveListener.onActionMove();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        AnimatedTextView.this.setBackground(background_unfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        AnimatedTextView.this.setBackground(background_unfocus);
                        if (!isOutside && onActionUpListener != null)
                            onActionUpListener.onActionUp();
                        break;
                }

                return true;
            }
        });
    }

    public OnActionUpListener onActionUpListener;
    public OnActionMoveListener onActionMoveListener;
    public OnActionDownListener onActionDownListener;

    public void setOnActionUpListener(OnActionUpListener listener){
        this.onActionUpListener = listener;
    }

    public void setOnActionMoveListener(OnActionMoveListener listener){
        this.onActionMoveListener = listener;
    }

    public void setOnActionDownListener(OnActionDownListener listener){
        this.onActionDownListener = listener;
    }

    public interface OnActionUpListener {
        void onActionUp();
    }

    public interface OnActionMoveListener {
        void onActionMove();
    }

    public interface OnActionDownListener {
        void onActionDown();
    }
}
