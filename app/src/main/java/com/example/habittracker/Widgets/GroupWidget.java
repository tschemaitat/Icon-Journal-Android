package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.ListItemId;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class GroupWidget extends EntryWidget implements FocusTreeParent, ListItemIdProvider {
    private WidgetLayout<BaseEntryWidget> layout;
    private ListItemId listItemId;
    private ListItemIdProvider listItemIdParent;
    Context context;

    public static final String className = "group widget";
    public GroupWidget(Context context, Element wrapperElement, EntryWidgetResources entryWidgetResources){
        super(context, wrapperElement, entryWidgetResources);
        this.context = context;
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout);
        getView().setId(R.id.groupWidget);
    }

    public ArrayList<BaseEntryWidget> getBaseEntryWidgets(){
        return layout.widgets();
    }
    public ArrayList<EntryWidget> getEntryWidgets(){
        ArrayList<EntryWidget> entryWidgets = new ArrayList<>();
        for(Object widget: layout.widgets()){
            entryWidgets.add((EntryWidget) widget);
        }
        return entryWidgets;
    }

    @Override
    public void setValueCustom(Object widgetValue) {
        ParentWidget.setValueCustom(getBaseEntryWidgets(), (GroupValue) widgetValue);
    }
    @Override
    public void setParamCustom(EntryWidgetParam param) {
        GroupWidgetParam groupParams = (GroupWidgetParam) param;
        ArrayList<Widget> inflatedWidgets = GLib.inflateAll(groupParams.params,entryWidgetResources , context);
        for(Widget widget: inflatedWidgets){
            layout.add((BaseEntryWidget) widget);
        }
        for(BaseEntryWidget widget: getBaseEntryWidgets()){
            widget.setFocusParent(this);
            widget.setListItemIdProvider(this);
        }
    }
    @Override
    public GroupValue getEntryValueTreeCustom() {
        return (GroupValue) ParentWidget.getEntryValueTreeCustom(getBaseEntryWidgets());
    }

    public WidgetLayout getWidgetLayout(){
        return layout;
    }
    public LinearElementLayout getLinearElementLayout() {
        return getWidgetLayout().getLinearElementLayout();
    }

    @Override
    public EntryWidget getFirstWidget() {
        EntryWidget firstWidget = (EntryWidget)layout.widgets().get(0);
        if(firstWidget instanceof FocusTreeParent focusTreeParent){
            return focusTreeParent.getFirstWidget();
        }
        return firstWidget;
    }
    @Override
    public EntryWidget findNextWidget(EntryWidget entryWidget){
        return FocusTreeParentHelper.findNextWidget(entryWidget, getEntryWidgets(), getFocusParent(), this);
    }

    @Override
    public ListItemId getListItemId() {
        return listItemId;
    }
    @Override
    public ArrayList<ListItemId> getListItemIdList() {
        ArrayList<ListItemId> result;
        if(listItemIdParent != null){
            result = listItemIdParent.getListItemIdList();
            if(listItemId == null){
                throw new RuntimeException();
            }

            result.add(listItemId);
        }else{
            result = new ArrayList<>();
        }

        return result;
    }
    @Override
    public void setParentListItemIdProvider(ListItemIdProvider listItemIdProvider) {
        this.listItemIdParent = listItemIdProvider;
    }
    public void setListItemId(ListItemId listItemId) {
        this.listItemId = listItemId;
    }

    public String getNameAndLocation(){
        ArrayList<ListItemId> itemIds = getListItemIdList();
        return getName() + itemIds;
    }

    @Override
    public void setTextErrorColor() {
        throw new RuntimeException();
    }

    @Override
    public void resetTextErrorColor() {
        throw new RuntimeException();
    }

    @Override
    public void setHint(String string) {
        throw new RuntimeException();
    }
}
