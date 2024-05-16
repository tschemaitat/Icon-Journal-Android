package com.example.habittracker.structures;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.RefItemPath;

import java.util.Objects;

public class WidgetInStructure {
    private Integer widgetId;
    private Integer structureId;
    public WidgetInStructure(Integer widgetId, Integer structureId){
        if(widgetId == null)
            throw new RuntimeException();
        if(structureId == null)
            throw new RuntimeException();
        this.widgetId = widgetId;
        this.structureId = structureId;
    }

    public Integer getWidgetId(){
        return widgetId;
    }

    public String toString(){
        return "<widgetInStructure, widgetId: " + widgetId + ", structureId: " + structureId + ">";
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(widgetId);
    }
    @Override
    public boolean equals(Object object){
        if( ! (object instanceof WidgetInStructure widgetInStructure))
            return false;

        if(!Objects.equals(widgetInStructure.widgetId, widgetId))
            return false;
        if(!Objects.equals(widgetInStructure.structureId, structureId))
            return false;

        return true;
    }

    public RefItemPath getNameWithPath(){
        WidgetPath widgetPath = getStructure().getWidgetInfo(this).getWidgetPath();
        return new RefItemPath(EnumLoop.makeList(widgetPath.getList(), (widgetId)->widgetId.getName()));
    }

    public CachedString getName(){
        return new LiteralString(getStructure().getWidgetInfo(this).getEntryWidgetParam().name);
    }



    public EntryWidgetParam getWidgetParam() {
        return getStructure().getWidgetParamFromId(this);
    }

    public Structure getStructure(){
        return Dictionary.getStructure(structureId);
    }

    public Header.WidgetInfo getWidgetInfo() {
        return getStructure().getWidgetInfo(this);
    }

    public WidgetPath getWidgetPath() {
        Structure structure = getStructure();
        return structure.getWidgetInfo(this).getWidgetPath();
    }

    public Integer getStructureId() {
        return structureId;
    }


}
