package com.example.habittracker.Structs;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetInStructure;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class EntryWidgetParam {

    public String name;
    private String className;
    public Boolean isUniqueAttribute = false;
    private Integer widgetIdTracker = null;
    private Integer structureId;

    public EntryWidgetParam(String name, String className){
        this.name = name;
        this.className = className;
    }

    public EntryWidgetParam(EntryWidgetParamBuilder builder, String className){
        this.name = builder.name;
        this.className = className;
        this.isUniqueAttribute = builder.isUniqueAttribute;
        this.widgetIdTracker = builder.widgetIdTracker;
        this.structureId = builder.structureId;
    }

    public void setIsUniqueAttribute(Boolean isUniqueAttribute){
        this.isUniqueAttribute = isUniqueAttribute;
    }

    public Integer getWidgetId(){
        if(widgetIdTracker == null)
            throw new RuntimeException();
        return widgetIdTracker;
    }

    public String hierarchyString(){
        return hierarchyString(0);
    }
    public abstract String hierarchyString(int numTabs);
    public abstract HeaderNode createHeaderNode();

    public final void setStructure(Integer structureId) {
        this.structureId = structureId;
        setStructureCustom(structureId);
    }




    public Structure getStructure(){
        return Dictionary.getStructure(structureId);
    }

    public WidgetInStructure getWidgetInStructure(){
        WidgetInStructure widgetInStructure = getWidgetInStructureNullable();
        if(widgetInStructure == null){
            MainActivity.log("widget id: " + widgetIdTracker + ", structureId: " + structureId);
            throw new RuntimeException();
        }
        return widgetInStructure;

    }

    public WidgetInStructure getWidgetInStructureNullable(){
        if(widgetIdTracker == null || structureId == null){
            return null;
        }
        return new WidgetInStructure(widgetIdTracker, structureId);
    }

    public boolean hasWidgetInStructure() {
        return getWidgetInStructureNullable() != null;
    }

    public void setStructureCustom(Integer structureId){

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
        jsonObject.put("idTracker", widgetIdTracker.intValue());
        jsonObject.put("isUniqueAttribute", isUniqueAttribute);
        //don't need to put className in builder because it is used by the switch, the variable is put in by the inheriting class
        jsonObject.put("className", className);
        jsonObject.put("structureId", structureId.intValue());
        return jsonObject;
    }

    public static EntryWidgetParamBuilder getBuilderFromJSON(JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        boolean isUniqueAttribute = jsonObject.getBoolean("isUniqueAttribute");
        int widgetIdTracker = jsonObject.getInt("idTracker");
        int structureId = jsonObject.getInt("structureId");
        return new EntryWidgetParamBuilder(name, isUniqueAttribute, widgetIdTracker, structureId);
    }

    protected abstract JSONObject getJSONCustom() throws JSONException;

    public boolean hasWidgetId() {
        return widgetIdTracker != null;
    }

    public Integer getWidgetIdNullable() {
        return widgetIdTracker;
    }

    public Integer getStructureId() {
        if(structureId == null)
            throw new RuntimeException();
        return structureId;
    }

    public boolean hasStructure(){
        return structureId != null;
    }


    public static class EntryWidgetParamBuilder{
        public String name;
        public Boolean isUniqueAttribute = false;
        public Integer widgetIdTracker = null;
        private Integer structureId;

        public EntryWidgetParamBuilder(String name, Boolean isUniqueAttribute, Integer widgetIdTracker, Integer structureId) {
            this.name = name;
            this.isUniqueAttribute = isUniqueAttribute;
            this.widgetIdTracker = widgetIdTracker;
            this.structureId = structureId;
        }




    }
}
