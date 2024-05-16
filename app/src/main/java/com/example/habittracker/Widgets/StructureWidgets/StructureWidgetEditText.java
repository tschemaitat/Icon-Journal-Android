package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;

public class StructureWidgetEditText implements Widget {
    private Context context;
    private LinLayout layout;
    private LinLayout parent;
    public StructureWidgetEditText(Context context, LinLayout parent) {
        this.context = context;
        this.parent = parent;
        layout = new LinLayout(context);
        parent.add(layout.getView());
        init();
    }

    private void init(){

    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    public EntryWidgetParam getParam() {
        EditTextParam editTextParam = new EditTextParam((String) null);
        return editTextParam;
    }




    @Override
    public void setParam(EntryWidgetParam params) {

    }

    @Override
    public View getView() {
        return null;
    }
}
