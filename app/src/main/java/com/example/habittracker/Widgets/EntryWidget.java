package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.ViewWrapper;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Values.WidgetValueString;
import com.example.habittracker.structures.Structure;

public abstract class EntryWidget implements Widget{
    private Runnable onDataChanged;
    private String name;
    private ViewWrapper viewWrapper;
    private boolean dataSet = false;
    private Integer widgetIdTracker;
    private Structure structure;

    public EntryWidget(Context context){

        viewWrapper = new ViewWrapper(context);
        viewWrapper.getView().setId(R.id.entryWidgetWrapper);
    }

    public void setOnDataChangedListener(Runnable runnable){
        if(runnable == null)
            throw new RuntimeException();
        this.onDataChanged = runnable;
    }

    protected final void setChild(View view){
        viewWrapper.setChildView(view);
    }

    public final Runnable onDataChangedListener(){
        return onDataChanged;
    }
    protected abstract WidgetValue getEntryValueTreeCustom();

    public final WidgetValue getEntryValueTree(){
        WidgetValue tree = getEntryValueTreeCustom();

        return tree;
    }

    public WidgetId getWidgetId(){
        return new WidgetId(widgetIdTracker, structure);
    }

    protected abstract void setValueCustom(WidgetValue widgetValueString);

    public final void setValue(WidgetValue widgetValueString){
        setValueCustom(widgetValueString);
    }

    public final void setName(String name){
        //System.out.println("set name: " + name);
        viewWrapper.setName(name);
        this.name = name;
    }



    public final ViewWrapper getViewWrapper(){
        return viewWrapper;
    }



    public final String getName(){
        return name;
    }

    public final void setParam(EntryWidgetParam param){
        if(dataSet)
            throw new RuntimeException();
        dataSet = true;
        if(param.name != null)
            setName(param.name);
        this.widgetIdTracker = param.widgetIdTracker;
        setParamCustom(param);
    }

    protected abstract void setParamCustom(EntryWidgetParam param);

    public final View getView(){
        return viewWrapper.getView();
    }


    protected Integer getWidgetIdTracker() {
        return widgetIdTracker;
    }

    public final void setStructure(Structure structure) {
        this.structure = structure;
        setStructureCustom(structure);
    }

    public void setStructureCustom(Structure structure){

    }

    public Structure getStructure(){
        return structure;
    }

    public int getValueId(){
        return structure.getHeader().getValueId(widgetIdTracker).getLast();
    }
}
