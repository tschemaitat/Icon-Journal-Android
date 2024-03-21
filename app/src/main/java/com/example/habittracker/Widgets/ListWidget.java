package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.GLib;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.WidgetLinearLayout;

import java.util.ArrayList;

public class ListWidget extends WidgetLinearLayout implements Widget {

    private WidgetParam cloneParams = null;
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
        ListParam params = new ListParam(cloneParams, getDataWidgets());

        return params;
    }

    @Override
    public WidgetValue value(){
        ListValue value = new ListValue(getValueWidgets());

        return value;
    }


    @Override
    public void setData(WidgetParam params){
        System.out.println("setting data for list");
        ListParam listParams = (ListParam) params;
        cloneParams = listParams.cloneableWidget;
        setWidgetsInLayout(listParams.currentWidgets);
        makeButton();


        for(int i = 0; i < widgets().size(); i++){
            widgets().get(i).setOnDataChangedListener(new Runnable() {
                @Override
                public void run() {
                    onDataChangedListener.run();
                }
            });
        }

        System.out.println("data finished set for list");
        System.out.println(getData());
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
        public WidgetParam cloneableWidget;
        public ArrayList<WidgetParam> currentWidgets;

        public ListParam(WidgetParam cloneableWidget, ArrayList<WidgetParam> currentWidgets){
            this.widgetClass = "list";
            this.cloneableWidget = cloneableWidget;
            this.currentWidgets = currentWidgets;
        }

        public ListParam(WidgetParam cloneableWidget){
            this.widgetClass = "list";
            this.cloneableWidget = cloneableWidget;
            this.widgetClass = widgetClass;
            currentWidgets = new ArrayList<>();
        }
        public String toString(){
            return "{list" + ", " + cloneableWidget + ", " +currentWidgets + "}";
        }
    }

    public static class ListValue extends WidgetValue{
        public ArrayList<WidgetValue> currentWidgets;
        public ListValue(ArrayList<WidgetValue> currentWidgets){
            this.currentWidgets = currentWidgets;
        }
    }
}
