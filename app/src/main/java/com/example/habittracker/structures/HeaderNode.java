package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;

import java.util.ArrayList;

public class HeaderNode{
    private ArrayList<HeaderNode> children;
    private EntryWidgetParam widgetParam;
    private HeaderNode parent;
    public HeaderNode(EntryWidgetParam widgetParam){
        if(widgetParam == null)
            throw new RuntimeException();
        //MainActivity.log("saving headerNode, name: " + name + ", widgetParam: " + widgetParam.name);
        this.widgetParam = widgetParam;
        children = new ArrayList<>();
    }

    public void add(HeaderNode child){
        children.add(child);
    }

    public String getName(){
        return widgetParam.name;
    }

    public HeaderNode getByWidget(WidgetInStructure widgetInStructure){
        for(HeaderNode headerNode: children){
            if(headerNode.widgetParam.widgetIdTracker == widgetInStructure.getWidgetId()){
                return headerNode;
            }
        }
        MainActivity.log("tried to find headerNode from widget: " + widgetInStructure);
        MainActivity.log("available widget ids: " + getWidgetIdListOfChildren());
        throw new RuntimeException();
    }

    private ArrayList<WidgetInStructure> getWidgetIdListOfChildren() {
        return EnumLoop.makeList(children, (headerNode -> headerNode.widgetParam.getWidgetId()));
    }

    public ArrayList<HeaderNode> getChildren(){
        return children;
    }

    public ArrayList<HeaderNode> gatherNodes(){
        ArrayList<HeaderNode> result = new ArrayList<>();
        result.add(this);
        for(HeaderNode headerNode: children){
            result.addAll(headerNode.gatherNodes());
        }
        return result;
    }

    public WidgetPath getWidgetPath(){
        ArrayList<WidgetInStructure> widgetInStructureList = new ArrayList<>();
        getWidgetPathIteration(widgetInStructureList);
        widgetInStructureList.remove(0);
        return new WidgetPath(widgetInStructureList);
    }

    public void getWidgetPathIteration(ArrayList<WidgetInStructure> currentWidgetPath){
        if(parent != null)
            parent.getWidgetPathIteration(currentWidgetPath);
        currentWidgetPath.add(widgetParam.getWidgetId());
    }




    public String hierarchyString(int numTabs){
        String tabString = GLib.tabs(numTabs);
        Integer id = null;
        if(widgetParam.getWidgetId() != null)
            id = widgetParam.getWidgetId().getWidgetId();
        String result = tabString + "(" + getChildren().size() + ")" +getName() + ", id: "+id+"\n";

        for(HeaderNode node: getChildren()){
            result += node.hierarchyString(numTabs+1);
        }
        return result;
    }


    public EntryWidgetParam getWidgetParam() {
        return widgetParam;
    }

    public void setDoublePointers() {
        for(HeaderNode headerNode: children){
            headerNode.parent = this;
            headerNode.setDoublePointers();
        }
    }
}