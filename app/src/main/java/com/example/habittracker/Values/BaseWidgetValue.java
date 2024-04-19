package com.example.habittracker.Values;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.WidgetId;

public abstract class BaseWidgetValue extends WidgetValue{

    public BaseWidgetValue(WidgetId widgetId) {
        super(widgetId);
    }

    public abstract CachedString getDisplayCachedString();

    public abstract CachedString getCachedString();
}
