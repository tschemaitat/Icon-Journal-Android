package com.example.habittracker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class CustomLinearLayout {
    private Context context;


    private ConstraintLayout outlineLayout;
    private LinearLayout widgetLayout;
    private LinearLayout buttonAndWidgetLayout;
    private View addButton = null;
    private TextView nameTextView;
    ArrayList<View> views = new ArrayList<>();

    boolean hasButton = false;

    private int horizontalMargin = 0;
    private int verticalMargin = 0;

    private int rightMargin = 5;

    public CustomLinearLayout(Context context){
        this.context = context;
        outlineLayout = new ConstraintLayout(context);

        buttonAndWidgetLayout = createOuterLayout();
        buttonAndWidgetLayout.setId(R.id.nameButtonWidgetLayout);
        outlineLayout.addView(buttonAndWidgetLayout);

        widgetLayout = createWidgetLayout();
        widgetLayout.setId(R.id.widgetLayout);
        buttonAndWidgetLayout.addView(widgetLayout);


    }

    public void addBorder(){
        Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.blue_outline);
        outlineLayout.setBackground(drawable);
    }

    public void addName(String name){
        if(nameTextView == null){
            nameTextView = new TextView(context);
            nameTextView.setText(name);
            buttonAndWidgetLayout.addView(nameTextView, 0);

            int margin = GLib.dpToPx(context, 20);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(40, 0, 0, 10);
            nameTextView.setLayoutParams(layoutParams);
            return;
        }

        nameTextView.setText(name);

    }

    public void setMargin(int dpHor, int dpVert){
        verticalMargin = dpVert;
        horizontalMargin = dpHor;
        for(View view: views){
            setLayoutParams(view);
        }
    }

    public void add(View view){
        widgetLayout.addView(view);
        views.add(view);
        setLayoutParams(view);

    }

    public void remove(View view){
        if(view == null)
            return;
        views.remove(view);
        widgetLayout.removeView(view);
    }

    public void setLayoutParams(View view){
        view.setLayoutParams(linearLayoutParams(verticalMargin, horizontalMargin));
    }

    public void insertButton(View.OnClickListener listener){
        hasButton = true;
        addButton = GLib.getButton(listener, context);
        buttonAndWidgetLayout.addView(addButton);
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
        linearLayout.setLayoutParams(linearLayoutParams(0, 0));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    private LinearLayout createOuterLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(constraintLayoutParams(0, 0));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    public ConstraintLayout.LayoutParams constraintLayoutParams(int dpVert, int dpHor){
        int vert = GLib.dpToPx(context, dpVert);
        int hor = GLib.dpToPx(context, dpHor);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);

        layoutParams.setMargins(hor, vert, 0, vert);
        return layoutParams;
    }

    public LinearLayout.LayoutParams linearLayoutParams(int dpVert, int dpHor){
        int vert = GLib.dpToPx(context, dpVert);
        int hor = GLib.dpToPx(context, dpHor);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(hor, vert, rightMargin, vert);
        return layoutParams;
    }

    public View getView() {
        return outlineLayout;
    }

}
