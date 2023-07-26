package com.example.datemanager;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DataManager {
    private static final DataManager instance = new DataManager();

    public HashMap<String, DateInfo> infoTable;

    public Calendar calendar = Calendar.getInstance();

    private DataManager(){
        initData();
    }

    public static DataManager getInstance(){
        return instance;
    }

    public void initData(){
        Type type = new TypeToken<HashMap<String, DateInfo>>(){}.getType();
        infoTable = FileManager.loadFromJson(type, "infoTable.txt");
        if (infoTable == null)
            infoTable = new HashMap<>();
    }

    public void saveData(){
        FileManager.saveAsJson(infoTable, "infoTable.txt");
    }

    public void removeData(MatterInfo matterInfo){
        String dateStart = matterInfo.getDateStart();
        String dateEnd = matterInfo.getDateEnd();
        ArrayList<String> keys = getKeys(dateStart, dateEnd);
        for (int i = 0;i < keys.size();i++) {
            if (infoTable.containsKey(keys.get(i))) {
                ArrayList<MatterInfo> matters = infoTable.get(keys.get(i)).matters;
                for (int j = 0; j < matters.size(); j++) {
                    if (matters.get(j) == matterInfo) {
                        matters.remove(matterInfo);
                        NotifyManager.getInstance().removeNotify(matterInfo);
                        break;
                    }
                }
            }
        }

        bankDataChanged();
    }

    public void addData(MatterInfo matterInfo, boolean startReminder){
        String dateStart = matterInfo.getDateStart();
        String dateEnd = matterInfo.getDateEnd();
        ArrayList<String> keys = getKeys(dateStart, dateEnd);
        for (int i = 0;i < keys.size();i++) {
            DateInfo dateInfo = infoTable.containsKey(keys.get(i)) ?
                    infoTable.get(keys.get(i)) : new DateInfo();
            dateInfo.matters.add(matterInfo);
            if (!infoTable.containsKey(keys.get(i)))
                infoTable.put(keys.get(i), dateInfo);
        }
        if (startReminder)
            matterInfo.remind();

        bankDataChanged();
    }

    public void resetReminder(MatterInfo matterInfo){
        NotifyManager.getInstance().removeNotify(matterInfo);
        matterInfo.remind();
        bankDataChanged();
    }

    public ArrayList<MatterInfo> findDateData(String date){
        ArrayList<MatterInfo> matters = new ArrayList<>();
        if (infoTable.containsKey(date)){
            matters.addAll(infoTable.getOrDefault(date, new DateInfo()).matters);
        }
        String month = date.substring(0, date.length() - 3);
        if (infoTable.containsKey(month)){
            ArrayList<MatterInfo> temp = infoTable.get(month).matters;
            for (int i = 0;i < temp.size();i++){
                if (dateStartBefore(temp.get(i).getDateStart(), date) &&
                    dateEndAfter(temp.get(i).getDateEnd(), date)){
                    matters.add(temp.get(i));
                }
            }
        }
        String year = date.substring(0, 4);
        if (infoTable.containsKey(year)){
            ArrayList<MatterInfo> temp = infoTable.get(year).matters;
            for (int i = 0;i < temp.size();i++){
                if (dateStartBefore(temp.get(i).getDateStart(), date) &&
                        dateEndAfter(temp.get(i).getDateEnd(), date)){
                    matters.add(temp.get(i));
                }
            }
        }

        return matters;
    }

    public void removeNotifyRegister(MatterInfo matterInfo){
        String dateStart = matterInfo.getDateStart();
        String dateEnd = matterInfo.getDateEnd();
        String key = dateStart.equals(dateEnd) ? dateStart : dateStart.substring(0, dateStart.length() - 3);
        if (!infoTable.containsKey(key))
            return;
        ArrayList<MatterInfo> matters = infoTable.get(key).matters;
        for (int i = 0;i < matters.size();i++){
            if (matters.get(i).equals(matterInfo)){
                matters.get(i).removeTag();
            }
        }

        saveData();
    }

    public ArrayList<String> getKeys(String left, String right){
        ArrayList<String> keys = new ArrayList<>();
        int[] dateLeft = dateToInteger(left);
        int[] dateRight = dateToInteger(right);
        if (left.equals(right))
            keys.add(left);
        else if (dateLeft[0] == dateRight[0] && dateLeft[1] == dateRight[1])
            keys.add(left.substring(0, left.length() - 3));
        else if (dateLeft[0] == dateRight[0]){
            keys.add(String.valueOf(dateLeft[0]));
        }
        else {
            for (int i = dateLeft[0];i <= dateRight[0];i++){
                keys.add(String.valueOf(i));
            }
        }

        return keys;
    }

    public static boolean dateStartBefore(String left, String right){
        int[] dateLeft = dateToInteger(left);
        int[] dateRight = dateToInteger(right);
        if (dateLeft[0] != dateRight[0])
            return dateLeft[0] < dateRight[0];
        if (dateLeft[1] != dateRight[1])
            return dateLeft[1] < dateRight[1];
        return dateLeft[2] <= dateRight[2];
    }

    public static boolean dateEndAfter(String left, String right){
        int[] dateLeft = dateToInteger(left);
        int[] dateRight = dateToInteger(right);
        if (dateLeft[0] != dateRight[0])
            return dateLeft[0] > dateRight[0];
        if (dateLeft[1] != dateRight[1])
            return dateLeft[1] > dateRight[1];
        return dateLeft[2] >= dateRight[2];
    }

    public static int[] dateToInteger(String date){
        String[] strDate = date.split("/");
        int[] output = new int[strDate.length];
        for (int i = 0;i < strDate.length;i++){
            output[i] = Integer.parseInt(strDate[i]);
        }

        return output;
    }

    public static boolean timeStartBefore(String left, String right){
        int[] timeLeft = timeToInteger(left);
        int[] timeRight = timeToInteger(right);
        if (timeLeft[0] != timeRight[0])
            return timeLeft[0] < timeRight[0];
        return timeLeft[1] <= timeRight[1];
    }

    public static boolean timeEndAfter(String left, String right){
        int[] timeLeft = timeToInteger(left);
        int[] timeRight = timeToInteger(right);
        if (timeLeft[0] != timeRight[0])
            return timeLeft[0] > timeRight[0];
        return timeLeft[1] >= timeRight[1];
    }

    public static int[] timeToInteger(String time){
        String[] strTime = time.split(":");
        int[] output = new int[strTime.length];
        for (int i = 0;i < strTime.length;i++){
            output[i] = Integer.parseInt(strTime[i]);
        }

        return output;
    }

    public String getCurrentDate(){
        return getYear() + "/" + getMonth() + "/" + getDate();
    }

    public String getYear() {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getMonth(){
        int month = calendar.get(Calendar.MONTH) + 1;
        return month < 10 ? "0" + month : String.valueOf(month);
    }

    public String getDate(){
        int date = calendar.get(Calendar.DATE);
        return date < 10 ? "0" + date : String.valueOf(date);
    }

    private ArrayList<OnBankDataChangedListener> listeners = new ArrayList<>();

    private void bankDataChanged(){
        saveData();
        for (int i = 0;i < listeners.size();i++)
            listeners.get(i).onBankDataChanged();
    }

    public void addListener(OnBankDataChangedListener listener){
        listeners.add(listener);
    }

    public void removeListener(OnBankDataChangedListener listener){
        listeners.remove(listener);
    }

    public interface OnBankDataChangedListener{
        void onBankDataChanged();
    }
}