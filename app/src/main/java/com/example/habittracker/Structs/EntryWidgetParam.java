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
    private WidgetId widgetIdTracker = null;
    private WidgetInStructure widgetInStructure;

    public EntryWidgetParam(String name, String className){
        this.name = name;
        this.className = className;
    }

    public EntryWidgetParam(EntryWidgetParamBuilder builder, String className){
        this.name = builder.name;
        this.className = className;
        this.isUniqueAttribute = builder.isUniqueAttribute;
        this.widgetIdTracker = builder.widgetIdTracker;
    }

    public void setIsUniqueAttribute(Boolean isUniqueAttribute){
        this.isUniqueAttribute = isUniqueAttribute;
    }

    public WidgetId getWidgetId(){
        if(widgetIdTracker == null)
            throw new RuntimeException();
        return widgetIdTracker;
    }

    public String hierarchyString(){
        return hierarchyString(0);
    }
    public abstract String hierarchyString(int numTabs);
    public abstract HeaderNode createHeaderNode();


    public void setWidgetInStructure(WidgetInStructure widgetInStructure){
        this.widgetInStructure = widgetInStructure;
    }




    public Structure getStructure(){
        return widgetInStructure.getStructure();
    }

    public WidgetInStructure getWidgetInStructure(){
        if(widgetInStructure == null){
            MainActivity.log("widget id: " + widgetIdTracker);
            throw new RuntimeException();
        }
        return widgetInStructure;

    }

    public WidgetInStructure getWidgetInStructureNullable(){
        return widgetInStructure;
    }

    public boolean hasWidgetInStructure() {
        return getWidgetInStructureNullable() != null;
    }

    public void setStructureCustom(StructureId structureId){

    }

    public void setWidgetId(WidgetId idCounter) {
        widgetIdTracker = idCounter;
    }

    public String getClassName() {
        return className;
    }

    public final JSONObject getJSON() throws JSONException{
        JSONObject jsonObject = getJSONCustom();
        jsonObject.put("name", name);
        jsonObject.put("idTracker", widgetIdTracker.getId().intValue());
        jsonObject.put("isUniqueAttribute", isUniqueAttribute);
        //don't need to put className in builder because it is used by the switch, the variable is put in by the inheriting class
        jsonObject.put("className", className);
        return jsonObject;
    }

    public static EntryWidgetParamBuilder getBuilderFromJSON(JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        boolean isUniqueAttribute = jsonObject.getBoolean("isUniqueAttribute");
        WidgetId widgetIdTracker = new WidgetId(jsonObject.getInt("idTracker"));
        return new EntryWidgetParamBuilder(name, isUniqueAttribute, widgetIdTracker);
    }

    protected abstract JSONObject getJSONCustom() throws JSONException;

    public boolean hasWidgetId() {
        return widgetIdTracker != null;
    }

    public WidgetId getWidgetIdNullable() {
        return widgetIdTracker;
    }

    public StructureId getStructureId() {
        if(widgetInStructure.getStructureId() == null)
            throw new RuntimeException();
        return widgetInStructure.getStructureId();
    }

    public boolean hasStructure(){
        return widgetInStructure.getStructureId() != null;
    }


    public static class EntryWidgetParamBuilder{
        public String name;
        public Boolean isUniqueAttribute = false;
        public WidgetId widgetIdTracker = null;

        public EntryWidgetParamBuilder(String name, Boolean isUniqueAttribute, WidgetId widgetIdTracker) {
            this.name = name;
            this.isUniqueAttribute = isUniqueAttribute;
            this.widgetIdTracker = widgetIdTracker;

        }




    }
}
