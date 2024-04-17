package com.example.habittracker.Structs;

import com.example.habittracker.structures.HeaderNode;

public abstract class EntryWidgetParam {

    public String name;
    public String className;
    public Boolean isUniqueAttribute = false;
    public Integer widgetIdTracker = null;

    public EntryWidgetParam(String name, String className){
        this.name = name;
        this.className = className;
    }

    public void setIsUniqueAttribute(Boolean isUniqueAttribute){
        this.isUniqueAttribute = isUniqueAttribute;
    }

    public String hierarchyString(){
        return hierarchyString(0);
    }
    public abstract String hierarchyString(int numTabs);
    public abstract HeaderNode createHeaderNode();
}
