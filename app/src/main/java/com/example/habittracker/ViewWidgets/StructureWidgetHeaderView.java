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
    private Context context;
    public StructureWidgetHeaderView(Context context){
        this.context = context;
        relativeLayout = new RelativeLayout(context);
        Margin.setStructureWidgetHeader(this);
    }

    public void addNameEditor(String name, Runnable onTextChange){
        nameEditor = new CustomEditText(context);
        nameEditor.setHint("widget name");
        if(name != null)
            nameEditor.setText(name);

        relativeLayout.addView(nameEditor.getView());
        nameEditor.setOnDataChangedListener(()->onTextChange.run());
        Margin.setStructureWidgetHeader(this);
    }

    public void addDeleteButton(Runnable runnable){
        deleteButton = (ImageButton) GLib.inflate(R.layout.delete_button);
        //deleteButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        relativeLayout.addView(deleteButton);

        deleteButton.setOnClickListener((view)->runnable.run());
        Margin.setStructureWidgetHeader(this);
    }

    public View getView(){
        return relativeLayout;
    }
}
