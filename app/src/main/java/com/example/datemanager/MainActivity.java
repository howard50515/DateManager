package com.example.datemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements IClickOutside{
    public CalendarViewController calendarViewController;
    public LinearLayout mainView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.mainView);
        ViewManager.getInstance().mainActivity = this;
        ViewManager.getInstance().dateInfoView = new DateInfoView(this);
        ViewManager.getInstance().moreOptionView = new MoreOptionView(this);
        ViewManager.getInstance().addInfoView = new AddInfoView(this);
        ViewManager.getInstance().addLayer(this);
        mainView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_UP){
                    ViewManager.getInstance().mouseClick(MainActivity.this);
                }

                return true;
            }
        });
        NotifyManager.getInstance().setMainActivity(this);
        calendarViewController = new CalendarViewController(findViewById(R.id.calendarView), this);
    }

    @Override
    public void closeView() {

    }
}