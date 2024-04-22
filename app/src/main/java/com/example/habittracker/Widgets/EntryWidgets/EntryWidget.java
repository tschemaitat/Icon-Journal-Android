package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.example.habittracker.Layouts.ViewWrapper;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.structures.WidgetId;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.structures.Structure;

public abstract class EntryWidget implements Widget {
    private Runnable onDataChanged;
    private EntryWidgetParam entryWidgetParam;
    private ViewWrapper viewWrapper;
    private boolean dataSet = false;
    private Integer widgetIdTracker;
    private boolean enabled = true;
    private Context context;


    public EntryWidget(Context context){
        this.context = context;

        viewWrapper = new ViewWrapper(context);
        viewWrapper.getView().setId(R.id.entryWidgetWrapper);
    }

    public void setOnDataChangedListener(Runnable runnable){
        if(runnable == null)
            throw new RuntimeException();
        this.onDataChanged = runnable;
    }

    public void disable(){
        if(!enabled)
            throw new RuntimeException();
        enabled = false;
        getView().setForeground(new ColorDrawable(ColorPalette.disableForeground));
        disableCustom();
    }

    public void enable(){
        if(enabled)
            throw new RuntimeException();
        enabled = true;
        getView().setForeground(null);
        enableCustom();
    }

    protected abstract void disableCustom();

    protected abstract void enableCustom();



    protected final void setViewWrapperChild(View view){
        viewWrapper.setChildView(view);
    }

    public final Runnable onDataChangedListener(){
        return onDataChanged;
    }
    protected abstract WidgetValue getEntryValueTreeCustom();

    public final WidgetValue getValue(){
        WidgetValue tree = getEntryValueTreeCustom();

        return tree;
    }

    public WidgetId getWidgetId(){
        return entryWidgetParam.getWidgetId();
    }

    protected abstract void setValueCustom(WidgetValue widgetValue);

    public final void setValue(WidgetValue widgetValue){
        setValueCustom(widgetValue);
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
        this.entryWidgetParam = param;
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
