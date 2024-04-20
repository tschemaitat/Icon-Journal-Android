package com.example.habittracker.Values;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.structures.WidgetId;

public class WidgetValueString extends BaseWidgetValue {
    private CachedString cachedString;

    public WidgetValueString(WidgetId widgetId, CachedString cachedString){
        super(widgetId);
        this.cachedString = cachedString;
    }

    public CachedString getStandardFormOfCachedString(){
        return cachedString;
    }

    @Override
    public CachedString getDebugCachedString() {
        return cachedString;
    }

    public CachedString getDisplayCachedString() {
        return cachedString;
    }


}
