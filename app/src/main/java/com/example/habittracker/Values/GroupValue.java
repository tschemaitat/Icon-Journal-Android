package com.example.habittracker.Values;

import com.example.habittracker.Algorithms.Lists;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.defaultImportPackage.ListGetterInterface;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.structurePack.WidgetPath;
import com.example.habittracker.structurePack.ListItemId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.habittracker.defaultImportPackage.ArrayList;

import java.util.Collection;
import java.util.Objects;

public class GroupValue extends WidgetValue{
    public static final String className = "groupValue";
    private ListItemId listItemId = null;
    private ArrayList<WidgetValue> values = new ArrayList<>();
    private ListValue parent;
    private boolean setIds = false;
    public GroupValue(ArrayList<WidgetValue> values){
        super(null);
        if(values == null)
            throw new RuntimeException();
        this.values = new ArrayList<>(values);

        for(WidgetValue widgetValue: values){
            widgetValue.setParentGroupValue(this);
        }
    }

    public GroupValue(){
        super(null);
        values = new ArrayList<>();
    }

    public ListGetterInterface<BaseWidgetValue> getBaseWidgetValues(){
        ArrayList<BaseWidgetValue> result = new ArrayList<>();
        for(WidgetValue widgetValue: values){
            if(widgetValue instanceof ListValue listValue){
                result.addAll((Collection<? extends BaseWidgetValue>) listValue.getBaseWidgetValues());
            }
        }
        return result;
    }



    public void setParentListValue(ListValue listValue){
        this.parent = listValue;
    }

    public ListValue getListValueByWidget(WidgetInStructure widgetInStructure){
        WidgetValue widgetValue = getWidgetValueByWidget(widgetInStructure);
        if(widgetValue instanceof ListValue listValue)
            return listValue;
        logErrorFindingWidgetValue(widgetInStructure);
        throw new RuntimeException();
    }

    public void logErrorFindingWidgetValue(WidgetInStructure widgetInStructure){
        MainActivity.log("tried to find: " + widgetInStructure + ", from: \n" + values);
        MainActivity.log("available widgetIds: " + getWidgetIdList());
    }

    public WidgetValue getWidgetValueByWidget(WidgetInStructure widgetInStructure) {
        for(WidgetValue widgetValue : values)
            if(widgetValue.getWidgetId().equals(widgetInStructure.getWidgetId()))
                return widgetValue;
        logErrorFindingWidgetValue(widgetInStructure);
        return null;
    }
    public BaseWidgetValue getBaseWidgetValueByWidget(WidgetInStructure widgetInStructure) {
        WidgetValue widgetValue = getWidgetValueByWidget(widgetInStructure);
        if(widgetValue instanceof BaseWidgetValue baseWidgetValue)
            return baseWidgetValue;
        //logErrorFindingWidgetValue(widgetInStructure);

//        ArrayList<WidgetId> widgetsInGroup = values.convert((index, widgetValue1) -> {
//            return widgetValue1.getWidgetId();
//        });
        return null;
//        String widgetInfoString = widgetInStructure.getStructure().getWidgetsInfoString();
//        throw new RuntimeException(widgetInfoString + "\ntried to find widget: " + widgetInStructure.getWidgetId() + ", " + widgetInStructure.getName() +
//                ", widget in group: " + widgetsInGroup);
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
        WidgetInStructure lastWidgetInStructure = widgetPath.getLast();
        for(GroupValue groupValue: groupValueList){
            BaseWidgetValue baseWidgetValue = groupValue.getBaseWidgetValueByWidget(lastWidgetInStructure);
            if(baseWidgetValue != null)
                baseWidgetValueList.add(baseWidgetValue);
        }

        return baseWidgetValueList;
    }

    public static ArrayList<GroupValue> processList(ArrayList<GroupValue> groupValueList, WidgetInStructure widgetInStructure){
        ArrayList<ListValue> currentTrees = getListArrayFromGroupValueList(groupValueList, widgetInStructure);
        return ListValue.gatherGroupValuesFromList(currentTrees);
    }



    public static ArrayList<ListValue> getListArrayFromGroupValueList(ArrayList<GroupValue> groupValueList, WidgetInStructure widgetInStructure){
        ArrayList<ListValue> result = new ArrayList<>();
        for(GroupValue groupValue: groupValueList){
            //System.out.println("index from tree: " + tree.nameAndLength());
            result.add(groupValue.getListValueByWidget(widgetInStructure));
        }
        return result;
    }

    public ListValue getParentListValue() {
        return parent;
    }

    public BaseWidgetValue getValue(WidgetPath path, ArrayList<ListItemId> listIdList) {
        for(ListItemId listItemId: listIdList){
            if(listItemId == null){
                MainActivity.log("list has a null id: " + listIdList);
                throw new RuntimeException("list item id is null");
            }
        }
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
            MainActivity.log("tried to set listItemId, current id: " + listItemId.getIntegerId() +
                    "\ncurrent tree: " + hierarchy());
            throw new RuntimeException();
        }
        this.listItemId = listItemId;
    }

    public ArrayList<WidgetValue> getValues() {
        return values;
    }

    public JSONObject getJSON() throws JSONException {
        JSONObject json = new JSONObject();
        if(listItemId != null)
            json.put("list id", listItemId.getIntegerId().intValue());
        JSONArray jsonArray = new JSONArray();
        for(WidgetValue widgetValue: getValues()){
            jsonArray.put(widgetValue.getJSON());
        }
        json.put("array", jsonArray);
        json.put("array size", getValues().size());
        json.put(WidgetValue.classNameKey, GroupValue.className);

        return json;
    }

    public static GroupValue getFromJSON(JSONObject jsonObject) throws JSONException{
        Integer listId = null;
        if(jsonObject.has("list id"))
            listId = jsonObject.getInt("list id");
        int arraySize = jsonObject.getInt("array size");
        JSONArray jsonArray = jsonObject.getJSONArray("array");
        ArrayList<WidgetValue> widgetValues = new ArrayList<>();
        for(int i = 0; i < arraySize; i++){
            JSONObject widgetValueJSON = jsonArray.getJSONObject(i);
            WidgetValue widgetValue = StructureTokenizer.getWidgetValue(widgetValueJSON);
            widgetValues.add(widgetValue);
        }
        GroupValue groupValue = new GroupValue(widgetValues);
        if(listId != null)
            groupValue.setListItemId(new ListItemId(listId));
        return groupValue;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof GroupValue groupValue))
            return false;
        if( ! Objects.equals(listItemId, groupValue.listItemId))
            return false;
        if( ! Objects.equals(values, groupValue.values))
            return false;
        if( ! Objects.equals(getWidgetId(), groupValue.getWidgetId()))
            return false;


        return true;
    }
    @Override
    public void equalsThrows(Object object){
        if( ! (object instanceof GroupValue groupValue))
            throw new RuntimeException();
        if(listItemId != null || groupValue.listItemId != null)
            listItemId.equalsThrows(groupValue.listItemId);
        Lists.equalsThrowsRecursive(values, groupValue.values);
        if(getWidgetId() != null || groupValue.getWidgetId() != null)
            getWidgetId().equalsThrows(groupValue.getWidgetId());

        if( ! this.equals(object))
            throw new RuntimeException();
    }

    public String debugString() {
        return "<GroupValue, listId: " + listItemId + ", widgetId: " + getWidgetId() + ">";
    }
}
