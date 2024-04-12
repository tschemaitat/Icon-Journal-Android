package com.example.habittracker.Structs;



import com.example.habittracker.StaticClasses.Dictionary;

import java.util.ArrayList;

public class CachedString {
    String literalString = null;
    boolean literalStringMode = false;
    StructureId structureId = null;
    WidgetId widgetId = null;
    Integer entryId = null;
    Integer listId = null;
    public CachedString(StructureId structureId, WidgetId widgetId, int entryId, int listId){
        this.structureId = structureId;
        this.widgetId = widgetId;
        this.entryId = entryId;
        this.listId = listId;
    }

    public CachedString(String literalString){
        literalStringMode = true;
        this.literalString = literalString;
    }

    public String getString(){
        if(literalStringMode)
            return literalString;
        Structure structure = Dictionary.getStructure(structureId);
        Entry entry = structure.getEntry(entryId);
        ValueTreePath path = structure.getHeader().getPath(widgetId);

        ArrayList<CachedString> values = entry.entryValueTree.getValuesFromPath(path);
        return values.get(listId).getString();
    }

    public StructureId getStructureId() {
        return structureId;
    }

    public WidgetId getWidgetId() {
        return widgetId;
    }

    public int getEntryId() {
        return entryId;
    }

    public int getListId() {
        return listId;
    }


    public boolean isLiteral() {
        return literalStringMode;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof CachedString){
            CachedString cachedString = (CachedString) object;
            if(cachedString.isLiteral() != isLiteral())
                return false;
            if(cachedString.isLiteral())
                return cachedString.getString().equals(literalString);
            if( ! structureId.equals(cachedString.structureId))
                return false;
            if( ! widgetId.equals(cachedString.widgetId))
                return false;
            if( ! entryId.equals(cachedString.entryId))
                return false;
            if( ! listId.equals(cachedString.listId))
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

