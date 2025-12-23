package com.example.habittracker.ViewLibrary;

import static com.example.habittracker.MainActivity.context;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittracker.R;

public abstract class Element {
    private boolean setDisabled = false;
    private boolean interactionDisabled = false;
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
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Element otherElement))
            return false;

        if(this.getView().getId() == otherElement.getView().getId())
            return true;
        return false;
    }

    public void disableWithoutGray(){
        disableCheck();
        setDisabled = true;
        tryDisableInteraction();
    }
    public void disableWithGray(){
        disableCheck();
        setDisabled = true;
        setForeGroundGray();
        tryDisableInteraction();
    }
    private void disableCheck(){
        if(setDisabled){
            throw new RuntimeException();
        }
    }


    private void setForeGroundGray(){
        setForeground(context.getDrawable(R.drawable.rounded_foreground_inset));
    }
    public void enable(){
//        if(!setDisabled){
//            throw new RuntimeException();
//        }
        tryEnableInteraction();
    }
    protected void tryEnableInteraction(){
        //if nothing would change don't call
        if(!interactionDisabled)
            return;
        //if its supposed to stay disabled don't call
        if(setDisabled)
            return;
        enableInteraction();
    }
    protected void tryDisableInteraction(){
        //if nothing would change don't call
        if(interactionDisabled)
            return;
        //don't check setDisabled because it should be disabled if parent is
        disableInteraction();
    }


    protected abstract void enableInteraction();
    protected abstract void disableInteraction();



}
