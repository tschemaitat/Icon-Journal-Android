package com.example.habittracker.Values;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.WidgetId;

import java.util.ArrayList;

public class ListValue extends WidgetValue {
    private ArrayList<GroupValue> groupValueList;
    public ListValue(WidgetId widgetId, ArrayList<GroupValue> groupValueList){
        super(widgetId);
        this.groupValueList = groupValueList;
    }

    public ArrayList<GroupValue> getGroupValueList() {
        return groupValueList;
    }

    public GroupValue getByListItemId(ListItemId listItemId){
        for(GroupValue groupValue: groupValueList)
            if(groupValue.getListItemId().equals(listItemId)){
                return groupValue;
            }
        MainActivity.log("tried to find: " + listItemId + " from:\n" + groupValueList);
        MainActivity.log("available ids: " + getListItemIdList());
        throw new RuntimeException();
    }

    public ArrayList<ListItemId> getListItemIdList(){
        return EnumLoop.makeList(groupValueList, (groupValue)->groupValue.getListItemId());
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
        for(GroupValue groupValue: groupValueList){
            result += groupValue.hierarchy(tabs + 1);
        }
        return result;
    }

    private String nameAndLength() {
        return "list: " + getWidgetId() + " (" + groupValueList.size() +")";
    }
}
