package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.StructureWidget;
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
    public WidgetParam getData() {
        return new ListWidget.ListParam((GroupWidget.GroupWidgetParam) groupWidget.getData(), new ArrayList<>());
    }

    @Override
    public WidgetValue value() {
        return null;
    }

    @Override
    public void setData(WidgetParam params) {

    }

    @Override
    public View getView() {
        return null;
    }
}
