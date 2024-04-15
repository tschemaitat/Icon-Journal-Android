package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Layouts.ViewWrapper;
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
    protected abstract EntryValueTree getEntryValueTreeCustom();

    EntryValueTree.ListItemId id = null;
    public final EntryValueTree getEntryValueTree(){
        EntryValueTree tree = getEntryValueTreeCustom();
        tree.setId(id);
        return tree;
    }

    public abstract EntryWidgetParam getParam();

    protected abstract void setValueCustom(EntryValueTree entryValueTree);

    public final void setValue(EntryValueTree entryValueTree){
        id = entryValueTree.getListItemId();
        setValueCustom(entryValueTree);
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
        if(param.name != null)
            setName(param.name);
        setParamCustom(param);
    }

    protected abstract void setParamCustom(EntryWidgetParam params);

    public final View getView(){
        return viewWrapper.getView();
    }


}
