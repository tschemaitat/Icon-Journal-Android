package com.example.habittracker.Widgets.WidgetParams;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.structures.HeaderNode;

import org.json.JSONException;
import org.json.JSONObject;

public class EditTextParam extends EntryWidgetParam {
    public EditTextParam(String name){
        super(name, CustomEditText.className);
    }

    public EditTextParam(EntryWidgetParamBuilder builder){
        super(builder, CustomEditText.className);
    }

    public String hierarchyString(int numTabs){
        return GLib.tabs(numTabs) + "text, name: "+name+", structure: " + getStructure()+"\n";
    }

    @Override
    public HeaderNode createHeaderNode() {
        return new HeaderNode(this);
    }

    @Override
    protected JSONObject getJSONCustom() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    public static EditTextParam getFromJSON(JSONObject jsonObject) throws JSONException{
        EntryWidgetParamBuilder builder = EntryWidgetParam.getBuilderFromJSON(jsonObject);
        return new EditTextParam(builder);
    }


}
