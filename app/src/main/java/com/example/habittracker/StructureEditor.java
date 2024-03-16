package com.example.habittracker;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class StructureEditor implements Widget{
    @Override
    public Widget widgetClone() {
        return null;
    }

    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public WidgetParams getData() {
        return null;
    }

    @Override
    public WidgetValue value() {
        return null;
    }

    @Override
    public void setData(WidgetParams params) {

    }

    @Override
    public View getView() {
        return null;
    }


}
