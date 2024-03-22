package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.Dictionary;
import com.example.habittracker.GLib;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetDropDown;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetEditText;
import com.example.habittracker.Widgets.StructureWidgetState.StructureWidgetList;

public class StructureWidget implements Widget {
    private CustomEditText name = null;
    private DropDown typeDropDown = null;
    //used for slider and drop down

    //used for type list
    private StructureWidgetList structureWidgetList = null;

    //used for drop down
    private StructureWidgetDropDown structureWidgetDropDown = null;

    private StructureWidgetEditText structureWidgetEditText = null;


    private String currentType = DropDown.nullValue;
    private Context context;

    private GroupWidget groupWidget;

    public StructureWidget(Context context) {
        this.context = context;
        groupWidget = new GroupWidget(context);



        name = new CustomEditText(context);
        groupWidget.addWidget(name);
        name.setOnDataChangedListener(()->{});

        typeDropDown = new DropDown(context);
        groupWidget.addWidget(typeDropDown);
        DropDown.StaticDropDownParameters params = new DropDown.StaticDropDownParameters(Dictionary.getTypes());
        typeDropDown.setData(params);

        typeDropDown.setOnDataChangedListener(() -> onTypeChange());
        typeDropDown.setName("typeDropDown");
    }

    public void onTypeChange(){
        //System.out.println("<StructureWidget>data changed");
        String type = typeDropDown.value().selected.getName();
        //System.out.println("type = " + type);

        if( ! currentType.equals(type) )
            setType(type);

        currentType = type;
    }



    public void setType(String type){
        //System.out.println("reset type");
        clearWidgets();
        typeSwitch:{
            if(type.equals(DropDown.nullValue)){
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
    public void setOnDataChangedListener(Runnable runnable) {
    }

    @Override
    public WidgetParam getData(){
        String type = typeDropDown.value().selected.getName();
        typeSwitch:{
            if(type.equals(DropDown.nullValue)){
                //System.out.println("structure type null");
                return null;
            }
            if(type.equals("list")){
                ListWidget.ListParam param = (ListWidget.ListParam)structureWidgetList.getData();
                param.name = name.text();
                return param;
            }
            if(type.equals("drop down")){
                DropDown.DropDownParam param = (DropDown.DropDownParam) structureWidgetList.getData();
                param.name = name.text();
                return param;
            }

            if(type.equals("edit text")){
                CustomEditText.EditTextParam param = (CustomEditText.EditTextParam) structureWidgetEditText.getData();
                param.name = name.text();
                return param;
            }
        }
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
        return groupWidget.getView();
    }

    public static class StructureWidgetParam extends WidgetParam {
        public WidgetParam widgetParam;

        public StructureWidgetParam(WidgetParam widgetParam) {
            widgetClass = "structure widget";
            this.widgetParam = widgetParam;
        }

        @Override
        public String hierarchyString(int numTabs) {
            return GLib.tabs(numTabs) + "structure widget\n" + widgetParam.hierarchyString(numTabs+1);
        }

        @Override
        public DataTree header() {
            throw new RuntimeException();
        }
    }

    public static class StructureWidgetValue extends WidgetValue{


        public StructureWidgetValue(String name, String type, ListWidget list, String structureKey) {

        }
    }
}
