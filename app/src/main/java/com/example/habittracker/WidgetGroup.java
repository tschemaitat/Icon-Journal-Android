package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class WidgetGroup {
    Context context;


    public ConstraintLayout outlineLayout;
    public LinearLayout widgetLayout;
    ArrayList<Widget> widgetsInLayout = new ArrayList<>();


    public WidgetGroup(Context context){
        this.context = context;
        outlineLayout = GLib.createOutlinedMarginedLayout(context);
        widgetLayout = createWidgetLayout();

        outlineLayout.addView(widgetLayout);
    }



    public void addWidget(Widget widget){
        widgetLayout.addView(widget.getView());
        widgetsInLayout.add(widget);
        setParamsWidget(widget);
    }

    public void addWidgetLast(Widget widget){
        widgetLayout.addView(widget.getView(), widgetLayout.getChildCount() - 1);
        widgetsInLayout.add(widgetsInLayout.size(), widget);
        setParamsWidget(widget);
    }

    public void removeWidget(Widget widget){
        widgetLayout.addView(widget.getView());
        widgetsInLayout.add(widget);
    }

    public void setWidgetsInLayout(ArrayList<WidgetParams> params){
        System.out.println("setting widgets: " + params.size());
        for(int i = 0; i < params.size(); i++){
            System.out.println("\tadding widget and inflating: ");
            addWidget(GLib.inflateWidget(context, params.get(i)));
        }
    }

    public ArrayList<WidgetParams> getDataWidgets(){
        ArrayList<WidgetParams> params = new ArrayList<>();
        for(int i = 0; i < widgetsInLayout.size(); i++){
            params.add(widgetsInLayout.get(i).getData());
        }

        return params;
    }

    public ArrayList<WidgetValue> getValueWidgets(){
        ArrayList<WidgetValue> values = new ArrayList<>();
        for(int i = 0; i < widgetsInLayout.size(); i++){
            values.add(widgetsInLayout.get(i).value());
        }

        return values;
    }


    public void setParamsWidget(Widget widget){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        int margin = GLib.dpToPx(context, 10);
        layoutParams.setMargins(margin, margin, margin, margin);
        widget.getView().setLayoutParams(layoutParams);
    }

    public ConstraintLayout insertAddButtonAtEnd(View.OnClickListener listener){
        return GLib.insertAddButton(listener, widgetLayout, context);
    }

    private LinearLayout createWidgetLayout(){
        int margin = GLib.dpToPx(context, 15);
        LinearLayout linearLayout = new LinearLayout(context);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        layoutParams.setMargins(margin, margin, margin, margin);
        return linearLayout;
    }
}
