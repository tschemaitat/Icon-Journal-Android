package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.structures.WidgetId;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;

public class DropDownParam extends EntryWidgetParam {
    public Structure structure;
    public WidgetId valueKey;
    public ArrayList<WidgetId> groups;
    public String name = "null";

    public DropDownParam(String name, Structure structure,
                         WidgetId valueKey, ArrayList<WidgetId> groups){
        super(name, DropDown.className);
        if(structure == null)
            throw new RuntimeException();
        this.structure = structure;
        this.valueKey = valueKey;
        this.groups = groups;
    }

    @Override
    public String hierarchyString(int numTabs){
        String singleTab = "\t";
        String tabs = "";
        for(int i = 0; i < numTabs; i++)
            tabs += singleTab;
        return tabs + "drop down\n"
                + tabs + "\tstructure: " + structure + "\n"
                + tabs + "\tvalue: " + valueKey + "\n"
                + tabs + "\tgroups: " + groups + "\n";
    }

    @Override
    public HeaderNode createHeaderNode() {
        return new HeaderNode(this);
    }

    public String toString(){
        return "{" + className + ", " +structure + ", " +valueKey + ", " +groups + "}";
    }

}
