package com.example.habittracker.Widgets.ListWidgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.EntryWidgets.AbstractWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Widgets.FocusTreeParent;
import com.example.habittracker.Widgets.FocusTreeParentHelper;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.ListItemId;

public abstract class ListWidget extends AbstractWidget implements FocusTreeParent {


    public static final String className = "list";
    private Context context;
    protected WidgetLayout layout;
    protected AbstractWidget ghostItem;
    protected EntryWidgetParam cloneParam;


    public ListWidget(Context context, ViewWrapper wrapperElement, EntryWidgetResources entryWidgetResources){
        super(context, wrapperElement, entryWidgetResources);
        this.context = context;
        layout = new WidgetLayout(context);
        setWrapperChild();



        //makeButton(()->addItem());

    }
    protected ArrayList<AbstractWidget> getWidgetListWithoutGhost(){
        if(ghostItem == null)
            throw new RuntimeException();
        ArrayList<Widget> widgetList = layout.widgets();
        widgetList = (ArrayList<Widget>) widgetList.clone();
        widgetList.remove(widgetList.size() - 1);
        return EnumLoop.makeList(widgetList, widget->(AbstractWidget) widget);
    }
    protected ArrayList<AbstractWidget> getEntryWidgetListWithGhost(){
        ArrayList<AbstractWidget> widgetList = layout.widgets();
        widgetList = (ArrayList<AbstractWidget>) widgetList.clone();
        return EnumLoop.makeList(widgetList, widget->(AbstractWidget) widget);
    }




    protected void setValueListCustom(Object widgetValue){
        throw new RuntimeException();
    }
    protected void onItemCreated(AbstractWidget widget){
        throw new RuntimeException();
    }
    protected void addGhostItem(AbstractWidget widget){
        debugMessages.add("added ghost item");
        debugMessages.add("ghost item id: " + widget.getElement().getView().getId());
        MainActivity.log("adding ghost");
        widget.getElement().setForeground(new ColorDrawable(ColorPalette.listItemBeforeAddForeground));
        if(ghostItem != null)
            throw new RuntimeException();
        ghostItem = widget;
        widget.setEntryWidgetResources(getEntryWidgetResources());
        debugMessages.add("ghostitem object id: "+System.identityHashCode(ghostItem));
        debugMessages.add("ghost item element object id: "+System.identityHashCode(ghostItem.getElement()));

        layout.add(widget);
    }
    public void onGhostData() {
        if(ghostItem == null)
            throw new RuntimeException();
        if(isGhostItem()){
            getListParent().onGhostData();
        }

        MainActivity.log("on ghost data");
        ghostItem.getElement().setForeground(null);
        //ghostItem.setOnDataChangedListener(()->onDataChangedListener().run());
//        ghostItem.setOnDataListener(()->{
//            if(ghostItem instanceof GroupWidget)
//            entryWidgetResources.entryOnDataChange.onBaseEntryDataChange(
//                    ghostItem.getLocation(entryWidgetResources.entryInStructure),
//                    ghostItem.getValue());
//        });
        //TODO: need to have the ghost create the initial data and edit it in
        getEntryWidgetResources().entryOnDataChange.onListItemCreated(
                getLocation(getEntryWidgetResources().entryInStructure),
                ListWidgetSingleItem.createGroupValueFromWidgetValue(ghostItem),
                new EntryWidgetResources.ListIdCallBack() {
                    @Override
                    public void giveListId(ListItemId listItemId) {
                        //since this is a groupWidget, use set list id directly instead of provider
                        //group widget uses provider if its in a parent list
                        //regular widget uses provider from
                        if(ghostItem instanceof GroupWidget groupWidget){
                            groupWidget.setListItemId(listItemId);
                            groupWidget.setListItemIdProvider(getListItemIdProvider());
                        }else{
                            ghostItem.setListItemIdProvider(
                                    new SingleItemIdProvider(listItemId, getListItemIdProvider()));
                        }

                    }
                });
        //setViewDraggable(ghostItem);
        ghostItem = null;
        addGhostItem(createItem());
    }
    protected final AbstractWidget createItem(){
        MainActivity.log("list widget: creating item");
        EntryWidgetResources initialResources = new EntryWidgetResources(getEntryWidgetResources().keyBoardActionManager, getEntryWidgetResources().invisibleEditTextManager,
                new EntryWidgetResources.EntryOnDataChange() {
                    @Override
                    public void onBaseEntryDataChange(RefEntryString refEntryString, WidgetValue widgetValue) {
                        onGhostData();
                    }

                    @Override
                    public void onListItemCreated(RefEntryString refEntryString, GroupValue groupValue, EntryWidgetResources.ListIdCallBack listIdCallBack) {
                        onGhostData();
                    }
                }, getEntryWidgetResources().entryInStructure);
        AbstractWidget entryWidget = GLib.inflateWidget(context, cloneParam, initialResources);
        entryWidget.setFocusParent(this);
        onItemCreated(entryWidget);
        return entryWidget;
    }
    public ArrayList<GroupWidget> getGroupWidgets(){
        ArrayList<GroupWidget> groupWidgets = new ArrayList<>();
        for(Object widget: layout.widgets()){
            groupWidgets.add((GroupWidget) widget);
        }
        groupWidgets.remove(groupWidgets.size() - 1);
        return groupWidgets;
    }
    @Override
    public final void setValueCustom(Object widgetValue) {

        if(ghostItem == null)
            throw new RuntimeException();
        debugMessages.add("ghost item id: " + ghostItem.getElement().getView().getId());
        debugMessages.add("ghost item object id: "+System.identityHashCode(ghostItem));
        debugMessages.add("ghost item element object id: "+System.identityHashCode(ghostItem.getElement()));
        debugMessages.printDebugMessages();
        layout.remove(ghostItem);
        ghostItem = null;
        MainActivity.log("set value list");
        setValueListCustom(widgetValue);
        addGhostItem(createItem());
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
    public void setHint(String hintString) {
        throw new RuntimeException();
    }

    @Override
    protected Element widgetGetElement() {
        return layout.getElement();
    }

    @Override
    public AbstractWidget getFirstWidget() {
        AbstractWidget firstWidget = (AbstractWidget) layout.widgets().get(0);
        if(firstWidget instanceof FocusTreeParent focusTreeParent){
            return focusTreeParent.getFirstWidget();
        }
        return firstWidget;
    }



    @Override
    public AbstractWidget findNextWidget(AbstractWidget abstractWidget){
        return FocusTreeParentHelper.findNextWidget(abstractWidget, getEntryWidgetListWithGhost().convert(
                (index, entryWidget) -> entryWidget), getFocusParent(), this);
    }

    @Override
    public AbstractWidget getWidget() {
        return this;
    }

    protected void setWidgetParam(EntryWidgetParam entryWidgetParam){
        if(getEntryWidgetResources() == null){
            throw new RuntimeException("widget resources null");
        }
        this.cloneParam = entryWidgetParam;
        if(ghostItem != null)
            throw new RuntimeException();
        addGhostItem(createItem());
    }



    public GroupValue getGroupValueSingleItem(ListItemId listItemId) {
        throw new RuntimeException();
    }


    @Override
    public void setParamCustom(EntryWidgetParam param){
        throw new RuntimeException();
    }
}
