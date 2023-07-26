package com.example.datemanager;

import java.io.Serializable;

public class MatterInfo implements Serializable {
    public String topic, remark;
    public DateTime dateTimeStart, dateTimeEnd;
    public RemindInfo remindInfo;
    private int tag = -1;

    public MatterInfo(DateTime dateTimeStart, DateTime dateTimeEnd, String topic, String remark, RemindInfo remindInfo){
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.topic = topic;
        this.remark = remark;
        this.remindInfo = remindInfo;
    }

    public void setInfo(DateTime dateTimeStart, DateTime dateTimeEnd, String topic, String remark, RemindInfo remindInfo){
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.topic = topic;
        this.remark = remark;
        this.remindInfo = remindInfo;
    }

    public void remind(){
        if (tag == -1 && remindInfo.firstRemind != -1)
            tag = NotifyManager.getInstance().setNotify(this);
    }

    public String getDateStart(){
        return dateTimeStart.getDate();
    }

    public String getDateEnd(){
        return dateTimeEnd.getDate();
    }

    public String getTimeStart(){
        return dateTimeStart.getTime();
    }

    public String getTimeEnd(){
        return dateTimeEnd.getTime();
    }

    public int getTag(){
        return tag;
    }

    public void removeTag() {
        this.tag = -1;
    }

    public boolean dateEqual(){
        return dateTimeStart.getDate().equals(dateTimeEnd.getDate());
    }

    @Override
    public boolean equals(Object obj){
        MatterInfo matterInfo = (MatterInfo) obj;
        return this.dateTimeStart.equals(matterInfo.dateTimeStart) &&
               this.dateTimeEnd.equals(matterInfo.dateTimeEnd) &&
               this.remindInfo.equals(matterInfo.remindInfo) &&
               this.topic.equals(matterInfo.topic) &&
               this.remark.equals(matterInfo.remark);
    }
}
