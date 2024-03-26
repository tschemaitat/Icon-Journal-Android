package com.example.habittracker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class CustomLinearLayout {
    private Context context;


    private ConstraintLayout outlineLayout;
    private LinearLayout widgetLayout;
    private LinearLayout buttonAndWidgetLayout;
    private RelativeLayout nameLayout;
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
        widgetLayout.setId(R.id.editorSelectionLayout);
        buttonAndWidgetLayout.addView(widgetLayout);


    }

    public void addBorder(){
        Drawable drawable = AppCompatResources.getDrawable(context, R.drawable.blue_outline);
        outlineLayout.setBackground(drawable);
    }

    public void addName(String name){
        addNameLayout();
        if(nameTextView == null){
            nameTextView = new TextView(context);
            nameTextView.setText(name);
            nameLayout.addView(nameTextView, 0);

            int margin = GLib.dpToPx(context, 20);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
            layoutParams.setMargins(40, 0, 0, 10);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            nameTextView.setLayoutParams(layoutParams);
            return;
        }

        nameTextView.setText(name);

    }

    public void setNameColor(int color){
        nameTextView.setTextColor(color);
    }

    private View nameEditor;
    public static final int nameEditorRightMargin = 120;
    public void addNameEditor(View view){
        addNameLayout();

        nameLayout.addView(view, 0);

        int margin = GLib.dpToPx(context, 20);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        view.setLayoutParams(layoutParams);
        view.setPadding(1000, verticalMargin, nameEditorRightMargin, verticalMargin);
        nameEditor = view;
        return;

    }

    public void setMargin(int dpHor, int dpVert){

        verticalMargin =  GLib.dpToPx(context, dpVert);;
        horizontalMargin =  GLib.dpToPx(context, dpHor);;
        for(View view: views){
            setLayoutParams(view);
        }
        if(nameEditor != null){
            nameEditor.setPadding(horizontalMargin, verticalMargin, nameEditorRightMargin, verticalMargin);
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
        view.setLayoutParams(linearLayoutParams());
    }

    public void addButton(View.OnClickListener listener){
        addNameLayout();
        hasButton = true;
        addButton = GLib.getButton(listener, context);
        setLayoutParams(addButton);
        buttonAndWidgetLayout.addView(addButton);
    }

    public void addDeleteButton(Runnable runnable){
        addNameLayout();
        ImageButton deleteButton = (ImageButton) GLib.inflate(R.layout.delete_button);
        //deleteButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        nameLayout.addView(deleteButton);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        layoutParams.setMargins(20,20,20,20);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        deleteButton.setLayoutParams(layoutParams);
        deleteButton.setOnClickListener((view)->runnable.run());
    }

    public void addNameLayout(){
        if(nameLayout == null){
            nameLayout = new RelativeLayout(context);
            nameLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
            nameLayout.setMinimumWidth(2000);
            buttonAndWidgetLayout.addView(nameLayout, 0);
        }
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
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
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

    public LinearLayout.LayoutParams linearLayoutParams(){

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(horizontalMargin, verticalMargin, rightMargin, verticalMargin);
        return layoutParams;
    }

    public View getView() {
        return outlineLayout;
    }

}
