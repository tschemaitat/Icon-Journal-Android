package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.WidgetParams.ListSingleItemParam;

import java.util.ArrayList;

public class ListWidgetSingleItem extends ListWidget {
    public static final String className = "list single item";
    private Context context;
    private ListSingleItemParam param;

    public ListWidgetSingleItem(Context context) {
        super(context);
        this.context = context;
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout.getView());

        Margin.setListWidgetLayout(layout.getLinLayout());

    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<EntryWidget> entryWidgetList = EnumLoop.makeList(getWidgetListWithoutGhost(), widget -> (EntryWidget) widget);
        ArrayList<GroupValue> groupValueList = new ArrayList<>();
        for(EntryWidget entryWidget: entryWidgetList){
            WidgetValue widgetValue = entryWidget.getValue();
            ArrayList<WidgetValue> widgetValueList = new ArrayList<>();
            widgetValueList.add(widgetValue);
            GroupValue groupValue = new GroupValue(widgetValueList);
            groupValue.setListItemId(entryWidget.getListItemId());
            groupValueList.add(groupValue);
        }
        return new ListValue(param.getWidgetId(), groupValueList);
    }

    @Override
    public void onItemCreated(Widget widget){

    }

    @Override
    public void setValueListCustom(WidgetValue widgetValue) {
        MainActivity.log("set value single item");
        ListValue listValue = (ListValue) widgetValue;
        EntryWidgetParam entryWidgetParam = param.widgetParam;
        ArrayList<GroupValue> groupValueList = listValue.getGroupValueList();
        for(GroupValue groupValue: groupValueList){
            EntryWidget item = createItem();
            layout.add(item);
            WidgetValue valueInGroup = groupValue.getWidgetValueByWidget(entryWidgetParam.getWidgetId());
            item.setValue(valueInGroup);
            item.setListItemId(groupValue.getListItemId());
        }
        addGhostItem(createItem());

    }

    @Override
    protected void setHint(String hintString) {

    }

    @Override
    public void setParamCustom(EntryWidgetParam param) {
        this.param = (ListSingleItemParam) param;
        setWidgetParam(this.param.widgetParam);

    }
}
