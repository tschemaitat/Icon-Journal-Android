package com.example.habittracker.Values;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.structurePack.WidgetPath;
import com.example.habittracker.structurePack.ListItemId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ListValue extends WidgetValue {
    public static final String className = "list value";
    private ArrayList<GroupValue> groupValueList;
    public ListValue(WidgetId widgetId, ArrayList<GroupValue> groupValueList){
        super(widgetId);
        this.groupValueList = groupValueList;
        for(GroupValue groupValue: groupValueList)
            groupValue.setParentListValue(this);
    }



    public ArrayList<GroupValue> getGroupValueList() {
        return (ArrayList<GroupValue>) groupValueList.clone();
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

    public static ArrayList<GroupValue> gatherGroupValuesFromList(ArrayList<ListValue> listValueList){
        ArrayList<GroupValue> result = new ArrayList<>();
        for(ListValue listValue: listValueList){
            result.addAll(listValue.getGroupValueList());
        }
        return result;
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

    public BaseWidgetValue getValueIteration(WidgetPath path, ArrayList<ListItemId> listIdList, int level) {
        GroupValue groupValue = getByListItemId(listIdList.get(level));
        return groupValue.getValueIteration(path, listIdList, level + 1);
    }

    public void setIdIteration() {
        int max = -1000;
        for(GroupValue groupValue: groupValueList){
            if(groupValue.getListItemId() != null){
                max = Math.max(max, groupValue.getListItemId().getId());
            }
        }
        max++;
        for(GroupValue groupValue: groupValueList){
            if(groupValue.getListItemId() == null){
                groupValue.setListItemId(new ListItemId(max));
                max++;
            }
        }
        for(GroupValue groupValue: groupValueList)
            groupValue.setIdOfTree();
    }

    public JSONObject getJSON() throws JSONException{
        JSONArray jsonArray = new JSONArray();

        for(GroupValue groupValue: getGroupValueList()){
            jsonArray.put(groupValue.getJSON());
        }
        JSONObject result = new JSONObject();
        result.put("array", jsonArray);
        result.put("array size", getGroupValueList().size());
        result.put("widget id", getWidgetId().getInteger().intValue());
        result.put(WidgetValue.classNameKey, "list");
        return result;
    }

    public static ListValue getFromJSON(JSONObject jsonObject) throws JSONException{
        int arraySize = jsonObject.getInt("array size");
        int widgetId = jsonObject.getInt("widget id");
        JSONArray jsonArray = jsonObject.getJSONArray("array");
        ArrayList<GroupValue> groupValues = new ArrayList<>();
        for(int i = 0; i < arraySize; i++){
            JSONObject groupJSON = jsonArray.getJSONObject(i);
            GroupValue groupValue = (GroupValue) StructureTokenizer.getWidgetValue(groupJSON);
            groupValues.add(groupValue);
        }
        ListValue listValue = new ListValue(new WidgetId(widgetId), groupValues);
        return listValue;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof ListValue listValue))
            return false;
        if( ! Objects.equals(getWidgetId(), listValue.getWidgetId()))
            return false;
        if( ! Objects.equals(groupValueList, listValue.groupValueList))
            return false;
        return true;
    }
}
