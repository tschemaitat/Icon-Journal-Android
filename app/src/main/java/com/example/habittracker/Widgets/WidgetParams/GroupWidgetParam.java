package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupWidgetParam extends EntryWidgetParam {
    public ArrayList<EntryWidgetParam> params;
    public GroupWidgetParam(String name, ArrayList<EntryWidgetParam> params){
        super(name, GroupWidget.className);
        if(params == null)
            throw new RuntimeException("array is null");
        for(EntryWidgetParam param: params)
            if(param == null)
                throw new RuntimeException("param in array is null");
        this.params = params;
    }

    public GroupWidgetParam(EntryWidgetParamBuilder builder, ArrayList<EntryWidgetParam> params){
        super(builder, GroupWidget.className);
        if(params == null)
            throw new RuntimeException("array is null");
        for(EntryWidgetParam param: params)
            if(param == null)
                throw new RuntimeException("param in array is null");
        this.params = params;
    }

    public GroupWidgetParam(String name, EntryWidgetParam[] params){
        super(name, GroupWidget.className);
        this.params = new ArrayList<>(Arrays.asList(params));
    }




    @Override
    protected JSONObject getJSONCustom() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("arraySize", params.size());
        JSONArray paramsJSON = new JSONArray();
        for(EntryWidgetParam param: params){
            JSONObject paramJSON = param.getJSON();
            paramsJSON.put(paramJSON);
        }
        jsonObject.put("params", paramsJSON);
        return jsonObject;
    }

    public static EntryWidgetParam getFromJSON(JSONObject jsonObject) throws JSONException{
        EntryWidgetParamBuilder builder = EntryWidgetParam.getBuilderFromJSON(jsonObject);
        int arraySize = jsonObject.getInt("arraySize");
        JSONArray paramsJSON = jsonObject.getJSONArray("params");
        ArrayList<EntryWidgetParam> params = new ArrayList<>();
        for(int i = 0; i < arraySize; i++){
            JSONObject paramJSON = paramsJSON.getJSONObject(i);
            EntryWidgetParam entryWidgetParam = StructureTokenizer.getWidgetParam(paramJSON);
            params.add(entryWidgetParam);
        }
        return new GroupWidgetParam(builder, params);
    }

    public String toString(){
        return hierarchyString(0);
    }

    public String hierarchyString(int numTabs){
        String tabs = GLib.tabs(numTabs);
        String result = tabs + "group widget ("+name+") widgetId: " + getWidgetIdNullable() +"\n";
        result += hierarchyStringFromList(numTabs);
        return result;
    }

    @Override
    public HeaderNode createHeaderNode() {
        HeaderNode header = new HeaderNode(this);
        for(EntryWidgetParam param: params){
            header.add(param.createHeaderNode());
        }
        return header;
    }

    public String hierarchyStringFromList(int numTabs){
        String result = "";
        for(EntryWidgetParam widgetParam: params){
            result += widgetParam.hierarchyString(numTabs + 1);
        }
        return result;
    }
}
