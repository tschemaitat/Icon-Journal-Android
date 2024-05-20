package com.example.habittracker.structurePack;

import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;

import java.util.Objects;

public class WidgetInStructure {
    private Structure structure;
    private Header.WidgetInfo widgetInfo;
    public WidgetInStructure(Structure structure){

        if(structure == null)
            throw new RuntimeException();
        this.structure = structure;
    }

    public void setWidgetInfo(Header.WidgetInfo widgetInfo){
        this.widgetInfo = widgetInfo;
    }

    public WidgetId getWidgetId(){
        return widgetInfo.getEntryWidgetParam().getWidgetId();
    }

    public String toString(){
        return "<widgetInStructure, widgetId: " + getWidgetId() + ", structureId: " + structure + ">";
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(getWidgetId().getInteger());
    }
    @Override
    public boolean equals(Object object){
        if( ! (object instanceof WidgetInStructure widgetInStructure))
            return false;

        if(!Objects.equals(widgetInStructure.getWidgetId(), getWidgetId()))
            return false;
        if(!Objects.equals(widgetInStructure.structure, structure))
            return false;

        return true;
    }

    public RefItemPath getNameWithPath(){
        WidgetPath widgetPath = widgetInfo.getWidgetPath();
        return new RefItemPath(EnumLoop.makeList(widgetPath.getList(), (widgetId)->widgetId.getName()));
    }

    public CachedString getName(){
        return new LiteralString(widgetInfo.getEntryWidgetParam().name);
    }



    public EntryWidgetParam getWidgetParam() {
        return widgetInfo.getEntryWidgetParam();
    }

    public Structure getStructure(){
        return structure;
    }

    public Header.WidgetInfo getWidgetInfo() {
        return widgetInfo;
    }

    public WidgetPath getWidgetPath() {
        return widgetInfo.getWidgetPath();
    }

    public StructureId getStructureId() {
        return structure.getId();
    }


}
