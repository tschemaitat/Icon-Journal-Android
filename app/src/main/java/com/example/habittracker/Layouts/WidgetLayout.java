package com.example.habittracker.Layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementLayout;
import com.example.habittracker.ViewLibrary.ElementProvider;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class WidgetLayout<T extends ElementProvider> extends ElementLayout {
    private ArrayList<T> widgets = new ArrayList<>();
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





    public ArrayList<T> widgets(){
        return widgets;
    }


//    public ArrayList<Widget> inflateAll(ArrayList<EntryWidgetParam> params, Runnable onDataChange){
//        System.out.println("setting widgets: " + params.size());
//        for(int i = 0; i < params.size(); i++){
//            System.out.println("\tadding widget and inflating: ");
//            add(GLib.inflateWidget(context, params.get(i), onDataChange));
//        }
//        return widgets;
//    }



    public void add(T widget){
        this.debugMessages.add("add: " + widget.getElement().getView().getId());
        widgets.add(widget);
        parentOnAdd(widget.getElement());
    }
    @Override
    protected void onAdd(Element element) {

        MainActivity.log("on add widget layout");
        layout.add(element);
    }
    public void add(T widget, int index){
        this.debugMessages.add("index: " + index + "add: " + widget.getElement().getView().getId());
        widgets.add(index, widget);
        parentOnAdd(index, widget.getElement());
    }
    @Override
    protected void onAdd(int index, Element element) {
        layout.add(index, element);
    }

    public void remove(T widget){
        this.debugMessages.add("remove: " + widget.getElement().getView().getId());
        debugMessages.add("widgets: " + widgets.get(0).getElement().getView().getId());
        if(!widgets.contains(widget))
            throw new RuntimeException();
        parentOnRemove(widget.getElement());
    }
    @Override
    protected void onRemove(Element element) {
        layout.remove(element);
    }

    public void delete(T widget) {
        this.debugMessages.add("delete: " + widget.getElement().getView().getId());
        int index = widgets.indexOf(widget);
        this.remove(widget);
        getLinearElementLayout().remove(widget.getElement());
    }
    @Override
    protected void onRemove(int index, Element removedElement) {

    }

    public void moveUp(T widget) {
        this.debugMessages.add("moveUp: " + widget.getElement().getView().getId());
        int index = widgets.indexOf(widget);
        if(index == 0)
            throw new RuntimeException();
        widgets.remove(widget);
        getLinearElementLayout().remove(widget.getElement());
        widgets.add(index - 1, widget);
        getLinearElementLayout().add(index - 1, widget.getElement());
        parentOnMoveUp(widget.getElement());
    }

    public void moveDown(T widget) {
        this.debugMessages.add("moveDown: " + widget.getElement().getView().getId());
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

    public LinearElementLayout getElement() {
        return layout;
    }

    public View getView() {
        return layout.getView();
    }
}
