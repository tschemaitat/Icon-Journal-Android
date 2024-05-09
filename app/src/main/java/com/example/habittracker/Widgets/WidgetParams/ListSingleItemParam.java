package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetSingleItem;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

public class ListSingleItemParam extends EntryWidgetParam {
    public EntryWidgetParam widgetParam;

    public ListSingleItemParam(String name, EntryWidgetParam widgetParam){
        super(name, ListWidgetSingleItem.className);
        this.widgetParam = widgetParam;
        if(widgetParam instanceof GroupWidgetParam){
            MainActivity.log("put group widget param in single item list param");
            throw new RuntimeException();
        }
    }

    @Override
    public void setStructureCustom(Structure structure){
        widgetParam.setStructure(structure);
    }

    public String toString(){
        return hierarchyString(0);
    }

    public String hierarchyString(int numTabs){

        return GLib.tabs(numTabs) + "list ("+name+")\n"
                + widgetParam.hierarchyString(numTabs + 1);
    }

    @Override
    public HeaderNode createHeaderNode() {
        HeaderNode tree = widgetParam.createHeaderNode();
        HeaderNode result = new HeaderNode(this);
        result.add(tree);

        return result;
    }
}
