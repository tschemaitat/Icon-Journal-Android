package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.VertLayout;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;

public class StructureWidgetEditText implements Widget {
    private Context context;
    private LinearElementLayout layout;
    private LinearElementLayout parent;
    public StructureWidgetEditText(Context context, LinearElementLayout parent) {
        this.context = context;
        this.parent = parent;
        layout = new VertLayout(context);
        parent.add(layout);
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

    @Override
    public Element getElement() {
        return null;
    }
}
