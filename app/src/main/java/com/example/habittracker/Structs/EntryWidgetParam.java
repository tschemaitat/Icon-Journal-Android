package com.example.habittracker.Structs;

import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetInStructure;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class EntryWidgetParam {

    public String name;
    private String className;
    public Boolean isUniqueAttribute = false;
    public Integer widgetIdTracker = null;
    private Structure structure;

    public EntryWidgetParam(String name, String className){
        this.name = name;
        this.className = className;
    }

    public void setIsUniqueAttribute(Boolean isUniqueAttribute){
        this.isUniqueAttribute = isUniqueAttribute;
    }

    public String hierarchyString(){
        return hierarchyString(0);
    }
    public abstract String hierarchyString(int numTabs);
    public abstract HeaderNode createHeaderNode();

    public final void setStructure(Structure structure) {
        this.structure = structure;
        setStructureCustom(structure);
    }

    public Structure getStructure(){
        return structure;
    }

    public WidgetInStructure getWidgetId(){
        if(widgetIdTracker == null){
            return null;
        }
        return new WidgetInStructure(widgetIdTracker, structure);
    }

    public void setStructureCustom(Structure structure){

    }

    public void setWidgetId(int idCounter) {
        widgetIdTracker = idCounter;
    }

    public String getClassName() {
        return className;
    }

    public final JSONObject getJSON() throws JSONException{
        JSONObject jsonObject = getJSONCustom();
        jsonObject.put("name", name);
        jsonObject.put("idTracker", widgetIdTracker);
        jsonObject.put("is unique attribute", isUniqueAttribute);
        jsonObject.put("class name", className);
        return jsonObject;
    }

    protected abstract JSONObject getJSONCustom() throws JSONException;
}
