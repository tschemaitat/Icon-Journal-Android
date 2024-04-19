package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetPath;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.BaseWidgetValue;
import com.example.habittracker.Widgets.GroupWidget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Header {
    private HeaderNode parentNode;
    HashMap<WidgetId, WidgetInfo> widgetMap = new HashMap<>();
    private Structure structure;

    public Header(GroupWidgetParam groupWidgetParam, Structure structure){
        //MainActivity.log("header constructor: \n" + groupWidgetParam.hierarchyString());
        this.structure = structure;
        parentNode = groupWidgetParam.createHeaderNode();
        setupWidgetParams();
    }

    public void setupWidgetParams(){
        parentNode.setDoublePointers();
        ArrayList<HeaderNode> headerNodeList = parentNode.gatherNodes();
        HashSet<WidgetId> currentWidgetIds = new HashSet<>();
        for(HeaderNode headerNode: headerNodeList){
            WidgetId widgetId = headerNode.getWidgetParam().getWidgetId();
            if(widgetId != null)
                currentWidgetIds.add(widgetId);
        }

        int idCounter = 0;
        for(WidgetId widgetId: currentWidgetIds)
            idCounter = Math.max(idCounter, widgetId.getId());
        idCounter++;
        for(HeaderNode headerNode: headerNodeList){
            if(headerNode.getWidgetParam().getWidgetId() == null){
                headerNode.getWidgetParam().setWidgetId(idCounter);
                currentWidgetIds.add(headerNode.getWidgetParam().getWidgetId());
            }
        }
        widgetMap = new HashMap<>();
        for(HeaderNode headerNode: headerNodeList){
            WidgetInfo widgetInfo = new WidgetInfo(headerNode.getWidgetParam(), headerNode.getWidgetPath());
            widgetMap.put(headerNode.getWidgetParam().getWidgetId(), widgetInfo);
        }
    }




//    public void init(HashMap<Integer, WidgetPath> oldValuePaths){
//        MainActivity.log("init header: " + structure.getCachedName());
//        ArrayList<ImmutableList<Integer>> pathList = new ArrayList<>();
//        ArrayList<EntryWidgetParam> widgetParams = new ArrayList<>();
//        ArrayList<Integer> previousIdSet = new ArrayList<>();
//        parentNode.traverse(pathList, widgetParams, new ArrayList<>());
//
//        for(EntryWidgetParam entryWidgetParam: widgetParams){
//            if(entryWidgetParam.widgetIdTracker != null){
//                previousIdSet.add(entryWidgetParam.widgetIdTracker);
//            }
//        }
//        MainActivity.log("old paths: " + oldValuePaths + "\nprevious ids: " + previousIdSet);
//
//
//        int idCounter = 0;
//        for(int pathIndex = 0; pathIndex < pathList.size(); pathIndex++){
//            while(previousIdSet.contains(idCounter))
//                idCounter = idCounter+1;
//            EntryWidgetParam widgetParam = widgetParams.get(pathIndex);
//            WidgetId widgetId = null;
//            ImmutableList<Integer> widgetPath = null;
//            if(widgetParam.widgetIdTracker == null){
//                widgetPath = pathList.get(pathIndex);
//                MainActivity.log(widgetParam.name + ": setting new widget id to: " + idCounter);
//                widgetParam.widgetIdTracker = idCounter;
//                idCounter++;
//            }else{
//                widgetPath = oldValuePaths.get(widgetParam.widgetIdTracker);
//                if(widgetPath == null){
//                    MainActivity.log("current id: " + widgetParam.widgetIdTracker);
//                    MainActivity.log("old paths: " + oldValuePaths + "\n old ids: " + previousIdSet);
//                    throw new RuntimeException();
//                }
//
//                MainActivity.log(widgetParam.name + ": maintaining id: " + widgetParam.widgetIdTracker);
//            }
//
//            widgetId = new WidgetId(widgetParam.widgetIdTracker, structure);
//            widgetMap.put(widgetId, new WidgetInfo(widgetParam, pathList.get(pathIndex), widgetPath));
//        }
//    }



    public HeaderNode getParentNode(){
        return parentNode;
    }

    public WidgetPath getWidgetPath(WidgetId widgetId){
        return widgetMap.get(widgetId).getWidgetPath();
    }

    public ArrayList<WidgetPath> getWidgetPathList(ArrayList<WidgetId> widgetIdList){
        ArrayList<WidgetPath> pathList = new ArrayList<>();
        for(WidgetId widgetId : widgetIdList)
            pathList.add(getWidgetPath(widgetId));
        return pathList;
    }



    public WidgetInfo getWidgetInfo(WidgetId widgetId){
        return widgetMap.get(widgetId);
    }

    public ArrayList<WidgetId> getWidgetIdList() {
        return new ArrayList<>(widgetMap.keySet());
    }

    public EntryWidgetParam getWidgetParamFromId(WidgetId widgetId) {
        return widgetMap.get(widgetId).getEntryWidgetParam();
    }



    public static class WidgetInfo{
        private EntryWidgetParam entryWidgetParam;
        private WidgetPath widgetPath;
        public WidgetInfo(EntryWidgetParam entryWidgetParam, WidgetPath widgetPath){
            this.entryWidgetParam = entryWidgetParam;
            this.widgetPath = widgetPath;
        }

        public EntryWidgetParam getEntryWidgetParam() {
            return entryWidgetParam;
        }

        public WidgetPath getWidgetPath() {
            return widgetPath;
        }
    }
}
