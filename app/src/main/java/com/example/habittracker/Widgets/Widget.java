package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementProvider;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;

public interface Widget extends ElementProvider {
    void setValue(Object widgetValue);
    Object getEntryValueTreeCustom();
    void setParam(EntryWidgetParam param);
    void setOnDataChangedListener(Runnable runnable);
    Element getElement();
    void setTextErrorColor();
    void resetTextErrorColor();
    void setHint(String string);
    Context getContext();

}
