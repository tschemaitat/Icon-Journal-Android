package com.example.habittracker.Values;

import com.example.habittracker.structures.WidgetInStructure;
import com.example.habittracker.structures.ListItemId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class WidgetValue {
    public static final String classNameKey = "value type";
    private Integer widgetId;
    private GroupValue parent;
    public WidgetValue(Integer widgetId){
        this.widgetId = widgetId;
    }


    public Integer getWidgetId() {
        return widgetId;
    }

    public void setParentGroupValue(GroupValue parent){
        this.parent = parent;
    }

    public GroupValue getParentGroupValue(){
        return parent;
    }

    public ArrayList<ListItemId> getListItemIds(){
        ArrayList<ListItemId> result = new ArrayList<>();
        result.add(0, parent.getListItemId());
        GroupValue currentParent = parent;
        while(currentParent.getParentListValue() != null){
            ListValue listValue = parent.getParentListValue();
            currentParent = listValue.getParentGroupValue();
            result.add(0, currentParent.getListItemId());
        }

        result.remove(0);

        return result;
    }


    protected abstract JSONObject getJSON() throws JSONException;
}
