package com.example.habittracker.Widgets;

import android.view.View;

import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;

public interface Widget {

    void setOnDataChangedListener(Runnable runnable);
    WidgetParam getData();
    WidgetValue value();
    void setData(WidgetParam params);

    View getView();


}
