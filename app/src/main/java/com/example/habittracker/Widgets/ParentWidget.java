package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.ListItemId;

public class ParentWidget extends EntryWidget implements FocusTreeParent, ListItemIdProvider {
    private WidgetLayout layout;
    public static final String className = "parent widget";
    private ListItemId listItemId;
    private ListItemIdProvider listItemIdParent;
    private Context context;
    public ParentWidget(Context context, Element wrapperElement, EntryWidgetResources entryWidgetResources){
        super(context, wrapperElement, entryWidgetResources);
        this.context = context;
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout);
        getView().setId(R.id.parentWidget);
    }
    public ArrayList<BaseEntryWidget> getBaseEntryWidgets(){
        ArrayList<BaseEntryWidget> entryWidgets = new ArrayList<>();
        for(Object widget: layout.widgets()){
            entryWidgets.add((BaseEntryWidget) widget);
        }
        return entryWidgets;
    }
    public ArrayList<EntryWidget> getEntryWidgets(){
        ArrayList<EntryWidget> entryWidgets = new ArrayList<>();
        for(Object widget: layout.widgets()){
            entryWidgets.add((EntryWidget) widget);
        }
        return entryWidgets;
    }
    public WidgetLayout getWidgetLayout(){
        return layout;
    }
    @Override
    protected Object getEntryValueTreeCustom() {
        return getEntryValueTreeCustom(getBaseEntryWidgets());
    }
    public static Object getEntryValueTreeCustom(ArrayList<BaseEntryWidget> baseEntryWidgets){
        ArrayList<WidgetValue> result = new ArrayList<>();

        for(BaseEntryWidget widget: baseEntryWidgets){
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
    public static void setValueCustom(ArrayList<BaseEntryWidget> entryWidgets, GroupValue groupValue){
        MainActivity.log("group setting values: \n" + groupValue.hierarchy());
        for(int i = 0; i < entryWidgets.size(); i++){


            BaseEntryWidget entryWidget = entryWidgets.get(i);
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
        ArrayList<Widget> inflatedWidgets = GLib.inflateAll(groupParams.params, entryWidgetResources, context);
        for(Widget widget: inflatedWidgets){
            layout.add(widget);
        }

//        layout.inflateAll(groupParams.params, ()->onDataChangedListener().run());
        for(BaseEntryWidget widget: getBaseEntryWidgets()){
            widget.setFocusParent(this);
            widget.setListItemIdProvider(this);
        }
    }
    @Override
    public String getNameAndLocation() {
        return "parent widget";
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
