package com.example.habittracker;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class StructureWidget extends WidgetGroup implements Widget{
    CustomEditText name = null;
    CustomSpinner typeDropDown = null;
    //used for slider and drop down

    //used for type list
    WidgetList structureWidgets = null;

    //used for drop down
    CustomSpinner structureKeyDropDown = null;
    CustomSpinner valueKeyDropDown = null;
    WidgetGroup groupKeyDropDowns = null;



    String currentType = CustomSpinner.nullValue;

    Runnable onDataChangedListener;


    public StructureWidget(Context context) {
        super(context);

        onDataChangedListener = new Runnable() {
            @Override
            public void run() {
                onDataChange();
            }
        };

        name = new CustomEditText(context);
        addWidget(name);
        name.setOnDataChangedListener(onDataChangedListener);

        typeDropDown = new CustomSpinner(context);
        addWidget(typeDropDown);
        CustomSpinner.DropDownParams dropDownParams = new CustomSpinner.DropDownParams("types", "type");
        typeDropDown.setData(dropDownParams);
        typeDropDown.setOnDataChangedListener(onDataChangedListener);
    }

    public void onDataChange(){
        System.out.println("data changed");
        String type = typeDropDown.value().selected;
        if(currentType == null){
            if(type.equals(CustomSpinner.nullValue))
                return;
            resetType(type);
            return;
        }
        if(!currentType.equals(type)){
            resetType(type);
        }

        iterate();
        currentType = type;
        if(parentListener != null)
            parentListener.run();
    }

    public void iterate(){
        System.out.println("iterating");
        if(currentType.equals(CustomSpinner.className)){
            System.out.println("iterating drop down");
            String structureKey = structureKeyDropDown.value().selected;
            String[] header = Dictionary.header(structureKey);
            ArrayList<String> headerList = new ArrayList<>(Arrays.asList(header));
            DropDownPage valuePage = new DropDownPage("value keys");
            for(String s: header)
                valuePage.add(new DropDownPage(s));
            if(valueKeyDropDown == null){
                System.out.println("value key drop down is null");
                CustomSpinner.StaticDropDownParameters params = new CustomSpinner.StaticDropDownParameters(valuePage);
                valueKeyDropDown = (CustomSpinner) GLib.inflateWidget(context, params);
                addWidget(valueKeyDropDown);
                valueKeyDropDown.setOnDataChangedListener(onDataChangedListener);
                return;
            }
            String valueKey = valueKeyDropDown.value().selected;
            System.out.println("valueKey = " + valueKey);
            if(valueKey.equals(CustomSpinner.nullValue)){
                System.out.println("value key is null, removing group widgets");
                if(groupKeyDropDowns != null){
                    removeWidget(groupKeyDropDowns);
                    groupKeyDropDowns = null;
                }
                return;
            }


            headerList.remove(valueKey);

            if(maxNumGroups() == 0)
                return;
            if(groupKeyDropDowns == null){
                makeGroupKeyDropDowns();
                return;
            }
            checkDuplicateGroupBy();
        }
    }

    public void checkDuplicateGroupBy(){
        ArrayList<String> headerList = getHeaderList();
        ArrayList<CustomSpinner> groupDropDowns = getGroupDropDownList();
        for(int i = 0; i < groupDropDowns.size(); i++){
            CustomSpinner currentDropDown = groupDropDowns.get(i);
            String selected = currentDropDown.value().selected;
            if( ! headerList.contains(selected) && ! selected.equals(CustomSpinner.nullValue)){
                System.out.println("found groupBy duplicate at index: " + i);
                System.out.println("groupDropDowns.size() = " + groupDropDowns.size());
                for(int j = i; j < groupDropDowns.size(); j++){
                    groupKeyDropDowns.removeWidget(currentDropDown);
                }
                break;
            }
            headerList.remove(selected);
        }
        groupDropDowns = getGroupDropDownList();
        headerList = getHeaderList();
        headerList.remove(valueKeyDropDown.value().selected);
        for(int i = 0; i < groupDropDowns.size(); i++){
            CustomSpinner currentDropDown = groupDropDowns.get(i);
            if(currentDropDown.value().selected.equals(CustomSpinner.nullValue))
                currentDropDown.setData(new CustomSpinner.StaticDropDownParameters(headerList));
            headerList.remove(currentDropDown.value().selected);
        }

        tryAddButton();

    }

    public int maxNumGroups(){
        return getHeaderList().size() - 1;
    }

    public void tryAddButton(){
        ArrayList<CustomSpinner> groups = getGroupDropDownList();
        CustomSpinner lastGroup = groups.get(groups.size() - 1);
        boolean lastNull = lastGroup.value().selected.equals(CustomSpinner.nullValue);
        if(!groupKeyDropDowns.hasButton && groupKeyDropDowns.widgetsInLayout.size() < maxNumGroups() && ! lastNull){
            addGroupKeyDropDownAdd();
        }
    }

    public ArrayList<String> reducedHeaderList(){
        ArrayList<String> headerList = getHeaderList();
        String valueKey = valueKeyDropDown.value().selected;
        headerList.remove(valueKey);
        ArrayList<CustomSpinner> groupDropDowns = getGroupDropDownList();
        for(int i = 0; i < groupDropDowns.size(); i++){
            CustomSpinner currentDropDown = groupDropDowns.get(i);
            String selected = currentDropDown.value().selected;
            if( ! headerList.contains(selected) && ! selected.equals(CustomSpinner.nullValue)){
                throw new RuntimeException("found duplicate groupBy values when setting new groupBy drop down");
            }
            headerList.remove(selected);
        }
        return headerList;
    }

    public ArrayList<CustomSpinner> getGroupDropDownList(){
        ArrayList<CustomSpinner> groupDropDowns = new ArrayList<>();
        for(Widget widget: groupKeyDropDowns.widgetsInLayout)
            groupDropDowns.add((CustomSpinner) widget);
        return groupDropDowns;
    }

    public void addGroupBy(){
        System.out.println("adding groupBy");
        ArrayList<String> reducedHeaderList = reducedHeaderList();
        DropDownPage page = new DropDownPage("reduced group key options");
        for(String s: reducedHeaderList)
            page.add(new DropDownPage(s));
        CustomSpinner.StaticDropDownParameters params = new CustomSpinner.StaticDropDownParameters(page);
        CustomSpinner dropDown = (CustomSpinner)GLib.inflateWidget(context, params);
        dropDown.setOnDataChangedListener(onDataChangedListener);
        groupKeyDropDowns.addWidget(dropDown);
        System.out.println("groupKeyDropDowns.widgetsInLayout.size() = " + groupKeyDropDowns.widgetsInLayout.size());
        System.out.println("maxNumGroups() = " + maxNumGroups());
        if(groupKeyDropDowns.widgetsInLayout.size() >= maxNumGroups())
            groupKeyDropDowns.removeAddButton();
    }

    public void addGroupKeyDropDownAdd(){
        groupKeyDropDowns.insertAddButtonAtEnd(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processGroupClick();
            }
        });
    }

    public ArrayList<String> getHeaderList(){
        String structureKey = structureKeyDropDown.value().selected;
        String[] header = Dictionary.header(structureKey);
        return new ArrayList<>(Arrays.asList(header));
    }

    public void makeGroupKeyDropDowns(){
        ArrayList<String> headerList = getHeaderList();
        System.out.println("groupby widget group is null");
        DropDownPage groupPage = new DropDownPage("group keys");
        for(String s: headerList)
            groupPage.add(new DropDownPage(s));
        System.out.println("groupPage = " + groupPage);
        groupKeyDropDowns = new WidgetGroup(context);
        addWidget(groupKeyDropDowns);
        addGroupKeyDropDownAdd();
    }

    public void processGroupClick(){
        System.out.println("group button clicked");
        System.out.printf("num groups: " + groupKeyDropDowns.widgetsInLayout.size() + ", max: " + maxNumGroups());
        addGroupBy();
    }

    public void setType(WidgetParams widgetParams){
        System.out.println("set type");

        String type = widgetParams.widgetClass;
        if(type.equals(CustomSpinner.nullValue)){
            System.out.println("structure type null");
            return;
        }
        if(type.equals("list")){
            resetType("list");
            WidgetList.ListParams listParams = (WidgetList.ListParams) widgetParams;

            return;
        }
        if(type.equals("drop down")){
            resetType("drop down");
            return;
        }
        if(type.equals("edit text")){
            resetType("edit text");

            return;
        }
    }

    public void resetType(String type){
        System.out.println("reset type");
        clearWidgets();
        typeSwitch:{
            if(type.equals(CustomSpinner.nullValue)){
                System.out.println("structure type null");
                return;
            }
            if(type.equals("list")){
                StructureWidgetParams params = new StructureWidgetParams(null);
                WidgetList.ListParams listParams = new WidgetList.ListParams(params);
                structureWidgets = (WidgetList) GLib.inflateWidget(context, listParams);
                addWidget(structureWidgets);
                structureWidgets.setOnDataChangedListener(onDataChangedListener);
                return;
            }
            if(type.equals("drop down")){
                System.out.println("setting structureWidget to drop down");

                CustomSpinner.DropDownParams structureKeyParams = new CustomSpinner.DropDownParams("structure keys", null, null);
                structureKeyDropDown = (CustomSpinner) GLib.inflateWidget(context, structureKeyParams);
                addWidget(structureKeyDropDown);
                System.out.println("\n\n\nsetting structure key listener --------------");
                System.out.println("onDataChangeListener = " + onDataChangedListener);
                structureKeyDropDown.setOnDataChangedListener(onDataChangedListener);

                System.out.println("finished setting structure key listener --------------");

                return;
            }


            if(type.equals("edit text")){

                return;
            }
        }
    }

    public void clearWidgets(){
        removeWidget(structureWidgets);
        structureWidgets = null;
        removeWidget(valueKeyDropDown);
        valueKeyDropDown = null;
        removeWidget(groupKeyDropDowns);
        groupKeyDropDowns = null;
        removeWidget(structureKeyDropDown);
        structureKeyDropDown = null;
    }





    public Runnable parentListener;


    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        parentListener = runnable;
    }

    @Override
    public StructureWidgetParams getData(){
        return null;
    }

    @Override
    public WidgetValue value(){
        return null;
    }


    @Override
    public void setData(WidgetParams params){
        StructureWidgetParams structureWidgetParams = (StructureWidgetParams) params;
        if(structureWidgetParams.widgetParams == null)
            return;
    }

    @Override
    public View getView() {
        return super.outlineLayout;
    }

    public static class StructureWidgetParams extends WidgetParams{
        public WidgetParams widgetParams;

        public StructureWidgetParams(WidgetParams widgetParams) {
            widgetClass = "structure widget";
            this.widgetParams = widgetParams;
        }
    }

    public static class StructureWidgetValue extends WidgetValue{


        public StructureWidgetValue(String name, String type, WidgetList list, String structureKey) {

        }
    }
}
