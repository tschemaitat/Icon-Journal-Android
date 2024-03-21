package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.CustomEditText;
import com.example.habittracker.Dictionary;
import com.example.habittracker.GLib;
import com.example.habittracker.StructureWidgetDropDown;
import com.example.habittracker.WidgetLinearLayout;
import com.example.habittracker.Widgets.DropDown;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.Widget;

public class StructureWidget extends WidgetLinearLayout implements Widget {
    private CustomEditText name = null;
    private DropDown typeDropDown = null;
    //used for slider and drop down

    //used for type list
    private ListWidget structureWidgets = null;

    //used for drop down
    private StructureWidgetDropDown structureWidgetDropDown = null;


    private String currentType = DropDown.nullValue;
    private Runnable onDataChangedListener;
    private Context context;
    public Runnable parentListener;

    public StructureWidget(Context context) {
        super(context);
        this.context = context;

        onDataChangedListener = new Runnable() {
            @Override
            public void run() {
                onDataChange();
                parentListener.run();
            }
        };

        name = new CustomEditText(context);
        addWidget(name);
        name.setOnDataChangedListener(onDataChangedListener);

        typeDropDown = new DropDown(context);
        addWidget(typeDropDown);
        DropDown.StaticDropDownParameters params = new DropDown.StaticDropDownParameters(Dictionary.getTypes());
        typeDropDown.setData(params);
        System.out.println("onDataChangedListener = " + onDataChangedListener);
        typeDropDown.setOnDataChangedListener(onDataChangedListener);
        typeDropDown.setName("typeDropDown");
    }

    public void onDataChange(){
        System.out.println("<StructureWidget>data changed");
        String type = typeDropDown.value().selected.getName();
        System.out.println("type = " + type);

        if(!currentType.equals(type)){
            resetType(type);
        }
        currentType = type;

        if(parentListener != null)
            parentListener.run();
    }

    public void setType(WidgetParam widgetParam){
        System.out.println("set type");

        String type = widgetParam.widgetClass;
        if(type.equals(DropDown.nullValue)){
            System.out.println("structure type null");
            return;
        }
        if(type.equals("list")){
            resetType("list");
            ListWidget.ListParam listParams = (ListWidget.ListParam) widgetParam;

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
            if(type.equals(DropDown.nullValue)){
                System.out.println("structure type null");
                return;
            }
            if(type.equals("list")){
                StructureWidgetParam params = new StructureWidgetParam(null);
                ListWidget.ListParam listParams = new ListWidget.ListParam(params);
                structureWidgets = (ListWidget) GLib.inflateWidget(context, listParams);
                addWidget(structureWidgets);
                structureWidgets.setOnDataChangedListener(onDataChangedListener);
                return;
            }
            if(type.equals("drop down")){
                structureWidgetDropDown = new StructureWidgetDropDown(context, this);
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
        if(structureWidgetDropDown != null)
            structureWidgetDropDown.clear();
        structureWidgetDropDown = null;
    }


















    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        parentListener = runnable;
    }

    @Override
    public StructureWidgetParam getData(){
        return null;
    }

    @Override
    public WidgetValue value(){
        return null;
    }


    @Override
    public void setData(WidgetParam params){
        StructureWidgetParam structureWidgetParams = (StructureWidgetParam) params;
        if(structureWidgetParams.widgetParam == null)
            return;
    }

    @Override
    public View getView() {
        return super.getView();
    }

    public static class StructureWidgetParam extends WidgetParam {
        public WidgetParam widgetParam;

        public StructureWidgetParam(WidgetParam widgetParam) {
            widgetClass = "structure widget";
            this.widgetParam = widgetParam;
        }
    }

    public static class StructureWidgetValue extends WidgetValue{


        public StructureWidgetValue(String name, String type, ListWidget list, String structureKey) {

        }
    }
}
