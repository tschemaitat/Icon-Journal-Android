package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Widgets.CustomEditText;

public class StructureWidgetHeaderView {
    public RelativeLayout relativeLayout;
    public CustomEditText nameEditor;
    private Context context;
    public StructureWidgetHeaderView(Context context){
        this.context = context;
        relativeLayout = new RelativeLayout(context);
    }

    public void addNameEditor(String name, Runnable onTextChange){
        nameEditor = new CustomEditText(context);
        nameEditor.setHint("widget name");
        if(name != null)
            nameEditor.setText(name);
        nameEditor.getView().setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
        relativeLayout.addView(nameEditor.getView());
        nameEditor.setOnDataChangedListener(()->onTextChange.run());
    }

    public void addDeleteButton(Runnable runnable){
        ImageButton deleteButton = (ImageButton) GLib.inflate(R.layout.delete_button);
        //deleteButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        relativeLayout.addView(deleteButton);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        layoutParams.setMargins(20,20,20,20);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        deleteButton.setLayoutParams(layoutParams);
        deleteButton.setOnClickListener((view)->runnable.run());
    }

    public View getView(){
        return relativeLayout;
    }
}
