package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewWidgets.ListWidgetGhostManager;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.ListParam;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.Layouts.WidgetLayout;

import com.example.habittracker.structures.Structure;

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
        setViewWrapperChild(layout.getView());

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

//    public EntryWidgetParam getParam(){
//        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
//        ArrayList<GroupWidgetParam> groupWidgetParam = new ArrayList<>();
//        for(GroupWidget groupWidget: groupWidgets){
//            groupWidgetParam.add((GroupWidgetParam) groupWidget.getParam());
//        }
//        ListParam params = new ListParam(name, cloneParams, groupWidgetParam);
//
//        return params;
//    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {
        ListValue listValue = (ListValue) widgetValue;
        System.out.println("list setting value: " + listValue.hierarchy());
        for(GroupValue groupValue: listValue.getGroupValueList()){
            GroupWidget groupWidget = addGroup(cloneParams);
            groupWidget.setValue(groupValue);
        }

    }



    public GroupWidget addGroup(GroupWidgetParam param){
        MainActivity.log("adding group");
        GroupWidget groupWidget = new GroupWidget(context);
        groupWidget.setListParent(this);
        groupWidget.setOnDataChangedListener(onDataChangedListener());
        groupWidget.setParam(param);



        LinLayout linLayout = groupWidget.getLinLayout();
        linLayout.getView().setBackground(GLib.setBackgroundColorForView(context, ColorPalette.tertiary));
        Margin.setPadding(linLayout.getView(), Margin.listChildMargin());

        layout.add(groupWidget);
        return groupWidget;
    }

    public void startDrag(GroupWidget draggedGroupWidget) {
        ListWidgetGhostManager listWidgetGhostManager = new ListWidgetGhostManager(this,
                draggedGroupWidget, context, layout);
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
        ArrayList<GroupValue> groupValueList = new ArrayList<>();
        for(GroupWidget groupWidget: groupWidgets){
            groupValueList.add((GroupValue) groupWidget.getValue());
        }
        return new ListValue(getWidgetId(), groupValueList);
    }


    public void setParamCustom(EntryWidgetParam param){
        //System.out.println("setting data for list");
        ListParam listParams = (ListParam) param;
        name = listParams.name;
        cloneParams = listParams.cloneableWidget;
        makeButton();


        //System.out.println("data finished set for list");
    }

    public void makeButton(){
        layout.getLinLayout().addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroup(cloneParams);
            }
        });
    }





}
