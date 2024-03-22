package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.CustomLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupWidget implements Widget {
    Context context;
    ArrayList<Widget> widgets = new ArrayList<>();
    public static final String className = "group widget";
    public GroupWidget(Context context){
        this.context = context;
        customLinearLayout = new CustomLinearLayout(context);
    }

    private Runnable onDataChangedListener = null;
    private CustomLinearLayout customLinearLayout;

    public void addWidget(Widget widget){
        widgets.add(widget);
        customLinearLayout.add(widget.getView());
    }

    public void removeWidget(Widget widget){
        widgets.remove(widget);
        customLinearLayout.remove(widget.getView());
    }

    public boolean hasButton(){
        return customLinearLayout.hasButton();
    }

    public void removeButton(){
        customLinearLayout.removeButton();
    }

    public void insertButton(View.OnClickListener listener){
        customLinearLayout.insertButton(listener);
    }

    public ArrayList<Widget> widgets(){
        return ((ArrayList<Widget>) widgets.clone());
    }

    public ArrayList<Widget> inflateAll(ArrayList<WidgetParam> params){
        System.out.println("setting widgets: " + params.size());
        for(int i = 0; i < params.size(); i++){
            System.out.println("\tadding widget and inflating: ");
            addWidget(GLib.inflateWidget(context, params.get(i)));
        }
        return widgets();
    }

    public Widget inflate(WidgetParam widgetParam){
        Widget widget = GLib.inflateWidget(context, widgetParam);
        addWidget(widget);
        return widget;
    }

    public ArrayList<WidgetParam> getDataWidgets(){
        System.out.println("getting group widget data numWidget: " + widgets.size());
        ArrayList<WidgetParam> params = new ArrayList<>();
        for(int i = 0; i < widgets.size(); i++){
            params.add(widgets.get(i).getData());
        }

        return params;
    }

    public ArrayList<WidgetValue> getValueWidgets(){
        ArrayList<WidgetValue> values = new ArrayList<>();
        for(int i = 0; i < widgets.size(); i++){
            values.add(widgets.get(i).value());
        }

        return values;
    }

    public void onDataChange(){
        onDataChangedListener.run();
    }


    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public WidgetParam getData() {
        return new GroupWidgetParam(getDataWidgets());
    }

    @Override
    public WidgetValue value() {
        return new GroupWidgetValue(getValueWidgets());
    }

    @Override
    public DataTree getDataTree() {
        DataTree tree = new DataTree();
        for(Widget widget: widgets){
            tree.add(widget.getDataTree());
        }
        return tree;
    }

    @Override
    public void setData(WidgetParam params) {
        GroupWidgetParam groupParams = (GroupWidgetParam) params;
        inflateAll(groupParams.params);
        for(Widget widget: widgets)
            widget.setOnDataChangedListener(()->onDataChange());
    }

    @Override
    public View getView() {
        return customLinearLayout.getView();
    }

    public static class GroupWidgetParam extends WidgetParam {
        ArrayList<WidgetParam> params;
        public GroupWidgetParam(ArrayList<WidgetParam> params){
            this.params = params;
            this.widgetClass = className;
        }

        public GroupWidgetParam(WidgetParam[] params){
            this.params = new ArrayList<>(Arrays.asList(params));
            this.widgetClass = className;
        }

        public String toString(){
            return hierarchyString(0);
        }

        public String hierarchyString(int numTabs){
            String singleTab = "\t";
            String tabs = "";

            for(int i = 0; i < numTabs; i++)
                tabs += singleTab;
            String result = tabs + "group widget\n";
            for(WidgetParam widgetParam: params){
                result += widgetParam.hierarchyString(numTabs + 1);
            }
            return result;
        }

        @Override
        public DataTree header() {
            DataTree header = new DataTree("null");
            for(WidgetParam param: params){
                header.add(param.header());
            }
            return null;
        }
    }
    public static class GroupWidgetValue extends WidgetValue{
        ArrayList<WidgetValue> values;
        public GroupWidgetValue(ArrayList<WidgetValue> values){
            this.values = values;
        }
    }
}
