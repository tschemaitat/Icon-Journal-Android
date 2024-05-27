package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetMultipleItems;
import com.example.habittracker.structurePack.HeaderNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ListMultiItemParam extends EntryWidgetParam {
    public GroupWidgetParam cloneableWidget;

    public ListMultiItemParam(String name, GroupWidgetParam cloneableWidget){
        super(name, ListWidgetMultipleItems.className);
        this.cloneableWidget = cloneableWidget;
        if(cloneableWidget.params.size() == 1)
            throw new RuntimeException();
    }

    public ListMultiItemParam(EntryWidgetParamBuilder builder, GroupWidgetParam cloneableWidget){
        super(builder, ListWidgetMultipleItems.className);
        this.cloneableWidget = cloneableWidget;
        if(cloneableWidget.params.size() == 1)
            throw new RuntimeException();
    }

    @Override
    protected JSONObject getJSONCustom() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupParam", cloneableWidget.getJSON());
        return jsonObject;
    }

    public static EntryWidgetParam getFromJSON(JSONObject jsonObject) throws JSONException{
        EntryWidgetParamBuilder builder = EntryWidgetParam.getBuilderFromJSON(jsonObject);
        JSONObject groupParamJSON = jsonObject.getJSONObject("groupParam");
        GroupWidgetParam groupParam = (GroupWidgetParam)StructureTokenizer.getWidgetParam(groupParamJSON);
        return new ListMultiItemParam(builder, groupParam);
    }

    public String toString(){
        return hierarchyString(0);
    }

    public String hierarchyString(int numTabs){

        return GLib.tabs(numTabs) + "list ("+name+")\n"
                + cloneableWidget.hierarchyStringFromList(numTabs + 1);
    }

    @Override
    public HeaderNode createHeaderNode() {
        HeaderNode tree = cloneableWidget.createHeaderNode();
        HeaderNode result = new HeaderNode(this);
        for(HeaderNode child: tree.getChildren())
            result.add(child);

        return result;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof ListMultiItemParam listMultiItemParam))
            return false;
        if( ! Objects.equals(cloneableWidget, listMultiItemParam.cloneableWidget))
            return false;
        if( ! Objects.equals(name, listMultiItemParam.name))
            return false;
        if( ! Objects.equals(getWidgetId(), listMultiItemParam.getWidgetId()))
            return false;
        return true;
    }

    @Override
    public void equalsThrows(Object object){
        if( ! (object instanceof ListMultiItemParam listMultiItemParam))
            throw new RuntimeException(object.toString());
        if( ! Objects.equals(name, listMultiItemParam.name))
            throw new RuntimeException("first: " + name + ", second: " + listMultiItemParam.name);
        getWidgetId().equalsThrows(listMultiItemParam.getWidgetId());
        cloneableWidget.equalsThrows(listMultiItemParam.cloneableWidget);
        if( ! this.equals(object))
            throw new RuntimeException();
    }
}