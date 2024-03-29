package com.example.habittracker.Widgets.StructureWidgetState;

import android.content.Context;
import android.view.View;

import com.example.habittracker.LinLayout;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;

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

    @Override
    public EntryWidgetParam getParam() {
        CustomEditText.EditTextParam editTextParam = new CustomEditText.EditTextParam(null, "null");
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
