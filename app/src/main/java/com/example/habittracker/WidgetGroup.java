package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class WidgetGroup implements Widget{
    Context context;


    public ConstraintLayout outlineLayout;
    public LinearLayout widgetLayout;
    private LinearLayout outerLinearLayout;
    ArrayList<Widget> widgetsInLayout = new ArrayList<>();

    int margin = 5;

    public WidgetGroup(Context context){
        this.context = context;
        outlineLayout = GLib.createOutlinedMarginedLayout(context);

        outerLinearLayout = createOuterLayout();
        outlineLayout.addView(outerLinearLayout);

        widgetLayout = createWidgetLayout();
        outerLinearLayout.addView(widgetLayout);


    }



    public void addWidget(Widget widget){
        addWidgetLast(widget);

//        widgetLayout.addView(widget.getView());
//        widgetsInLayout.add(widget);
//        setParamsWidget(widget);
    }
    boolean hasButton = false;
    public void removeAddButton(){
        hasButton = false;
        outerLinearLayout.removeView(addButton);
    }

    public void addWidgetLast(Widget widget){
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
    ConstraintLayout addButton = null;
    public void insertAddButtonAtEnd(View.OnClickListener listener){
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
    private Runnable onDataChangedListener = null;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public WidgetParams getData() {
        ArrayList<WidgetParams> values = new ArrayList<>();
        for(Widget widget: widgetsInLayout)
            values.add(widget.getData());
        return new WidgetGroupParams(values);
    }

    @Override
    public WidgetValue value() {
        ArrayList<WidgetValue> values = new ArrayList<>();
        for(Widget widget: widgetsInLayout)
            values.add(widget.value());
        return new WidgetGroupValue(values);
    }

    @Override
    public void setData(WidgetParams params) {
        WidgetGroupParams groupParams = (WidgetGroupParams) params;
        for(WidgetParams widgetParams: groupParams.params){
            addWidget(GLib.inflateWidget(context, widgetParams));
        }
    }

    @Override
    public View getView() {
        return outlineLayout;
    }

    public static class WidgetGroupParams extends WidgetParams{
        ArrayList<WidgetParams> params;
        public WidgetGroupParams(ArrayList<WidgetParams> params){
            this.params = params;
            this.widgetClass = "widget group";
        }
    }
    public static class WidgetGroupValue extends WidgetValue{
        ArrayList<WidgetValue> values;
        public WidgetGroupValue(ArrayList<WidgetValue> values){
            this.values = values;
        }
    }
}
