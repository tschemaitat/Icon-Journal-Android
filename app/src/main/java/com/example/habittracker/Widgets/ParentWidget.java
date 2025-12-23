package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.EntryWidgets.AbstractWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.ListItemId;

public class ParentWidget extends AbstractWidget implements FocusTreeParent, ListItemIdProvider {
    private WidgetLayout<AbstractWidget> layout;
    public static final String className = "parent widget";
    private ListItemId listItemId;
    private ListItemIdProvider listItemIdParent;
    private Context context;

    public ParentWidget(Context context, ViewWrapper viewWrapper, EntryWidgetResources entryWidgetResources){
        super(context, viewWrapper, entryWidgetResources);
        this.context = context;
        layout = new WidgetLayout(context);
        setWrapperChild();
        getElement().getView().setId(R.id.parentWidget);
    }
    public ArrayList<AbstractWidget> getBaseEntryWidgets(){
        ArrayList<AbstractWidget> entryWidgets = new ArrayList<>();
        for(Object widget: layout.widgets()){
            entryWidgets.add((AbstractWidget) widget);
        }
        return entryWidgets;
    }
    public ArrayList<AbstractWidget> getEntryWidgets(){
        return layout.widgets().copy();
    }
    public WidgetLayout getWidgetLayout(){
        return layout;
    }
    @Override
    public Object getValue() {
        return getEntryValueTreeCustom(getBaseEntryWidgets());
    }
    public static Object getEntryValueTreeCustom(ArrayList<AbstractWidget> baseEntryWidgets){
        ArrayList<WidgetValue> result = new ArrayList<>();

        for(AbstractWidget widget: baseEntryWidgets){
            WidgetValue widgetValue = (WidgetValue) widget.getValue();
            if(widgetValue != null)
                result.add(widgetValue);
        }

        return new GroupValue(result);
    }
    @Override
    protected void setValueCustom(Object widgetValue) {
        setValueCustom(getBaseEntryWidgets(), (GroupValue) widgetValue);
    }
    public static void setValueCustom(ArrayList<AbstractWidget> entryWidgets, GroupValue groupValue){
        MainActivity.log("group setting values: \n" + groupValue.hierarchy());
        for(int i = 0; i < entryWidgets.size(); i++){


            AbstractWidget entryWidget = entryWidgets.get(i);
            MainActivity.log("widget: " + entryWidget.getName() + ", id: " + entryWidget.getWidgetInStructure());
            WidgetValue childWidgetValue = groupValue.getWidgetValueByWidget(entryWidget.getWidgetInStructure());
            if(childWidgetValue != null)
                entryWidget.setValue(childWidgetValue);

            MainActivity.log("group set list id provider: " + entryWidget);
        }
    }
    @Override
    protected void setParamCustom(EntryWidgetParam param) {
        GroupWidgetParam groupParams = (GroupWidgetParam) param;
        if(getEntryWidgetResources() == null){
            throw new RuntimeException();
        }
        ArrayList<AbstractWidget> inflatedWidgets = GLib.inflateAll(groupParams.params, getEntryWidgetResources(), context);
        for(AbstractWidget widget: inflatedWidgets){
            layout.add(widget);
        }

//        layout.inflateAll(groupParams.params, ()->onDataChangedListener().run());
        for(AbstractWidget widget: getBaseEntryWidgets()){
            widget.setFocusParent(this);
            widget.setListItemIdProvider(this);
        }
    }

    @Override
    public AbstractWidget getFirstWidget() {
        AbstractWidget firstWidget = (AbstractWidget)layout.widgets().get(0);
        if(firstWidget instanceof FocusTreeParent focusTreeParent){
            return focusTreeParent.getFirstWidget();
        }
        return firstWidget;
    }
    @Override
    public AbstractWidget findNextWidget(AbstractWidget entryWidget){
        return FocusTreeParentHelper.findNextWidget(entryWidget, getEntryWidgets(), getFocusParent(), this);
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
