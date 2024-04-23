package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.Structs.RefItemPath;

import java.util.ArrayList;

public class StaticDropDown extends EntryWidget {
    boolean dataSet = false;
    private Context context;
    private DropDown dropDown;
    private DropDown.DropDownOnSelected onSelected = null;
    public StaticDropDown(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public StaticDropDown(Context context, DropDownPage dropDownPage, DropDown.DropDownOnSelected onSelected){
        super(context);
        this.context = context;
        init();
        this.onSelected = onSelected;
        setup(dropDownPage, onSelected);
    }

    public StaticDropDown(Context context, DropDownPage dropDownPage, Runnable onSelected){
        super(context);
        this.context = context;
        this.onSelected = (itemPath, payload, prevItemPath, prevPayload) -> onSelected.run();
        init();

        setup(dropDownPage, (itemPath, payload, prevItemPath, prevPayload) -> onSelected.run());
    }

    private void init(){
        dropDown = new DropDown(context, (itemPath, payload, prevItemPath, prevPayload) -> {
            listenerWrapper(itemPath, payload, prevItemPath, prevPayload);
        });
        setViewWrapperChild(dropDown.getView());

    }

    private void listenerWrapper(RefItemPath refItemPath, Object payload, RefItemPath prevItemPath, Object prevPayload){
        if(onSelected != null)
            onSelected.onSelected(refItemPath, payload, prevItemPath, prevPayload);
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        throw new RuntimeException();
    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {
        throw new RuntimeException();
    }



    public void setParamCustom(EntryWidgetParam param){
        throw new RuntimeException();
    }

    public void setup(DropDownPage page, DropDown.DropDownOnSelected onSelected){
        this.onSelected = onSelected;
        dropDown.setDropDownPage(page);
    }



    public DropDown getDropDown(){
        return dropDown;
    }

    public CachedString getSelectedString() {
        RefItemPath itemPath = dropDown.getSelectedPath();
        if(itemPath == null)
            return null;
        return itemPath.getLast();
    }

    public void setHint(String item_to_be_selected) {
        dropDown.setHint(item_to_be_selected);
    }

    public void resetError() {
        dropDown.resetError();
    }

    public RefItemPath getSelectedPath() {
        return dropDown.getSelectedPath();
    }

    public void resetValue() {
        dropDown.resetValue();
    }

    public void setPage(DropDownPage reducedPage) {
        dropDown.setDropDownPage(reducedPage);
    }

    public void setError() {
        dropDown.setError();
    }

    public void setSelectedByPayload(Object payload) {
        if(payload == null)
            throw new RuntimeException();
        dropDown.setByPayload(payload);
    }

    public Object getPayload(){
        return dropDown.getPayload();
    }

    public void setSelected(RefItemPath refItemPath) {
        dropDown.setSelected(refItemPath);
    }

    public static class StaticDropDownParameters extends EntryWidgetParam {
        DropDownPage page;
        public StaticDropDownParameters(String name, DropDownPage page){
            super(name, DropDown.className);
            this.page = page;
        }


        @Override
        public String hierarchyString(int numTabs) {
            return null;
        }

        @Override
        public HeaderNode createHeaderNode() {
            throw new RuntimeException();
        }


    }
}
