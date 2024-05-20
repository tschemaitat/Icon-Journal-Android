package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.StaticDropDown;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureWidgetDropDown implements Widget {
    private Context context;
    private LinLayout customLinearLayout;
    private LinLayout parent;

    //used for drop down
    StaticDropDown structureKeyDropDown = null;
    StaticDropDown valueKeyDropDown = null;
    GroupWidget groupLayout = null;




    //used for state management when structure key changes and views need to be reset

    public StructureWidgetDropDown(Context context, LinLayout parent) {
        this.context = context;
        this.parent = parent;
        customLinearLayout = new LinLayout(context);
        parent.add(customLinearLayout.getView());
        init();
    }

    private void init(){
        createStructureKeyDropDown();
        createValueKeyDropDown();
        createGroupLayout();
        enableStructureKeyDropDown();
    }

    //region structure

    private DropDownPage createStructurePage(){
        ArrayList<Structure> structures = Dictionary.getStructures();
        //MainActivity.log("structures: " + EnumLoop.makeList(structures, structure -> structure.getCachedName().getString()));
        ArrayList<PayloadOption> payloadOptions = EnumLoop.makeList(structures, (input)-> new PayloadOption(
                input.getCachedName(), input));
        DropDownPage page = new DropDownPage().put(payloadOptions);
        //MainActivity.log("structure pages: \n" + page.hierarchyString());
        return page;
    }

    private void enableStructureKeyDropDown(){
        structureKeyDropDown.setup(createStructurePage(), (itemPath, payload, prevItemPath, prevPayload) -> {
            onStructureKeyChange((Structure)payload, (Structure)prevPayload);
        });
        structureKeyDropDown.enable();
    }

    private void createStructureKeyDropDown(){
        if(structureKeyDropDown != null)
            throw new RuntimeException();
        structureKeyDropDown = new StaticDropDown(context);
        structureKeyDropDown.getDropDown().setHint("select spreadsheet");
        customLinearLayout.add(structureKeyDropDown.getView());
        structureKeyDropDown.disableWithGrayOut();
    }




    private void onStructureKeyChange(Structure structure, Structure prevStructure){
        if(prevStructure == null && structure == null)
            return;
        if(structure == null){
            resetAndDisableValueKeyWidget();
            return;
        }
        if(structure.equals(prevStructure)){
            MainActivity.log("payload: " + structure + ", prev: " + prevStructure);
            throw new RuntimeException();
        }

        boolean previousValueWasNull = prevStructure == null;
        //now we know that we are setting a new value
        //structureKeyDropDown.getViewWrapper().resetNameColor();
        if( ! previousValueWasNull)
            resetAndDisableValueKeyWidget();
        enableValueKeyDropDown();

        structureKeyDropDown.getDropDown().resetError();

    }

    public Structure getSelectedStructure(){
        return (Structure) structureKeyDropDown.getPayload();
    }

    //endregion

    //region value

    private DropDownPage createValuePage(){
        ArrayList<WidgetInStructure> widgetInStructureList = getSelectedStructure().getWidgetIdList();
        ArrayList<RefItemPath> itemPathList = EnumLoop.makeList(widgetInStructureList, widgetId -> widgetId.getNameWithPath());
        ArrayList<Object> payloadList = EnumLoop.makeList(widgetInStructureList, widgetId -> widgetId);
        MainActivity.log("itemPathList: " + itemPathList);
        MainActivity.log("payloadList: " + payloadList);
        DropDownPage valuePage = DropDownPage.fromItemPathWithPayload(itemPathList, payloadList);;

        return valuePage;
    }

    private void enableValueKeyDropDown(){
        MainActivity.log("enable value key drop down");
        valueKeyDropDown.setPage(createValuePage());
        valueKeyDropDown.enable();
    }

    private void createValueKeyDropDown(){
        if(valueKeyDropDown != null)
            throw new RuntimeException();
        valueKeyDropDown = new StaticDropDown(context, null, (itemPath, payload, prevItemPath, prevPayload) -> {
            onValueKeyChange((WidgetInStructure) payload, (WidgetInStructure)prevPayload);
        });
        customLinearLayout.add(valueKeyDropDown.getView());
        valueKeyDropDown.setHint("item to be selected");
        valueKeyDropDown.disableWithGrayOut();
    }



    private void resetAndDisableValueKeyWidget(){
        //customLinearLayout.remove(valueKeyDropDown.getView());
        valueKeyDropDown.resetValue();
        valueKeyDropDown.disableWithGrayOut();
        resetAndDisableGroupKeyWidget();
    }


    private void onValueKeyChange(WidgetInStructure value, WidgetInStructure prevValue){
        if(prevValue == null && value == null)
            return;
        if(value == null){
            resetAndDisableValueKeyWidget();
            return;
        }
        boolean previousValueWasNull = prevValue == null;
        if(value.equals(prevValue))
            throw new RuntimeException();
        prevValue = value;
        if(maxNumGroups() == 0){
            MainActivity.log("on value key change: max num groups is 0");
            return;
        }
        if( ! previousValueWasNull)
            resetAndDisableGroupKeyWidget();
        enableGroupLayout();

        valueKeyDropDown.resetError();

    }

    private WidgetInStructure getSelectedValueWidget(){
        return (WidgetInStructure) valueKeyDropDown.getPayload();
    }

//endregion

    //region group

    private ArrayList<WidgetInStructure> getGroupValueList(){
        ArrayList<StaticDropDown> groupDropDownList = getGroupDropDownList();
        ArrayList<WidgetInStructure> groupValues = EnumLoop.makeList(groupDropDownList, (dropDown)->
            (WidgetInStructure) dropDown.getPayload()
        );
        return groupValues;
    }

    private void tryAddButton(){
        ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
        StaticDropDown lastGroup = groupDropDowns.get(groupDropDowns.size() - 1);
        boolean lastNull = lastGroup.getSelectedString() == null;
        //System.out.println("groupLayout.getLinLayout().hasButton() = " + groupLayout.getLinLayout().hasButton());
        //System.out.println("maxNumGroups() = " + maxNumGroups());
        if(!groupLayout.getLinLayout().hasButton() && groupDropDowns.size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    private void enableGroupLayout(){
        MainActivity.log("enabling group layout");
        groupLayout.enable();

    }

    private void createGroupLayout(){
        System.out.println("<drop down structure widget> creating group layout");
        groupLayout = new GroupWidget(context);
        Margin.setStructureWidgetGroupLayout(groupLayout.getLinLayout());
        customLinearLayout.add(groupLayout.getView());
        groupLayout.disableWithGrayOut();
        addGroupKeyDropDownAdd();
    }



    private DropDownPage createGroupPage(){
        ArrayList<WidgetInStructure> widgetInStructureList = getGroupWidgetCandidates();
        ArrayList<RefItemPath> itemPathList = EnumLoop.makeList(widgetInStructureList, widgetId -> widgetId.getNameWithPath());
        return DropDownPage.fromItemPathWithPayload(itemPathList, EnumLoop.makeList(widgetInStructureList, (obj)->obj));
    }

    private ArrayList<WidgetInStructure> getGroupWidgetCandidates(){

        ArrayList<WidgetInStructure> widgetInStructureList = getSelectedStructure().getWidgetIdList();
        //MainActivity.log("getting group candidates from: " + widgetIdList);
        widgetInStructureList.remove(getSelectedValueWidget());
        for(WidgetInStructure widgetInStructure : getGroupValueList())
            widgetInStructureList.remove(widgetInStructure);
        //MainActivity.log("result: " + widgetIdList);
        return widgetInStructureList;
    }

    private void addGroupBy(){
        ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
        int newIndex = groupDropDowns.size();
        StaticDropDown dropDown = new StaticDropDown(context, createGroupPage(), (itemPath, payload, prevItemPath, prevPayload) -> {
            onGroupValueChange((WidgetInStructure) payload, (WidgetInStructure) prevPayload, newIndex);
        });
        dropDown.setHint("select group");
        groupDropDowns.add(dropDown);
        groupLayout.getWidgetLayout().add(dropDown);
        //MainActivity.log("checking to remove button: " + groupDropDowns.size() + " >= " + maxNumGroups());
        if(groupDropDowns.size() >= maxNumGroups())
            groupLayout.getLinLayout().removeButton();
    }

    private void onGroupValueChange(WidgetInStructure payload, WidgetInStructure prevPayload, int index){
        ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
        ArrayList<WidgetInStructure> groupValues = getGroupValueList();
        if(groupValues.size() < index)
            throw new RuntimeException();
        int changedIndex = index;
        if(groupValues.size() == changedIndex)
            groupValues.add(changedIndex, payload);
        else
            groupValues.set(changedIndex, payload);
        //reset all values between changedIndex and groupWidgetList.size()
        for(int i = groupValues.size() - 1; i >= changedIndex + 1; i--){
            StaticDropDown staticDropDown = groupDropDowns.get(i);
            staticDropDown.resetValue();
            groupValues.remove(i);
        }
        int indexToResetPage = changedIndex + 1;
        if(groupDropDowns.size() > indexToResetPage)
            groupDropDowns.get(indexToResetPage).setPage(createGroupPage());
    }
    private void addGroupKeyDropDownAdd(){
        groupLayout.getLinLayout().addButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGroupClick();
            }
        });
    }



    private void resetAndDisableGroupKeyWidget(){
        ArrayList<Widget> widgets = groupLayout.getWidgetLayout().widgets();
        for(Widget widget: widgets)
            groupLayout.getWidgetLayout().remove(widget);
        //customLinearLayout.remove(groupLayout.getView());
        //groupLayout = null;
        groupLayout.disableWithGrayOut();
    }



    private void processGroupClick(){
        groupLayout.getViewWrapper().resetNameColor();
        //System.out.println("group button clicked");
        //System.out.println("num groups: " + groupKeyDropDowns.widgets().size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    private ArrayList<StaticDropDown> getGroupDropDownList() {
        ArrayList<StaticDropDown> dropDowns = new ArrayList<>();
        if(groupLayout == null)
            return dropDowns;
        for(Widget widget: groupLayout.getWidgetLayout().widgets())
            dropDowns.add(((StaticDropDown) widget));
        return dropDowns;
    }

    private int maxNumGroups(){
        ArrayList<WidgetInStructure> totalPossibleWidgets = getSelectedStructure().getWidgetIdList();
        //MainActivity.log("candidate widgets: " + totalPossibleWidgets + ", groupValues: " + groupValues);
        return totalPossibleWidgets.size() - 1;
    }
    //endregion

    //region interface

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }


    public EntryWidgetParam getParam() {
        if(getSelectedStructure() == null){
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
            ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
            ArrayList<WidgetInStructure> groupValues = getGroupValueList();
            if(groupValues.size() == 0)
                break checkingGroup;
            for(StaticDropDown dropDown: groupDropDowns){
                if(dropDown.getSelectedString() == null) {
                    dropDown.setError();
                    error = true;
                }
            }
            if(groupValues.get(groupValues.size() - 1) == null) {
                //System.out.println("last group value is null");
                groupLayout.getViewWrapper().setNameRed();
                return null;
            }
            if(error)
                return null;
        }

        return new DropDownParam((String) null, getSelectedStructure().getId(), getSelectedValueWidget().getWidgetId(),
                EnumLoop.makeList(getGroupValueList(), (widgetInStructure)-> widgetInStructure.getWidgetId()));
    }

    @Override
    public void setParam(EntryWidgetParam param) {
        DropDownParam dropDownParam = ((DropDownParam) param);
        //structure = dropDownParam.structure;
        //createStructureKeyDropDown();
        structureKeyDropDown.setSelectedByPayload(dropDownParam.getStructure());
        onStructureKeyChange(dropDownParam.getStructure(), null);
        //onStructureKeyChange(structure);
        //valueWidget = dropDownParam.valueKey;
        valueKeyDropDown.setSelectedByPayload(dropDownParam.getValueWidget());
        onValueKeyChange(dropDownParam.getValueWidget(), null);
        //onValueKeyChange(valueWidget);
        for(int i = 0; i < dropDownParam.getGroupWidgets().size(); i++){
            WidgetInStructure widgetInStructure = dropDownParam.getGroupWidgets().get(i);
            addGroupBy();
            getGroupDropDownList().get(i).setSelectedByPayload(widgetInStructure);
            onGroupValueChange(widgetInStructure, null, i);
        }
    }

    @Override
    public View getView() {
        return customLinearLayout.getView();
    }

    //endregion



}
