package com.example.habittracker;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StructureWidget extends WidgetGroup implements Widget{
    JSONObject json = new JSONObject();
    CustomEditText name;
    CustomSpinner typeDropDown;
    ArrayList<CustomSpinner> groupDropDowns = new ArrayList<>();
    String structureKey;

    WidgetList list = null;

    String currentType = CustomSpinner.nullValue;



    public StructureWidget(Context context) {
        super(context);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onDataChange();
            }
        };

        name = new CustomEditText(context);
        addWidget(name);
        name.setOnDataChangedListener(runnable);

        typeDropDown = new CustomSpinner(context);
        addWidget(typeDropDown);
        CustomSpinner.DropDownParams dropDownParams = new CustomSpinner.DropDownParams("types", "type");
        typeDropDown.setData(dropDownParams);
        typeDropDown.setOnDataChangedListener(runnable);

    }

    public void onDataChange(){
        StructureWidgetData data = null;
        try {
            data = gatherData();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String type = data.type;
        if(!currentType.equals(type)){

        }

    }

    public void setData(StructureWidgetData data){
        name.setValue(data.name);
        typeDropDown.setValue(data.type);
        this.structureKey = data.structureKey;

    }

    public void changeType(StructureWidgetData data){
        String type = data.type;
        clearWidgets();
        typeSwitch:{
            if(type.equals(CustomSpinner.nullValue)){
                System.out.println("structure type null");
                return;
            }
            if(type.equals("list")){
                StructureWidget newStructureWidget = new StructureWidget(context);
                list = new WidgetList(context);

                WidgetList.ListParams listParams = new WidgetList.ListParams(newStructureWidget.getData());
                list.setData(listParams);

                addWidget(list);
                return;
            }
            if(type.equals("drop down")){
                return;
            }
            if(type.equals("edit text")){

            }
        }
    }

    public void clearWidgets(){
        removeWidget(list);
        for(CustomSpinner groupDropDown: groupDropDowns){
            removeWidget(groupDropDown);
        }
    }

    public StructureWidgetData gatherData() throws JSONException{

        StructureWidgetData data = new StructureWidgetData();
        data.name = name.text + "";
        data.type = typeDropDown.getData().selected;
        for(CustomSpinner dropDown: groupDropDowns){
            String dropDownData = dropDown.getData().selected;
            data.dropDownGroups.add(dropDownData);
        }
        ArrayList<Widget> structureWidgetsForList = list.widgetsInLayout;
        for(Widget widget: structureWidgetsForList){
            StructureWidget structureWidget = ((StructureWidget) widget);
            StructureWidgetData widgetData = structureWidget.gatherData();
            data.listWidgets.add(widgetData);
        }

        return data;
    }



    public void addDropDown(){

    }

    @Override
    public Widget widgetClone(){
        return null;
    }

    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public StructureWidgetParams getData(){
        return null;
    }

    @Override
    public WidgetValue value(){
        return null;
    }

    public static JSONObject makeJSON(StructureWidgetData data) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", data.type);
        json.put("name", data.name);
        if(data.structureKey != null)
            json.put("structure key", data.structureKey);
        if(data.listWidgets.size() > 0){
            JSONArray array = new JSONArray();

            for(StructureWidgetData widget: data.listWidgets){
                array.put(makeJSON(widget));
            }
            json.put("list widgets", array);
        }
        if(data.dropDownGroups.size() > 0){
            JSONArray array = new JSONArray();

            for(String group: data.dropDownGroups){
                array.put(group);
            }
            json.put("group widgets", array);
        }

        return json;
    }

    @Override
    public void setData(WidgetParams params){
        StructureWidgetParams casted = (StructureWidgetParams) params;

//        StructureWidgetData data = (StructureWidgetData)json.get("widget data");
//        setData(data);

    }

    @Override
    public View getView() {
        return outlineLayout;
    }

    public static class StructureWidgetParams extends WidgetParams{
        public String name;
        public String type;
        public WidgetList list;
        public String structureKey;

        public StructureWidgetParams(String name, String type, WidgetList list, String structureKey) {
            this.widgetClass = "structure widget";
            this.name = name;
            this.type = type;
            this.list = list;
            this.structureKey = structureKey;
        }
    }

    public static class StructureWidgetValue extends WidgetValue{
        public String name;
        public String type;
        public WidgetList list;
        public String structureKey;

        public StructureWidgetValue(String name, String type, WidgetList list, String structureKey) {
            this.name = name;
            this.type = type;
            this.list = list;
            this.structureKey = structureKey;
        }
    }
}
