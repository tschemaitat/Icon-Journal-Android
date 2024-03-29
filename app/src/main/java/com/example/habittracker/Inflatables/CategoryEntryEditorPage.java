package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.Structs.Entry;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.GroupWidget;

public class CategoryEntryEditorPage implements Inflatable{
    private Context context;
    private Structure structure;
    private Entry entry;
    private GroupWidget groupWidget;
    private LinearLayout linearLayout;
    public CategoryEntryEditorPage(Context context, Structure category, Entry entry){
        this.context = context;
        this.structure = category;
        this.entry = entry;
        linearLayout = new LinearLayout(context);
    }

    public void onDataChanged(){
        System.out.println("new data: \n" + groupWidget.getDataTree());
    }


    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {
        groupWidget = new GroupWidget(context);
        groupWidget.setOnDataChangedListener(()->onDataChanged());
        linearLayout.addView(groupWidget.getView());
        groupWidget.setParam(structure.getParam());

        if(entry != null)
            groupWidget.setValue(entry.dataTree);
    }
}
