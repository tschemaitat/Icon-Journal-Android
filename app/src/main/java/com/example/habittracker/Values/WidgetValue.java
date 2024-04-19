package com.example.habittracker.Values;

import com.example.habittracker.Structs.WidgetId;

import java.util.ArrayList;

public abstract class WidgetValue {
    private WidgetId widgetId;
    private GroupValue parent;
    public WidgetValue(WidgetId widgetId){
        this.widgetId = widgetId;
    }


    public WidgetId getWidgetId() {
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
}
