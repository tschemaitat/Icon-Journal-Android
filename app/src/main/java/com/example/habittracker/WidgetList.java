package com.example.habittracker;

import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetList extends WidgetGroup implements Widget{
    public Widget widget;

    public ConstraintLayout addView;

    public WidgetList(Context context){
        super(context);



    }


    @Override
    public Widget widgetClone(){
        System.out.println("cloning: " + this);

        System.out.println("this.getData() = " + this.getData());

        Widget list = new WidgetList(context);
        list.setData(getData());

        return list;
    }
    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }


    @Override
    public WidgetParams getData(){
        ListParams params = new ListParams(widget.getData(), getDataWidgets());

        return params;
    }

    @Override
    public WidgetValue value(){
        ListValue value = new ListValue(getValueWidgets());

        return value;
    }

    public Widget getCloneableWidget(){
        return widget;
    }

    @Override
    public void setData(WidgetParams params){
        ListParams listParams = (ListParams) params;
        widget = GLib.inflateWidget(context, listParams.cloneableWidget);

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

        System.out.println("data finished set: ");
        System.out.println(getData());
    }

    public void makeButton(){
        addView = insertAddButtonAtEnd(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Widget newWidget = null;

                newWidget = getCloneableWidget().widgetClone();
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
            this.cloneableWidget = cloneableWidget;
            this.widgetClass = widgetClass;
            currentWidgets = new ArrayList<>();
        }
    }

    public static class ListValue extends WidgetValue{
        public ArrayList<WidgetValue> currentWidgets;
        public ListValue(ArrayList<WidgetValue> currentWidgets){
            this.currentWidgets = currentWidgets;
        }
    }
}
