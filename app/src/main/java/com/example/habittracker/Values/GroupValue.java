package com.example.habittracker.Values;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.WidgetId;

import java.util.ArrayList;

public class GroupValue extends WidgetValue{
    private ListItemId listItemId = null;
    ArrayList<WidgetValue> values = new ArrayList<>();
    public GroupValue(ArrayList<WidgetValue> values){
        super(null);
        if(values == null)
            throw new RuntimeException();
        this.values = values;
    }

    public GroupValue(){
        super(null);
        values = new ArrayList<>();
    }

    public ListValue getListValueByPosition(int index){
        return (ListValue) values.get(index);
    }

    public WidgetValue getWidgetValueByPosition(int index){
        return values.get(index);
    }

    public ListValue getListValueByWidget(WidgetId widgetId){
        for(WidgetValue widgetValue : values){
            if(widgetValue instanceof ListValue listValue){
                if(listValue.getWidgetId().equals(widgetId))
                    return listValue;
            }
        }
        MainActivity.log("tried to find: " + widgetId + ", from: \n" + values);
        MainActivity.log("available widgetIds: " + getWidgetIdList());
        throw new RuntimeException();
    }

    public WidgetValue getWidgetValueByWidget(WidgetId widgetId) {
        for(WidgetValue widgetValue : values){
            if(widgetValue.getWidgetId().equals(widgetId))
                return widgetValue;
        }
        MainActivity.log("tried to find: " + widgetId + ", from: \n" + values);
        MainActivity.log("available widgetIds: " + getWidgetIdList());
        throw new RuntimeException();
    }

    public ArrayList<WidgetId> getWidgetIdList(){
        return EnumLoop.makeList(values, (value)->value.getWidgetId());
    }

    public ListItemId getListItemId() {
        return listItemId;
    }

    public String hierarchy(){
        return hierarchy(0);
    }

    protected String hierarchy(int tabs){
        String tab = "\t";
        String tabString = "";
        for(int i = 0; i < tabs; i++){
            tabString += tab;
        }
        String result = "";
        result += tabString + nameAndLength() + "\n";
        for(WidgetValue obj: values){
            if( obj instanceof BaseWidgetValue baseWidgetValue){
                result += tabString + tab + baseWidgetValue.getDisplayCachedString() + "\n";
                continue;
            }
            if(obj instanceof ListValue listValue){
                result += listValue.hierarchy(tabs + 1);
            }
        }
        return result;
    }

    private String nameAndLength() {
        return "group (" + values.size() +")";
    }


}
