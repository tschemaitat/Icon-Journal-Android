package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.WidgetParams.ListParam;

import java.util.ArrayList;

public class ListWidgetMultipleItems extends ListWidget{
    private ListParam listParam = null;
    private Context context;
    public static final String className = "list multiple items";
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
        }

    }


    @Override
    protected void onItemCreated(Widget item){
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
        return new ListValue(getWidgetId(), groupValueList);
    }

    public void setParamCustom(EntryWidgetParam param){
        this.listParam = (ListParam) param;
    }
}
