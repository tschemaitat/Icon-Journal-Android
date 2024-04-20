package com.example.habittracker.Structs.CachedStrings;



import com.example.habittracker.structures.WidgetPath;
import com.example.habittracker.structures.WidgetId;
import com.example.habittracker.Values.BaseWidgetValue;
import com.example.habittracker.structures.ListItemId;
import com.example.habittracker.structures.Entry;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;

public class RefEntryString implements CachedString{
    Structure structure;
    WidgetId widgetId;
    Integer entryId;
    ArrayList<ListItemId> listIdList;
    public RefEntryString(Structure structure, WidgetId widgetId, int entryId, ArrayList<ListItemId> listIdList){
        if(structure == null)
            throw new RuntimeException();
        this.structure = structure;
        this.widgetId = widgetId;
        this.entryId = entryId;
        this.listIdList = listIdList;
    }

    public String getString(){
        Entry entry = structure.getEntry(entryId);
        WidgetPath path = structure.getWidgetInfo(widgetId).getWidgetPath();

        BaseWidgetValue value = entry.getGroupValue().getValue(path, listIdList);
        return value.getStandardFormOfCachedString().getString();
    }

    public Structure getStructureId() {
        return structure;
    }

    public WidgetId getWidgetId() {
        return widgetId;
    }

    public int getEntryId() {
        return entryId;
    }

    public ArrayList<ListItemId> getListIdList() {
        return listIdList;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof CachedString){
            RefEntryString cachedString = (RefEntryString) object;
            if( ! structure.equals(cachedString.structure))
                return false;
            if( ! widgetId.equals(cachedString.widgetId))
                return false;
            if( ! entryId.equals(cachedString.entryId))
                return false;
            if( ! listIdList.equals(cachedString.listIdList))
                return false;
            return true;
        }

        return false;
    }

    @Override
    public String toString(){
        return getString();
    }

}

