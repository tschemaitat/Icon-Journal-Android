package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Header {
    private HeaderNode parentNode;
    HashMap<WidgetInStructure, WidgetInfo> widgetMap = new HashMap<>();
    private Structure structure;

    public Header(GroupWidgetParam groupWidgetParam, Structure structure){
        //MainActivity.log("header constructor: \n" + groupWidgetParam.hierarchyString());
        this.structure = structure;
        parentNode = groupWidgetParam.createHeaderNode();
        setupWidgetParams();
    }

    public void setupWidgetParams(){
        MainActivity.log("\n\nsetup header for: " + structure.getCachedName());
        parentNode.setDoublePointers();
        ArrayList<HeaderNode> headerNodeList = parentNode.gatherNodes();
        headerNodeList.remove(parentNode);
        MainActivity.log("header nodes after gather: \n" + parentNode.hierarchyString(0));
        HashSet<WidgetInStructure> currentWidgetInStructures = new HashSet<>();
        for(HeaderNode headerNode: headerNodeList){
            WidgetInStructure widgetInStructure = headerNode.getWidgetParam().getWidgetId();
            if(widgetInStructure != null)
                currentWidgetInStructures.add(widgetInStructure);
        }

        int idCounter = 0;
        for(WidgetInStructure widgetInStructure : currentWidgetInStructures)
            idCounter = Math.max(idCounter, widgetInStructure.getWidgetId());
        idCounter++;
        for(HeaderNode headerNode: headerNodeList){
            if(headerNode.getWidgetParam().getWidgetId() == null){
                headerNode.getWidgetParam().setWidgetId(idCounter);
                currentWidgetInStructures.add(headerNode.getWidgetParam().getWidgetId());
                idCounter++;
            }
        }
        MainActivity.log("header nodes after setting ids: \n" + parentNode.hierarchyString(0));
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

    public WidgetPath getWidgetPath(WidgetInStructure widgetInStructure){
        return widgetMap.get(widgetInStructure).getWidgetPath();
    }

    public ArrayList<WidgetPath> getWidgetPathList(ArrayList<WidgetInStructure> widgetInStructureList){
        ArrayList<WidgetPath> pathList = new ArrayList<>();
        for(WidgetInStructure widgetInStructure : widgetInStructureList)
            pathList.add(getWidgetPath(widgetInStructure));
        return pathList;
    }



    public WidgetInfo getWidgetInfo(WidgetInStructure widgetInStructure){
        return widgetMap.get(widgetInStructure);
    }

    public ArrayList<WidgetInStructure> getWidgetIdList() {
        return new ArrayList<>(widgetMap.keySet());
    }

    public EntryWidgetParam getWidgetParamFromId(WidgetInStructure widgetInStructure) {
        return widgetMap.get(widgetInStructure).getEntryWidgetParam();
    }



    public static class WidgetInfo{
        private EntryWidgetParam entryWidgetParam;
        private WidgetPath widgetPath;
        public WidgetInfo(EntryWidgetParam entryWidgetParam, WidgetPath widgetPath){
            this.entryWidgetParam = entryWidgetParam;
            this.widgetPath = widgetPath;
            compileReference();
        }

        public EntryWidgetParam getEntryWidgetParam() {
            return entryWidgetParam;
        }

        public WidgetPath getWidgetPath() {
            return widgetPath;
        }

        private WidgetInStructure reference;
        private void compileReference(){
            if(entryWidgetParam instanceof DropDownParam dropDownParam){
                reference = dropDownParam.getWidgetId();
            }else{
                reference = null;
            }
        }

        public WidgetInStructure getReference(){
            return reference;
        }
    }
}
