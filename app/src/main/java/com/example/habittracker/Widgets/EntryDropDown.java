package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.DropDownPageFactory;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.DropDownPages.DropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.Structs.WidgetId;

import java.util.ArrayList;

public class EntryDropDown extends EntryWidget{
    private boolean dataSet = false;
    private Structure structure = null;
    private WidgetId valueId = null;
    private ArrayList<WidgetId> groupIdList = new ArrayList<>();

    private RefItemPath selectedValuePath = null;

    private DropDown dropDown;

    private DropDownPage dropDownPage = null;
    private Context context;

    public EntryDropDown(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){

        dropDown = new DropDown(context, new DropDown.DropDownOnSelected() {
            @Override
            public void onSelected(ItemPath itemPath, Object payload) {
                onDataChanged((RefItemPath)payload);
            }
        });
        setChild(dropDown.getView());
    }

    private void onDataChanged(RefItemPath refItemPath){
        MainActivity.log("on data changed: " + refItemPath);
        selectedValuePath = refItemPath;
        onDataChangedListener().run();
    }


    @Override
    public DropDownParam getParam(){
        if(!dataSet){
            throw new RuntimeException();
        }
        DropDownParam params = new DropDownParam(getName(), selectedValuePath, structure, valueId, groupIdList);
        return params;
    }

    @Override
    public void setValueCustom(EntryValueTree entryValueTree) {

        if(entryValueTree.getItemPath() == null){
            MainActivity.log(entryValueTree.getParent().hierarchy());
            throw new RuntimeException();
        }

        setSelected(entryValueTree.getItemPath());
    }

    public void setSelected(RefItemPath itemPath){
        dropDown.setSelected(itemPath.getItemPath());
    }



    public RefItemPath getSelectedPath(){
        return selectedValuePath;
    }

    @Override
    public EntryValueTree getEntryValueTreeCustom() {
        MainActivity.log("saving value path: " + selectedValuePath);
        return new EntryValueTree(selectedValuePath);
    }

    @Override
    public void setParamCustom(EntryWidgetParam params){
        dataSet = true;
        DropDownParam dropDownParams = ((DropDownParam) params);
        dropDownPage = DropDownPageFactory.getGroupedPages(dropDownParams.structure, dropDownParams.valueKey, dropDownParams.groups);
        structure = dropDownParams.structure;
        valueId = dropDownParams.valueKey;
        groupIdList = dropDownParams.groups;
        dropDown.setDropDownPage(dropDownPage);
    }

    public static class DropDownParam extends EntryWidgetParam {
        public RefItemPath selected;
        public Structure structure;
        public WidgetId valueKey;
        public ArrayList<WidgetId> groups;
        public String name = "null";

        public DropDownParam(String name, RefItemPath selected, Structure structure, WidgetId valueKey, ArrayList<WidgetId> groups){
            super(name, DropDown.className);
            if(structure == null)
                throw new RuntimeException();
            this.selected = selected;
            this.structure = structure;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        @Override
        public String hierarchyString(int numTabs){
            String singleTab = "\t";
            String tabs = "";
            for(int i = 0; i < numTabs; i++)
                tabs += singleTab;
            return tabs + "drop down\n"
                    + tabs + "\tstructure: " + structure + "\n"
                    + tabs + "\tvalue: " + valueKey + "\n"
                    + tabs + "\tgroups: " + groups + "\n";
        }

        @Override
        public HeaderNode createHeaderNode() {
            return new HeaderNode(name);
        }

        public String toString(){
            return "{" + className + ", " + selected + ", " +structure + ", " +valueKey + ", " +groups + "}";
        }

    }
}
