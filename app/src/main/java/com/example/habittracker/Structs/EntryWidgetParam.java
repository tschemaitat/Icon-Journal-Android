package com.example.habittracker.Structs;

public abstract class EntryWidgetParam {

    public String name;
    public String className;

    public EntryWidgetParam(String name, String className){
        this.name = name;
        this.className = className;
    }

    public String hierarchyString(){
        return hierarchyString(0);
    }
    public abstract String hierarchyString(int numTabs);
    public abstract DataTree header();
}
