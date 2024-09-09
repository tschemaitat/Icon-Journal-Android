package com.example.habittracker.Structs.CachedStrings;



import com.example.habittracker.Algorithms.Lists;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.WidgetValueReference;
import com.example.habittracker.Values.WidgetValueStringPath;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Values.BaseWidgetValue;
import com.example.habittracker.structurePack.ListItemId;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.Structure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.habittracker.defaultImportPackage.ArrayList;
import java.util.Objects;

public class RefEntryString<E extends BaseWidgetValue> implements CachedString{
    public static final String className = "refEntryString";
    WidgetInStructure widgetInStructure;
    EntryInStructure entryInStructure;
    ArrayList<ListItemId> listIdList;
    public RefEntryString(WidgetInStructure widgetInStructure, EntryInStructure entryInStructure, ArrayList<ListItemId> listIdList){
        if(widgetInStructure == null)
            throw new RuntimeException();
        if(entryInStructure == null)
            throw new RuntimeException();
        for(ListItemId listItemId: listIdList){
            if(listItemId == null){
                MainActivity.log("list has a null id: " + listIdList);
                throw new RuntimeException("list item id is null");
            }
        }
        this.widgetInStructure = widgetInStructure;
        this.entryInStructure = entryInStructure;
        this.listIdList = listIdList;
        //checking for error during construction
        BaseWidgetValue baseWidgetValue = getBaseWidgetValue();
    }

    public RefEntryString(Structure structure, EntryId entryId, BaseWidgetValue baseWidgetValue) {
        //checking cast
        E casted = (E) baseWidgetValue;
        ArrayList<ListItemId> listIdList = baseWidgetValue.getListItemIds();
        WidgetInStructure widgetInStructure = structure.getWidgetInStructureFromId(baseWidgetValue.getWidgetId());
        EntryInStructure entryInStructure = structure.getEntryInStructure(entryId);
        init(widgetInStructure, entryInStructure, listIdList);


    }

    private void init(WidgetInStructure widgetInStructure, EntryInStructure entryInStructure, ArrayList<ListItemId> listIdList){

    }


    public E getBaseWidgetValue(){
        BaseWidgetValue baseWidgetValue = entryInStructure.getGroupValue().getValue(widgetInStructure.getWidgetPath(), listIdList);
        return (E) baseWidgetValue;
    }

    public String getString(){

        BaseWidgetValue value = getBaseWidgetValue();
        return value.getStandardFormOfCachedString().getString();
    }

    public ArrayList<RefEntryString> getRefEntryStringListOfValue(){
        WidgetValueStringPath value = (WidgetValueStringPath)getBaseWidgetValue();
        ArrayList<CachedString> cachedStrings = value.getRefItemPath().getPath();
        return EnumLoop.makeList(cachedStrings, (cachedString -> (RefEntryString) cachedString));
    }



    @Override
    public JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CachedString.typeOfClassKey, className);
        jsonObject.put("structureId", widgetInStructure.getStructureId().getInteger().intValue());
        jsonObject.put("widgetId", widgetInStructure.getWidgetId().getIntegerId().intValue());
        jsonObject.put("entryId", entryInStructure.getId().getInteger().intValue());
        jsonObject.put("listIdArraySize", listIdList.size());
        JSONArray listIdArray = new JSONArray();
        for(ListItemId listItemId: listIdList){
            listIdArray.put(listItemId.getIntegerId().intValue());
        }
        jsonObject.put("listIdArray", listIdArray);
        return jsonObject;
    }



    public static RefEntryString getFromJSON(JSONObject jsonObject) throws JSONException{
        StructureId structureId = new StructureId(jsonObject.getInt("structureId"));
        Structure structure = Dictionary.getStructure(structureId);
        WidgetId widgetId = new WidgetId(jsonObject.getInt("widgetId"));
        WidgetInStructure widgetInStructure = structure.getWidgetInStructureFromId(widgetId);
        EntryId entryId = new EntryId(jsonObject.getInt("entryId"));
        EntryInStructure entryInStructure = structure.getEntryInStructure(entryId);
        int listIdArraySize = jsonObject.getInt("listIdArraySize");
        JSONArray listIdJSON = jsonObject.getJSONArray("listIdArray");
        ArrayList<ListItemId> listIdList = new ArrayList<>();
        for(int i = 0; i < listIdArraySize; i++){
            int listIdInt = listIdJSON.getInt(i);
            listIdList.add(new ListItemId(listIdInt));
        }
        return new RefEntryString(widgetInStructure, entryInStructure, listIdList);
    }

    public static void getFromJSONFake(JSONObject jsonObject) throws JSONException {





    }



    public Structure getStructure() {

        return widgetInStructure.getStructure();
    }

    public WidgetInStructure getWidgetInStructure() {
        return widgetInStructure;
    }

    public EntryInStructure getEntryInStructure() {
        return entryInStructure;
    }

    public ArrayList<ListItemId> getListIdList() {
        return listIdList;
    }

    @Override
    public boolean equals(Object object){

        if( ! (object instanceof RefEntryString refEntryString))
            return false;
        if( ! Objects.equals(widgetInStructure, refEntryString.widgetInStructure))
            return false;
        if( ! Objects.equals(entryInStructure, refEntryString.entryInStructure))
            return false;
        if( ! Objects.equals(listIdList, refEntryString.listIdList))
            return false;
        return true;
    }

    @Override
    public void equalsThrows(Object object) {
        if( ! (object instanceof RefEntryString refEntryString))
            throw new RuntimeException(object.getClass().toString());
        widgetInStructure.equalsThrows(refEntryString.widgetInStructure);
        entryInStructure.equalsThrows(refEntryString.entryInStructure);
        Lists.equalsThrowsRecursive(listIdList, refEntryString.listIdList);
        if( ! this.equals(object))
            throw new RuntimeException();
    }

    public String getLocationString(){
        return "s: " + widgetInStructure.getStructure().getCachedName().getString() + ", w: <" + widgetInStructure.getName().getString() + ">, entry: " + entryInStructure.getId() + ", " + listIdList;
    }

    @Override
    public String toString(){
        return "<RefEntryString, value: " + getString() + ", loc: "+getLocationString()+">";
    }

}

