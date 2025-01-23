package com.example.habittracker.Layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.Widget;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class WidgetLayout extends ElementLayout {
    private ArrayList<Widget> widgets = new ArrayList<>();
    private LinearElementLayout layout;
    private Context context;
    public WidgetLayout(Context context){
        super(context);
        this.context = context;
        layout = new LinearElementLayout(context, LinearLayout.VERTICAL);
        layout.getView().setId(R.id.widgetLayout);
    }

    @Override
    protected ViewGroup.LayoutParams createLayoutParams() {
        return new LinearLayout.LayoutParams(-10, -10);
    }

    @Override
    public ViewGroup getViewGroup() {
        return layout.getViewGroup();
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
        parentOnAdd(widget.getElement());
    }
    @Override
    protected void onAdd(Element element) {
        layout.add(element);
    }
    public void add(Widget widget, int index){
        widgets.add(index, widget);
        parentOnAdd(index, widget.getElement());
    }
    @Override
    protected void onAdd(int index, Element element) {
        layout.add(index, element);
    }

    public void remove(Widget widget){
        if(!widgets.contains(widget))
            throw new RuntimeException();
        widgets.remove(widget);
        parentOnRemove(widget.getElement());
    }
    @Override
    protected void onRemove(Element element) {
        layout.remove(element);
    }

    public void delete(Widget widget) {
        int index = widgets.indexOf(widget);
        this.remove(widget);
        getLinearElementLayout().remove(widget.getElement());
    }
    @Override
    protected void onRemove(int index, Element removedElement) {

    }

    public void moveUp(Widget widget) {
        ArrayList<Widget> widgets = this.widgets();
        int index = widgets.indexOf(widget);
        if(index == 0)
            throw new RuntimeException();
        widgets.remove(widget);
        getLinearElementLayout().remove(widget.getElement());
        widgets.add(index - 1, widget);
        getLinearElementLayout().add(index - 1, widget.getElement());
        parentOnMoveUp(widget.getElement());
    }

    public void moveDown(Widget widget) {
        ArrayList<Widget> widgets = this.widgets();
        int index = widgets.indexOf(widget);
        if(index == widgets.size() - 1)
            throw new RuntimeException();
        widgets.remove(widget);
        layout.remove(widget.getElement());
        widgets.add(index + 1, widget);
        layout.add(index + 1, widget.getElement());
        parentOnMoveDown(widget.getElement());
    }

    @Override
    protected void enableInteractionLayout() {
        layout.enable();

    }

    @Override
    protected void disableInteractionLayout() {
        layout.disableWithoutGray();
    }

    public LinearElementLayout getLinearElementLayout(){
        return layout;
    }

    public Element getElement() {
        return layout;
    }

    public View getView() {
        return layout.getView();
    }

    public void resetNameColor() {
        for(Widget widget: widgets){
            widget.
        }
    }
}
