package com.example.habittracker.Widgets.EntryWidgets;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.view.inputmethod.EditorInfo;

import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticStateManagers.InvisibleEditTextManager;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.FocusTreeParent;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;

public abstract class AbstractWidget {
    private ViewWrapper viewWrapper;
    private FocusTreeParent focusParent;
    private boolean dataSet = false;
    private ListItemIdProvider listItemIdProvider;
    private EntryWidgetParam entryWidgetParam;

    private Runnable onDataListener;

    public AbstractWidget(Context context, ViewWrapper viewWrapper){
        this.viewWrapper = (ViewWrapper) viewWrapper;
        this.viewWrapper.getView().setId(R.id.entryWidgetWrapper);
    }

    protected void setWrapperChild(){
        viewWrapper.setChildView(widgetGetElement());
    }
    public void setOnDataListener(Runnable onDataListener){
        this.onDataListener = onDataListener;
    }
    abstract void setParamCustom(EntryWidgetParam entryWidgetParam);
    abstract void setValueCustom(Object widgetValue);
    abstract void setHint(String string);
    abstract Element widgetGetElement();
    public void setParam(EntryWidgetParam entryWidgetParam){
        this.entryWidgetParam = entryWidgetParam;
        setParam(entryWidgetParam);
        //probably going to move this to each individual widget class
        //if( instanceof BaseEntryWidget baseEntryWidget){
            if(entryWidgetParam.name != null)
                setHint(entryWidgetParam.name);
        //}

    }
    abstract void setTextErrorColor();
    abstract void resetTextErrorColor();
    abstract Object getValue();
    protected void onDataChanged(){
        onDataListener.run();
    }

    public void keyListener(int keyCode){
        MainActivity.log("entry widget key listener: " + keyCode);
        if (keyCode == EditorInfo.IME_ACTION_NEXT) {
            if(focusParent == null){

                MainActivity.log("focus parent null");
                return;
            }

            EntryWidget nextWidget = focusParent.findNextWidget(this);
            InvisibleEditTextManager.getManager().removeFocusedWidget();
            if(nextWidget != null)
                InvisibleEditTextManager.getManager().setFocusedWidget(nextWidget);
        }
    }

    public void setListItemIdProvider(ListItemIdProvider listItemIdProvider){
        this.listItemIdProvider = listItemIdProvider;
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
}
