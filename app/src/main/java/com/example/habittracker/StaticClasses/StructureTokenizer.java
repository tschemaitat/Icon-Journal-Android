package com.example.habittracker.StaticClasses;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Values.WidgetValueString;
import com.example.habittracker.Values.WidgetValueStringPath;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetMultipleItems;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetSingleItem;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.ListMultiItemParam;
import com.example.habittracker.Widgets.WidgetParams.ListSingleItemParam;
import com.example.habittracker.structurePack.Entry;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.Structures;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class StructureTokenizer {


    public static JSONObject getStructureArrayToken(){
        try {
            return getStructureArrayTokenThrows();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONObject getStructureArrayTokenThrows() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        ArrayList<Structure> structures = Dictionary.getStructures();
        JSONArray jsonArray = new JSONArray();
        for(Structure structure: structures){
            JSONObject structureJSON = jsonFromStructure(structure);
            Structure extractedStructure = makeStructure(structureJSON);
            Structures.isStructuresSame(structure, extractedStructure);

            jsonArray.put(structureJSON);
        }
        jsonObject.put("structures", jsonArray);
        return jsonObject;
    }



    public static void setStructures(JSONObject jsonObject){
        try {
            JSONArray structuresJSON = jsonObject.getJSONArray("structures");
            for(int i = 0; i < structuresJSON.length(); i++){
                JSONObject structureJSON = structuresJSON.getJSONObject(i);
                setStructureFromJSON(structureJSON);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject jsonFromStructure(Structure structure) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("name", structure.getCachedName().getString());
        json.put("id", structure.getId().getInteger().intValue());
        json.put("widgetParams", structure.getWidgetParam().getJSON());
        json.put("type", structure.getType());
        ArrayList<EntryInStructure> entryInStructures = structure.getEntriesInStructure();
        json.put("entriesSize", entryInStructures.size());

        JSONArray entriesJSON = new JSONArray();
        for(EntryInStructure entryInStructure : entryInStructures){
            JSONObject entryJSON = getEntryJSON(entryInStructure.getId(), entryInStructure.getGroupValue());
            entriesJSON.put(entryJSON);
        }
        json.put("entries", entriesJSON);
        return json;
    }

    public static Structure makeStructure(JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");
        StructureId structureId = new StructureId(jsonObject.getInt("id"));
        JSONObject widgetParamsJSON = jsonObject.getJSONObject("widgetParams");
        GroupWidgetParam groupWidgetParam = (GroupWidgetParam)getWidgetParam(widgetParamsJSON);
        JSONArray entriesJSON = jsonObject.getJSONArray("entries");
        ArrayList<Entry> entries = new ArrayList<>();
        int entriesSize = jsonObject.getInt("entriesSize");
        for(int i = 0; i < entriesSize; i++){
            JSONObject entryJSON = entriesJSON.getJSONObject(i);
            entries.add(getEntryFromJSON(entryJSON));
        }
        return new Structure(name, groupWidgetParam, type, entries, structureId);
    }

    public static void setStructureFromJSON(JSONObject jsonObject) throws JSONException{
        Structure structure = makeStructure(jsonObject);
        Dictionary.addStructureFromSave(structure.getCachedName().getString(), structure.getWidgetParam(),
                structure.getType(), structure.getEntries(), structure.getId());
    }

    public static JSONObject getEntryJSON(EntryId entryId, GroupValue groupValue) throws JSONException{
        JSONObject entryJSON = new JSONObject();
        entryJSON.put("id", entryId.getInteger().intValue());
        entryJSON.put("value", groupValue.getJSON());
        return entryJSON;
    }

    public static Entry getEntryFromJSON(JSONObject jsonObject) throws JSONException{
        EntryId entryId = new EntryId(jsonObject.getInt("id"));
        GroupValue groupValue = (GroupValue) getWidgetValue(jsonObject.getJSONObject("value"));
        return new Entry(groupValue, entryId);
    }

    public static WidgetValue getWidgetValue(JSONObject jsonObject) throws JSONException{
        String className = jsonObject.getString(WidgetValue.classNameKey);
        switch(className){
            case GroupValue.className->{return GroupValue.getFromJSON(jsonObject);}
            case ListValue.className->{return ListValue.getFromJSON(jsonObject);}
            case WidgetValueString.className->{return WidgetValueString.getFromJSON(jsonObject);}
            case WidgetValueStringPath.className->{return WidgetValueStringPath.getFromJSON(jsonObject);}
        }
        MainActivity.log("couldn't find cached string class: " + className);
        throw new RuntimeException();
    }

    public static CachedString getCachedString(JSONObject jsonObject) throws JSONException{
        String className = jsonObject.getString(CachedString.typeOfClassKey);
        switch(className){
            case LiteralString.className->{return LiteralString.getFromJSON(jsonObject);}
            case RefEntryString.className->{return RefEntryString.getFromJSON(jsonObject);}
        }
        MainActivity.log("couldn't find cached string class: " + className);
        throw new RuntimeException();
    }

    public static EntryWidgetParam getWidgetParam(JSONObject jsonObject) throws JSONException{
        String className = jsonObject.getString("className");
        switch(className){
            case DropDown.className->{return DropDownParam.getFromJSON(jsonObject);}
            case CustomEditText.className->{return EditTextParam.getFromJSON(jsonObject);}
            case ListWidgetSingleItem.className->{return ListSingleItemParam.getFromJSON(jsonObject);}
            case ListWidgetMultipleItems.className->{return ListMultiItemParam.getFromJSON(jsonObject);}
            case GroupWidget.className->{return GroupWidgetParam.getFromJSON(jsonObject);}
        }
        MainActivity.log("couldn't find cached string class: " + className);
        throw new RuntimeException();

    }
}
