package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Structs.HeaderNode;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupWidget extends EntryWidget {
    Context context;
    WidgetLayout layout;

    public static final String className = "group widget";
    public GroupWidget(Context context){
        super(context);
        this.context = context;
        layout = new WidgetLayout(context);
        setChild(layout.getView());
        getView().setId(R.id.groupWidget);
    }

    @Override
    public EntryValueTree getEntryValueTree() {
        EntryValueTree tree = new EntryValueTree();
        for(EntryWidget widget: entryWidgets()){
            tree.add(widget.getEntryValueTree());
        }
        return tree;
    }

    public ArrayList<EntryWidget> entryWidgets(){
        ArrayList<EntryWidget> entryWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            entryWidgets.add((EntryWidget) widget);
        }
        return entryWidgets;
    }



    public ArrayList<EntryWidgetParam> getDataWidgets(){
        ArrayList<EntryWidget> entryWidgets = entryWidgets();
        //System.out.println("getting group widget data numWidget: " + entryWidgets.size());
        ArrayList<EntryWidgetParam> params = new ArrayList<>();
        for(EntryWidget entryWidget: entryWidgets){
            EntryWidgetParam entryWidgetParam = entryWidget.getParam();
            params.add(entryWidgetParam);
        }

        return params;
    }

    @Override
    public EntryWidgetParam getParam() {
        return new GroupWidgetParam(null, getDataWidgets());
    }

    @Override
    public void setValue(EntryValueTree entryValueTree) {
        ArrayList<EntryWidget> entryWidgets = entryWidgets();
        //System.out.println("group widget setting value: " + dataTree.hierarchy());
        for(int i = 0; i < entryWidgets.size(); i++){

            EntryWidget entryWidget = entryWidgets.get(i);
            //System.out.println("setting value for entry widget: " + entryWidget);
            entryWidget.setValue(entryValueTree.getTree(i));
        }
    }

    @Override
    public void setParamCustom(EntryWidgetParam params) {
        GroupWidgetParam groupParams = (GroupWidgetParam) params;
        layout.inflateAll(groupParams.params, onDataChangedListener());
    }

    public WidgetLayout getWidgetLayout(){
        return layout;
    }

    public LinLayout getLinLayout() {
        return getWidgetLayout().getLinLayout();
    }

    public static class GroupWidgetParam extends EntryWidgetParam {
        public ArrayList<EntryWidgetParam> params;
        public GroupWidgetParam(String name, ArrayList<EntryWidgetParam> params){
            super(name, GroupWidget.className);
            if(params == null)
                throw new RuntimeException("array is null");
            for(EntryWidgetParam param: params)
                if(param == null)
                    throw new RuntimeException("param in array is null");
            this.params = params;
        }

        public GroupWidgetParam(String name, EntryWidgetParam[] params){
            super(name, GroupWidget.className);
            this.params = new ArrayList<>(Arrays.asList(params));
        }

        public String toString(){
            return hierarchyString(0);
        }

        public String hierarchyString(int numTabs){
            String tabs = GLib.tabs(numTabs);
            String result = tabs + "group widget ("+name+") \n";
            result += hierarchyStringFromList(numTabs);
            return result;
        }

        @Override
        public HeaderNode createHeaderNode() {
            HeaderNode header = new HeaderNode(null);
            for(EntryWidgetParam param: params){
                header.add(param.createHeaderNode());
            }
            return header;
        }

        public String hierarchyStringFromList(int numTabs){
            String result = "";
            for(EntryWidgetParam widgetParam: params){
                result += widgetParam.hierarchyString(numTabs + 1);
            }
            return result;
        }
    }
}
