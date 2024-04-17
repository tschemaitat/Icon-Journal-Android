package com.example.habittracker.Values;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Structs.WidgetId;

public class WidgetValueStringPath extends BaseWidgetValue{
    private RefItemPath refItemPath;
    public WidgetValueStringPath(WidgetId widgetId, RefItemPath refItemPath){
        super(widgetId);
        this.refItemPath = refItemPath;
    }

    public RefItemPath getRefItemPath() {
        return refItemPath;
    }

    @Override
    public CachedString getDisplayCachedString() {
        return new ArrayString(refItemPath.getPath());
    }
}
