package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetInStructure;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.structures.HeaderNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DropDownParam extends EntryWidgetParam {
    public Integer referenceStructureId;
    public Integer valueId;
    public ArrayList<Integer> groups;

    public DropDownParam(String name, Integer referenceStructureId,
                         Integer valueId, ArrayList<Integer> groups){
        super(name, DropDown.className);
        if(referenceStructureId == null)
            throw new RuntimeException();
        this.referenceStructureId = referenceStructureId;
        this.valueId = valueId;
        this.groups = groups;
        try{
            Dictionary.getStructure(referenceStructureId).getWidgetInfo(new WidgetInStructure(valueId, referenceStructureId));
        }catch(RuntimeException runtimeException){

            MainActivity.log("invalid params structureId: " + referenceStructureId + ", valueId: " + valueId);
            throw runtimeException;
        }
        MainActivity.log("ref structure id: " + referenceStructureId);

        getValueWidget().getWidgetInfo();


    }

    public DropDownParam(EntryWidgetParamBuilder builder, Integer referenceStructureId,
                         Integer valueId, ArrayList<Integer> groups){
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
        jsonObject.put("structureId", referenceStructureId.intValue());
        jsonObject.put("valueId", valueId.intValue());
        jsonObject.put("groupSize", groups.size());
        JSONArray groupsJSON = new JSONArray();
        for(Integer groupId: groups)
            groupsJSON.put(groupId.intValue());
        jsonObject.put("groups", groupsJSON);
        return null;
    }

    public static DropDownParam getFromJSON(JSONObject jsonObject) throws JSONException{
        int structureId = jsonObject.getInt("structureId");
        int valueId = jsonObject.getInt("valueId");
        int groupSize = jsonObject.getInt("groupSize");
        JSONArray groupsJSON = jsonObject.getJSONArray("groups");
        ArrayList<Integer> groups = new ArrayList<>();
        for(int i = 0; i < groupSize; i++){
            groups.add(groupsJSON.getInt(i));
        }
        EntryWidgetParamBuilder builder = EntryWidgetParam.getBuilderFromJSON(jsonObject);
        return new DropDownParam(builder, structureId, valueId, groups);
    }

    public String toString(){
        return "{" + getClassName() + ", " + referenceStructureId + ", " +valueId + ", " +groups + "}";
    }

    public WidgetInStructure getValueWidget() {

        WidgetInStructure widgetInStructure = new WidgetInStructure(valueId, referenceStructureId);
        MainActivity.log(this + "making value widgetInStructure: " + widgetInStructure);
        return widgetInStructure;
    }

    public ArrayList<WidgetInStructure> getGroupWidgets(){
        return EnumLoop.makeList(groups, (group)-> new WidgetInStructure(group, referenceStructureId));
    }

    public Structure getReferenceStructure() {
        return Dictionary.getStructure(referenceStructureId);
    }
}
