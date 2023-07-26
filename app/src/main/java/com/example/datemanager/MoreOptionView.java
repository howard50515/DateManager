package com.example.datemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MoreOptionView extends LinearLayout implements IClickOutside {
    public boolean isActivate;
    public Context context;
    public Button btEdit, btDelete, btCopy;
    public MatterInfo matterInfo;

    public MoreOptionView(Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.window_moreoptionview, this);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        btEdit = findViewById(R.id.btEdit);
        btDelete = findViewById(R.id.btDelete);
        btCopy = findViewById(R.id.btCopy);
        btEdit.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btEdit.setBackgroundResource(R.drawable.background_rectangle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btEdit.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btEdit.getHeight() || motionEvent.getY() < 0;
                        if (!isOutside)
                            btEdit.setBackgroundResource(R.drawable.background_rectangle_focus);
                        else
                            btEdit.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        btEdit.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        if (!isOutside) {
                            closeView();
                            ViewManager.getInstance().addInfoView.showView((Activity) context, matterInfo);
                        }
                        break;
                }

                return true;
            }
        });
        btDelete.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btDelete.setBackgroundResource(R.drawable.background_rectangle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btDelete.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btDelete.getHeight() || motionEvent.getY() < 0;
                        if (!isOutside)
                            btDelete.setBackgroundResource(R.drawable.background_rectangle_focus);
                        else
                            btDelete.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        btDelete.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        if (!isOutside) {
                            DataManager.getInstance().removeData(matterInfo);
                            closeView();
                        }
                        break;
                }

                return true;
            }
        });
        btCopy.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btCopy.setBackgroundResource(R.drawable.background_rectangle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btCopy.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btCopy.getHeight() || motionEvent.getY() < 0;
                        if (!isOutside)
                            btCopy.setBackgroundResource(R.drawable.background_rectangle_focus);
                        else
                            btCopy.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        btCopy.setBackgroundResource(R.drawable.background_rectangle_transparent_unfocus);
                        if (!isOutside)
                            closeView();
                        break;
                }

                return true;
            }
        });
    }

    public void showView(Activity activity, int x, int y, MatterInfo matterInfo){
        if (isActivate) closeView();
        this.matterInfo = matterInfo;

        ViewManager.getInstance().addLayer(this);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = x - 200;
        params.topMargin = y;
        activity.addContentView(this, params);
        isActivate = true;
    }

    public void closeView(){
        if (!isActivate) return;

        ViewManager.getInstance().removeLayer(this);

        ((ViewGroup) this.getParent()).removeView(this);
        isActivate = false;
    }
}
