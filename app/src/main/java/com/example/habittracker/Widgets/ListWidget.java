package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;

public class ListWidget extends GroupWidget implements Widget {

    private GroupWidgetParam cloneParams = null;
    public static final String className = "list";
    private Context context;

    public ListWidget(Context context){
        super(context);
        this.context = context;



    }
    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }


    @Override
    public WidgetParam getData(){
        ArrayList<GroupWidgetParam> groupWidgetParam = new ArrayList<>();
        for(WidgetParam widgetParam: getDataWidgets())
            groupWidgetParam.add((GroupWidgetParam) widgetParam);

        ListParam params = new ListParam(cloneParams, groupWidgetParam);

        return params;
    }

    @Override
    public WidgetValue value(){
        ListValue value = new ListValue(getValueWidgets());

        return value;
    }

    @Override
    public DataTree getDataTree(){
        return super.getDataTree();
    }


    @Override
    public void setData(WidgetParam params){
        System.out.println("setting data for list");
        ListParam listParams = (ListParam) params;
        cloneParams = listParams.cloneableWidget;
        inflateAll(new ArrayList<>(listParams.currentWidgets));
        makeButton();


        for(int i = 0; i < widgets().size(); i++){
            widgets().get(i).setOnDataChangedListener(()->onDataChange());
        }

        System.out.println("data finished set for list");
        System.out.println(getData());
    }

    public void onDataChange(){
        onDataChangedListener.run();
    }



    public void makeButton(){
        insertButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Widget newWidget = null;

                newWidget = GLib.inflateWidget(context, cloneParams);
                newWidget.setOnDataChangedListener(new Runnable() {
                    @Override
                    public void run() {
                        onDataChangedListener.run();
                    }
                });


                addWidget(newWidget);
            }
        });
    }

    @Override
    public View getView() {
        return super.getView();
    }

    public static class ListParam extends WidgetParam {
        public String name = "null";
        public GroupWidgetParam cloneableWidget;
        public ArrayList<GroupWidgetParam> currentWidgets;

        public ListParam(GroupWidgetParam cloneableWidget, ArrayList<GroupWidgetParam> currentWidgets){
            this.widgetClass = "list";
            this.cloneableWidget = cloneableWidget;
            this.currentWidgets = currentWidgets;
        }

        public ListParam(GroupWidgetParam cloneableWidget){
            this.widgetClass = "list";
            this.cloneableWidget = cloneableWidget;
            this.widgetClass = widgetClass;
            currentWidgets = new ArrayList<>();
        }
        public String toString(){
            return hierarchyString(0);
        }

        public String hierarchyString(int numTabs){

            return GLib.tabs(numTabs) + "list\n"
                    + cloneableWidget.hierarchyString(numTabs + 1);
        }

        @Override
        public DataTree header() {
            DataTree tree = cloneableWidget.header();

            return tree;
        }
    }

    public static class ListValue extends WidgetValue{
        public ArrayList<WidgetValue> currentWidgets;
        public ListValue(ArrayList<WidgetValue> currentWidgets){
            this.currentWidgets = currentWidgets;
        }
    }
}
