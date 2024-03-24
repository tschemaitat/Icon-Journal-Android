package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.GroupWidget.*;

import java.util.ArrayList;

public class ListWidget extends EntryWidget {

    private GroupWidgetParam cloneParams = null;
    public static final String className = "list";
    private Context context;
    private GroupWidget groupWidget;
    private String name;

    public ListWidget(Context context){
        super(context);
        this.context = context;
        groupWidget = new GroupWidget(context);
        setChild(groupWidget.getView());


    }



    public EntryWidgetParam getParam(){
        ArrayList<GroupWidgetParam> groupWidgetParam = new ArrayList<>();
        for(EntryWidgetParam widgetParam: groupWidget.getDataWidgets())
            groupWidgetParam.add((GroupWidget.GroupWidgetParam) widgetParam);

        ListParam params = new ListParam(name, cloneParams, groupWidgetParam);

        return params;
    }

    @Override
    public DataTree getDataTree() {
        return groupWidget.getDataTree();
    }


    public void setParamCustom(EntryWidgetParam params){
        System.out.println("setting data for list");
        ListParam listParams = (ListParam) params;
        name = listParams.name;
        cloneParams = listParams.cloneableWidget;
        groupWidget.inflateAll(new ArrayList<>(listParams.currentWidgets));
        makeButton();


        for(Widget widget: groupWidget.widgets()){
            GroupWidget groupChild = (GroupWidget)widget;
            setNewGruop(groupChild);

        }


        System.out.println("data finished set for list");
        System.out.println(this.getParam());
    }

    public void onDataChange(){
        onDataChangedListener().run();
    }

    public void setNewGruop(GroupWidget groupChild){
        groupChild.setOnDataChangedListener(()->onDataChange());
        System.out.println("<list> add border");
        groupChild.addBorder();
        groupChild.setMargin(5, 10);
    }

    public void makeButton(){
        groupWidget.insertButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupWidget newWidget = null;

                newWidget = (GroupWidget) GLib.inflateWidget(context, cloneParams);
                setNewGruop(newWidget);

                groupWidget.addWidget(newWidget);
            }
        });
    }


    public static class ListParam extends EntryWidgetParam {
        public String name = "null";
        public GroupWidget.GroupWidgetParam cloneableWidget;
        public ArrayList<GroupWidgetParam> currentWidgets;

        public ListParam(String name, GroupWidgetParam cloneableWidget, ArrayList<GroupWidgetParam> currentWidgets){
            super(name, ListWidget.className);
            this.cloneableWidget = cloneableWidget;
            this.currentWidgets = currentWidgets;
        }

        public ListParam(String name, GroupWidgetParam cloneableWidget){
            super(name, ListWidget.className);
            this.cloneableWidget = cloneableWidget;
            currentWidgets = new ArrayList<>();
        }

        public ListParam(String name, EntryWidgetParam[] entryWidgetParams){
            super(name, ListWidget.className);
            this.cloneableWidget = new GroupWidgetParam(null, entryWidgetParams);
            currentWidgets = new ArrayList<>();
        }
        public String toString(){
            return hierarchyString(0);
        }

        public String hierarchyString(int numTabs){

            return GLib.tabs(numTabs) + "list\n"
                    + cloneableWidget.hierarchyString(numTabs + 1);
        }

        @Override
        public DataTree header() {
            DataTree tree = cloneableWidget.header();

            return tree;
        }
    }

    public static class ListValue extends WidgetValue{
        public ArrayList<WidgetValue> currentWidgets;
        public ListValue(ArrayList<WidgetValue> currentWidgets){
            this.currentWidgets = currentWidgets;
        }
    }
}
