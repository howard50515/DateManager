package com.example.datemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

public class CalendarViewController {
    public Context context;
    public CalendarView calendarView;

    public CalendarViewController(CalendarView calendarView, Context context){
        this.calendarView = calendarView;
        this.context = context;
        init();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String month = (i1 + 1) < 10 ? "0" + (i1 + 1) : String.valueOf(i1 + 1);
                String date = i2 < 10 ? "0" + i2 : String.valueOf(i2);
                String key = String.format(Locale.TAIWAN, "%d/%s/%s", i, month, date);
                ArrayList<MatterInfo> data = DataManager.getInstance().findDateData(key);
                ViewManager.getInstance().dateInfoView.showView((Activity) context, data, String.valueOf(i), month, date);
            }
        });
    }
}
