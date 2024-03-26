package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Dictionary;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetDropDown;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetEditText;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetList;

public class StructureWidget implements Widget {
    private CustomEditText nameEditor = null;

    private DropDownSpinner typeDropDown = null;
    private String currentType = DropDownSpinner.nullValue;

    private StructureWidgetList structureWidgetList = null;
    private StructureWidgetDropDown structureWidgetDropDown = null;
    private StructureWidgetEditText structureWidgetEditText = null;

    Runnable onDataChangeListener;

    private Context context;

    private GroupWidget groupWidget;

    public StructureWidget(Context context) {
        this.context = context;
        groupWidget = new GroupWidget(context);
        groupWidget.addBorder();


        nameEditor = new CustomEditText(context);
        groupWidget.addNameEditor(nameEditor.getView());
        groupWidget.setMargin(5, 10);
        //groupWidget.addWidget(name);
        nameEditor.setOnDataChangedListener(()->{});
        nameEditor.setName("widget name");

        typeDropDown = new DropDownSpinner(context);
        groupWidget.addWidget(typeDropDown);
        DropDownSpinner.StaticDropDownParameters params = new DropDownSpinner.StaticDropDownParameters("type", Dictionary.getTypes());
        typeDropDown.setParam(params);

        typeDropDown.setOnDataChangedListener(() -> onTypeChange());
        typeDropDown.setName("attribute type");
    }

    public void disableNameEditor(){
        nameEditor.disableEdit();
    }

    public void onTypeChange(){
        typeDropDown.resetNameColor();
        //System.out.println("<StructureWidget>data changed");
        String type = typeDropDown.getSelectedString();
        //System.out.println("type = " + type);

        if( ! currentType.equals(type) )
            setType(type);

        currentType = type;
    }

    public void onDataChange(){
        onDataChangeListener.run();
    }



    public void setType(String type){
        //System.out.println("reset type");
        clearWidgets();
        typeSwitch:{
            if(type.equals(DropDownSpinner.nullValue)){

                //System.out.println("structure type null");
                return;
            }
            if(type.equals("list")){
                structureWidgetList = new StructureWidgetList(context, groupWidget);
                return;
            }
            if(type.equals("drop down")){
                structureWidgetDropDown = new StructureWidgetDropDown(context, groupWidget);
                return;
            }


            if(type.equals("edit text")){
                structureWidgetEditText = new StructureWidgetEditText(context, groupWidget);
                return;
            }
        }
    }

    public void clearWidgets(){
        if(structureWidgetList != null)
            groupWidget.removeWidget(structureWidgetList);
        structureWidgetList = null;
        if(structureWidgetDropDown != null)
            groupWidget.removeWidget(structureWidgetDropDown);
        structureWidgetDropDown = null;
    }

    @Override
    public EntryWidgetParam getParam(){
        EntryWidgetParam result = null;

        String type = typeDropDown.getSelectedString();
        typeSwitch:{
            if(type == null){
                typeDropDown.setNameRed();
                //System.out.println("structure type null");
                break typeSwitch;
            }

            if(type.equals("list")){
                ListWidget.ListParam param = (ListWidget.ListParam)structureWidgetList.getParam();
                if(param != null)
                    param.name = nameEditor.text();
                result = param;
                break typeSwitch;
            }
            if(type.equals("drop down")){
                DropDownSpinner.DropDownParam param = (DropDownSpinner.DropDownParam) structureWidgetDropDown.getParam();
                if(param != null)
                    param.name = nameEditor.text();
                result = param;
                break typeSwitch;
            }

            if(type.equals("edit text")){
                CustomEditText.EditTextParam param = (CustomEditText.EditTextParam) structureWidgetEditText.getParam();
                if(param != null)
                    param.name = nameEditor.text();
                result = param;
                break typeSwitch;
            }
        }
        if(nameEditor.text() == null){
            nameEditor.setNameRed();
            return null;
        }
        return result;
    }



    @Override
    public void setParam(EntryWidgetParam param){
        String type = param.className;
        nameEditor.setText(param.name);
        typeDropDown.setSelected(type);
        setType(type);
        typeSwitch:{

            if(type.equals(ListWidget.className)){
                structureWidgetList.setParam(param);
            }
            if(type.equals(DropDownSpinner.className)){
                structureWidgetDropDown.setParam(param);
            }

            if(type.equals(CustomEditText.className)){
                structureWidgetEditText.setParam(param);
            }
        }
    }

    @Override
    public View getView() {
        return groupWidget.getView();
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangeListener = runnable;
    }


    public void addDeleteButton(Runnable runnable) {
        groupWidget.addDeleteButton(runnable);
    }
}
