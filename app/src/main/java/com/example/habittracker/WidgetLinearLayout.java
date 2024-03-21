package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class WidgetLinearLayout{
    private Context context;


    private ConstraintLayout outlineLayout;
    private LinearLayout widgetLayout;
    private LinearLayout outerLinearLayout;
    private ArrayList<Widget> widgetsInLayout = new ArrayList<>();
    private ConstraintLayout addButton = null;
    boolean hasButton = false;

    private int margin = 5;

    public WidgetLinearLayout(Context context){
        this.context = context;
        outlineLayout = GLib.createOutlinedMarginedLayout(context);

        outerLinearLayout = createOuterLayout();
        outlineLayout.addView(outerLinearLayout);

        widgetLayout = createWidgetLayout();
        outerLinearLayout.addView(widgetLayout);


    }

    public ArrayList<Widget> widgets(){
        return ((ArrayList<Widget>) widgetsInLayout.clone());
    }

    public void addWidget(Widget widget){
        widgetLayout.addView(widget.getView());
        widgetsInLayout.add(widgetsInLayout.size(), widget);
        setParamsWidget(widget);
    }

    public void removeWidget(Widget widget){
        if(widget == null)
            return;
        widgetLayout.removeView(widget.getView());
        widgetsInLayout.remove(widget);
    }


    public void removeButton(){
        hasButton = false;
        outerLinearLayout.removeView(addButton);
    }

    public boolean hasButton(){
        return hasButton;
    }

    public void setWidgetsInLayout(ArrayList<WidgetParam> params){
        System.out.println("setting widgets: " + params.size());
        for(int i = 0; i < params.size(); i++){
            System.out.println("\tadding widget and inflating: ");
            addWidget(GLib.inflateWidget(context, params.get(i)));
        }
    }

    public ArrayList<WidgetParam> getDataWidgets(){
        ArrayList<WidgetParam> params = new ArrayList<>();
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

    public void insertButton(View.OnClickListener listener){
        hasButton = true;
        addButton = GLib.insertAddButton(listener, outerLinearLayout, context);
    }

    private LinearLayout createWidgetLayout(){

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(linearLayoutParams(margin));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    private LinearLayout createOuterLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(constraintLayoutParams(margin));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    public ConstraintLayout.LayoutParams constraintLayoutParams(int dp){
        int margin = GLib.dpToPx(context, dp);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);

        layoutParams.setMargins(margin, margin, margin, margin);
        return layoutParams;
    }

    public LinearLayout.LayoutParams linearLayoutParams(int dp){
        int margin = GLib.dpToPx(context, dp);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(margin, margin, margin, margin);
        return layoutParams;
    }

    public View getView() {
        return outlineLayout;
    }

}
