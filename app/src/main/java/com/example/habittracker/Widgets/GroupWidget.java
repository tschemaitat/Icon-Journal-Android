package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

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
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<WidgetValue> result = new ArrayList<>();

        for(EntryWidget widget: entryWidgets()){
            result.add(widget.getEntryValueTree());
        }

        return new GroupValue(result);
    }

    public ArrayList<EntryWidget> entryWidgets(){
        ArrayList<EntryWidget> entryWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            entryWidgets.add((EntryWidget) widget);
        }
        return entryWidgets;
    }





//    public ArrayList<EntryWidgetParam> getDataWidgets(){
//        ArrayList<EntryWidget> entryWidgets = entryWidgets();
//        //System.out.println("getting group widget data numWidget: " + entryWidgets.size());
//        ArrayList<EntryWidgetParam> params = new ArrayList<>();
//        for(EntryWidget entryWidget: entryWidgets){
//            EntryWidgetParam entryWidgetParam = entryWidget.getParam();
//            params.add(entryWidgetParam);
//        }
//
//        return params;
//    }
//
//    @Override
//    public EntryWidgetParam getParam() {
//        return new GroupWidgetParam(null, getDataWidgets());
//    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {
        GroupValue groupValue = (GroupValue) widgetValue;
        MainActivity.log("group setting values: \n" + groupValue.hierarchy());

        ArrayList<EntryWidget> entryWidgets = entryWidgets();
        for(int i = 0; i < entryWidgets.size(); i++){

            EntryWidget entryWidget = entryWidgets.get(i);
            MainActivity.log("widget: " + entryWidget.getName() + ", id: " + entryWidget.getWidgetId());
            entryWidget.setValue(groupValue.getWidgetValueByWidget(entryWidget.getWidgetId()));
        }
    }

    @Override
    public void setParamCustom(EntryWidgetParam param) {
        GroupWidgetParam groupParams = (GroupWidgetParam) param;
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

        @Override
        public void setStructureCustom(Structure structure){
            for(EntryWidgetParam entryWidgetParam: params)
                entryWidgetParam.setStructure(structure);
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
            HeaderNode header = new HeaderNode(this);
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
