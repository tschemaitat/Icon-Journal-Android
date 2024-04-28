package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.ListWidgetMultipleItems;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

public class ListParam extends EntryWidgetParam {
    public GroupWidgetParam cloneableWidget;

    public ListParam(String name, GroupWidgetParam cloneableWidget){
        super(name, ListWidgetMultipleItems.className);
        this.cloneableWidget = cloneableWidget;
        if(cloneableWidget.params.size() == 1)
            throw new RuntimeException();
    }

    @Override
    public void setStructureCustom(Structure structure){
        cloneableWidget.setStructure(structure);
    }

    public String toString(){
        return hierarchyString(0);
    }

    public String hierarchyString(int numTabs){

        return GLib.tabs(numTabs) + "list ("+name+")\n"
                + cloneableWidget.hierarchyStringFromList(numTabs + 1);
    }

    @Override
    public HeaderNode createHeaderNode() {
        HeaderNode tree = cloneableWidget.createHeaderNode();
        HeaderNode result = new HeaderNode(this);
        for(HeaderNode child: tree.getChildren())
            result.add(child);

        return result;
    }
}