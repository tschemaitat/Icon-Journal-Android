package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Widgets.StructureWidget;

public class TestPage implements Inflatable {
    private LinearLayout linearLayout;
    private Context context;

    public TestPage(Context context){
        this.context = context;
        linearLayout = MainActivity.createVerticalLayout();
        setupTestLayout();
    }

    @Override
    public View getView() {
        return linearLayout;
    }


    public void setupTestLayout(){
        StructureWidget structureWidget = new StructureWidget(context);
        linearLayout.addView(structureWidget.getView());
    }
}
