package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.Dictionary;
import com.example.habittracker.DropDownPage;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.DropDownSpinner;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureWidgetDropDown implements Widget {
    private Context context;
    private GroupWidget groupWidget;
    private GroupWidget parent;
    public StructureWidgetDropDown(Context context, GroupWidget parent) {
        this.context = context;
        this.parent = parent;
        groupWidget = new GroupWidget(context);
        parent.addWidget(groupWidget);
        init();
    }

    private void init(){
        createStructureKeyDropDown();
    }

    private void createStructureKeyDropDown(){
        DropDownSpinner.StaticDropDownParameters structureKeyParams = new DropDownSpinner.StaticDropDownParameters("structure name", new DropDownPage(null, Dictionary.getStructureKeys()));
        structureKeyDropDown = (DropDownSpinner) GLib.inflateWidget(context, structureKeyParams);
        groupWidget.addWidget(structureKeyDropDown);
        structureKeyDropDown.setOnDataChangedListener(() -> onStructureKeyChange());
        structureKeyDropDown.setName("spreadsheet name");
    }

    //used for drop down
    DropDownSpinner structureKeyDropDown = null;
    DropDownSpinner valueKeyDropDown = null;
    GroupWidget groupKeyDropDowns = null;

    //used for state management when structure key changes and views need to be reset
    String currentStructureKey = null;

    private void onStructureKeyChange(){
        //System.out.println("structure key change");
        String structureKey = structureKeyDropDown.getSelectedString();
        if(structureKey == null){
            if(valueKeyDropDown != null)
                resetValueKeyWidget();
            return;
        }
        if( ! structureKey.equals(currentStructureKey)){
            structureKeyDropDown.resetNameColor();
            resetValueKeyWidget();
        }
        currentStructureKey = structureKey;

        if(valueKeyDropDown == null)
            createValueKeyDropDown();

    }

    private void resetValueKeyWidget(){
        System.out.println("resetting value key widget");
        if(valueKeyDropDown == null)
            return;
        groupWidget.removeWidget(valueKeyDropDown);
        valueKeyDropDown = null;
        resetGroupKeyWidget();
    }

    private void resetGroupKeyWidget(){
        System.out.println("resetting gorup key widget");
        groupWidget.removeWidget(groupKeyDropDowns);
        groupKeyDropDowns = null;
        selectedGroupKeys = null;
    }

    private void onValueKeyChange(){
        //System.out.println("value key change");
        String valueKey = valueKeyDropDown.getSelectedString();
        //System.out.println("valueKey = " + valueKey);
        if(valueKey == null){
            if(groupKeyDropDowns != null){
                valueKeyDropDown.resetNameColor();
                resetGroupKeyWidget();
            }
            return;
        }
        if(maxNumGroups() == 0){
            System.out.println("can't make any groupby");
            return;
        }
        if(groupKeyDropDowns == null)
            makeGroupKeyDropDowns();
    }



    private ArrayList<ItemPath> selectedGroupKeys = null;

    private void createValueKeyDropDown(){
        //System.out.println("creating value key drop down");
        String structureKey = structureKeyDropDown.getSelectedString();
        ArrayList<String> headerList = valueKeyList(structureKey);
        DropDownPage valuePage = new DropDownPage("value keys", headerList);
        //System.out.println("value key drop down is null");
        DropDownSpinner.StaticDropDownParameters params = new DropDownSpinner.StaticDropDownParameters("desired value name", valuePage);
        valueKeyDropDown = (DropDownSpinner) GLib.inflateWidget(context, params);
        valueKeyDropDown.setName("item to be selected");
        groupWidget.addWidget(valueKeyDropDown);
        valueKeyDropDown.setOnDataChangedListener(()->onValueKeyChange());
    }

    private void tryAddButton(){
        ArrayList<DropDownSpinner> groups = getGroupDropDownList();
        DropDownSpinner lastGroup = groups.get(groups.size() - 1);
        boolean lastNull = lastGroup.getSelectedString() == null;
        if(!groupKeyDropDowns.hasButton() && groupKeyDropDowns.widgets().size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    private ArrayList<DropDownSpinner> getGroupDropDownList(){

        ArrayList<DropDownSpinner> groupDropDowns = new ArrayList<>();
        for(Widget widget: groupKeyDropDowns.widgets())
            groupDropDowns.add((DropDownSpinner) widget);
        return groupDropDowns;
    }

    private void addGroupBy(){
        //System.out.println("adding groupBy");
        DropDownPage page = DropDownPage.fromItems(getReducedGroupKeyList(selectedGroupKeys));
        //System.out.println("group by page: \n" + page);
        DropDownSpinner dropDown = (DropDownSpinner)GLib.inflateWidget(context, new DropDownSpinner.StaticDropDownParameters("group by", page));
        dropDown.setOnDataChangedListener(() -> processGroupItemSelected());
        groupKeyDropDowns.addWidget(dropDown);
        //System.out.println("groupKeyDropDowns.widgetsInLayout.size() = " + groupKeyDropDowns.widgets().size());
        //System.out.println("maxNumGroups() = " + maxNumGroups());
        if(groupKeyDropDowns.widgets().size() >= maxNumGroups())
            groupKeyDropDowns.removeButton();
    }

    private void processGroupItemSelected(){
        groupKeyDropDowns.resetNameColor();
        ArrayList<DropDownSpinner> dropDowns = getGroupDropDownList();
        selectedGroupKeys = new ArrayList<>();
        int removeIndex = dropDowns.size();
        for(int i = 0; i < dropDowns.size(); i++){
            DropDownSpinner dropDown = dropDowns.get(i);
            ItemPath item = dropDown.getSelectedPath();
            if(item == null){
                removeIndex = i + 1;
                break;
            }
            selectedGroupKeys.add(item);
        }
        for(int i = removeIndex; i < dropDowns.size(); i++){
            DropDownSpinner dropDown = dropDowns.get(i);
            dropDown.resetValue();
        }
        DropDownPage reducedPage = DropDownPage.fromItems(getReducedGroupKeyList(selectedGroupKeys));
        for(int i = removeIndex; i < dropDowns.size(); i++) {
            DropDownSpinner dropDown = dropDowns.get(i);
            dropDown.setParam(new DropDownSpinner.StaticDropDownParameters("group by", reducedPage));
        }
    }

    private void addGroupKeyDropDownAdd(){
        groupKeyDropDowns.addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGroupClick();
            }
        });
    }

    private void makeGroupKeyDropDowns(){
        selectedGroupKeys = new ArrayList<>();
        //System.out.println("groupby widget group is null, making group key drop downs");
        groupKeyDropDowns = new GroupWidget(context);
        groupKeyDropDowns.setName("group by");
        groupWidget.addWidget(groupKeyDropDowns);
        addGroupKeyDropDownAdd();
    }



    private void processGroupClick(){
        groupKeyDropDowns.resetNameColor();
        //System.out.println("group button clicked");
        //System.out.println("num groups: " + groupKeyDropDowns.widgets().size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    private ArrayList<ItemPath> getGroupValues(){
        ArrayList<DropDownSpinner> dropDowns = getGroupDropDownList();
        ArrayList<ItemPath> items = new ArrayList<>();
        for(DropDownSpinner dropDown: dropDowns){
            items.add(dropDown.getSelectedPath());
        }
        return items;
    }

    //region type dropdown gather functions
    private ArrayList<ItemPath> getGroupKeyList(){
        //System.out.println("getting group key list");
        ArrayList<ItemPath> items = DataTree.gatherItems(Dictionary.header(structureKey()));
        items.remove(valueKeyDropDown.getSelectedPath());
        return items;
    }
    private ArrayList<ItemPath> getReducedGroupKeyList(ArrayList<ItemPath> selectedGroupKeys){
        ArrayList<ItemPath> items = getGroupKeyList();
        for(ItemPath item: selectedGroupKeys){
            items.remove(item);
        }
        return items;
    }
    private ArrayList<String> valueKeyList(String structureKey){
        DataTree header = Dictionary.header(structureKey);
        ArrayList<String> headerList = new ArrayList<>();
        for(int i = 0; i < header.size(); i++){
            if(!header.hasChildren(i))
                headerList.add(header.getString(i));
        }
        return headerList;
    }
    private int maxNumGroups(){
        return getGroupKeyList().size() - 1;
    }
    private String structureKey(){
        return structureKeyDropDown.getSelectedString();
    }
    //endregion
    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    @Override
    public EntryWidgetParam getParam() {
        if(structureKey() == null){
            structureKeyDropDown.setNameRed();
            return null;
        }

        if(valueKeyDropDown.getSelectedString() == null){
            valueKeyDropDown.setNameRed();
            return null;
        }
        checkingGroup:
        if(groupKeyDropDowns != null){
            ArrayList<ItemPath> groupValues = getGroupValues();
            if(groupValues.size() == 0)
                break checkingGroup;
            if(groupValues.get(groupValues.size() - 1) == null) {
                System.out.println("last group value is null");
                groupKeyDropDowns.setNameRed();
                return null;
            }
        }

        return new DropDownSpinner.DropDownParam(null, null, structureKey(), valueKeyDropDown.getSelectedString(), getGroupValues());
    }

    @Override
    public void setParam(EntryWidgetParam param) {
        DropDownSpinner.DropDownParam dropDownParam = ((DropDownSpinner.DropDownParam) param);
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
            DropDownSpinner dropDown = (DropDownSpinner) groupKeyDropDowns.widgets().get(i);
            dropDown.setSelected(groups.get(i));
        }

    }

    @Override
    public View getView() {
        return groupWidget.getView();
    }



}
