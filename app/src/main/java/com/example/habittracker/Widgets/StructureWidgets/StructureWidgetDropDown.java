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
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetId;
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

    WidgetId valueWidget = null;
    ArrayList<WidgetId> groupValues = null;


    //used for state management when structure key changes and views need to be reset
    Structure structure = null;

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

    private DropDownPage createStructurePage(){
        ArrayList<Structure> structures = Dictionary.getStructures();
        //MainActivity.log("structures: " + EnumLoop.makeList(structures, structure -> structure.getCachedName().getString()));
        ArrayList<PayloadOption> payloadOptions = EnumLoop.makeList(structures, (input)-> new PayloadOption(
                input.getCachedName(), input));
        DropDownPage page = new DropDownPage().put(payloadOptions);
        //MainActivity.log("structure pages: \n" + page.hierarchyString());
        return page;
    }

    private void createStructureKeyDropDown(){
        structureKeyDropDown = new StaticDropDown(context);
        structureKeyDropDown.setup(createStructurePage(), (itemPath, payload) -> {
            onStructureKeyChange((Structure)payload);
        });
        structureKeyDropDown.getDropDown().setHint("select spreadsheet");
        customLinearLayout.add(structureKeyDropDown.getView());
    }



    private void onStructureKeyChange(Structure payload){
        if(structure == null){
            if(payload == null){
                MainActivity.log("structure payload is null");
                return;
            }
            structureKeyDropDown.getViewWrapper().resetNameColor();
            resetValueKeyWidget();
        }else{
            if( ! structure.equals(payload)){
                structureKeyDropDown.getViewWrapper().resetNameColor();
                resetValueKeyWidget();
            }
        }



        structure = payload;
        structureKeyDropDown.getDropDown().resetError();
        System.out.println("structure key change");
        if(structure == null && valueKeyDropDown != null) {
            resetValueKeyWidget();
            return;
        }
        MainActivity.log("checking if value drop down was created");
        if(valueKeyDropDown == null){
            createValueKeyDropDown();
        }

    }

    private DropDownPage createValuePage(){
        ArrayList<WidgetId> widgetIdList = structure.getWidgetIdList();
        ArrayList<RefItemPath> itemPathList = EnumLoop.makeList(widgetIdList, widgetId -> widgetId.getNameWithPath());
        ArrayList<Object> payloadList = EnumLoop.makeList(widgetIdList, widgetId -> widgetId);
        //MainActivity.log("widgetIdList: "+widgetIdList);
        //MainActivity.log("itemPathList: "+itemPathList);
        //MainActivity.log("payloadList: "+itemPathList);
        DropDownPage valuePage = DropDownPage.fromItemPathWithPayload(itemPathList, payloadList);;
        //MainActivity.log("valuePage: \n" + valuePage.hierarchyString());

        return valuePage;
    }

    private void createValueKeyDropDown(){
        //MainActivity.log("creating value key drop down");
        valueKeyDropDown = new StaticDropDown(context, createValuePage(), (itemPath, payload) -> {
            onValueKeyChange((WidgetId) payload);
        });
        valueKeyDropDown.setHint("item to be selected");
        customLinearLayout.add(valueKeyDropDown.getView());
    }
    private void resetValueKeyWidget(){
        //System.out.println("resetting value key widget");
        if(valueKeyDropDown == null)
            return;
        customLinearLayout.remove(valueKeyDropDown.getView());
        valueKeyDropDown = null;
        resetGroupKeyWidget();
    }
    private void onValueKeyChange(WidgetId payload){
        //MainActivity.log("value key change");
        if(valueWidget != payload){
            resetGroupKeyWidget();
        }
        valueWidget = payload;
        valueKeyDropDown.resetError();

        if(valueWidget == null && groupLayout != null){
            valueKeyDropDown.getViewWrapper().resetNameColor();
            resetGroupKeyWidget();
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
        ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
        StaticDropDown lastGroup = groupDropDowns.get(groupDropDowns.size() - 1);
        boolean lastNull = lastGroup.getSelectedString() == null;
        //System.out.println("groupLayout.getLinLayout().hasButton() = " + groupLayout.getLinLayout().hasButton());
        //System.out.println("maxNumGroups() = " + maxNumGroups());
        if(!groupLayout.getLinLayout().hasButton() && groupDropDowns.size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    private void createGroupLayout(){
        groupValues = new ArrayList<>();
        System.out.println("<drop down structure widget> creating group layout");
        groupLayout = new GroupWidget(context);
        //groupLayout.getViewWrapper().setName("group by");
        Margin.setStructureWidgetGroupLayout(groupLayout.getLinLayout());

        customLinearLayout.add(groupLayout.getView());
        //System.out.println("adding group add button");
        addGroupKeyDropDownAdd();
    }

    private DropDownPage createGroupPage(int groupWidget){
        ArrayList<WidgetId> widgetIdList = getGroupWidgetCandidates();
        ArrayList<RefItemPath> itemPathList = EnumLoop.makeList(widgetIdList, widgetId -> widgetId.getNameWithPath());
        ArrayList<Object> payloadList = EnumLoop.makeList(widgetIdList, widgetId -> new GroupValuePayload(groupWidget, widgetId));
        return DropDownPage.fromItemPathWithPayload(itemPathList, payloadList);
    }

    private ArrayList<WidgetId> getGroupWidgetCandidates(){

        ArrayList<WidgetId> widgetIdList = structure.getWidgetIdList();
        //MainActivity.log("getting group candidates from: " + widgetIdList);
        widgetIdList.remove(valueWidget);
        for(WidgetId widgetId: groupValues)
            widgetIdList.remove(widgetId);
        //MainActivity.log("result: " + widgetIdList);
        return widgetIdList;
    }

    private void addGroupBy(){
        ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
        int newIndex = groupDropDowns.size();
        StaticDropDown dropDown = new StaticDropDown(context, createGroupPage(newIndex), (itemPath, payload) -> {
            onGroupValueChange((GroupValuePayload) payload);
        });
        dropDown.setHint("select group");
        groupDropDowns.add(dropDown);
        groupLayout.getWidgetLayout().add(dropDown);
        //MainActivity.log("checking to remove button: " + groupDropDowns.size() + " >= " + maxNumGroups());
        if(groupDropDowns.size() >= maxNumGroups())
            groupLayout.getLinLayout().removeButton();
    }

    private void onGroupValueChange(GroupValuePayload payload){
        ArrayList<StaticDropDown> groupDropDowns = getGroupDropDownList();
        if(groupValues.size()< payload.groupWidgetIndex)
            throw new RuntimeException();
        int changedIndex = payload.groupWidgetIndex;
        if(groupValues.size() == changedIndex)
            groupValues.add(changedIndex, payload.widgetId);
        else
            groupValues.set(changedIndex, payload.widgetId);
        //reset all values between changedIndex and groupWidgetList.size()
        for(int i = groupValues.size() - 1; i >= changedIndex + 1; i--){
            StaticDropDown staticDropDown = groupDropDowns.get(i);
            staticDropDown.resetValue();
            groupValues.remove(i);
        }
        int indexToResetPage = changedIndex + 1;
        if(groupDropDowns.size() > indexToResetPage)
            groupDropDowns.get(indexToResetPage).setPage(createGroupPage(indexToResetPage));
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
        if(groupLayout == null)
            return;
        //System.out.println("resetting gorup key widget");
        customLinearLayout.remove(groupLayout.getView());
        groupLayout = null;
        groupValues = null;
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
        ArrayList<WidgetId> totalPossibleWidgets = structure.getWidgetIdList();
        //MainActivity.log("candidate widgets: " + totalPossibleWidgets + ", groupValues: " + groupValues);
        return totalPossibleWidgets.size() - 1;
    }
    private String structureKeyPair(){
        return structureKeyDropDown.getSelectedString();
    }
    //endregion
    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }


    public EntryWidgetParam getParam() {
        if(structure == null){
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

        return new DropDownParam(null, structure, valueWidget, groupValues);
    }

    @Override
    public void setParam(EntryWidgetParam param) {
        DropDownParam dropDownParam = ((DropDownParam) param);
        structure = dropDownParam.structure;
        createStructureKeyDropDown();
        structureKeyDropDown.setSelectedByPayload(structure);
        onStructureKeyChange(structure);
        valueWidget = dropDownParam.valueKey;
        valueKeyDropDown.setSelectedByPayload(valueWidget);
        onValueKeyChange(valueWidget);
        for(int i = 0; i < dropDownParam.groups.size(); i++){
            WidgetId widgetId = dropDownParam.groups.get(i);
            addGroupBy();
            GroupValuePayload groupValuePayload = new GroupValuePayload(i, widgetId);
            getGroupDropDownList().get(i).setSelectedByPayload(groupValuePayload);
            onGroupValueChange(groupValuePayload);
        }
    }

    @Override
    public View getView() {
        return customLinearLayout.getView();
    }

    public static class GroupValuePayload{
        public int groupWidgetIndex;
        public WidgetId widgetId;
        public GroupValuePayload(int groupWidgetIndex, WidgetId widgetId){
            this.groupWidgetIndex = groupWidgetIndex;
            this.widgetId = widgetId;
        }

        public String toString(){
            return "<group payload> " + widgetId + ", groupWidgetIndex: " + groupWidgetIndex;
        }

        @Override
        public boolean equals(Object object){
            if(object instanceof GroupValuePayload payload){
                return payload.groupWidgetIndex == groupWidgetIndex && payload.widgetId.equals(widgetId);
            }
            return false;
        }
    }



}
