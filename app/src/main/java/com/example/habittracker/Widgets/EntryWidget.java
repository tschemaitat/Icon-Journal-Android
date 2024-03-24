package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.CustomLinearLayout;
import com.example.habittracker.DataTree;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;

public abstract class EntryWidget implements Widget{
    private Runnable onDataChanged;
    public String name;
    private CustomLinearLayout entryWidgetWrapper;

    public EntryWidget(Context context){

        entryWidgetWrapper = new CustomLinearLayout(context);
        entryWidgetWrapper.getView().setId(R.id.entryWidgetWrapper);
    }

    public void setOnDataChangedListener(Runnable runnable){
        if(runnable == null)
            throw new RuntimeException();
        this.onDataChanged = runnable;
    }

    public void addBorder(){
        entryWidgetWrapper.addBorder();
    }

    public final void setChild(View view){
        entryWidgetWrapper.add(view);
    }

    public final Runnable onDataChangedListener(){
        return onDataChanged;
    }
    public abstract DataTree getDataTree();

    public abstract EntryWidgetParam getParam();

    public final void setName(String name){
        System.out.println("set name: " + name);
        entryWidgetWrapper.addName(name);
        this.name = name;
    }

    public final void setParam(EntryWidgetParam param){
        if(param.name != null)
            setName(param.name);
        setParamCustom(param);
    }

    protected abstract void setParamCustom(EntryWidgetParam params);

    public final View getView(){
        return entryWidgetWrapper.getView();
    }


}
