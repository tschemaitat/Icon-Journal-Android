package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Widgets.GroupWidget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Header {
    private HeaderNode parentNode;
    HashMap<WidgetId, WidgetInfo> widgetMap = new HashMap<>();
    private Structure structure;

    public Header(GroupWidgetParam groupWidgetParam, Structure structure,
                  HashMap<Integer, ValueTreePath> oldValuePaths){
        //MainActivity.log("header constructor: \n" + groupWidgetParam.hierarchyString());
        this.structure = structure;
        parentNode = groupWidgetParam.createHeaderNode();
        init(oldValuePaths);
    }



    public void init(HashMap<Integer, ValueTreePath> oldValuePaths){
        MainActivity.log("init header: " + structure.getCachedName());
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        ArrayList<EntryWidgetParam> widgetParams = new ArrayList<>();
        ArrayList<Integer> previousIdSet = new ArrayList<>();
        parentNode.traverse(pathList, widgetParams, new ArrayList<>());

        for(EntryWidgetParam entryWidgetParam: widgetParams){
            if(entryWidgetParam.widgetIdTracker != null){
                previousIdSet.add(entryWidgetParam.widgetIdTracker);
            }
        }
        MainActivity.log("old paths: " + oldValuePaths + "\nprevious ids: " + previousIdSet);


        int idCounter = 0;
        for(int pathIndex = 0; pathIndex < pathList.size(); pathIndex++){
            while(previousIdSet.contains(idCounter))
                idCounter = idCounter+1;
            EntryWidgetParam widgetParam = widgetParams.get(pathIndex);
            WidgetId widgetId = null;
            ValueTreePath valueTreePath = null;
            if(widgetParam.widgetIdTracker == null){
                valueTreePath = pathList.get(pathIndex);
                MainActivity.log(widgetParam.name + ": setting new widget id to: " + idCounter);
                widgetParam.widgetIdTracker = idCounter;
                idCounter++;
            }else{
                valueTreePath = oldValuePaths.get(widgetParam.widgetIdTracker);
                if(valueTreePath == null){
                    MainActivity.log("current id: " + widgetParam.widgetIdTracker);
                    MainActivity.log("old paths: " + oldValuePaths + "\n old ids: " + previousIdSet);
                    throw new RuntimeException();
                }

                MainActivity.log(widgetParam.name + ": maintaining id: " + widgetParam.widgetIdTracker);
            }

            widgetId = new WidgetId(widgetParam.widgetIdTracker, structure);
            widgetMap.put(widgetId, new WidgetInfo(widgetParam, pathList.get(pathIndex), valueTreePath));
        }
    }

    public ArrayList<WidgetId> getImportantWidgets(){
        ArrayList<WidgetId> result = new ArrayList<>();
        for(WidgetId widgetId: widgetMap.keySet()){
            if(widgetId.getWidgetParam().isUniqueAttribute)
                result.add(widgetId);
        }
        return result;
    }

    public HeaderNode getParentNode(){
        return parentNode;
    }

    public ValueTreePath getWidgetPath(WidgetId widgetId){
        return widgetMap.get(widgetId).getWidgetPath();
    }

    public ArrayList<ValueTreePath> getWidgetPathList(ArrayList<WidgetId> widgetIdList){
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        for(WidgetId widgetId : widgetIdList)
            pathList.add(getWidgetPath(widgetId));
        return pathList;
    }

    protected CachedString getEntryName(Entry entry) {
        MainActivity.log("getting entry name");

        ArrayList<WidgetId> importantWidgetList = getImportantWidgets();
        ArrayList<CachedString> arrayStringList = new ArrayList<>();
        MainActivity.log("important widgets: " + importantWidgetList);
        for(WidgetId widgetId: importantWidgetList){

            ArrayList<EntryValueTree> treeList = entry.getEntryValueTree().getValuesFromPath(getWidgetPath(widgetId));
            ArrayString string = new ArrayString(EnumLoop.makeList(treeList, (tree)->tree.generateCachedStringForDisplay()));
            MainActivity.log("getting values from widgetId: " + treeList);
            arrayStringList.add(string);
        }
        return new ArrayString(arrayStringList);
    }


    public ItemPath getWidgetNamePath(WidgetId widgetId) {
        //MainActivity.log("getting widget name");
        ValueTreePath valueTreePath = getWidgetPath(widgetId);
        //MainActivity.log("path: " + valueTreePath);
        ArrayList<String> namePath = new ArrayList<>();
        parentNode.getPathName(namePath, valueTreePath, 0);
        return new ItemPath(namePath);
    }

    public ArrayList<WidgetId> getWidgetIdList() {
        return new ArrayList<>(widgetMap.keySet());
    }

    public HashMap<Integer, ValueTreePath> getValuePathMap() {
        HashMap<Integer, ValueTreePath> result = new HashMap<>();
        for(WidgetId widgetId: widgetMap.keySet()){
            result.put(widgetId.getId(), widgetMap.get(widgetId).getValuePath());
        }
        return result;
    }

    public EntryWidgetParam getWidgetParamFromId(WidgetId widgetId) {
        return widgetMap.get(widgetId).getEntryWidgetParam();
    }

    public ValueTreePath getValueId(int id) {
        return widgetMap.get(new WidgetId(id, structure)).valuePath;
    }


    public static class WidgetInfo{
        private EntryWidgetParam entryWidgetParam;
        private ValueTreePath widgetPath;
        private ValueTreePath valuePath;
        public WidgetInfo(EntryWidgetParam entryWidgetParam, ValueTreePath widgetPath, ValueTreePath valuePath){
            this.entryWidgetParam = entryWidgetParam;
            this.widgetPath = widgetPath;
            this.valuePath = valuePath;
        }

        public EntryWidgetParam getEntryWidgetParam() {
            return entryWidgetParam;
        }

        public ValueTreePath getWidgetPath() {
            return widgetPath;
        }

        public ValueTreePath getValuePath() {
            return valuePath;
        }
    }
}
