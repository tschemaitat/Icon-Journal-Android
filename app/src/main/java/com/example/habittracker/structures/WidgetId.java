package com.example.habittracker.structures;

import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.RefItemPath;

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
        return "widget id: " + id + ", path: " + getNameWithPath();
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

    public RefItemPath getNameWithPath(){
        WidgetPath widgetPath = structure.getWidgetInfo(this).getWidgetPath();
        return new RefItemPath(EnumLoop.makeList(widgetPath.getList(), (widgetId)->widgetId.getName()));
    }

    public CachedString getName(){
        return new LiteralString(structure.getWidgetInfo(this).getEntryWidgetParam().name);
    }



    public EntryWidgetParam getWidgetParam() {
        return structure.getWidgetParamFromId(this);
    }

    public Header.WidgetInfo getWidgetInfo() {
        return structure.getWidgetInfo(this);
    }
}
