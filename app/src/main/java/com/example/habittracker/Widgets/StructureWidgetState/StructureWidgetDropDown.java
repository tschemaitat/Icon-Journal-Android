package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.Dictionary;
import com.example.habittracker.DropDownPage;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.DropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureWidgetDropDown implements Widget{
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
        //System.out.println("setting structureWidget to drop down");

        DropDown.StaticDropDownParameters structureKeyParams = new DropDown.StaticDropDownParameters(Dictionary.getStructureKeys());
        structureKeyDropDown = (DropDown) GLib.inflateWidget(context, structureKeyParams);
        groupWidget.addWidget(structureKeyDropDown);
        //System.out.println("\n\n\nsetting structure key listener --------------");
        structureKeyDropDown.setOnDataChangedListener(() -> onStructureKeyChange());

        //System.out.println("finished setting structure key listener --------------");

    }

    //used for drop down
    DropDown structureKeyDropDown = null;
    DropDown valueKeyDropDown = null;
    GroupWidget groupKeyDropDowns = null;

    String currentKey = DropDown.nullValue;

    private void onStructureKeyChange(){
        //System.out.println("structure key change");
        String structureKey = structureKeyDropDown.value().selected.getName();
        if(structureKey.equals(DropDown.nullValue)){
            if(valueKeyDropDown != null)
                resetValueKeyWidget();
            currentKey = structureKey;
            return;
        }
        if( ! currentKey.equals(structureKey)){
            resetValueKeyWidget();
        }

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
        String valueKey = valueKeyDropDown.value().selected.getName();
        //System.out.println("valueKey = " + valueKey);
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



    private ArrayList<ItemPath> selectedGroupKeys = null;

    private void createValueKeyDropDown(){
        //System.out.println("creating value key drop down");
        String structureKey = structureKeyDropDown.value().selected.getName();
        ArrayList<String> headerList = valueKeyList(structureKey);
        DropDownPage valuePage = new DropDownPage("value keys", headerList);
        //System.out.println("value key drop down is null");
        DropDown.StaticDropDownParameters params = new DropDown.StaticDropDownParameters(valuePage);
        valueKeyDropDown = (DropDown) GLib.inflateWidget(context, params);
        valueKeyDropDown.setName("valueKeyDropDown");
        groupWidget.addWidget(valueKeyDropDown);
        valueKeyDropDown.setOnDataChangedListener(()->onValueKeyChange());
    }

    private void tryAddButton(){
        ArrayList<DropDown> groups = getGroupDropDownList();
        DropDown lastGroup = groups.get(groups.size() - 1);
        boolean lastNull = lastGroup.value().selected.equals(DropDown.nullValue);
        if(!groupKeyDropDowns.hasButton() && groupKeyDropDowns.widgets().size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    private ArrayList<DropDown> getGroupDropDownList(){

        ArrayList<DropDown> groupDropDowns = new ArrayList<>();
        for(Widget widget: groupKeyDropDowns.widgets())
            groupDropDowns.add((DropDown) widget);
        return groupDropDowns;
    }

    private void addGroupBy(){
        //System.out.println("adding groupBy");
        DropDownPage page = DropDownPage.fromItems(getReducedGroupKeyList(selectedGroupKeys));
        //System.out.println("group by page: \n" + page);
        DropDown dropDown = (DropDown)GLib.inflateWidget(context, new DropDown.StaticDropDownParameters(page));
        dropDown.setOnDataChangedListener(() -> processGroupItemSelected());
        groupKeyDropDowns.addWidget(dropDown);
        //System.out.println("groupKeyDropDowns.widgetsInLayout.size() = " + groupKeyDropDowns.widgets().size());
        //System.out.println("maxNumGroups() = " + maxNumGroups());
        if(groupKeyDropDowns.widgets().size() >= maxNumGroups())
            groupKeyDropDowns.removeButton();
    }

    private void processGroupItemSelected(){
        ArrayList<DropDown> dropDowns = getGroupDropDownList();
        selectedGroupKeys = new ArrayList<>();
        int removeIndex = dropDowns.size();
        for(int i = 0; i < dropDowns.size(); i++){
            DropDown dropDown = dropDowns.get(i);
            ItemPath item = dropDown.value().selected;
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

    private void addGroupKeyDropDownAdd(){
        groupKeyDropDowns.insertButton(new View.OnClickListener() {
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
        groupWidget.addWidget(groupKeyDropDowns);
        addGroupKeyDropDownAdd();
    }



    private void processGroupClick(){
        //System.out.println("group button clicked");
        //System.out.println("num groups: " + groupKeyDropDowns.widgets().size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    //region type dropdown gather functions
    private ArrayList<ItemPath> getGroupKeyList(){
        //System.out.println("getting group key list");
        ArrayList<ItemPath> items = DataTree.gatherItems(Dictionary.header(structureKey()));
        items.remove(valueKeyDropDown.value().selected);
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
        return structureKeyDropDown.value().selected.getName();
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    @Override
    public WidgetParam getData() {
        return new DropDown.DropDownParam(new ItemPath(DropDown.nullValue), structureKey(), valueKeyDropDown.value().selected.getName(), getGroupKeyList());
    }

    @Override
    public WidgetValue value() {
        return null;
    }

    @Override
    public void setData(WidgetParam params) {

    }

    @Override
    public View getView() {
        return groupWidget.getView();
    }


    //endregion
}
