package com.example.habittracker;

import android.content.Context;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StructureEditor{

    Context context;
    WidgetGroup widgetGroup;


    public StructureEditor(Context context){
        this.context = context;
        widgetGroup = new WidgetGroup(context);

        widgetGroup.insertAddButtonAtEnd(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StructureWidget structureWidget = new StructureWidget(context);
                widgetGroup.addWidget(structureWidget);
            }
        });
    }

    public ArrayList<StructureWidget> structureWidgets(){
        ArrayList<StructureWidget> structureWidgets = new ArrayList<>();
        for(Widget widget: widgetGroup.widgetsInLayout)
            structureWidgets.add((StructureWidget) widget);
        return structureWidgets;
    }



}
