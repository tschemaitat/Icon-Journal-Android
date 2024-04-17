package com.example.habittracker.Values;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Structs.WidgetId;

public class WidgetValueString extends BaseWidgetValue {
    private CachedString cachedString;

    public WidgetValueString(WidgetId widgetId, CachedString cachedString){
        super(widgetId);
        this.cachedString = cachedString;
    }

    public CachedString getCachedString(){
        return cachedString;
    }

    public CachedString getDisplayCachedString() {
        return cachedString;
    }


}
