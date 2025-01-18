package com.example.habittracker.Widgets;

import android.view.View;

import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;

public interface Widget {
    public void setParam(EntryWidgetParam param);
    public View getView();
    public void setOnDataChangedListener(Runnable runnable);
    Element getElement();
}
