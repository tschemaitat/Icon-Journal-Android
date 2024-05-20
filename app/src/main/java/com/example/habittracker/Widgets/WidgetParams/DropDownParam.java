package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.structurePack.HeaderNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DropDownParam extends EntryWidgetParam {
    public StructureId referenceStructureId;
    public WidgetId valueId;
    public ArrayList<WidgetId> groups;

    public DropDownParam(String name, StructureId referenceStructureId,
                         WidgetId valueId, ArrayList<WidgetId> groups){
        super(name, DropDown.className);
        if(referenceStructureId == null)
            throw new RuntimeException();
        this.referenceStructureId = referenceStructureId;
        this.valueId = valueId;
        this.groups = groups;

        MainActivity.log("ref structure id: " + referenceStructureId);



    }

    public DropDownParam(EntryWidgetParamBuilder builder, StructureId referenceStructureId,
                         WidgetId valueId, ArrayList<WidgetId> groups){
        super(builder, DropDown.className);
        if(referenceStructureId == null)
            throw new RuntimeException();
        this.referenceStructureId = referenceStructureId;
        this.valueId = valueId;
        this.groups = groups;
    }

    @Override
    public String hierarchyString(int numTabs){
        String singleTab = "\t";
        String tabs = "";
        for(int i = 0; i < numTabs; i++)
            tabs += singleTab;
        return tabs + "drop down\n"
                + tabs + "\tstructure: " + referenceStructureId + "\n"
                + tabs + "\tvalue: " + valueId + "\n"
                + tabs + "\tgroups: " + groups + "\n";
    }

    @Override
    public HeaderNode createHeaderNode() {
        return new HeaderNode(this);
    }

    @Override
    protected JSONObject getJSONCustom() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("structureId", referenceStructureId.getInteger().intValue());
        jsonObject.put("valueId", valueId.getInteger().intValue());
        jsonObject.put("groupSize", groups.size());
        JSONArray groupsJSON = new JSONArray();
        for(WidgetId groupId: groups)
            groupsJSON.put(groupId.getInteger().intValue());
        jsonObject.put("groups", groupsJSON);
        return null;
    }

    public static DropDownParam getFromJSON(JSONObject jsonObject) throws JSONException{
        int structureId = jsonObject.getInt("structureId");
        int valueId = jsonObject.getInt("valueId");
        int groupSize = jsonObject.getInt("groupSize");
        JSONArray groupsJSON = jsonObject.getJSONArray("groups");
        ArrayList<WidgetId> groups = new ArrayList<>();
        for(int i = 0; i < groupSize; i++){
            groups.add(new WidgetId(groupsJSON.getInt(i)));
        }
        EntryWidgetParamBuilder builder = EntryWidgetParam.getBuilderFromJSON(jsonObject);
        return new DropDownParam(builder, new StructureId(structureId), new WidgetId(valueId), groups);
    }

    public String toString(){
        return "{" + getClassName() + ", " + referenceStructureId + ", " +valueId + ", " +groups + "}";
    }

    public WidgetInStructure getValueWidget() {

        return getReferenceStructure().getWidgetInStructureFromId(valueId);
    }

    public ArrayList<WidgetInStructure> getGroupWidgets(){
        Structure referenceStructure = getReferenceStructure();
        return EnumLoop.makeList(groups, (group)-> referenceStructure.getWidgetInStructureFromId(group));
    }

    public Structure getReferenceStructure() {
        return Dictionary.getStructure(referenceStructureId);
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof DropDownParam dropDownParam))
            return false;
        if( ! Objects.equals(groups, dropDownParam.groups))
            return false;
        if( ! Objects.equals(valueId, dropDownParam.valueId))
            return false;
        if( ! Objects.equals(name, dropDownParam.name))
            return false;
        if( ! Objects.equals(referenceStructureId, dropDownParam.referenceStructureId))
            return false;
        if( ! Objects.equals(getWidgetId(), dropDownParam.getWidgetId()))
            return false;
        return true;
    }
}
