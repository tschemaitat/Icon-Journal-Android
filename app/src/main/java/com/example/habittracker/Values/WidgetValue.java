package com.example.habittracker.Values;

import com.example.habittracker.Algorithms.ThrowableEqualsWithId;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.defaultImportPackage.ListGetterInterface;
import com.example.habittracker.structurePack.ListItemId;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class WidgetValue implements ThrowableEqualsWithId {
    public static final String classNameKey = "value type";
    private WidgetId widgetId;
    private GroupValue parent;
    public WidgetValue(WidgetId widgetId){
        this.widgetId = widgetId;
    }

    public WidgetId getWidgetId() {
        return widgetId;
    }

    public Integer getIntegerId(){
        return widgetId.getIntegerId();
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

//    public RefEntryString getReference(EntryInStructure entryInStructure){
//        Structure structure = entryInStructure.getStructure();
//        return new RefEntryString(structure.getWidgetInStructureFromId(widgetId), entryInStructure, getListItemIds());
//    }


    protected abstract JSONObject getJSON() throws JSONException;

    public abstract void equalsThrows(Object object);
}
