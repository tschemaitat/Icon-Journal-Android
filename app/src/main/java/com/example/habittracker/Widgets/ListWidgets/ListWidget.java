package com.example.habittracker.Widgets.ListWidgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.example.habittracker.Layouts.InterceptLinearLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewWidgets.ListWidgetGhostManager;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Widgets.FocusTreeParent;
import com.example.habittracker.Widgets.FocusTreeParentHelper;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class ListWidget extends BaseEntryWidget implements FocusTreeParent {


    public static final String className = "list";
    private Context context;
    protected WidgetLayout layout;
    protected Widget ghostItem;
    protected EntryWidgetParam cloneParam;

    public ListWidget(Context context){
        super(context);
        this.context = context;
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout.getView());


        //makeButton(()->addItem());

    }

    protected ArrayList<EntryWidget> getWidgetListWithoutGhost(){
        ArrayList<Widget> widgetList = layout.widgets();
        widgetList = (ArrayList<Widget>) widgetList.clone();
        widgetList.remove(widgetList.size() - 1);
        return EnumLoop.makeList(widgetList, widget->(EntryWidget) widget);
    }

    protected ArrayList<EntryWidget> getEntryWidgetList(){
        ArrayList<Widget> widgetList = layout.widgets();
        widgetList = (ArrayList<Widget>) widgetList.clone();
        return EnumLoop.makeList(widgetList, widget->(EntryWidget) widget);
    }

    protected void setValueListCustom(WidgetValue widgetValue){
        throw new RuntimeException();
    }

    protected void onItemCreated(Widget widget){
        throw new RuntimeException();
    }

    protected void addGhostItem(Widget widget){
        MainActivity.log("adding ghost");
        widget.getView().setForeground(new ColorDrawable(ColorPalette.listItemBeforeAddForeground));
        ghostItem = widget;
        widget.setOnDataChangedListener(()->onGhostData());
        layout.add(widget);
    }

    private void onGhostData() {
        MainActivity.log("on ghost data");
        ghostItem.getView().setForeground(null);
        ghostItem.setOnDataChangedListener(()->onDataChangedListener().run());
        setViewDraggable(ghostItem);
        ghostItem = null;
        addGhostItem(createItem());
    }

    protected final EntryWidget createItem(){
        MainActivity.log("list widget: creating item");
        EntryWidget entryWidget = (EntryWidget) GLib.inflateWidget(context, cloneParam, ()->onDataChangedListener().run());
        entryWidget.setFocusParent(this);
        onItemCreated(entryWidget);
        return entryWidget;
    }

    public ArrayList<GroupWidget> getGroupWidgets(){
        ArrayList<GroupWidget> groupWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            groupWidgets.add((GroupWidget) widget);
        }
        groupWidgets.remove(groupWidgets.size() - 1);
        return groupWidgets;
    }

    @Override
    public final void setValueCustom(WidgetValue widgetValue) {
        layout.remove(ghostItem);
        ghostItem = null;
        MainActivity.log("set value list");
        setValueListCustom(widgetValue);
        addGhostItem(createItem());
    }

    @Override
    protected void setHint(String hintString) {

    }

    protected void setViewDraggable(Widget widget){
        ((InterceptLinearLayout) widget.getView()).enableIntercept(()->startDrag(widget));
    }

    protected void startDrag(Widget widget) {
        ListWidgetGhostManager listWidgetGhostManager = new ListWidgetGhostManager(this,
                widget, context, layout);
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
        return FocusTreeParentHelper.findNextWidget(entryWidget, getEntryWidgetList(), getFocusParent(), this);
    }

    protected void setWidgetParam(EntryWidgetParam entryWidgetParam){
        this.cloneParam = entryWidgetParam;
        if(ghostItem != null)
            throw new RuntimeException();
        addGhostItem(createItem());
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        throw new RuntimeException();
    }

    @Override
    public void setParamCustom(EntryWidgetParam param){
        throw new RuntimeException();
    }

    public void makeButton(Runnable runnable){
        layout.getLinLayout().addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnable.run();
            }
        });
    }


    public void enableDeleteValueMode() {
        for(EntryWidget entryWidget: getEntryWidgetList()){
            if(entryWidget instanceof GroupWidget groupWidget){
                groupWidget.enableDeleteValueMode();
            }else{
                entryWidget.enableDelete();
            }
        }
    }

    @Override
    public ArrayList<BaseEntryWidget> getWidgetsForDelete() {
        return getWidgetsForDeleteIteration();
    }

    public ArrayList<BaseEntryWidget> getWidgetsForDeleteIteration(){
        throw new RuntimeException();
    }

//    public ArrayList<EntryWidget> gatherWidgetsChecked() {
//        ArrayList<EntryWidget> resultList = new ArrayList<>();
//        gatherWidgetsCheckedIteration(resultList);
//        return resultList;
//    }

    public void gatherWidgetsCheckedIteration(ArrayList<EntryWidget> resultList) {
        throw new RuntimeException();
    }
}
