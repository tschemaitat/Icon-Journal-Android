package com.example.habittracker;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class WidgetLayout {
    private ArrayList<Widget> widgets = new ArrayList<>();
    private LinLayout layout;
    private Context context;
    public WidgetLayout(Context context){
        this.context = context;
        layout = new LinLayout(context);
        layout.getView().setId(R.id.widgetLayout);
    }

    public ArrayList<Widget> widgets(){
        return widgets;
    }


    public ArrayList<Widget> inflateAll(ArrayList<EntryWidgetParam> params){
        System.out.println("setting widgets: " + params.size());
        for(int i = 0; i < params.size(); i++){
            System.out.println("\tadding widget and inflating: ");
            add(GLib.inflateWidget(context, params.get(i)));
        }
        return widgets;
    }

    public Widget inflate(EntryWidgetParam widgetParam){
        Widget widget = GLib.inflateWidget(context, widgetParam);
        add(widget);
        return widget;
    }

    public void add(Widget widget){
        widgets.add(widget);
        layout.add(widget.getView());
    }

    public void remove(Widget widget){
        widgets.remove(widget);
        layout.remove(widget.getView());
    }



    public LinLayout getLinLayout(){
        return layout;
    }

    public View getView() {
        return layout.getView();
    }
}
