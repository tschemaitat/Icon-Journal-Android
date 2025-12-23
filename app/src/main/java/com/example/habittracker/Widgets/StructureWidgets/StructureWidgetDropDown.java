package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.ViewLibrary.ButtonElement;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.VertLayout;
import com.example.habittracker.ViewLibrary.ViewElement;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Widgets.StaticDropDown;
import com.example.habittracker.Widgets.Widget;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class StructureWidgetDropDown{
    private Context context;
    private LinearElementLayout customLinearLayout;
    private LinearElementLayout parent;

    //used for drop down
    StaticDropDown structureKeyDropDown = null;
    StaticDropDown valueKeyDropDown = null;
    LinearElementLayout groupButtonLayout = null;
    //this has a name? and sets it to red on error?
    //i changed it so that it sets hint of every widget to error color
    //need to look at
    WidgetLayout<StaticDropDown> groupWidgetLayout = null;
    ButtonElement addButton = null;






    //used for state management when structure key changes and views need to be reset

    public StructureWidgetDropDown(Context context, LinearElementLayout parent) {
        this.context = context;
        this.parent = parent;
        customLinearLayout = new VertLayout(context);
        parent.add(customLinearLayout);
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
        structureKeyDropDown.getElement().enable();
    }

    private void createStructureKeyDropDown(){
        if(structureKeyDropDown != null)
            throw new RuntimeException();
        structureKeyDropDown = new StaticDropDown(context, new ViewWrapper(context), null);
        structureKeyDropDown.getDropDown().setHint("select spreadsheet");
        customLinearLayout.add(structureKeyDropDown.getElement());
        structureKeyDropDown.getElement().disableWithGray();
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
        MainActivity.log("creating value page");
        MainActivity.log("selected structure: " + getSelectedStructure().toString());
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
        valueKeyDropDown.getElement().enable();
    }

    private void createValueKeyDropDown(){
        if(valueKeyDropDown != null)
            throw new RuntimeException();
        valueKeyDropDown = new StaticDropDown(context, new ViewWrapper(context), null, null, (itemPath, payload, prevItemPath, prevPayload) -> {
            onValueKeyChange((WidgetInStructure) payload, (WidgetInStructure)prevPayload);
        });
        customLinearLayout.add(valueKeyDropDown.getElement());
        valueKeyDropDown.setHint("item to be selected");
        valueKeyDropDown.getElement().disableWithGray();
    }



    private void resetAndDisableValueKeyWidget(){
        //customLinearLayout.remove(valueKeyDropDown.getView());
        valueKeyDropDown.resetValue();
        valueKeyDropDown.getElement().disableWithGray();
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
//        if(!groupWidgetLayout.getLinearElementLayout().hasButton() && groupDropDowns.size() < maxNumGroups() && ! lastNull){
//            addGroupKeyDropDownAdd();
//        }
        if(addButton == null && groupDropDowns.size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    private void enableGroupLayout(){
        MainActivity.log("enabling group layout");
        groupWidgetLayout.enable();

    }

    private void createGroupLayout(){
        groupButtonLayout = new VertLayout(context);

        System.out.println("<drop down structure widget> creating group layout");
        groupWidgetLayout = new WidgetLayout(context);
        Margin.setStructureWidgetGroupLayout(groupWidgetLayout.getLinearElementLayout());
        groupButtonLayout.addWithParam(groupWidgetLayout.getElement(), -2, -2);
        customLinearLayout.add(groupButtonLayout);
        groupWidgetLayout.disableWithGray();
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
        StaticDropDown dropDown = new StaticDropDown(context, new ViewWrapper(context), null, createGroupPage(), (itemPath, payload, prevItemPath, prevPayload) -> {
            onGroupValueChange((WidgetInStructure) payload, (WidgetInStructure) prevPayload, newIndex);
        });
        dropDown.setHint("select group");
        groupDropDowns.add(dropDown);
        groupWidgetLayout.add(dropDown);
        //MainActivity.log("checking to remove button: " + groupDropDowns.size() + " >= " + maxNumGroups());
        if(groupDropDowns.size() >= maxNumGroups()){
            groupButtonLayout.remove(addButton);
            addButton = null;
            //groupWidgetLayout.getLinearElementLayout().removeButton();
        }

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

        addButton = new ButtonElement(context, "add", this::processGroupClick);
        groupButtonLayout.addWithParam(addButton, -2, -2);

//        groupLayout.getLinearElementLayout().addButton(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                processGroupClick();
//            }
//        });
    }



    private void resetAndDisableGroupKeyWidget(){
        ArrayList<StaticDropDown> widgets = groupWidgetLayout.widgets();
        for(StaticDropDown widget: widgets)
            groupWidgetLayout.remove(widget);
        //customLinearLayout.remove(groupLayout.getView());
        //groupLayout = null;
        groupWidgetLayout.disableWithGray();
    }



    private void processGroupClick(){
        for(StaticDropDown widget: groupWidgetLayout.widgets())
            widget.resetTextErrorColor();
            //groupWidgetLayout.resetNameColor();
        //System.out.println("group button clicked");
        //System.out.println("num groups: " + groupKeyDropDowns.widgets().size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    private ArrayList<StaticDropDown> getGroupDropDownList() {

        return groupWidgetLayout.widgets();
    }

    private int maxNumGroups(){
        ArrayList<WidgetInStructure> totalPossibleWidgets = getSelectedStructure().getWidgetIdList();
        //MainActivity.log("candidate widgets: " + totalPossibleWidgets + ", groupValues: " + groupValues);
        return totalPossibleWidgets.size() - 1;
    }
    //endregion

    //region interface





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
        if(groupWidgetLayout != null){
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
                for(StaticDropDown widget: groupWidgetLayout.widgets())
                    widget.setTextErrorColor();
                //groupWidgetLayout.getViewWrapper().setNameRed();
                return null;
            }
            if(error)
                return null;
        }

        return new DropDownParam((String) null, getSelectedStructure().getId(), getSelectedValueWidget().getWidgetId(),
                EnumLoop.makeList(getGroupValueList(), (widgetInStructure)-> widgetInStructure.getWidgetId()));
    }


    public void setParam(EntryWidgetParam param) {
        DropDownParam dropDownParam = ((DropDownParam) param);
        //structure = dropDownParam.structure;
        //createStructureKeyDropDown();
        MainActivity.log("structure payload set: " + dropDownParam.getReferenceStructure());
        structureKeyDropDown.setSelectedByPayload(dropDownParam.getReferenceStructure());
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


    public View getView() {
        return customLinearLayout.getView();
    }


    public Element getElement() {
        return new ViewElement() {
            @Override
            public View getView() {
                return customLinearLayout.getView();
            }
        };
    }

    //endregion



}
