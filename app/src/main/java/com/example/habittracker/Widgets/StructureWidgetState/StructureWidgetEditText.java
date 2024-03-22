package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

public class StructureWidgetEditText implements Widget {
    private Context context;
    private GroupWidget groupWidget;
    private GroupWidget parent;
    public StructureWidgetEditText(Context context, GroupWidget parent) {
        this.context = context;
        this.parent = parent;
        groupWidget = new GroupWidget(context);
        parent.addWidget(groupWidget);
        init();
    }

    private void init(){

    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    @Override
    public WidgetParam getData() {
        CustomEditText.EditTextParam editTextParam = new CustomEditText.EditTextParam("null");
        return editTextParam;
    }

    @Override
    public WidgetValue value() {
        return null;
    }

    @Override
    public DataTree getDataTree() {
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
