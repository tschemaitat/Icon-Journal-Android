package com.example.habittracker.Structs;

import java.util.ArrayList;
import java.util.HashMap;

public class Header {
    private HeaderNode parentNode;
    HashMap<WidgetId, ValueTreePath> map;
    private Structure structure;

    public Header(HeaderNode headerNode, Structure structure){
        parentNode = headerNode;
        ArrayList<ValueTreePath> pathList = getPathListFromTraverse();
        for(int i = 0; i < pathList.size(); i++){
            map.put(new WidgetId(i, structure), pathList.get(i));
        }
    }

    public HeaderNode getParentNode(){
        return parentNode;
    }

    public ArrayList<ValueTreePath> getPathListFromTraverse(){
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        parentNode.traverse(pathList, new ArrayList<>());
        return pathList;
    }

    public ValueTreePath getPath(WidgetId widgetId){
        return map.get(widgetId);
    }

    private void generatePathList(){

    }

    public ArrayList<ValueTreePath> getPathList(ArrayList<WidgetId> widgetIdList){
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        for(WidgetId widgetId : widgetIdList)
            pathList.add(getPath(widgetId));
        return pathList;
    }


    public ItemPath getWidgetNamePath(WidgetId widgetId) {
        ValueTreePath valueTreePath = getPath(widgetId);
        ArrayList<String> namePath = new ArrayList<>();
        parentNode.getPathName(namePath, valueTreePath, 0);
        return new ItemPath(namePath);
    }

    public ArrayList<WidgetId> getWidgetIdList() {
        return new ArrayList<>(map.keySet());
    }
}
