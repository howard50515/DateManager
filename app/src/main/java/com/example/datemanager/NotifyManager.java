package com.example.datemanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

public class NotifyManager {
    private static final NotifyManager instance = new NotifyManager();

    public MainActivity mainActivity;
    public NotificationManager notificationManager;
    public NotificationChannel channel;
    public AlarmManager alarm;
    public Integer tagSize;
    public Queue<Integer> unusedTags;

    private NotifyManager() {
        tagSize = FileManager.loadFromJson(Integer.class, "tagSize.txt");
        if (tagSize == null)
            tagSize = 0;
        Type type = new TypeToken<Queue<Integer>>(){}.getType();
        unusedTags = FileManager.loadFromJson(type, "unusedTags.txt");
        if (unusedTags == null)
            unusedTags = new LinkedList<>();
    }

    public static NotifyManager getInstance() {
        return instance;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        alarm = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        notificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("0", "myChannel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("最重要的人");
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public int setNotify(MatterInfo matterInfo) {
        RemindInfo remindInfo = matterInfo.remindInfo;
        long millisecond = matterInfo.dateTimeStart.getTimeInMillis();
        long time = System.currentTimeMillis();
        long triggerAtMillis = millisecond - (long) remindInfo.firstRemind * 1000 * 60;
        long lastTriggerAtMillis = millisecond - (long) remindInfo.firstRemind * 1000 * 60;
        if (remindInfo.mode == RemindMode.REPEAT)
            lastTriggerAtMillis = millisecond -
                (long) (remindInfo.firstRemind % remindInfo.remindInterval) * 1000 * 60;

        if (lastTriggerAtMillis < time) return -1;

        int tag = generateTag();

        Intent intent = new Intent();
        intent.putExtra("bundle", getMatterInfoAlarmBundle(matterInfo, tag));
        intent.setClass(mainActivity, AlarmReceiver.class);

        PendingIntent pending = PendingIntent.getBroadcast(mainActivity, tag, intent, PendingIntent.FLAG_IMMUTABLE);

        long intervalMillis = (long) remindInfo.remindInterval * 1000 * 60;

        System.out.println(time);
        System.out.println(triggerAtMillis);
        System.out.println(lastTriggerAtMillis);

        saveData();

        if (remindInfo.remindInterval == -1)
            alarm.setAndAllowWhileIdle(AlarmManager.RTC, triggerAtMillis, pending);
        else
            alarm.setRepeating(AlarmManager.RTC, triggerAtMillis, intervalMillis, pending);

        return tag;
    }

    public Bundle getMatterInfoAlarmBundle(MatterInfo matterInfo, int tag){
        Bundle bundle = new Bundle();
        bundle.putInt("tag", tag);
        long millisecond = matterInfo.dateTimeStart.getTimeInMillis();
        bundle.putLong("time", millisecond);
        long intervalMillis = (long) matterInfo.remindInfo.remindInterval * 1000 * 60;
        bundle.putLong("interval", intervalMillis);
        bundle.putLong("extra", System.currentTimeMillis());
        bundle.putSerializable("matterInfo", matterInfo);

        return bundle;
    }

    public void removeNotify(MatterInfo matterInfo){
        removeNotify(matterInfo.getTag(), matterInfo);
    }

    private void removeNotify(int tag, MatterInfo matterInfo){
        if (tag == -1) return;

        Intent intent = new Intent();
        intent.setClass(mainActivity, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainActivity, tag, intent, PendingIntent.FLAG_IMMUTABLE);
        alarm.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
        alarm.cancel(pendingIntent);
        DataManager.getInstance().removeNotifyRegister(matterInfo);
        releaseTag(tag);
        saveData();
    }


    private int generateTag(){
        if (unusedTags.isEmpty())
            return tagSize++;
        else
            return unusedTags.remove();
    }

    private void releaseTag(int tag){
        unusedTags.add(tag);
    }

    private void saveData(){
        FileManager.saveAsJson(tagSize, "tagSize.txt");
        FileManager.saveAsJson(unusedTags, "unusedTags.txt");
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Bundle bundle = intent.getBundleExtra("bundle");
            Notification notification = null;
            MatterInfo matterInfo = (MatterInfo) bundle.getSerializable("matterInfo");
            int tag = bundle.getInt("tag", 0);
            long time = System.currentTimeMillis();
            NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
            textStyle.bigText("Start At : " + matterInfo.getDateStart() + "\n" + matterInfo.remark);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.background_rectangle_unfocus)
                        .setChannelId("0")
                        .setContentTitle("tag : " + tag + " " + matterInfo.topic)
                        .setStyle(textStyle)
                        .setWhen(time)
                        .build();
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tag, intent, PendingIntent.FLAG_IMMUTABLE);

            notificationManager.notify(tag, notification);
            long millisecond = bundle.getLong("time", 0);
            long interval = bundle.getLong("interval", 0);
            if (time + interval > millisecond) {
                alarm.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
                alarm.cancel(pendingIntent);
            }
            NotifyManager.getInstance().removeNotify(tag, matterInfo);
        }
    }
}