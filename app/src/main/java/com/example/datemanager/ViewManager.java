package com.example.datemanager;

import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Hashtable;

public class ViewManager {
    private static final ViewManager instance = new ViewManager();
    public MainActivity mainActivity;
    public DateInfoView dateInfoView;
    public MoreOptionView moreOptionView;
    public AddInfoView addInfoView;
    private ArrayList<IClickOutside> views = new ArrayList<>();

    private ViewManager(){
    }

    public static ViewManager getInstance(){
        return instance;
    }
    public void addLayer(IClickOutside view){
        views.add(view);
    }

    public void removeLayer(IClickOutside view){
        views.remove(view);
    }

    public void mouseClick(IClickOutside view){
        for (int i = 0;i < views.size();i++){
            if (views.get(i) == view){
                if (views.size() > (i + 1)){
                    views.get(i+1).closeView();
                }
                break;
            }
        }
    }
}
