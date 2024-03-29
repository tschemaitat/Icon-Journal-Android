package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.ViewWrapper;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;

public abstract class EntryWidget implements Widget{
    private Runnable onDataChanged;
    private String name;
    private ViewWrapper viewWrapper;

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
    public abstract DataTree getDataTree();

    public abstract EntryWidgetParam getParam();

    public abstract void setValue(DataTree dataTree);

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
        if(param.name != null)
            setName(param.name);
        setParamCustom(param);
    }

    protected abstract void setParamCustom(EntryWidgetParam params);

    public final View getView(){
        return viewWrapper.getView();
    }


}
