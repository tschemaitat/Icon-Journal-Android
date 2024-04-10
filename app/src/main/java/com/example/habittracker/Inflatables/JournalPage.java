package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Widgets.GroupWidget;

public class JournalPage implements Inflatable{
    private Context context;
    private GroupWidget groupWidget;
    private StructureId structureId;


    public JournalPage(Context context, StructureId structureId){
        System.out.println("opening journal");
        this.context = context;
        this.structureId = structureId;
        groupWidget = new GroupWidget(context);


    }

    @Override
    public View getView() {
        return groupWidget.getView();
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {
        Structure structure = Dictionary.getStructure(structureId);
        EntryWidgetParam params = structure.getParam();
        System.out.println("journal params: \n" + params.hierarchyString(0));
        System.out.println(params);
        //groupWidget = (GroupWidget)GLib.inflateWidget(context, params, onDataChange);
    }

    @Override
    public boolean tryToRemove(Inflatable page) {
        return true;
    }
}
