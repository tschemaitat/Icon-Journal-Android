package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Widgets.CustomEditText;

public class StructureWidgetHeaderView {
    public RelativeLayout relativeLayout;
    public CustomEditText nameEditor;
    public ImageButton deleteButton;
    public View upButton;
    public View downButton;
    private Context context;
    public StructureWidgetHeaderView(Context context, Runnable onTextChange, Runnable onDelete, Runnable moveUp, Runnable moveDown){
        this.context = context;
        relativeLayout = new RelativeLayout(context);
        Margin.setStructureWidgetHeader(this);
        addNameEditor(null, onTextChange);
        addDeleteButton(onDelete);
        addMoveButtons(moveUp, moveDown);
    }

    private void addMoveButtons(Runnable moveUp, Runnable moveDown) {
        int size = 100;
        int horMargin = 20;


        downButton = new View(context);
        downButton.setId(View.generateViewId());
        downButton.setBackground(GLib.downArrow);
        downButton.setOnClickListener(view -> moveDown.run());
        RelativeLayout.LayoutParams downParam = new RelativeLayout.LayoutParams(size, size);
        //downParam.addRule(RelativeLayout.LEFT_OF, upButton.getId());
        downParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        downParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        downParam.setMargins(horMargin, 0, horMargin, 0);
        downButton.setLayoutParams(downParam);
        relativeLayout.addView(downButton);

        upButton = new View(context);
        upButton.setId(View.generateViewId());
        upButton.setBackground(GLib.upArrow);
        upButton.setOnClickListener(view -> moveUp.run());
        RelativeLayout.LayoutParams upParam = new RelativeLayout.LayoutParams(size, size);
        //upParam.addRule(RelativeLayout.LEFT_OF, deleteButton.getId());
        upParam.addRule(RelativeLayout.RIGHT_OF, downButton.getId());
        upParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upParam.setMargins(horMargin, 0, 2*horMargin, 0);
        upButton.setLayoutParams(upParam);
        relativeLayout.addView(upButton);

        ((RelativeLayout.LayoutParams) nameEditor.getView().getLayoutParams()).addRule(RelativeLayout.BELOW, downButton.getId());
    }

    private void addNameEditor(String name, Runnable onTextChange){
        nameEditor = new CustomEditText(context);
        nameEditor.setHint("widget name");
        if(name != null)
            nameEditor.setText(name);

        relativeLayout.addView(nameEditor.getView());
        nameEditor.setOnDataChangedListener(()->onTextChange.run());
        Margin.setStructureWidgetHeader(this);
        nameEditor.getView().setId(View.generateViewId());
    }

    private void addDeleteButton(Runnable runnable){
        deleteButton = (ImageButton) GLib.inflate(R.layout.delete_button);
        //deleteButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        deleteButton.setId(View.generateViewId());
        relativeLayout.addView(deleteButton);

        deleteButton.setOnClickListener((view)->runnable.run());
        Margin.setStructureWidgetHeader(this);

    }

    public View getView(){
        return relativeLayout;
    }
}
