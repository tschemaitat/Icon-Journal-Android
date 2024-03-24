package com.example.habittracker.Widgets;

import android.view.View;

import com.example.habittracker.Structs.EntryWidgetParam;

public interface Widget {
    public EntryWidgetParam getParam();
    public void setParam(EntryWidgetParam param);
    public View getView();
    public void setOnDataChangedListener(Runnable runnable);
}
