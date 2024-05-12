package com.example.habittracker.Values;

import com.example.habittracker.Structs.CachedStrings.CachedString;

public abstract class BaseWidgetValue extends WidgetValue{

    public BaseWidgetValue(Integer widgetInStructure) {
        super(widgetInStructure);
    }

    public abstract CachedString getDisplayCachedString();

    public abstract CachedString getStandardFormOfCachedString();

    public abstract CachedString getDebugCachedString();
}
