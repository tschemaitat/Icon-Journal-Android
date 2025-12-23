package com.example.habittracker.Widgets.WidgetParams;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Widgets.ListWidgets.ListWidget;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetMultipleItems;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetSingleItem;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.HeaderNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ListParam extends EntryWidgetParam{
    public GroupWidgetParam cloneableWidget;
    public String classNameOfChild = null;

    public ListParam(String name, GroupWidgetParam cloneableWidget){
        super(name, ListWidget.className);
        this.cloneableWidget = cloneableWidget;
        init();
    }

    public ListParam(EntryWidgetParamBuilder builder, GroupWidgetParam cloneableWidget){
        super(builder, ListWidget.className);
        this.cloneableWidget = cloneableWidget;
        init();
    }

    public void init(){
        if(cloneableWidget.params.size() < 2){
            classNameOfChild = ListWidgetSingleItem.childClassName;
        }else{
            classNameOfChild = ListWidgetMultipleItems.childClassName;
        }
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
        GroupWidgetParam groupParam = (GroupWidgetParam) StructureTokenizer.getWidgetParam(groupParamJSON);
        return new ListParam(builder, groupParam);
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
        if( ! (object instanceof ListParam listParam))
            return false;
        if( ! Objects.equals(cloneableWidget, listParam.cloneableWidget))
            return false;
        if( ! Objects.equals(name, listParam.name))
            return false;
        if( ! Objects.equals(getWidgetId(), listParam.getWidgetId()))
            return false;
        return true;
    }

    @Override
    public void equalsThrows(Object object){
        if( ! (object instanceof ListParam listParam))
            throw new RuntimeException(object.toString());
        if( ! Objects.equals(name, listParam.name))
            throw new RuntimeException("first: " + name + ", second: " + listParam.name);
        getWidgetId().equalsThrows(listParam.getWidgetId());
        cloneableWidget.equalsThrows(listParam.cloneableWidget);
        if( ! this.equals(object))
            throw new RuntimeException();
    }

    @Override
    public Object getEmptyWidgetValue() {
        //i think this should have no values in the list
        //this is for a list that hasn't created any children yet
        return new ListValue(getWidgetId(), new ArrayList<>());
    }
}
