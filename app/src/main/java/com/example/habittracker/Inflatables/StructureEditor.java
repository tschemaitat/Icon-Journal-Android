package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Dictionary;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.StructureWidget;

public class StructureEditor implements Inflatable {


    Context context;
    GroupWidget groupWidget;
    Structure structure;


    public StructureEditor(Context context, String structureKey){
        this.context = context;
        if(structureKey.equals("null")){
            structure = new Structure();
        }else{
            structure = Dictionary.getStructure(structureKey);
        }
        groupWidget = new GroupWidget(context);


        groupWidget.insertButton(view -> {
            StructureWidget structureWidget = new StructureWidget(context);
            structureWidget.setOnDataChangedListener(()->{});
            groupWidget.addWidget(structureWidget);
        });
    }


    @Override
    public View getView() {
        return groupWidget.getView();
    }

    @Override
    public void onRemoved() {
        System.out.println("structure editor being removed");
        WidgetParam widgetParam = groupWidget.getData();
        System.out.println("exporting widgetParams: \n" + widgetParam.hierarchyString(0));
        Structure newStructure = new Structure(structure.getName(), widgetParam, structure.getType());
        Dictionary.saveStructure(newStructure);
    }

    @Override
    public void onOpened() {

    }
}
