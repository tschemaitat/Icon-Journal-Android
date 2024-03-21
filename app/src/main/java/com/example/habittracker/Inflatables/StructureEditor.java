package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.StructureWidget;
import com.example.habittracker.WidgetLinearLayout;

public class StructureEditor implements Inflatable {


    Context context;
    WidgetLinearLayout widgetLinearLayout;


    public StructureEditor(Context context, Structure structure){
        this.context = context;
        widgetLinearLayout = new WidgetLinearLayout(context);

        widgetLinearLayout.insertButton(view -> {
            StructureWidget structureWidget = new StructureWidget(context);
            widgetLinearLayout.addWidget(structureWidget);
        });
    }


    @Override
    public View getView() {
        return widgetLinearLayout.getView();
    }
}
