package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.Structs.WidgetId;

import java.util.ArrayList;
import java.util.HashMap;

public class Header {
    private HeaderNode parentNode;
    HashMap<WidgetId, ValueTreePath> map = new HashMap<>();
    private Structure structure;

    public Header(HeaderNode headerNode, Structure structure){
        this.structure = structure;
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
        //MainActivity.log("header: \n" + parentNode.hierarchyString(0));
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        //MainActivity.log("paths for structure: " + structure.getCachedName().getString() + "\n" +pathList.toString());
        parentNode.traverse(pathList, new ArrayList<>());
        //MainActivity.log(pathList.toString());
        return pathList;
    }

    public ValueTreePath getPath(WidgetId widgetId){
        return map.get(widgetId);
    }

    public ArrayList<ValueTreePath> getPathList(ArrayList<WidgetId> widgetIdList){
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        for(WidgetId widgetId : widgetIdList)
            pathList.add(getPath(widgetId));
        return pathList;
    }


    public ItemPath getWidgetNamePath(WidgetId widgetId) {
        MainActivity.log("getting widget name");
        ValueTreePath valueTreePath = getPath(widgetId);
        MainActivity.log("path: " + valueTreePath);
        ArrayList<String> namePath = new ArrayList<>();
        parentNode.getPathName(namePath, valueTreePath, 0);
        return new ItemPath(namePath);
    }

    public ArrayList<WidgetId> getWidgetIdList() {
        return new ArrayList<>(map.keySet());
    }
}
