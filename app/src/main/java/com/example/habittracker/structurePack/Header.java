package com.example.habittracker.structurePack;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;

import com.example.habittracker.defaultImportPackage.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Header {
    private HeaderNode parentNode;
    Set<WidgetInStructure> widgetMap = new HashSet<>();
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

        parentNode.setAsParent();
        headerNodeList.remove(parentNode);

        String headerNodePrintOut = "headerNode Id's: ";
        for(HeaderNode headerNode: headerNodeList){
            headerNodePrintOut += headerNode.debugId + ", ";
        }
        MainActivity.log(headerNodePrintOut);
        MainActivity.log("header nodes after gather: \n" + parentNode.hierarchyString(0));
        HashSet<WidgetId> currentWidgets = new HashSet<>();
        for(HeaderNode headerNode: headerNodeList){
            EntryWidgetParam entryWidgetParam = headerNode.getWidgetParam();
            if(entryWidgetParam.hasWidgetId()){
                MainActivity.log("headerNode: " + headerNode.debugId + ", has widgetId: " + entryWidgetParam.getWidgetId());
                currentWidgets.add(entryWidgetParam.getWidgetId());
            }

        }

        int idCounter = -1;
        for(WidgetId widgetId : currentWidgets)
            idCounter = Math.max(idCounter, widgetId.getIntegerId());
        idCounter++;
        for(HeaderNode headerNode: headerNodeList){
            if( ! headerNode.getWidgetParam().hasWidgetId()){
                MainActivity.log("headerNode: " + headerNode.debugId + ", setting widgetId: " + idCounter);
                headerNode.getWidgetParam().setWidgetId(new WidgetId(idCounter));
                currentWidgets.add(headerNode.getWidgetParam().getWidgetId());
                idCounter++;
                MainActivity.log("checking if set worked: " + headerNode.getWidgetParam().getWidgetId());
            }
        }
        MainActivity.log("header nodes after setting ids: \n" + parentNode.hierarchyString(0));
        for(HeaderNode headerNode: headerNodeList){
            headerNode.getWidgetParam().setWidgetInStructure(new WidgetInStructure(structure));
        }

        for(HeaderNode headerNode: headerNodeList){
            MainActivity.log("final loop, header node: " + headerNode.debugId);
            WidgetInfo widgetInfo = new WidgetInfo(headerNode.getWidgetParam(), headerNode.getWidgetPath());
            WidgetInStructure widgetInStructure = headerNode.getWidgetParam().getWidgetInStructure();
            widgetInStructure.setWidgetInfo(widgetInfo);

            widgetMap.add(widgetInStructure);
        }

        MainActivity.log("map keys generated: " + widgetMap);
        MainActivity.log("\n\n");
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



    public static class WidgetInfo{
        private EntryWidgetParam entryWidgetParam;
        private WidgetPath widgetPath;
        public WidgetInfo(EntryWidgetParam entryWidgetParam, WidgetPath widgetPath){
            this.entryWidgetParam = entryWidgetParam;
            this.widgetPath = widgetPath;
            if( ! entryWidgetParam.hasStructure()){
                MainActivity.log("widget doesn't have structure, widgetId: " + entryWidgetParam.getWidgetIdNullable());
                throw new RuntimeException("widget info no structure");
            }
            if( ! entryWidgetParam.hasWidgetId()){
                MainActivity.log("widget doesn't have widget id, widgetId: " + entryWidgetParam.getWidgetIdNullable());
                throw new RuntimeException("widget info no widget id");
            }
            MainActivity.log("widget info made: " + entryWidgetParam.getWidgetIdNullable());
        }

        public EntryWidgetParam getEntryWidgetParam() {
            return entryWidgetParam;
        }

        public WidgetPath getWidgetPath() {
            return widgetPath;
        }

        public ArrayList<WidgetInStructure> getReferences(){
            if( ! (entryWidgetParam instanceof DropDownParam dropDownParam))
                return new ArrayList<>();
            return dropDownParam.getWidgetReferences();

        }
    }
}
