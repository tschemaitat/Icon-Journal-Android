package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractBasicElement implements Element{
    private Context context;
    private ElementLayout parent;

    public void setBackground(){

    }

    public void setForeground(){

    }

    public void setParent(ElementLayout elementLayout){
        this.parent = elementLayout;
    }

    public ElementLayout getParent(ElementLayout elementLayout){
        return parent;
    }

    protected void removeParent() {
        this.parent = null;
    }

    public abstract View getView();

    public ViewGroup.LayoutParams getLayoutParams(){
        return getView().getLayoutParams();
    }


}
