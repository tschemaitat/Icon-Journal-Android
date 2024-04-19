package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.structures.Header;
import com.example.habittracker.structures.Structure;

public class WidgetId {
    private int id;
    private Structure structure;
    public WidgetId(int id, Structure structure){
        this.id = id;
        this.structure = structure;
    }

    public int getId(){
        return id;
    }

    public String toString(){
        return "tree id: " + id;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof WidgetId){
            WidgetId widgetId = ((WidgetId) object);
            if(widgetId.id == id)
                return true;
        }
        return false;
    }

    public ItemPath getNameWithPath(){
        WidgetPath widgetPath = structure.getWidgetInfo(this).getWidgetPath();
        return new ItemPath(EnumLoop.makeList(widgetPath.getList(), (widgetId)->widgetId.getName()));
    }

    public String getName(){
        return structure.getWidgetInfo(this).getEntryWidgetParam().name;
    }



    public EntryWidgetParam getWidgetParam() {
        return structure.getWidgetParamFromId(this);
    }

    public Header.WidgetInfo getWidgetInfo() {
        return structure.getWidgetInfo(this);
    }
}
