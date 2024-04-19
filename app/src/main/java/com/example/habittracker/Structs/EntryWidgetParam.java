package com.example.habittracker.Structs;

import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

public abstract class EntryWidgetParam {

    public String name;
    public String className;
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

    public WidgetId getWidgetId(){
        if(widgetIdTracker == null){
            return null;
        }
        return new WidgetId(widgetIdTracker, structure);
    }

    public void setStructureCustom(Structure structure){

    }

    public void setWidgetId(int idCounter) {
        widgetIdTracker = idCounter;
    }
}
