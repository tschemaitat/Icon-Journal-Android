package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.Algorithms.ThrowableEqualsWithId;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.HeaderNode;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class EntryWidgetParam implements ThrowableEqualsWithId{

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
        if(name != null)
            jsonObject.put("name", name);
        if(widgetIdTracker != null)
            jsonObject.put("idTracker", widgetIdTracker.getIntegerId().intValue());
        jsonObject.put("isUniqueAttribute", isUniqueAttribute);
        //don't need to put className in builder because it is used by the switch, the variable is put in by the inheriting class
        jsonObject.put("className", className);
        return jsonObject;
    }

    public Integer getIntegerId(){
        return widgetIdTracker.getIntegerId();
    }

    public static EntryWidgetParamBuilder getBuilderFromJSON(JSONObject jsonObject) throws JSONException{
        String name = null;
        if(jsonObject.has("name"))
            name = jsonObject.getString("name");
        boolean isUniqueAttribute = jsonObject.getBoolean("isUniqueAttribute");
        WidgetId widgetIdTracker = null;
        if(jsonObject.has("idTracker"))
            widgetIdTracker = new WidgetId(jsonObject.getInt("idTracker"));
        return new EntryWidgetParamBuilder(name, isUniqueAttribute, widgetIdTracker);
    }

    protected abstract JSONObject getJSONCustom() throws JSONException;

    public WidgetId getWidgetId(){
        if(widgetIdTracker == null)
            throw new RuntimeException();
        return widgetIdTracker;
    }



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

    public abstract void equalsThrows(Object object);


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
