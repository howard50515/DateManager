package com.example.datemanager;

import java.io.Serializable;

public class RemindInfo implements Serializable {
    public int firstRemind, remindInterval;
    public RemindMode mode = RemindMode.GENERAL;

    public RemindInfo() {}

    public RemindInfo(int firstRemind, int remindInterval){
        this.firstRemind = firstRemind;
        this.remindInterval = remindInterval;
    }

    @Override
    public boolean equals(Object obj){
        RemindInfo remindInfo = (RemindInfo) obj;
        return this.firstRemind == remindInfo.firstRemind &&
               this.remindInterval == remindInfo.remindInterval &&
               this.mode == remindInfo.mode;
    }
}
