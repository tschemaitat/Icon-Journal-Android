package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetSingleItem;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void setStructureCustom(Integer structureId){
        widgetParam.setStructure(structureId);
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
}
