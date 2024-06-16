package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.R;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.ListWidgets.ListWidget;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.ListItemId;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class GroupWidget extends EntryWidget implements FocusTreeParent, ListItemIdProvider {
    private WidgetLayout layout;
    private ListItemId listItemId;
    private ListItemIdProvider listItemIdParent;

    public static final String className = "group widget";
    public GroupWidget(Context context){
        super(context);
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout.getView());
        getView().setId(R.id.groupWidget);
    }

    @Override
    public ArrayList<BaseEntryWidget> getWidgetsForDelete() {
        if(!isDeleteChecked)
            throw new RuntimeException();
        return getWidgetsForDeleteIteration();
    }


    public ArrayList<BaseEntryWidget> getWidgetsForDeleteIteration() {
        return GroupWidget.gatherRefForDeleteWidgetsAndList(getBaseEntryWidgets());
    }

    public ArrayList<EntryWidget> gatherWidgetsChecked(){
        ArrayList<EntryWidget> resultList = new ArrayList<>();
        gatherWidgetsCheckedIteration(resultList);
        return resultList;
    }

    public void gatherWidgetsCheckedIteration(ArrayList<EntryWidget> resultList){
        if(isDeleteChecked)
            throw new RuntimeException();
        ArrayList<BaseEntryWidget> baseEntryWidgets = getBaseEntryWidgets();
        for(BaseEntryWidget baseEntryWidget: baseEntryWidgets){
            if(baseEntryWidget.isDeleteChecked){
                resultList.add(baseEntryWidget);
                continue;
            }
            if(baseEntryWidget instanceof ListWidget listWidget){
                listWidget.gatherWidgetsCheckedIteration(resultList);
            }
        }
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<WidgetValue> result = new ArrayList<>();

        for(BaseEntryWidget widget: getBaseEntryWidgets()){
            WidgetValue widgetValue = widget.getValue();
            if(widgetValue != null)
                result.add(widgetValue);
        }

        return new GroupValue(result);
    }

    public ArrayList<BaseEntryWidget> getBaseEntryWidgets(){
        ArrayList<BaseEntryWidget> entryWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            entryWidgets.add((BaseEntryWidget) widget);
        }
        return entryWidgets;
    }

    public ArrayList<EntryWidget> getEntryWidgets(){
        ArrayList<EntryWidget> entryWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            entryWidgets.add((EntryWidget) widget);
        }
        return entryWidgets;
    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {
        GroupValue groupValue = (GroupValue) widgetValue;
        MainActivity.log("group setting values: \n" + groupValue.hierarchy());

        ArrayList<BaseEntryWidget> entryWidgets = getBaseEntryWidgets();
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
    protected void setHint(String hintString) {

    }

    @Override
    public void setParamCustom(EntryWidgetParam param) {
        GroupWidgetParam groupParams = (GroupWidgetParam) param;
        layout.inflateAll(groupParams.params, ()->onDataChangedListener().run());
        for(BaseEntryWidget widget: getBaseEntryWidgets()){
            widget.setFocusParent(this);
            widget.setListItemIdProvider(this);
        }
    }

    public WidgetLayout getWidgetLayout(){
        return layout;
    }

    public LinLayout getLinLayout() {
        return getWidgetLayout().getLinLayout();
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

    public void enableDeleteValueMode() {
        for(EntryWidget entryWidget: getBaseEntryWidgets()){
            if(entryWidget instanceof ListWidget listWidget){
                listWidget.enableDeleteValueMode();
            }else{
                entryWidget.enableDelete();
            }
        }
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

    public static ArrayList<BaseEntryWidget> gatherRefForDeleteWidgetsAndList(ArrayList<BaseEntryWidget> baseEntryWidgets){
        ArrayList<BaseEntryWidget> resultList = new ArrayList<>();
        for(BaseEntryWidget baseEntryWidget: baseEntryWidgets){
            if(baseEntryWidget instanceof ListWidget listWidget){
                resultList.addAll(listWidget.getWidgetsForDeleteIteration());
                continue;
            }
            resultList.add(baseEntryWidget);
        }
        return resultList;
    }

    public String getNameAndLocation(){
        ArrayList<ListItemId> itemIds = getListItemIdList();
        return getName() + itemIds;
    }
}
