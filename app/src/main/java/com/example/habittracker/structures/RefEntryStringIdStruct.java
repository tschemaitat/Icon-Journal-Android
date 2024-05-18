package com.example.habittracker.structures;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RefEntryStringIdStruct {
    private StructureId structureId;
    private WidgetId widgetId;
    private EntryId entryId;
    private ArrayList<ListItemId> listIdList;

    public RefEntryStringIdStruct(StructureId structureId, WidgetId widgetId, EntryId entryId, ArrayList<ListItemId> listIdList) {
        this.structureId = structureId;
        this.widgetId = widgetId;
        this.entryId = entryId;
        this.listIdList = listIdList;
    }

    public static RefEntryStringIdStruct getFromJSON(JSONObject jsonObject) throws JSONException {
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
        return new RefEntryStringIdStruct(new StructureId(structureId),new WidgetId(widgetIdInt), new EntryId(entryIdInt), listIdList);
    }

    public StructureId getStructureId() {
        return structureId;
    }

    public WidgetId getWidgetId() {
        return widgetId;
    }

    public EntryId getEntryId() {
        return entryId;
    }

    public ArrayList<ListItemId> getListIdList() {
        return listIdList;
    }
}
