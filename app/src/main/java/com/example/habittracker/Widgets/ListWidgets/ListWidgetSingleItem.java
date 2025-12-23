package com.example.habittracker.Widgets.ListWidgets;

import android.content.Context;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.EntryWidgets.AbstractWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.ListValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.ListParam;

import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.ListItemId;

public class ListWidgetSingleItem extends ListWidget {
    public static final String className = "list";
    public static final String childClassName = "list single item";
    private Context context;
    private ListParam param;

    public ListWidgetSingleItem(Context context, ViewWrapper wrapperElement, EntryWidgetResources entryWidgetResources) {
        super(context, wrapperElement, entryWidgetResources);
        this.context = context;

        Margin.setListWidgetLayout(layout.getLinearElementLayout());

    }

    public static GroupValue createGroupValueFromWidgetValue(AbstractWidget baseEntryWidget){
        WidgetValue widgetValue = (WidgetValue) baseEntryWidget.getValue();
        ArrayList<WidgetValue> widgetValueList = new ArrayList<>();
        widgetValueList.add(widgetValue);
        GroupValue groupValue = new GroupValue(widgetValueList);
        groupValue.setListItemId(baseEntryWidget.getListItemId());
        return groupValue;
    }

    @Override
    public WidgetValue getValue() {
        ArrayList<AbstractWidget> entryWidgetList = getWidgetListWithoutGhost();
        ArrayList<GroupValue> groupValueList = new ArrayList<>();
        for(AbstractWidget entryWidget: entryWidgetList){
            groupValueList.add(createGroupValueFromWidgetValue(entryWidget));
        }
        return new ListValue(param.getWidgetInStructure().getWidgetId(), groupValueList);
    }

    @Override
    public GroupValue getGroupValueSingleItem(ListItemId listItemId) {

        ArrayList<AbstractWidget> entryWidgetList = getWidgetListWithoutGhost();
        AbstractWidget selectedWidget = null;
        for(AbstractWidget entryWidget: entryWidgetList){
            if(entryWidget.getListItemId().equals(listItemId)){
                selectedWidget = entryWidget;
                break;
            }
        }
        WidgetValue widgetValue = (WidgetValue) selectedWidget.getValue();
        ArrayList<WidgetValue> widgetValueList = new ArrayList<>();
        widgetValueList.add(widgetValue);
        GroupValue groupValue = new GroupValue(widgetValueList);
        groupValue.setListItemId(selectedWidget.getListItemId());
        return groupValue;
    }

    @Override
    public void onItemCreated(AbstractWidget widget){

    }

    @Override
    public void setValueListCustom(Object widgetValue) {
        MainActivity.log("set value single item");
        ListValue listValue = (ListValue) widgetValue;
        EntryWidgetParam entryWidgetParam = param.cloneableWidget.params.get(0);
        ArrayList<GroupValue> groupValueList = listValue.getGroupValueList();
        for(GroupValue groupValue: groupValueList){
            AbstractWidget item = (AbstractWidget)createItem();
            layout.add(item);
            WidgetValue valueInGroup = groupValue.getWidgetValueByWidget(entryWidgetParam.getWidgetInStructure());
            item.setValue(valueInGroup);
            item.setListItemIdProvider(new SingleItemIdProvider(groupValue.getListItemId(), getListItemIdProvider()));
        }
        addGhostItem(createItem());

    }

    @Override
    public void setParamCustom(EntryWidgetParam param) {
        this.param = (ListParam) param;
        setWidgetParam(this.param.cloneableWidget.params.get(0));

    }
}
