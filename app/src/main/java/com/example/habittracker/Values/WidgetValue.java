package com.example.habittracker.Values;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.WidgetId;

public abstract class WidgetValue {
    private WidgetId widgetId;
    public WidgetValue(WidgetId widgetId){
        this.widgetId = widgetId;
    }


    public WidgetId getWidgetId() {
        return widgetId;
    }
}
