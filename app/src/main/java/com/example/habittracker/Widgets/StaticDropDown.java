package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.DropDownPages.DropDownPage;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.HeaderNode;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.RefItemPath;

public class StaticDropDown extends EntryWidget{
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
        setChild(dropDown.getView());

    }

    private void listenerWrapper(ItemPath itemPath, Object payload){
        if(onSelected != null)
            onSelected.onSelected(itemPath, payload);
    }

    @Override
    public EntryValueTree getEntryValueTree() {
        throw new RuntimeException();
    }

    @Override
    public EntryWidgetParam getParam() {
        throw new RuntimeException();
    }

    @Override
    public void setValue(EntryValueTree entryValueTree) {
        throw new RuntimeException();
    }



    public void setParamCustom(EntryWidgetParam params){
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
        return dropDown.getSelectedPath().getName();
    }

    public void setHint(String item_to_be_selected) {
        dropDown.setHint(item_to_be_selected);
    }

    public void resetError() {
        dropDown.resetError();
    }

    public ItemPath getSelectedPath() {
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
        dropDown.setSelected(refItemPath.getItemPath());
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
