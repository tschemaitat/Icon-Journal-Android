package com.example.habittracker.Widgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.R;
import com.example.habittracker.ViewWidgets.StructureWidgetHeaderView;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetDropDown;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetEditText;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetList;

public class StructureWidget implements Widget {
    private StructureWidgetHeaderView headerView = null;

    private DropDown typeDropDown = null;
    private String currentType = DropDown.nullValue;

    private StructureWidgetList structureWidgetList = null;
    private StructureWidgetDropDown structureWidgetDropDown = null;
    private StructureWidgetEditText structureWidgetEditText = null;

    Runnable onDataChangeListener;

    private Context context;

    private LinLayout layout;

    public StructureWidget(Context context) {
        this.context = context;
        layout = new LinLayout(context);
        layout.getView().setId(R.id.structureWidget);

        headerView = new StructureWidgetHeaderView(context);
        headerView.addNameEditor(null);
        layout.add(headerView.getView());
        //groupWidget.addWidget(name);

        typeDropDown = new DropDown(context);
        layout.add(typeDropDown.getView());
        DropDown.StaticDropDownParameters params = new DropDown.StaticDropDownParameters(null, Dictionary.getTypes());
        typeDropDown.setParam(params);
        typeDropDown.setHint("select type");

        typeDropDown.setOnDataChangedListener(() -> onTypeChange());


        Margin.setStructureWidgetLayout(layout);
    }

    public LinLayout getLinLayout(){
        return layout;
    }

    public void addDeleteButton(Runnable runnable) {
        headerView.addDeleteButton(runnable);
    }

    public void disableNameEditor(){
        headerView.nameEditor.disableEdit();
    }

    public void onTypeChange(){
        typeDropDown.getViewWrapper().resetNameColor();
        //System.out.println("<StructureWidget>data changed");
        String type = typeDropDown.getSelectedString();
        //System.out.println("type = " + type)
        if(type == null){
            if(currentType == null)
                return;
            currentType = type;
            setType();
            return;
        }

        if( ! type.equals(currentType) ){
            currentType = type;
            setType();
        }



    }

    public void onDataChange(){
        onDataChangeListener.run();
    }



    public void setType(){
        //System.out.println("reset type");
        clearWidgets();
        typeSwitch:{
            if(currentType == null){

                //System.out.println("structure type null");
                return;
            }
            if(currentType.equals("list")){
                structureWidgetList = new StructureWidgetList(context, layout);
                return;
            }
            if(currentType.equals("drop down")){
                structureWidgetDropDown = new StructureWidgetDropDown(context, layout);
                return;
            }


            if(currentType.equals("edit text")){
                structureWidgetEditText = new StructureWidgetEditText(context, layout);
                return;
            }
        }
    }

    public void clearWidgets(){
        if(structureWidgetList != null)
            layout.remove(structureWidgetList.getView());
        structureWidgetList = null;
        if(structureWidgetDropDown != null)
            layout.remove(structureWidgetDropDown.getView());
        structureWidgetDropDown = null;
    }

    @Override
    public EntryWidgetParam getParam(){
        EntryWidgetParam result = null;

        String type = typeDropDown.getSelectedString();
        typeSwitch:{
            if(type == null){
                typeDropDown.getViewWrapper().setNameRed();
                //System.out.println("structure type null");
                break typeSwitch;
            }

            if(type.equals("list")){
                ListWidget.ListParam param = (ListWidget.ListParam)structureWidgetList.getParam();
                if(param != null)
                    param.name = headerView.nameEditor.getText();
                result = param;
                break typeSwitch;
            }
            if(type.equals("drop down")){
                DropDown.DropDownParam param = (DropDown.DropDownParam) structureWidgetDropDown.getParam();
                if(param != null)
                    param.name = headerView.nameEditor.getText();
                result = param;
                break typeSwitch;
            }

            if(type.equals("edit text")){
                CustomEditText.EditTextParam param = (CustomEditText.EditTextParam) structureWidgetEditText.getParam();
                if(param != null)
                    param.name = headerView.nameEditor.getText();
                result = param;
                break typeSwitch;
            }
        }
        if(headerView.nameEditor.getText() == null){
            headerView.nameEditor.getViewWrapper().setNameRed();
            return null;
        }
        return result;
    }



    @Override
    public void setParam(EntryWidgetParam param){
        System.out.println("setting param: " + param);
        String type = param.className;
        headerView.nameEditor.setText(param.name);
        typeDropDown.setSelected(type);
        currentType = type;
        setType();
        typeSwitch:{
            if(type == null)
                throw new RuntimeException();

            if(type.equals(ListWidget.className)){
                System.out.println("type is list");
                structureWidgetList.setParam(param);
                break typeSwitch;
            }
            if(type.equals(DropDown.className)){
                System.out.println("type is drop down");
                structureWidgetDropDown.setParam(param);
                break typeSwitch;
            }

            if(type.equals(CustomEditText.className)){
                System.out.println("type is edit text");
                structureWidgetEditText.setParam(param);
                break typeSwitch;
            }
            throw new RuntimeException("invalid type: " + type);
        }
    }

    @Override
    public View getView() {
        return layout.getView();
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangeListener = runnable;
    }


}
