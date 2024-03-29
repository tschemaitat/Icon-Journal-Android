package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.WidgetLayout;
import com.example.habittracker.Widgets.GroupWidget.*;

import java.util.ArrayList;

public class ListWidget extends EntryWidget {

    private GroupWidgetParam cloneParams = null;
    public static final String className = "list";
    private Context context;
    private WidgetLayout layout;
    private String name;

    public ListWidget(Context context){
        super(context);
        this.context = context;
        layout = new WidgetLayout(context);
        setChild(layout.getView());


    }

    public ArrayList<GroupWidget> getGroupWidgets(){
        ArrayList<GroupWidget> groupWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            groupWidgets.add((GroupWidget) widget);
        }
        return groupWidgets;
    }

    public EntryWidgetParam getParam(){
        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
        ArrayList<GroupWidgetParam> groupWidgetParam = new ArrayList<>();
        for(GroupWidget groupWidget: groupWidgets){
            groupWidgetParam.add((GroupWidgetParam) groupWidget.getParam());
        }
        ListParam params = new ListParam(name, cloneParams, groupWidgetParam);

        return params;
    }

    @Override
    public void setValue(DataTree dataTree) {
        System.out.println("list setting value: " + dataTree.hierarchy());
        ArrayList<Widget> widgets = layout.widgets();
        for(DataTree tree: dataTree.getList()){
            GroupWidget entryWidget = (GroupWidget) GLib.inflateWidget(context, cloneParams);
            setNewGruop(entryWidget);
            entryWidget.setValue(tree);
            layout.add(entryWidget);
        }

    }

    @Override
    public DataTree getDataTree() {
        DataTree dataTree = new DataTree();
        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
        for(GroupWidget groupWidget: groupWidgets){
            dataTree.put(groupWidget.getDataTree());
        }
        return dataTree;
    }


    public void setParamCustom(EntryWidgetParam params){
        System.out.println("setting data for list");
        ListParam listParams = (ListParam) params;
        name = listParams.name;
        cloneParams = listParams.cloneableWidget;
        layout.inflateAll(new ArrayList<>(listParams.currentWidgets));
        makeButton();


        for(Widget widget: layout.widgets()){
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
    }

    public void makeButton(){
        layout.getLinLayout().addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupWidget newWidget = null;

                newWidget = (GroupWidget) GLib.inflateWidget(context, cloneParams);
                setNewGruop(newWidget);

                layout.add(newWidget);
            }
        });
    }


    public static class ListParam extends EntryWidgetParam {
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

            return GLib.tabs(numTabs) + "list ("+name+")\n"
                    + cloneableWidget.hierarchyStringFromList(numTabs + 1);
        }

        @Override
        public DataTree header() {
            DataTree tree = cloneableWidget.header();
            tree = new DataTree(name).put(tree.getList());

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
