package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.EntryWidgets.AbstractWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.ListItemId;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class GroupWidget extends AbstractWidget implements FocusTreeParent, ListItemIdProvider {
    private WidgetLayout<AbstractWidget> layout;
    private ListItemId listItemId;
    private ListItemIdProvider listItemIdParent;
    Context context;

    public static final String className = "group widget";
    public GroupWidget(Context context, ViewWrapper wrapperElement, EntryWidgetResources entryWidgetResources){
        super(context, wrapperElement, entryWidgetResources);
        this.context = context;
        layout = new WidgetLayout(context);
        setWrapperChild();
        getElement().getView().setId(R.id.groupWidget);
    }

    public ArrayList<AbstractWidget> getBaseEntryWidgets(){
        return layout.widgets();
    }
    public ArrayList<AbstractWidget> getEntryWidgets(){
        ArrayList<AbstractWidget> entryWidgets = new ArrayList<>();
        for(AbstractWidget widget: layout.widgets()){
            entryWidgets.add(widget);
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
        ArrayList<AbstractWidget> inflatedWidgets = GLib.inflateAll(groupParams.params,getEntryWidgetResources() , context);
        for(AbstractWidget widget: inflatedWidgets){
            layout.add((AbstractWidget) widget);
        }
        for(AbstractWidget widget: getBaseEntryWidgets()){
            widget.setFocusParent(this);
            widget.setListItemIdProvider(this);
        }
    }
    @Override
    public GroupValue getValue() {
        return (GroupValue) ParentWidget.getEntryValueTreeCustom(getBaseEntryWidgets());
    }

    public WidgetLayout getWidgetLayout(){
        return layout;
    }
    public LinearElementLayout getLinearElementLayout() {
        return getWidgetLayout().getLinearElementLayout();
    }

    @Override
    public AbstractWidget getFirstWidget() {
        AbstractWidget firstWidget = layout.widgets().get(0);
        if(firstWidget instanceof FocusTreeParent focusTreeParent){
            return focusTreeParent.getFirstWidget();
        }
        return firstWidget;
    }
    @Override
    public AbstractWidget findNextWidget(AbstractWidget abstractWidget){
        return FocusTreeParentHelper.findNextWidget(abstractWidget, getEntryWidgets(), getFocusParent(), this);
    }

    @Override
    public AbstractWidget getWidget() {
        return this;
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

    @Override
    protected Element widgetGetElement() {
        return layout.getElement();
    }
}
