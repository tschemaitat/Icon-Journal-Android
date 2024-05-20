package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;

import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;

public class JournalPage implements Inflatable{
    private Context context;
    private GroupWidget groupWidget;
    private Structure structure;


    public JournalPage(Context context, Structure structureId){
        System.out.println("opening journal");
        this.context = context;
        this.structure = structure;
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
        EntryWidgetParam params = structure.getWidgetParam();
        System.out.println("journal params: \n" + params.hierarchyString(0));
        System.out.println(params);
        //groupWidget = (GroupWidget)GLib.inflateWidget(context, params, onDataChange);
    }

    @Override
    public boolean tryToRemove(Inflatable page) {
        return true;
    }
}
