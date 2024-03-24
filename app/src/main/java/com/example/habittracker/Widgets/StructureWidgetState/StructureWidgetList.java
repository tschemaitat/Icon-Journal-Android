package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.StructureWidget;
import com.example.habittracker.Widgets.EntryWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureWidgetList implements Widget {
    private Context context;
    private GroupWidget groupWidget;
    private ArrayList<StructureWidget> structureWidgets = new ArrayList<>();
    private GroupWidget parent;
    public StructureWidgetList(Context context, GroupWidget parent) {
        this.context = context;
        this.parent = parent;
        groupWidget = new GroupWidget(context);
        parent.addWidget(groupWidget);
        init();
    }

    private void init(){
        groupWidget.insertButton(view -> {
            StructureWidget structureWidget = new StructureWidget(context);
            structureWidgets.add(structureWidget);
            groupWidget.addWidget(structureWidget);
        });
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    @Override
    public EntryWidgetParam getParam() {
        return new ListWidget.ListParam(null, (GroupWidget.GroupWidgetParam) groupWidget.getParam(), new ArrayList<>());
    }

    @Override
    public void setParam(EntryWidgetParam params) {

    }

    @Override
    public View getView() {
        return null;
    }
}
