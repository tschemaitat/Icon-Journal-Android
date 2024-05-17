package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Widgets.ListWidgets.ListWidgetMultipleItems;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

import org.json.JSONException;
import org.json.JSONObject;

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
}