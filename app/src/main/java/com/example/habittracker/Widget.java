package com.example.habittracker;

import android.view.View;

import com.example.habittracker.Slider.TextSlider;

public interface Widget {

    void setOnDataChangedListener(Runnable runnable);
    WidgetParams getData();
    WidgetValue value();
    void setData(WidgetParams params);

    View getView();
}
