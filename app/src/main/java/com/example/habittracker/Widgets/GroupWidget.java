package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.CustomLinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupWidget extends EntryWidget {
    Context context;
    ArrayList<Widget> widgets = new ArrayList<>();

    public static final String className = "group widget";
    private CustomLinearLayout customLinearLayout;
    public GroupWidget(Context context){
        super(context);
        this.context = context;
        customLinearLayout = new CustomLinearLayout(context);
        customLinearLayout.getView().setId(R.id.groupWidgetOuterLayout);
        setChild(customLinearLayout.getView());
    }

    public void setMargin(int hor, int vert){
        customLinearLayout.setMargin(hor, vert);
    }

    @Override
    public DataTree getDataTree() {
        DataTree tree = new DataTree();
        for(Widget widget: widgets()){
            tree.add(((EntryWidget) widget).getDataTree());
        }
        return tree;
    }


    public ArrayList<Widget> widgets(){
        return ((ArrayList<Widget>) widgets.clone());
    }

    public ArrayList<Widget> inflateAll(ArrayList<EntryWidgetParam> params){
        System.out.println("setting widgets: " + params.size());
        for(int i = 0; i < params.size(); i++){
            System.out.println("\tadding widget and inflating: ");
            addWidget(GLib.inflateWidget(context, params.get(i)));
        }
        return widgets();
    }

    public Widget inflate(EntryWidgetParam widgetParam){
        Widget widget = GLib.inflateWidget(context, widgetParam);
        addWidget(widget);
        return widget;
    }

    public ArrayList<EntryWidgetParam> getDataWidgets(){
        System.out.println("getting group widget data numWidget: " + widgets.size());
        ArrayList<EntryWidgetParam> params = new ArrayList<>();
        for(int i = 0; i < widgets.size(); i++){
            params.add(widgets.get(i).getParam());
        }

        return params;
    }

    @Override
    public EntryWidgetParam getParam() {
        return new GroupWidgetParam(null, getDataWidgets());
    }



    @Override
    public void setParamCustom(EntryWidgetParam params) {
        GroupWidgetParam groupParams = (GroupWidgetParam) params;
        inflateAll(groupParams.params);
        for(Widget widget: widgets)
            widget.setOnDataChangedListener(()->onDataChangedListener());
    }

    //region wrapper

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
    //endregion

    public static class GroupWidgetParam extends EntryWidgetParam {
        ArrayList<EntryWidgetParam> params;
        public GroupWidgetParam(String name, ArrayList<EntryWidgetParam> params){
            super(name, GroupWidget.className);
            this.params = params;
        }

        public GroupWidgetParam(String name, EntryWidgetParam[] params){
            super(name, GroupWidget.className);
            this.params = new ArrayList<>(Arrays.asList(params));
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
            for(EntryWidgetParam widgetParam: params){
                result += widgetParam.hierarchyString(numTabs + 1);
            }
            return result;
        }

        @Override
        public DataTree header() {
            DataTree header = new DataTree("null");
            for(EntryWidgetParam param: params){
                header.add(param.header());
            }
            return null;
        }
    }
}
