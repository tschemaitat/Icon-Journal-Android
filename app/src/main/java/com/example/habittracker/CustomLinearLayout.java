package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomLinearLayout {
    private Context context;


    private ConstraintLayout outlineLayout;
    private LinearLayout widgetLayout;
    private LinearLayout buttonAndWidgetLayout;
    private ConstraintLayout addButton = null;

    boolean hasButton = false;

    private int margin = 5;

    public CustomLinearLayout(Context context){
        this.context = context;
        outlineLayout = GLib.createOutlinedMarginedLayout(context);

        buttonAndWidgetLayout = createOuterLayout();
        outlineLayout.addView(buttonAndWidgetLayout);

        widgetLayout = createWidgetLayout();
        buttonAndWidgetLayout.addView(widgetLayout);


    }

    public void add(View view){
        widgetLayout.addView(view);
        setLayoutParams(view);
    }

    public void remove(View view){
        if(view == null)
            return;
        widgetLayout.removeView(view);
    }

    public void setLayoutParams(View view){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        int margin = GLib.dpToPx(context, 10);
        layoutParams.setMargins(margin, margin, margin, margin);
        view.setLayoutParams(layoutParams);
    }

    public void insertButton(View.OnClickListener listener){
        hasButton = true;
        addButton = GLib.insertAddButton(listener, buttonAndWidgetLayout, context);
    }

    public void removeButton(){
        hasButton = false;
        buttonAndWidgetLayout.removeView(addButton);
    }

    public boolean hasButton(){
        return hasButton;
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
