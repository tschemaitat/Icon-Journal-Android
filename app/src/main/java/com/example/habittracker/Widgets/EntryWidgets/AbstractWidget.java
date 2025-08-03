package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;
import android.view.inputmethod.EditorInfo;

import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticStateManagers.InvisibleEditTextManager;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementProvider;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.FocusTreeParent;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.ListWidgets.ListWidget;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.ListItemId;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;

public abstract class AbstractWidget implements ElementProvider, FocusTreeParent.Focusable {
    public static int widgetDebugIdCounter = 0;
    public int widgetDebugId;
    private ViewWrapper viewWrapper;
    private FocusTreeParent focusParent;
    private boolean dataSet = false;
    private ListItemIdProvider listItemIdProvider;
    private EntryWidgetParam entryWidgetParam;

    private EntryWidgetResources entryWidgetResources;

    private ListWidget listParent = null;

    private boolean isGhostItem = false;

    private DataChangeListener onDataChangedListener;

    public AbstractWidget(Context context, ViewWrapper viewWrapper, EntryWidgetResources entryWidgetResources){
        this.entryWidgetResources = entryWidgetResources;
        widgetDebugId = widgetDebugIdCounter;
        widgetDebugIdCounter++;
        this.viewWrapper = viewWrapper;
        this.viewWrapper.getView().setId(R.id.entryWidgetWrapper);
    }

    public ViewWrapper getViewWrapper(){
        return viewWrapper;
    }

    protected void setWrapperChild(){
        viewWrapper.setChildView(widgetGetElement());
    }
    public void setEntryWidgetResources(EntryWidgetResources entryWidgetResources){
        this.entryWidgetResources = entryWidgetResources;

    }

    public void setOnDataChangedListener(DataChangeListener listener){
        if(listener == null)
            throw new RuntimeException();
        this.onDataChangedListener = listener;
        if(entryWidgetResources != null)
            throw new RuntimeException();
    }




    protected abstract void setParamCustom(EntryWidgetParam entryWidgetParam);
    protected abstract void setValueCustom(Object widgetValue);
    protected abstract void setHint(String string);
    protected abstract Element widgetGetElement();
    public String getNameAndLocation(){
        ArrayList<ListItemId> itemIds = listItemIdProvider.getListItemIdList();
        return getName() + itemIds;
    }
    public void setParam(EntryWidgetParam entryWidgetParam){
        this.entryWidgetParam = entryWidgetParam;
        setParamCustom(entryWidgetParam);
        //probably going to move this to each individual widget class
        //if( instanceof BaseEntryWidget baseEntryWidget){
            if(entryWidgetParam.name != null)
                setHint(entryWidgetParam.name);
        //}
    }


    protected abstract void setTextErrorColor();
    protected abstract void resetTextErrorColor();
    public abstract Object getValue();

    private Object previousValue;

    protected void onDataChanged(){
        if(onDataChangedListener != null){



            onDataChangedListener.onDataChange(previousValue, getValue());
            previousValue = getValue();
            if(entryWidgetResources != null){
                throw new RuntimeException();
            }
        }

        if(isGhostItem){
            listParent.onGhostData();
        }
        if(entryWidgetResources != null){
            entryWidgetResources.entryOnDataChange.onBaseEntryDataChange(
                    getLocation(entryWidgetResources.entryInStructure), (WidgetValue) getValue()
            );
        }

        if(entryWidgetResources == null && onDataChangedListener == null){
            throw new RuntimeException();
        }

    }

    public void setGhostItemCallBack(ListWidget parent){
        isGhostItem = true;
        this.listParent = parent;
    }

    public boolean isGhostItem(){
        return isGhostItem;
    }

    public ListWidget getListParent(){
        return listParent;
    }



    public void keyListener(int keyCode){
        MainActivity.log("entry widget key listener: " + keyCode);
        if (keyCode == EditorInfo.IME_ACTION_NEXT) {
            if(focusParent == null){

                MainActivity.log("focus parent null");
                return;
            }

            AbstractWidget nextWidget = focusParent.findNextWidget(this);
            InvisibleEditTextManager.getManager().removeFocusedWidget();
            if(nextWidget != null)
                InvisibleEditTextManager.getManager().setFocusedWidget(nextWidget);
        }
    }

    public void setListItemIdProvider(ListItemIdProvider listItemIdProvider){
        this.listItemIdProvider = listItemIdProvider;
    }

    public ListItemId getListItemId(){
        return listItemIdProvider.getListItemId();
    }

    public ListItemIdProvider getListItemIdProvider(){
        return listItemIdProvider;
    }

    public void setFocusParent(FocusTreeParent parent){
        this.focusParent = parent;
        viewWrapper.getElement().getView().setFocusable(true);
        viewWrapper.getElement().getView().setFocusableInTouchMode(true);
    }
    public FocusTreeParent getFocusParent(){
        return focusParent;
    }

    protected WidgetId getWidgetId() {
        return entryWidgetParam.getWidgetId();
    }

    public void onFocusChange(boolean hasFocus){
        MainActivity.log("entry widget on focus change: " + this);
        if(hasFocus){
            viewWrapper.showBorderView();
        }else{
            viewWrapper.hideBorderView();
        }
    }

    public RefEntryString getLocation(EntryInStructure entryInStructure) {
        if(listItemIdProvider == null){
            MainActivity.log("provider null: " + this);
            throw new RuntimeException();
        }
        RefEntryString result = new RefEntryString(getWidgetInStructure(),
                entryInStructure, listItemIdProvider.getListItemIdList());
        return result;
    }
    public WidgetInStructure getWidgetInStructure(){
        return entryWidgetParam.getWidgetInStructure();
    }
    public Structure getStructure(){
        return entryWidgetParam.getStructure();
    }
    public StructureId getStructureId(){
        return entryWidgetParam.getWidgetInStructure().getStructureId();
    }

    public EntryWidgetParam getEntryWidgetParams() {
        return entryWidgetParam;
    }

    public final String getName(){
        if(entryWidgetParam == null)
            return null;
        return entryWidgetParam.name;
    }

    public final String toString(){
        String className = "no param";
        EntryWidgetParam entryWidgetParam = getEntryWidgetParams();
        if(entryWidgetParam != null)
            className = entryWidgetParam.getClassName();
        return "<" + className + ": " + widgetDebugId + ">";
    }
    protected EntryWidgetResources getEntryWidgetResources(){
        return entryWidgetResources;
    }

    public Element getElement() {
        return viewWrapper.getElement();
    }

    public void setValue(Object valueInGroup) {
        setValueCustom(valueInGroup);
    }
}
