package com.example.habittracker.structures;

import com.example.habittracker.Structs.EntryWidgetParam;

public class EntryWidgetInfo {
    private EntryWidgetParam param;
    private Boolean isUniqueAttribute;
    public EntryWidgetInfo(EntryWidgetParam param, Boolean isUniqueAttribute){
        this.isUniqueAttribute = isUniqueAttribute;
        this.param = param;
    }

    public EntryWidgetParam getParam() {
        return param;
    }

    public Boolean getUniqueAttribute() {
        return isUniqueAttribute;
    }
}
