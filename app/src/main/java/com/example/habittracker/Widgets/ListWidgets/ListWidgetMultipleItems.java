package com.example.habittracker.Widgets.ListWidgets;

import android.content.Context;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;


import com.example.habittracker.Widgets.WidgetParams.ListParam;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class ListWidgetMultipleItems extends ListWidget {
    public static String childClassName = "list multiple items";
    private ListParam listMultiItemParam = null;
    private Context context;
    public ListWidgetMultipleItems(Context context, Element wrapperElement) {
        super(context, wrapperElement);
        this.context = context;
        Margin.setListWidgetLayout(layout.getLinearElementLayout());
    }

    @Override
    public void setValueListCustom(Object widgetValue) {
        ListValue listValue = (ListValue) widgetValue;
        System.out.println("list setting value: " + listValue.hierarchy());
        for(GroupValue groupValue: listValue.getGroupValueList()){
            GroupWidget groupWidget = (GroupWidget) createItem();
            layout.add(groupWidget);
            groupWidget.setValue(groupValue);
            groupWidget.setListItemId(groupValue.getListItemId());
            groupWidget.setParentListItemIdProvider(getListItemIdProvider());
        }

    }


    @Override
    protected void onItemCreated(Widget item){
        MainActivity.log("setting group widget onItemCreated");
        GroupWidget groupWidget = (GroupWidget)item;
        LinearElementLayout linLayout = groupWidget.getLinearElementLayout();

        linLayout.setBackground(GLib.setBackgroundColorForView(context, ColorPalette.tertiary));
        Margin.setPadding(linLayout.getView(), Margin.listChildMargin());
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
        ArrayList<GroupValue> groupValueList = new ArrayList<>();
        for(GroupWidget groupWidget: groupWidgets){
            GroupValue groupValue = (GroupValue) groupWidget.getValue();
            if(groupWidget.getListItemId() != null)
                groupValue.setListItemId(groupValue.getListItemId());
            groupValueList.add(groupValue);
        }
        return new ListValue(getWidgetInStructure().getWidgetId(), groupValueList);
    }
    public void setParamCustom(EntryWidgetParam param){
        this.listMultiItemParam = (ListParam) param;
        setWidgetParam(listMultiItemParam.cloneableWidget);
    }
}
