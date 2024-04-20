package com.example.habittracker.Widgets;

import android.content.Context;

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
        this.onSelected = (itemPath, payload) -> onSelected.run();
        init();

        setup(dropDownPage, (itemPath, payload) -> onSelected.run());
    }

    private void init(){
        dropDown = new DropDown(context, (itemPath, payload) -> {
            listenerWrapper(itemPath, payload);
        });
        setViewWrapperChild(dropDown.getView());

    }

    private void listenerWrapper(ArrayList<String> itemPath, Object payload){
        if(onSelected != null)
            onSelected.onSelected(itemPath, payload);
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

    public String getSelectedString() {
        ArrayList<String> itemPath = dropDown.getSelectedPath();
        if(itemPath == null)
            return null;
        return itemPath.get(itemPath.size() - 1);
    }

    public void setHint(String item_to_be_selected) {
        dropDown.setHint(item_to_be_selected);
    }

    public void resetError() {
        dropDown.resetError();
    }

    public ArrayList<String> getSelectedPath() {
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

        dropDown.setByPayload(payload);
    }

    public void setSelected(RefItemPath refItemPath) {
        dropDown.setSelected(refItemPath.getStringList());
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
