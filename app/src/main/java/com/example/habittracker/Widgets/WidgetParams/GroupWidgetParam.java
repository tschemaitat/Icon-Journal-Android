package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupWidgetParam extends EntryWidgetParam {
    public ArrayList<EntryWidgetParam> params;
    public GroupWidgetParam(String name, ArrayList<EntryWidgetParam> params){
        super(name, GroupWidget.className);
        if(params == null)
            throw new RuntimeException("array is null");
        for(EntryWidgetParam param: params)
            if(param == null)
                throw new RuntimeException("param in array is null");
        this.params = params;
    }

    public GroupWidgetParam(String name, EntryWidgetParam[] params){
        super(name, GroupWidget.className);
        this.params = new ArrayList<>(Arrays.asList(params));
    }

    @Override
    public void setStructureCustom(Structure structure){
        for(EntryWidgetParam entryWidgetParam: params)
            entryWidgetParam.setStructure(structure);
    }

    public String toString(){
        return hierarchyString(0);
    }

    public String hierarchyString(int numTabs){
        String tabs = GLib.tabs(numTabs);
        String result = tabs + "group widget ("+name+") structure: " + getStructure() +"\n";
        result += hierarchyStringFromList(numTabs);
        return result;
    }

    @Override
    public HeaderNode createHeaderNode() {
        HeaderNode header = new HeaderNode(this);
        for(EntryWidgetParam param: params){
            header.add(param.createHeaderNode());
        }
        return header;
    }

    public String hierarchyStringFromList(int numTabs){
        String result = "";
        for(EntryWidgetParam widgetParam: params){
            result += widgetParam.hierarchyString(numTabs + 1);
        }
        return result;
    }
}
