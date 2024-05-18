package com.example.habittracker.Structs.CachedStrings;



import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.structures.WidgetInStructure;
import com.example.habittracker.structures.WidgetPath;
import com.example.habittracker.Values.BaseWidgetValue;
import com.example.habittracker.structures.ListItemId;
import com.example.habittracker.structures.Entry;
import com.example.habittracker.structures.Structure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RefEntryString implements CachedString{
    public static final String className = "refEntryString";
    StructureId structureId;
    WidgetId widgetId;
    EntryId entryId;
    ArrayList<ListItemId> listIdList;
    public RefEntryString(WidgetInStructure widgetInStructure, Entry entry, ArrayList<ListItemId> listIdList){
        if(widgetInStructure == null)
            throw new RuntimeException();
        this.structureId = widgetInStructure.getStructureId();
        this.widgetId = widgetInStructure.getWidgetId();
        this.entryId = entryId;
        this.listIdList = listIdList;
    }



    public String getString(){
        WidgetInStructure widgetInStructure = Dictionary.getStructure(structureId).getWidgetInStructureFromId(widgetId);
        WidgetPath path = widgetInStructure.getWidgetPath();
        widgetInStructure.getStructure();

        Entry entry = widgetInStructure.getStructure().getEntry(entryId);
//        Entry entry = structure.getEntry(entryId);
//        WidgetPath path = structure.getWidgetInfo(widgetInStructure).getWidgetPath();

        BaseWidgetValue value = entry.getGroupValue().getValue(path, listIdList);
        return value.getStandardFormOfCachedString().getString();
    }

    @Override
    public JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CachedString.typeOfClassKey, className);
        jsonObject.put("structure id", structureId.getId().intValue());
        jsonObject.put("widget id", widgetId.getId().intValue());
        jsonObject.put("entry id", entryId.getId().intValue());
        JSONArray listIdArray = new JSONArray();
        for(ListItemId listItemId: listIdList){
            listIdArray.put(listItemId.getId().intValue());
        }
        jsonObject.put("list id array", listIdArray);
        return jsonObject;
    }



    public Structure getStructure() {
        Structure structure = Dictionary.getStructure(structureId);
        return structure;
    }
    public StructureId getStructureId() {
        return structureId;
    }

    public WidgetInStructure getWidgetInStructure() {
        return getStructure().getWidgetInStructureFromId(widgetId);
    }

    public EntryId getEntryId() {
        return entryId;
    }

    public ArrayList<ListItemId> getListIdList() {
        return listIdList;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof CachedString){
            RefEntryString cachedString = (RefEntryString) object;
            if( ! structureId.equals(cachedString.structureId))
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

    public String getLocationString(){
        return "ref: " + structureId + ", <" + widgetId + ">, " + entryId + ", " + listIdList;
    }

    @Override
    public String toString(){
        return getString();
    }

}

