package com.example.habittracker.Widgets.ListWidgets;

import android.content.Context;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;
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
        ArrayList<BaseEntryWidget> entryWidgetList = EnumLoop.makeList(getWidgetListWithoutGhost(), widget -> (BaseEntryWidget) widget);
        ArrayList<GroupValue> groupValueList = new ArrayList<>();
        for(BaseEntryWidget entryWidget: entryWidgetList){
            WidgetValue widgetValue = entryWidget.getValue();
            ArrayList<WidgetValue> widgetValueList = new ArrayList<>();
            widgetValueList.add(widgetValue);
            GroupValue groupValue = new GroupValue(widgetValueList);
            groupValue.setListItemId(entryWidget.getListItemId());
            groupValueList.add(groupValue);
        }
        return new ListValue(param.getWidgetInStructure().getWidgetId(), groupValueList);
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
            BaseEntryWidget item = (BaseEntryWidget)createItem();
            layout.add(item);
            WidgetValue valueInGroup = groupValue.getWidgetValueByWidget(entryWidgetParam.getWidgetInStructure());
            item.setValue(valueInGroup);
            item.setListItemIdProvider(new SingleItemIdProvider(groupValue.getListItemId()));
        }
        addGhostItem(createItem());

    }

    @Override
    public ArrayList<BaseEntryWidget> getWidgetsForDeleteIteration(){
        ArrayList<BaseEntryWidget> baseEntryWidgets = EnumLoop.makeList(getEntryWidgetList(),
                (entryWidget -> (BaseEntryWidget) entryWidget));
        return GroupWidget.gatherRefForDeleteWidgetsAndList(baseEntryWidgets);
    }

    public void gatherWidgetsCheckedIteration(ArrayList<EntryWidget> resultList) {
        if(isDeleteChecked)
            throw new RuntimeException();
        for(EntryWidget entryWidget: getEntryWidgetList()){
            if(entryWidget.isDeleteChecked)
                resultList.add(entryWidget);
        }
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
