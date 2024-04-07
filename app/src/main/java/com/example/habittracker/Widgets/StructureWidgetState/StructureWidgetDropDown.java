package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.DataTree;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.StaticDropDownPage;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.DropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureWidgetDropDown implements Widget {
    private Context context;
    private LinLayout customLinearLayout;
    private LinLayout parent;

    //used for drop down
    DropDown structureKeyDropDown = null;
    DropDown valueKeyDropDown = null;
    GroupWidget groupLayout = null;


    //used for state management when structure key changes and views need to be reset
    String currentStructureKey = null;

    public StructureWidgetDropDown(Context context, LinLayout parent) {
        this.context = context;
        this.parent = parent;
        customLinearLayout = new LinLayout(context);
        parent.add(customLinearLayout.getView());
        init();
    }

    private void init(){
        createStructureKeyDropDown();
    }

    private void createStructureKeyDropDown(){
        DropDown.StaticDropDownParameters structureKeyParams = new DropDown.StaticDropDownParameters(null, new StaticDropDownPage(null, Dictionary.getStructureNames()));
        structureKeyDropDown = (DropDown) GLib.inflateWidget(context, structureKeyParams, () -> onStructureKeyChange());
        structureKeyDropDown.setHint("select spreadsheet");
        customLinearLayout.add(structureKeyDropDown.getView());
    }



    private void onStructureKeyChange(){
        structureKeyDropDown.resetError();
        System.out.println("structure key change");
        String structureKey = structureKeyDropDown.getSelectedString();
        if(structureKey == null){
            if(valueKeyDropDown != null)
                resetValueKeyWidget();
            return;
        }
        if( ! structureKey.equals(currentStructureKey)){
            structureKeyDropDown.getViewWrapper().resetNameColor();
            resetValueKeyWidget();
        }
        currentStructureKey = structureKey;

        if(valueKeyDropDown == null)
            createValueKeyDropDown();

    }

    private void createValueKeyDropDown(){
        //System.out.println("creating value key drop down");
        String structureKey = structureKeyDropDown.getSelectedString();
        ArrayList<String> headerList = valueKeyList(structureKey);
        StaticDropDownPage valuePage = new StaticDropDownPage("value keys", headerList);
        //System.out.println("value key drop down is null");
        DropDown.StaticDropDownParameters params = new DropDown.StaticDropDownParameters(null, valuePage);
        valueKeyDropDown = (DropDown) GLib.inflateWidget(context, params, ()->onValueKeyChange());
        valueKeyDropDown.setHint("item to be selected");
        customLinearLayout.add(valueKeyDropDown.getView());
    }
    private void resetValueKeyWidget(){
        System.out.println("resetting value key widget");
        if(valueKeyDropDown == null)
            return;
        customLinearLayout.remove(valueKeyDropDown.getView());
        valueKeyDropDown = null;
        resetGroupKeyWidget();
    }
    private void onValueKeyChange(){
        valueKeyDropDown.resetError();
        System.out.println("<drop down structure widget> value key change");
        String valueKey = valueKeyDropDown.getSelectedString();
        //System.out.println("valueKey = " + valueKey);
        if(valueKey == null){
            if(groupLayout != null){
                valueKeyDropDown.getViewWrapper().resetNameColor();
                resetGroupKeyWidget();
            }
            return;
        }
        if(maxNumGroups() == 0){
            System.out.println("can't make any groupby");
            return;
        }
        if(groupLayout == null)
            createGroupLayout();
    }







    private void tryAddButton(){
        ArrayList<DropDown> groupDropDowns = getGroupDropDownList();
        DropDown lastGroup = groupDropDowns.get(groupDropDowns.size() - 1);
        boolean lastNull = lastGroup.getSelectedString() == null;
        //System.out.println("groupLayout.getLinLayout().hasButton() = " + groupLayout.getLinLayout().hasButton());
        //System.out.println("maxNumGroups() = " + maxNumGroups());
        if(!groupLayout.getLinLayout().hasButton() && groupDropDowns.size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    private void createGroupLayout(){
        System.out.println("<drop down structure widget> creating group layout");
        groupLayout = new GroupWidget(context);
        //groupLayout.getViewWrapper().setName("group by");
        Margin.setStructureWidgetGroupLayout(groupLayout.getLinLayout());

        customLinearLayout.add(groupLayout.getView());
        //System.out.println("adding group add button");
        addGroupKeyDropDownAdd();
    }
    private void addGroupBy(){
        ArrayList<DropDown> groupDropDowns = getGroupDropDownList();
        ArrayList<ItemPath> groupValues = getGroupValues();
        validateGroupValues(groupValues);
        //System.out.println("adding group by: " + groupValues);
        while(groupValues.remove(null)){

        }


        //System.out.println("adding groupBy");
        StaticDropDownPage page = StaticDropDownPage.fromItems(getReducedGroupKeyList(groupValues));
        //System.out.println("group by page: \n" + page);
        DropDown dropDown = (DropDown)GLib.inflateWidget(context, new DropDown.StaticDropDownParameters(null, page), () -> processGroupItemSelected());
        dropDown.setHint("select group");
        groupDropDowns.add(dropDown);
        groupLayout.getWidgetLayout().add(dropDown);
        //System.out.println("groupKeyDropDowns.widgetsInLayout.size() = " + groupLayout.getWidgetLayout().widgets().size());
        //System.out.println("maxNumGroups() = " + maxNumGroups());

        if(groupDropDowns.size() >= maxNumGroups())
            groupLayout.getLinLayout().removeButton();
    }
    private void validateGroupValues(ArrayList<ItemPath> groupValues){
        //this is called by addGroupBy and checks for if there is a value after a null value just for debugging
        boolean nullFound = false;
        for(ItemPath itemPath: groupValues){
            if(itemPath != null){
                if(nullFound)
                    throw new RuntimeException("group values are messed up: " + groupValues);
                continue;
            }
            nullFound = true;
        }
    }
    private void processGroupItemSelected(){
        System.out.println("group value changed");
        ArrayList<DropDown> groupDropDowns = getGroupDropDownList();
        ArrayList<ItemPath> selectedGroupKeys = new ArrayList<>();
        int removeIndex = groupDropDowns.size();
        for(int i = 0; i < groupDropDowns.size(); i++){
            DropDown dropDown = groupDropDowns.get(i);
            dropDown.resetError();
            ItemPath item = dropDown.getSelectedPath();
            if(item == null){
                removeIndex = i;
                break;
            }
            selectedGroupKeys.add(item);
        }


        for(int i = removeIndex; i < groupDropDowns.size(); i++){
            DropDown dropDown = groupDropDowns.get(i);
            dropDown.resetValue();
        }
        StaticDropDownPage reducedPage = StaticDropDownPage.fromItems(getReducedGroupKeyList(selectedGroupKeys));
        //System.out.println("setting reduced page to remaining drop downs: " + removeIndex);
        //System.out.println("reducedPage = " + reducedPage);
        for(int i = removeIndex; i < groupDropDowns.size(); i++) {
            DropDown dropDown = groupDropDowns.get(i);
            dropDown.setParam(new DropDown.StaticDropDownParameters(null, reducedPage));
        }
    }
    private void addGroupKeyDropDownAdd(){
        groupLayout.getLinLayout().addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGroupClick();
            }
        });
    }



    private void resetGroupKeyWidget(){
        System.out.println("resetting gorup key widget");
        customLinearLayout.remove(groupLayout.getView());
        groupLayout = null;
    }



    private void processGroupClick(){
        groupLayout.getViewWrapper().resetNameColor();
        //System.out.println("group button clicked");
        //System.out.println("num groups: " + groupKeyDropDowns.widgets().size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    private ArrayList<ItemPath> getGroupValues(){
        ArrayList<DropDown> dropDowns = getGroupDropDownList();
        ArrayList<ItemPath> items = new ArrayList<>();
        for(DropDown dropDown: dropDowns){
            ItemPath item = dropDown.getSelectedPath();
            items.add(item);
        }
        return items;
    }

    private ArrayList<DropDown> getGroupDropDownList() {
        ArrayList<DropDown> dropDowns = new ArrayList<>();
        if(groupLayout == null)
            return dropDowns;
        for(Widget widget: groupLayout.getWidgetLayout().widgets())
            dropDowns.add(((DropDown) widget));
        return dropDowns;
    }

    //region type dropdown gather functions
    private ArrayList<ItemPath> getGroupKeyList(){
        //System.out.println("getting group key list");
        ArrayList<ItemPath> items = DataTree.gatherItems(Dictionary.header(structureKey()));
        //System.out.println("DataTree.gatherItems(Dictionary.header(structureKey())) = " + DataTree.gatherItems(Dictionary.header(structureKey())));
        items.remove(valueKeyDropDown.getSelectedPath());
        return items;
    }
    private ArrayList<ItemPath> getReducedGroupKeyList(ArrayList<ItemPath> selectedGroupKeys){
        //System.out.println("getting reduced group key list");
        //System.out.println("group keys selected: " + selectedGroupKeys);
        ArrayList<ItemPath> items = getGroupKeyList();
        //System.out.println("getGroupKeyList() = " + items);
        for(ItemPath item: selectedGroupKeys){
            items.remove(item);
        }
        //System.out.println("result: " + items);
        return items;
    }
    private ArrayList<String> valueKeyList(String structureKey){
        DataTree header = Dictionary.header(structureKey);
        if(header == null)
            throw new RuntimeException("header null key: " + structureKey);
        ArrayList<String> headerList = new ArrayList<>();
        for(int i = 0; i < header.size(); i++){
            if(!header.hasChildren(i))
                headerList.add(header.getString(i));
        }
        return headerList;
    }
    private int maxNumGroups(){
        return getGroupKeyList().size();
    }
    private String structureKeyPair(){
        return structureKeyDropDown.getSelectedString();
    }
    //endregion
    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    @Override
    public EntryWidgetParam getParam() {
        if(structureKey() == null){
            structureKeyDropDown.setError();
            return null;
        }

        if(valueKeyDropDown.getSelectedString() == null){
            valueKeyDropDown.setError();
            return null;
        }
        checkingGroup:
        if(groupLayout != null){
            boolean error = false;
            ArrayList<DropDown> groupDropDowns = getGroupDropDownList();
            ArrayList<ItemPath> groupValues = getGroupValues();
            if(groupValues.size() == 0)
                break checkingGroup;
            for(DropDown dropDown: groupDropDowns){
                if(dropDown.getSelectedString() == null) {
                    dropDown.setError();
                    error = true;
                }
            }
            if(groupValues.get(groupValues.size() - 1) == null) {
                System.out.println("last group value is null");
                groupLayout.getViewWrapper().setNameRed();
                return null;
            }
            if(error)
                return null;
        }

        return new DropDown.DropDownParam(null, null, structureKey(), valueKeyDropDown.getSelectedString(), getGroupValues());
    }

    @Override
    public void setParam(EntryWidgetParam param) {
        DropDown.DropDownParam dropDownParam = ((DropDown.DropDownParam) param);
        structureKeyDropDown.setSelected(dropDownParam.structureKey);
        onStructureKeyChange();
        valueKeyDropDown.setSelected(dropDownParam.valueKey);
        ArrayList<ItemPath> groups = dropDownParam.groups;
        onValueKeyChange();
        if(groups == null){
            return;
        }
        if(groups.size() == 0){
            return;
        }
        for(int i = 0; i < groups.size(); i++){
            addGroupBy();
            DropDown dropDown = (DropDown) groupLayout.getWidgetLayout().widgets().get(i);
            dropDown.setSelected(groups.get(i));
        }

    }

    @Override
    public View getView() {
        return customLinearLayout.getView();
    }



}
