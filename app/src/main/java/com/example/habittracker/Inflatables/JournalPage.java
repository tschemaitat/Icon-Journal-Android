package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;

import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;

public class JournalPage extends Inflatable{
    private Context context;
    private GroupWidget groupWidget;
    private Structure structure;


    public JournalPage(Context context, Structure structureId){
        System.out.println("opening journal");
        this.context = context;
        this.structure = structure;
        groupWidget = new GroupWidget(context, new ViewWrapper(context), null);


    }

    @Override
    public View getView() {
        return groupWidget.getElement().getView();
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
    public boolean canRemoveImpl(Inflatable page) {
        return true;
    }
}
