package com.example.datemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class DateInfoView extends LinearLayout implements IClickOutside {
    public boolean isActivate;
    public Context context;
    public TextView dragText, txtDate;
    public MyScrollView scrollView;
    public LinearLayout body;
    public Button btAdd;
    public ArrayList<MatterView> infoView = new ArrayList<>();

    public DataManager.OnBankDataChangedListener listener = new DataManager.OnBankDataChangedListener() {
        @Override
        public void onBankDataChanged() {
            setView((Activity) context, DataManager.getInstance().findDateData(date));
        }
    };

    public String date;

    public DateInfoView(Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.window_dateinfoview, this);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        body = findViewById(R.id.body);
        dragText = findViewById(R.id.dragText);
        scrollView = findViewById(R.id.scrollView);
        txtDate = findViewById(R.id.txtDate);
        btAdd = findViewById(R.id.btAdd);
        this.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_UP){
                    ViewManager.getInstance().mouseClick(DateInfoView.this);
                }

                return true;
            }
        });
        dragText.setOnTouchListener(new OnTouchListener() {
            private float startY, viewY, newY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        startY = motionEvent.getY();
                        viewY = DateInfoView.this.getY();
                        newY = Math.max(DateInfoView.this.getY() + (motionEvent.getY() - startY), viewY);
                        dragText.setBackgroundResource(R.drawable.background_rectangle_radius_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        newY = Math.max(DateInfoView.this.getY() + (motionEvent.getY() - startY), viewY);
                        DateInfoView.this.setY(newY);
                        break;
                    case MotionEvent.ACTION_UP:
                        DateInfoView.this.setY(viewY);
                        ViewManager.getInstance().mouseClick(DateInfoView.this);
                        if (newY > 1400) {
                            closeView();
                        }
                        dragText.setBackgroundResource(R.drawable.background_rectangle_radius_unfocus);
                        break;
                }
                return true;
            }
        });
        btAdd.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btAdd.setBackgroundResource(R.drawable.background_circle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btAdd.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btAdd.getHeight() || motionEvent.getY() < 0;
                        if (!isOutside)
                            btAdd.setBackgroundResource(R.drawable.background_circle_focus);
                        else
                            btAdd.setBackgroundResource(R.drawable.background_circle_unfocus);
                        break;
                    case MotionEvent.ACTION_UP:
                        btAdd.setBackgroundResource(R.drawable.background_circle_unfocus);
                        if (!isOutside) {
                            ViewManager.getInstance().addInfoView.showView((Activity) context, DateInfoView.this.date);
                            ViewManager.getInstance().mouseClick(DateInfoView.this);
                        }
                        break;
                }

                return true;
            }
        });
    }

    private void setView(Activity activity, ArrayList<MatterInfo> data){
        if (data == null) return;
        for (int i = 0; i < data.size(); i++) {
            if (infoView.size() <= i) {
                MatterView matterView = new MatterView(activity);
                matterView.setMatterInfo(data.get(i));
                matterView.scrollView = scrollView;
                matterView.closeDetailInfoView();
                infoView.add(matterView);
                body.addView(matterView);
            } else {
                infoView.get(i).setMatterInfo(data.get(i));
                infoView.get(i).setVisibility(VISIBLE);
                infoView.get(i).closeDetailInfoView();
            }
        }

        for (int i = data.size();i < infoView.size();i++){
            infoView.get(i).setVisibility(GONE);
        }

    }

    @SuppressLint("SetTextI18n")
    public void showView(Activity activity, ArrayList<MatterInfo> data, String year, String month, String date){
        ViewManager.getInstance().addLayer(this);

        txtDate.setText(month + "月" + date + "日的行程");
        setView(activity, data);
        this.date = year + "/" + month + "/" + date;

        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.bottom_up);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        this.startAnimation(animation);

        if (isActivate) return;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        activity.addContentView(this, params);
        DataManager.getInstance().addListener(listener);
        isActivate = true;
    }

    public void closeView(){
        if (!isActivate) return;

        ViewManager.getInstance().removeLayer(this);
        ((ViewGroup) this.getParent()).removeView(this);
        for (int i = 0;i<infoView.size();i++)
            infoView.get(i).setVisibility(INVISIBLE);
        DataManager.getInstance().removeListener(listener);
        isActivate = false;
    }

}
