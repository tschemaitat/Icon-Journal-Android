package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.WidgetLinearLayout;

import java.util.ArrayList;

public class GroupWidget implements Widget {
    Context context;
    public GroupWidget(Context context){
        this.context = context;
        widgetLinearLayout = new WidgetLinearLayout(context);
    }

    private Runnable onDataChangedListener = null;
    private WidgetLinearLayout widgetLinearLayout;

    public ArrayList<Widget> widgets(){
        return widgetLinearLayout.widgets();
    }

    public void addWidget(Widget widget){
        widgetLinearLayout.addWidget(widget);
    }

    public void removeWidget(Widget widget){
        widgetLinearLayout.removeWidget(widget);
    }

    public boolean hasButton(){
        return widgetLinearLayout.hasButton();
    }

    public void removeButton(){
        widgetLinearLayout.removeButton();
    }

    public void insertButton(View.OnClickListener listener){
        widgetLinearLayout.insertButton(listener);
    }


    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public WidgetParam getData() {
        return new GroupWidgetParam(widgetLinearLayout.getDataWidgets());
    }

    @Override
    public WidgetValue value() {
        return new GroupWidgetValue(widgetLinearLayout.getValueWidgets());
    }

    @Override
    public void setData(WidgetParam params) {
        GroupWidgetParam groupParams = (GroupWidgetParam) params;
        widgetLinearLayout.setWidgetsInLayout(groupParams.params);
    }

    @Override
    public View getView() {
        return widgetLinearLayout.getView();
    }

    public static class GroupWidgetParam extends WidgetParam {
        ArrayList<WidgetParam> params;
        public GroupWidgetParam(ArrayList<WidgetParam> params){
            this.params = params;
            this.widgetClass = "widget group";
        }
    }
    public static class GroupWidgetValue extends WidgetValue{
        ArrayList<WidgetValue> values;
        public GroupWidgetValue(ArrayList<WidgetValue> values){
            this.values = values;
        }
    }
}
