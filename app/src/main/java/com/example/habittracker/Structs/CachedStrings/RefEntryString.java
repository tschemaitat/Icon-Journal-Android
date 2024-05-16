package com.example.habittracker.Structs.CachedStrings;



import com.example.habittracker.StaticClasses.Dictionary;
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
    WidgetInStructure widgetInStructure;
    Integer entryId;
    ArrayList<ListItemId> listIdList;
    public RefEntryString(Integer structureId, Integer widgetIdTracker, Integer entryId, ArrayList<ListItemId> listIdList){
        if(structureId == null)
            throw new RuntimeException();
        this.widgetInStructure = new WidgetInStructure(structureId, widgetIdTracker);
        this.entryId = entryId;
        this.listIdList = listIdList;
    }
    public RefEntryString(WidgetInStructure widgetInStructure, Integer entryId, ArrayList<ListItemId> listIdList){
        if(widgetInStructure == null)
            throw new RuntimeException();
        this.widgetInStructure = widgetInStructure;
        this.entryId = entryId;
        this.listIdList = listIdList;
    }



    public String getString(){
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
        jsonObject.put("structure id", widgetInStructure.getStructureId().intValue());
        jsonObject.put("widget id", widgetInStructure.getWidgetId().intValue());
        jsonObject.put("entry id", entryId.intValue());
        JSONArray listIdArray = new JSONArray();
        for(ListItemId listItemId: listIdList){
            listIdArray.put(listItemId.getId().intValue());
        }
        jsonObject.put("list id array", listIdArray);
        return jsonObject;
    }

    public static CachedString getFromJSON(JSONObject jsonObject) throws JSONException {
        int structureId = jsonObject.getInt("structure id");
        int widgetIdInt = jsonObject.getInt("widget id");
        int entryIdInt = jsonObject.getInt("entry id");
        int listIdArraySize = jsonObject.getInt("listIdArray size");
        JSONArray listIdJSON = jsonObject.getJSONArray("list id array");
        ArrayList<ListItemId> listIdList = new ArrayList<>();
        for(int i = 0; i < listIdArraySize; i++){
            int listIdInt = listIdJSON.getInt(i);
            listIdList.add(new ListItemId(listIdInt));
        }
        return new RefEntryString(structureId,widgetIdInt, entryIdInt, listIdList);
    }

    public Structure getStructure() {
        Structure structure = Dictionary.getStructure(widgetInStructure.getStructureId());
        return structure;
    }
    public Integer getStructureId() {
        return widgetInStructure.getStructureId();
    }

    public WidgetInStructure getWidgetInStructure() {
        return widgetInStructure;
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
            if( ! widgetInStructure.equals(cachedString.widgetInStructure))
                return false;
//            if( ! widgetId.equals(cachedString.widgetId))
//                return false;
            if( ! entryId.equals(cachedString.entryId))
                return false;
            if( ! listIdList.equals(cachedString.listIdList))
                return false;
            return true;
        }

        return false;
    }

    public String getLocationString(){
        return "ref: " + widgetInStructure.getStructureId() + ", <" + widgetInStructure.getWidgetId() + ">, " + entryId + ", " + listIdList;
    }

    @Override
    public String toString(){
        return getString();
    }

}

