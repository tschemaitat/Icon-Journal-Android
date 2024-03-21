package com.example.habittracker;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Widgets.DropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.StructureWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureWidgetDropDown {
    private Context context;
    StructureWidget parent;

    public StructureWidgetDropDown(Context context, StructureWidget parent){
        this.context = context;
        this.parent = parent;
        init();
    }

    public void init(){
        System.out.println("setting structureWidget to drop down");

        DropDown.StaticDropDownParameters structureKeyParams = new DropDown.StaticDropDownParameters(Dictionary.getStructureKeys());
        structureKeyDropDown = (DropDown) GLib.inflateWidget(context, structureKeyParams);
        parent.addWidget(structureKeyDropDown);
        System.out.println("\n\n\nsetting structure key listener --------------");
        System.out.println("onDataChangeListener = " + parent.onDataChangedListener);
        structureKeyDropDown.setOnDataChangedListener(() -> onStructureKeyChange());

        System.out.println("finished setting structure key listener --------------");

    }

    //used for drop down
    DropDown structureKeyDropDown = null;
    DropDown valueKeyDropDown = null;
    GroupWidget groupKeyDropDowns = null;

    public void onStructureKeyChange(){
        System.out.println("structure key change");
        String structureKey = structureKeyDropDown.value().selected.getName();
        if(structureKey.equals(DropDown.nullValue)){
            if(valueKeyDropDown != null)
                resetValueKeyWidget();
            return;
        }
        if(valueKeyDropDown == null)
            createValueKeyDropDown();
    }

    public void resetValueKeyWidget(){
        System.out.println("resetting value key widget");
        parent.removeWidget(valueKeyDropDown);
        valueKeyDropDown = null;
        resetGroupKeyWidget();
    }

    public void resetGroupKeyWidget(){
        System.out.println("resetting gorup key widget");
        parent.removeWidget(groupKeyDropDowns);
        groupKeyDropDowns = null;
        selectedGroupKeys = null;
    }

    public void onValueKeyChange(){
        System.out.println("value key change");
        String valueKey = valueKeyDropDown.value().selected.getName();
        System.out.println("valueKey = " + valueKey);
        if(valueKey.equals(DropDown.nullValue)){
            if(groupKeyDropDowns != null){
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



    ArrayList<DataTreeItem> selectedGroupKeys = null;

    public void createValueKeyDropDown(){
        System.out.println("creating value key drop down");
        String structureKey = structureKeyDropDown.value().selected.getName();
        ArrayList<String> headerList = valueKeyList(structureKey);
        DropDownPage valuePage = new DropDownPage("value keys", headerList);
        System.out.println("value key drop down is null");
        DropDown.StaticDropDownParameters params = new DropDown.StaticDropDownParameters(valuePage);
        valueKeyDropDown = (DropDown) GLib.inflateWidget(context, params);
        valueKeyDropDown.setName("valueKeyDropDown");
        parent.addWidget(valueKeyDropDown);
        valueKeyDropDown.setOnDataChangedListener(()->onValueKeyChange());
    }

    public void tryAddButton(){
        ArrayList<DropDown> groups = getGroupDropDownList();
        DropDown lastGroup = groups.get(groups.size() - 1);
        boolean lastNull = lastGroup.value().selected.equals(DropDown.nullValue);
        if(!groupKeyDropDowns.hasButton() && groupKeyDropDowns.widgets().size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    public ArrayList<DropDown> getGroupDropDownList(){

        ArrayList<DropDown> groupDropDowns = new ArrayList<>();
        for(Widget widget: groupKeyDropDowns.widgets())
            groupDropDowns.add((DropDown) widget);
        return groupDropDowns;
    }

    public void addGroupBy(){
        System.out.println("adding groupBy");
        DropDownPage page = DropDownPage.fromItems(getReducedGroupKeyList(selectedGroupKeys));
        System.out.println("group by page: \n" + page);
        DropDown dropDown = (DropDown)GLib.inflateWidget(context, new DropDown.StaticDropDownParameters(page));
        dropDown.setOnDataChangedListener(() -> processGroupItemSelected());
        groupKeyDropDowns.addWidget(dropDown);
        System.out.println("groupKeyDropDowns.widgetsInLayout.size() = " + groupKeyDropDowns.widgets().size());
        System.out.println("maxNumGroups() = " + maxNumGroups());
        if(groupKeyDropDowns.widgets().size() >= maxNumGroups())
            groupKeyDropDowns.removeButton();
    }

    public void processGroupItemSelected(){
        ArrayList<DropDown> dropDowns = getGroupDropDownList();
        selectedGroupKeys = new ArrayList<>();
        int removeIndex = dropDowns.size();
        for(int i = 0; i < dropDowns.size(); i++){
            DropDown dropDown = dropDowns.get(i);
            DataTreeItem item = dropDown.value().selected;
            if(item.getName().equals(DropDown.nullValue)){
                removeIndex = i + 1;
                break;
            }
            selectedGroupKeys.add(item);
        }
        for(int i = removeIndex; i < dropDowns.size(); i++){
            DropDown dropDown = dropDowns.get(i);
            dropDown.resetValue();
        }
        DropDownPage reducedPage = DropDownPage.fromItems(getReducedGroupKeyList(selectedGroupKeys));
        for(int i = removeIndex; i < dropDowns.size(); i++) {
            DropDown dropDown = dropDowns.get(i);
            dropDown.setData(new DropDown.StaticDropDownParameters(reducedPage));
        }
    }

    public void addGroupKeyDropDownAdd(){
        groupKeyDropDowns.insertButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGroupClick();
            }
        });
    }

    public void makeGroupKeyDropDowns(){
        selectedGroupKeys = new ArrayList<>();
        System.out.println("groupby widget group is null, making group key drop downs");
        groupKeyDropDowns = new GroupWidget(context);
        parent.addWidget(groupKeyDropDowns);
        addGroupKeyDropDownAdd();
    }



    public void processGroupClick(){
        System.out.println("group button clicked");
        System.out.printf("num groups: " + groupKeyDropDowns.widgets().size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    //region type dropdown gather functions
    public ArrayList<DataTreeItem> getGroupKeyList(){
        System.out.println("getting group key list");
        ArrayList<DataTreeItem> items = DataTree.gatherItems(Dictionary.header(structureKey()));
        items.remove(valueKeyDropDown.value().selected);
        return items;
    }
    public ArrayList<DataTreeItem> getReducedGroupKeyList(ArrayList<DataTreeItem> selectedGroupKeys){
        ArrayList<DataTreeItem> items = getGroupKeyList();
        for(DataTreeItem item: selectedGroupKeys){
            items.remove(item);
        }
        return items;
    }
    public ArrayList<String> valueKeyList(String structureKey){
        DataTree header = Dictionary.header(structureKey);
        ArrayList<String> headerList = new ArrayList<>();
        for(int i = 0; i < header.list.size(); i++){
            if(!header.isTree(i))
                headerList.add(header.getString(i));
        }
        return headerList;
    }
    public int maxNumGroups(){
        return getGroupKeyList().size() - 1;
    }
    public String structureKey(){
        return structureKeyDropDown.value().selected.getName();
    }

    public void clear() {
        parent.removeWidget(valueKeyDropDown);
        valueKeyDropDown = null;
        parent.removeWidget(groupKeyDropDowns);
        groupKeyDropDowns = null;
        parent.removeWidget(structureKeyDropDown);
        structureKeyDropDown = null;
    }
    //endregion
}
