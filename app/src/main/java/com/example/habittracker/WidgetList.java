package com.example.habittracker;

import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetList extends WidgetGroup implements Widget{

    public WidgetParams cloneParams = null;
    public static final String className = "list";

    public WidgetList(Context context){
        super(context);



    }
    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }


    @Override
    public WidgetParams getData(){
        ListParams params = new ListParams(cloneParams, getDataWidgets());

        return params;
    }

    @Override
    public WidgetValue value(){
        ListValue value = new ListValue(getValueWidgets());

        return value;
    }


    @Override
    public void setData(WidgetParams params){
        System.out.println("setting data for list");
        ListParams listParams = (ListParams) params;
        cloneParams = listParams.cloneableWidget;
        setWidgetsInLayout(listParams.currentWidgets);
        makeButton();


        for(int i = 0; i < widgetsInLayout.size(); i++){
            widgetsInLayout.get(i).setOnDataChangedListener(new Runnable() {
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
        insertAddButtonAtEnd(new View.OnClickListener() {
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


                addWidgetLast(newWidget);
            }
        });
    }

    @Override
    public View getView() {
        return outlineLayout;
    }

    public static class ListParams extends WidgetParams{
        public WidgetParams cloneableWidget;
        public ArrayList<WidgetParams> currentWidgets;

        public ListParams(WidgetParams cloneableWidget, ArrayList<WidgetParams> currentWidgets){
            this.widgetClass = "list";
            this.cloneableWidget = cloneableWidget;
            this.currentWidgets = currentWidgets;
        }

        public ListParams(WidgetParams cloneableWidget){
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
