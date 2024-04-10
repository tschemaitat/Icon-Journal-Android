package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.HeaderNode;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Layouts.WidgetLayout;
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

        layout.getLinLayout().getView().setBackground(GLib.setBackgroundColorForView(context, ColorPalette.secondary));
        Margin.setPadding(layout.getLinLayout().getView(), Margin.listPadding());

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
    public void setValue(EntryValueTree entryValueTree) {
        System.out.println("list setting value: " + entryValueTree.hierarchy());
        ArrayList<Widget> widgets = layout.widgets();
        for(EntryValueTree tree: entryValueTree.getList()){
            GroupWidget groupWidget = addGroup(cloneParams);
            groupWidget.setValue(tree);
        }

    }

    public GroupWidget addGroup(GroupWidgetParam param){
        GroupWidget groupWidget = new GroupWidget(context);
        groupWidget.setOnDataChangedListener(onDataChangedListener());
        groupWidget.setParam(param);

        LinLayout linLayout = groupWidget.getLinLayout();
        linLayout.getView().setBackground(GLib.setBackgroundColorForView(context, ColorPalette.tertiary));
        Margin.setPadding(linLayout.getView(), Margin.listChildMargin());

        layout.add(groupWidget);
        return groupWidget;
    }

    @Override
    public EntryValueTree getEntryValueTree() {
        EntryValueTree entryValueTree = new EntryValueTree();
        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
        for(GroupWidget groupWidget: groupWidgets){
            entryValueTree.put(groupWidget.getEntryValueTree());
        }
        return entryValueTree;
    }


    public void setParamCustom(EntryWidgetParam params){
        System.out.println("setting data for list");
        ListParam listParams = (ListParam) params;
        name = listParams.name;
        cloneParams = listParams.cloneableWidget;
        layout.inflateAll(new ArrayList<>(listParams.currentWidgets), onDataChangedListener());
        makeButton();


        System.out.println("data finished set for list");
        System.out.println(this.getParam());
    }

    public void makeButton(){
        layout.getLinLayout().addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupWidget newWidget = null;

                newWidget = (GroupWidget) GLib.inflateWidget(context, cloneParams, onDataChangedListener());

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
        public HeaderNode createHeaderNode() {
            HeaderNode tree = cloneableWidget.createHeaderNode();
            HeaderNode result = new HeaderNode(name);
            for(HeaderNode child: tree.getChildren())
                result.add(child);

            return result;
        }
    }

    public static class ListValue extends WidgetValue{
        public ArrayList<WidgetValue> currentWidgets;
        public ListValue(ArrayList<WidgetValue> currentWidgets){
            this.currentWidgets = currentWidgets;
        }
    }
}
