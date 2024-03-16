package com.example.habittracker;

import java.util.ArrayList;

public class StructureWidgetData {
    public StructureWidgetData(){

    }
    public String name = null;
    public String type = null;;
    public ArrayList<String> dropDownGroups = new ArrayList<>();
    public String structureKey = null;
    public ArrayList<StructureWidgetData> listWidgets = new ArrayList<>();
}
