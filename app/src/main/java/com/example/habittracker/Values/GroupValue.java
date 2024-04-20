package com.example.habittracker.Values;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.structures.WidgetPath;
import com.example.habittracker.structures.WidgetId;
import com.example.habittracker.structures.ListItemId;

import java.util.ArrayList;

public class GroupValue extends WidgetValue{
    private ListItemId listItemId = null;
    private ArrayList<WidgetValue> values = new ArrayList<>();
    private ListValue parent;
    private boolean setIds = false;
    public GroupValue(ArrayList<WidgetValue> values){
        super(null);
        if(values == null)
            throw new RuntimeException();
        this.values = values;
        for(WidgetValue widgetValue: values){
            widgetValue.setParentGroupValue(this);
        }
    }

    public GroupValue(){
        super(null);
        values = new ArrayList<>();
    }

    public void setParentListValue(ListValue listValue){
        this.parent = listValue;
    }

    public ListValue getListValueByWidget(WidgetId widgetId){
        WidgetValue widgetValue = getWidgetValueByWidget(widgetId);
        if(widgetValue instanceof ListValue listValue)
            return listValue;
        logErrorFindingWidgetValue(widgetId);
        throw new RuntimeException();
    }

    public void logErrorFindingWidgetValue(WidgetId widgetId){
        MainActivity.log("tried to find: " + widgetId + ", from: \n" + values);
        MainActivity.log("available widgetIds: " + getWidgetIdList());
    }

    public WidgetValue getWidgetValueByWidget(WidgetId widgetId) {
        for(WidgetValue widgetValue : values)
            if(widgetValue.getWidgetId().equals(widgetId))
                return widgetValue;
        logErrorFindingWidgetValue(widgetId);
        throw new RuntimeException();
    }
    public BaseWidgetValue getBaseWidgetValueByWidget(WidgetId widgetId) {
        WidgetValue widgetValue = getWidgetValueByWidget(widgetId);
        if(widgetValue instanceof BaseWidgetValue baseWidgetValue)
            return baseWidgetValue;
        logErrorFindingWidgetValue(widgetId);
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
                result += tabString + tab + baseWidgetValue.getDebugCachedString() + "\n";
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


    public ArrayList<BaseWidgetValue> getValuesFromWidgetPath(WidgetPath widgetPath) {
        //get values from group indexes. The indexes are the key for the location
        //MainActivity.log("indexes: \n" + indexes);
        if(widgetPath.size() == 0){
            MainActivity.log(this.hierarchy());
            throw new RuntimeException();
        }


        ArrayList<GroupValue> groupValueList = new ArrayList<>();
        groupValueList.add(this);

        for(int i = 0; i < widgetPath.size() - 1; i++){
            groupValueList = processList(groupValueList, widgetPath.get(i));
        }
        ArrayList<BaseWidgetValue> baseWidgetValueList = new ArrayList<>();
        WidgetId lastWidgetId = widgetPath.getLast();
        for(GroupValue groupValue: groupValueList){
            baseWidgetValueList.add(groupValue.getBaseWidgetValueByWidget(lastWidgetId));
        }

        return baseWidgetValueList;
    }

    public static ArrayList<GroupValue> processList(ArrayList<GroupValue> groupValueList, WidgetId widgetId){
        ArrayList<ListValue> currentTrees = getListArrayFromGroupValueList(groupValueList, widgetId);
        return ListValue.gatherGroupValuesFromList(currentTrees);
    }



    public static ArrayList<ListValue> getListArrayFromGroupValueList(ArrayList<GroupValue> groupValueList, WidgetId widgetId){
        ArrayList<ListValue> result = new ArrayList<>();
        for(GroupValue groupValue: groupValueList){
            //System.out.println("index from tree: " + tree.nameAndLength());
            result.add(groupValue.getListValueByWidget(widgetId));
        }
        return result;
    }

    public ListValue getParentListValue() {
        return parent;
    }

    public BaseWidgetValue getValue(WidgetPath path, ArrayList<ListItemId> listIdList) {
        if(path.size() - 1 != listIdList.size()){
            MainActivity.log("widget path: " + path + "\nlist id list: " + listIdList);
        }
        return getValueIteration(path, listIdList, 0);
    }

    public BaseWidgetValue getValueIteration(WidgetPath path, ArrayList<ListItemId> listIdList, int level){
        if(level == path.size() - 1){
            BaseWidgetValue baseWidgetValue = getBaseWidgetValueByWidget(path.get(level));
            return baseWidgetValue;
        }
        ListValue listValue = getListValueByWidget(path.get(level));
        return listValue.getValueIteration(path, listIdList, level);
    }

    public void setIdOfTree() {
        if(setIds)
            throw new RuntimeException();
        setIdIteration();
    }

    public void setIdIteration(){
        setIds = true;

        for(WidgetValue widgetValue: values){
            if(widgetValue instanceof ListValue listValue){
                listValue.setIdIteration();
            }
        }

    }

    public void setListItemId(ListItemId listItemId) {
        if(this.listItemId != null){
            MainActivity.log("tried to set listItemId, current id: " + listItemId.getId() +
                    "\ncurrent tree: " + hierarchy());
            throw new RuntimeException();
        }
        this.listItemId = listItemId;
    }
}
