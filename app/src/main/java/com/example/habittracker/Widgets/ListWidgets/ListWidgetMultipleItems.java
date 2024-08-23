package com.example.habittracker.Widgets.ListWidgets;

import android.content.Context;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.ListMultiItemParam;

import com.example.habittracker.Widgets.WidgetParams.ListParam;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class ListWidgetMultipleItems extends ListWidget {
    public static String childClassName = "list multiple items";
    private ListParam listMultiItemParam = null;
    private Context context;
    public static final String className = "list";
    public ListWidgetMultipleItems(Context context) {
        super(context);
        this.context = context;
        Margin.setListWidgetLayout(layout.getLinLayout());
    }

    @Override
    public void setValueListCustom(WidgetValue widgetValue) {
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
        LinLayout linLayout = groupWidget.getLinLayout();

        linLayout.getView().setBackground(GLib.setBackgroundColorForView(context, ColorPalette.tertiary));
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


    @Override
    public ArrayList<BaseEntryWidget> getWidgetsForDeleteIteration(){
        ArrayList<BaseEntryWidget> result = new ArrayList<>();
        ArrayList<GroupWidget> groupWidgets = getGroupWidgets();
        for(GroupWidget groupWidget: groupWidgets){
            result.addAll(groupWidget.getWidgetsForDeleteIteration());
        }
        return result;
    }

    public void gatherWidgetsCheckedIteration(ArrayList<EntryWidget> resultList) {
        if(isDeleteChecked)
            throw new RuntimeException();
        for(GroupWidget groupWidget: getGroupWidgets()){
            if(groupWidget.isDeleteChecked){
                resultList.add(groupWidget);
                continue;
            }
            groupWidget.gatherWidgetsCheckedIteration(resultList);
        }
    }

    public void setParamCustom(EntryWidgetParam param){
        this.listMultiItemParam = (ListParam) param;
        setWidgetParam(listMultiItemParam.cloneableWidget);
    }
}
