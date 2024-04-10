package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.StaticClasses.DropDownPageFactory;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.DropDownPages.RefDropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.HeaderNode;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.TreeId;

import java.util.ArrayList;

public class RefDropDown extends EntryWidget{
    private boolean dataSet = false;
    private StructureId structureId = null;
    private TreeId valueId = null;
    private ArrayList<TreeId> groupIdList = new ArrayList<>();

    private RefItemPath selectedValuePath = null;

    private DropDown dropDown;

    private RefDropDownPage refDropDownPage = null;
    private Context context;

    public RefDropDown(Context context) {
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
    }

    private void onDataChanged(RefItemPath refItemPath){
        selectedValuePath = refItemPath;
        onDataChangedListener().run();
    }


    @Override
    public DropDownParam getParam(){
        if(!dataSet){
            throw new RuntimeException();
        }
        DropDownParam params = new DropDownParam(getName(), selectedValuePath, structureId, valueId, groupIdList);
        return params;
    }

    @Override
    public void setValue(EntryValueTree entryValueTree) {
        setSelected(entryValueTree.getItemPath());
    }

    public void setSelected(RefItemPath itemPath){
        dropDown.setSelected(itemPath.getItemPath());
    }



    public RefItemPath getSelectedPath(){
        return selectedValuePath;
    }

    @Override
    public EntryValueTree getEntryValueTree() {
        return new EntryValueTree(selectedValuePath);
    }

    @Override
    public void setParamCustom(EntryWidgetParam params){
        dataSet = true;
        DropDownParam dropDownParams = ((DropDownParam) params);
        refDropDownPage = DropDownPageFactory.getGroupedPages(dropDownParams.structureId, dropDownParams.valueKey, dropDownParams.groups);
        structureId = dropDownParams.structureId;
        valueId = dropDownParams.valueKey;
        groupIdList = dropDownParams.groups;
    }

    public static class DropDownParam extends EntryWidgetParam {
        public RefItemPath selected;
        public StructureId structureId;
        public TreeId valueKey;
        public ArrayList<TreeId> groups;
        public String name = "null";

        public DropDownParam(String name, RefItemPath selected, StructureId structureId, TreeId valueKey, ArrayList<TreeId> groups){
            super(name, DropDown.className);
            if(structureId == null)
                throw new RuntimeException();
            this.selected = selected;
            this.structureId = structureId;
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
                    + tabs + "\tstructure: " + structureId + "\n"
                    + tabs + "\tvalue: " + valueKey + "\n"
                    + tabs + "\tgroups: " + groups + "\n";
        }

        @Override
        public HeaderNode createHeaderNode() {
            return new HeaderNode(name);
        }

        public String toString(){
            return "{" + className + ", " + selected + ", " +structureId + ", " +valueKey + ", " +groups + "}";
        }

    }
}
