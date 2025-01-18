package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public abstract class Element {
    private ElementLayout parent;

    public void setBackground(Drawable drawable){
        getView().setBackground(drawable);
    }

    public void setForeground(Drawable drawable){
        getView().setForeground(drawable);
    }

    public void setLayoutParams(ViewGroup.LayoutParams params){
        getView().setLayoutParams(params);
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
