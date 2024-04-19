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
    private EntryWidgetParam entryWidgetParam;
    private ViewWrapper viewWrapper;
    private boolean dataSet = false;
    private Integer widgetIdTracker;


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
        return new WidgetId(widgetIdTracker, entryWidgetParam.getStructure());
    }

    protected abstract void setValueCustom(WidgetValue widgetValueString);

    public final void setValue(WidgetValue widgetValueString){
        setValueCustom(widgetValueString);
    }

    public final void setName(String name){
        //System.out.println("set name: " + name);
        viewWrapper.setName(name);
    }



    public final ViewWrapper getViewWrapper(){
        return viewWrapper;
    }



    public final String getName(){
        return entryWidgetParam.name;
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



    public Structure getStructure(){
        return entryWidgetParam.getStructure();
    }
}
