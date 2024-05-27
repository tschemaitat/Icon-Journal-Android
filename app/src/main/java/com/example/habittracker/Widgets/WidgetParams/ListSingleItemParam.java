package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetSingleItem;
import com.example.habittracker.structurePack.HeaderNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ListSingleItemParam extends EntryWidgetParam {
    public EntryWidgetParam widgetParam;

    public ListSingleItemParam(String name, EntryWidgetParam widgetParam){
        super(name, ListWidgetSingleItem.className);
        this.widgetParam = widgetParam;
        if(widgetParam instanceof GroupWidgetParam){
            MainActivity.log("put group widget param in single item list param");
            throw new RuntimeException();
        }
    }

    public ListSingleItemParam(EntryWidgetParamBuilder builder, EntryWidgetParam widgetParam){
        super(builder, ListWidgetSingleItem.className);
        this.widgetParam = widgetParam;
        if(widgetParam instanceof GroupWidgetParam){
            MainActivity.log("put group widget param in single item list param");
            throw new RuntimeException();
        }
    }





    @Override
    protected JSONObject getJSONCustom() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("singleItemParam", widgetParam.getJSON());
        return jsonObject;
    }

    public static EntryWidgetParam getFromJSON(JSONObject jsonObject) throws JSONException{
        EntryWidgetParamBuilder builder = EntryWidgetParam.getBuilderFromJSON(jsonObject);
        JSONObject singleItemParamJSON = jsonObject.getJSONObject("singleItemParam");
        EntryWidgetParam singleItemParam = StructureTokenizer.getWidgetParam(singleItemParamJSON);
        return new ListSingleItemParam(builder, singleItemParam);
    }

    public String toString(){
        return hierarchyString(0);
    }

    public String hierarchyString(int numTabs){

        return GLib.tabs(numTabs) + "list ("+name+")\n"
                + widgetParam.hierarchyString(numTabs + 1);
    }

    @Override
    public HeaderNode createHeaderNode() {
        HeaderNode tree = widgetParam.createHeaderNode();
        HeaderNode result = new HeaderNode(this);
        result.add(tree);

        return result;
    }



    @Override
    public boolean equals(Object object){
        if( ! (object instanceof ListSingleItemParam listSingleItemParam))
            return false;
        if( ! Objects.equals(widgetParam, listSingleItemParam.widgetParam))
            return false;
        if( ! Objects.equals(name, listSingleItemParam.name))
            return false;
        if( ! Objects.equals(getWidgetId(), listSingleItemParam.getWidgetId()))
            return false;
        return true;
    }

    @Override
    public void equalsThrows(Object object){
        if( ! (object instanceof ListSingleItemParam listSingleItemParam))
            throw new RuntimeException(object.toString());
        if( ! Objects.equals(name, listSingleItemParam.name))
            throw new RuntimeException("first: " + name + ", second: " + listSingleItemParam.name);
        getWidgetId().equalsThrows(listSingleItemParam.getWidgetId());
        widgetParam.equalsThrows(listSingleItemParam.widgetParam);
        if( ! this.equals(object))
            throw new RuntimeException();
    }
}
