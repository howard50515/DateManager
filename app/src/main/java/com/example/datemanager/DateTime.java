package com.example.datemanager;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;

public class DateTime implements Serializable {
    public static Calendar calendar = Calendar.getInstance();
    private String date;
    private String time;
    private String dateTime;
    private int year, month, day, hour, minute;

    public DateTime(int year, int month, int day, int hour, int minute){

    }

    public DateTime(String date, String time){
        this.date = date;
        this.time = time;
        this.dateTime = date + " " + time;
        setValues(dateTime);
    }

    public String getDateTime(){
        return dateTime;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public void setValues(String dateTime){
        String[] str = dateTime.split("/| |:");
        year = Integer.parseInt(str[0]);
        month = Integer.parseInt(str[1]);
        day = Integer.parseInt(str[2]);
        hour = Integer.parseInt(str[3]);
        minute = Integer.parseInt(str[4]);
    }

    public int[] getValues(){
        int[] values = new int[5];
        values[0] = year;
        values[1] = month;
        values[2] = day;
        values[3] = hour;
        values[4] = minute;
        return values;
    }

    public long getTimeInMillis(){
        calendar.set(year, month - 1, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean equals(Object obj){
        DateTime dateTime = (DateTime) obj;
        return this.dateTime.equals(dateTime.getDateTime());
    }
}
