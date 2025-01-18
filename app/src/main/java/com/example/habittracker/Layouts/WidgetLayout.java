package com.example.habittracker.Layouts;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.Widget;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class WidgetLayout {
    private ArrayList<Widget> widgets = new ArrayList<>();
    private LinearElementLayout layout;
    private Context context;
    public WidgetLayout(Context context){
        this.context = context;
        layout = new LinearElementLayout(context, LinearLayout.VERTICAL);
        layout.getView().setId(R.id.widgetLayout);
    }

    public ArrayList<Widget> widgets(){
        return widgets;
    }


    public ArrayList<Widget> inflateAll(ArrayList<EntryWidgetParam> params, Runnable onDataChange){
        System.out.println("setting widgets: " + params.size());
        for(int i = 0; i < params.size(); i++){
            System.out.println("\tadding widget and inflating: ");
            add(GLib.inflateWidget(context, params.get(i), onDataChange));
        }
        return widgets;
    }

    public void add(Widget widget){
        widgets.add(widget);
        layout.add(widget.getElement());
    }

    public void add(Widget widget, int index){
        widgets.add(index, widget);
        layout.add(index, widget.getElement());
    }

    public void remove(Widget widget){
        widgets.remove(widget);
        layout.remove(widget.getElement());
    }

    public void delete(Widget widget) {
        int index = widgets.indexOf(widget);
        this.remove(widget);
        getLinearElementLayout().remove(widget.getElement());
    }

    public void moveUp(Widget widget) {
        ArrayList<Widget> widgets = this.widgets();
        int index = widgets.indexOf(widget);
        if(index == 0)
            return;
        widgets.remove(widget);
        getLinearElementLayout().remove(widget.getElement());
        widgets.add(index - 1, widget);
        getLinearElementLayout().add(index - 1, widget.getElement());
    }

    public void moveDown(Widget widget) {
        ArrayList<Widget> widgets = this.widgets();
        int index = widgets.indexOf(widget);
        if(index == widgets.size() - 1)
            return;
        widgets.remove(widget);
        layout.remove(widget.getElement());
        widgets.add(index + 1, widget);
        layout.add(index + 1, widget.getElement());
    }



    public LinearElementLayout getLinearElementLayout(){
        return layout;
    }

    public Element getElement() {
        return layout;
    }
}
