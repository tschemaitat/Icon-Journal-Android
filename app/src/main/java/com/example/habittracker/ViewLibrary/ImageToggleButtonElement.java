package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.habittracker.defaultImportPackage.DefaultImportClass;

public class ImageToggleButtonElement extends ButtonElement{
    Drawable onDrawable;
    Drawable offDrawable;
    DefaultImportClass.BooleanListener booleanListener;
    boolean state;
    public ImageToggleButtonElement(Context context, DefaultImportClass.BooleanListener booleanListener, Drawable offDrawable, Drawable onDrawable, boolean startState) {
        super(context, "");
        this.onDrawable = onDrawable;
        this.offDrawable = offDrawable;
        this.booleanListener = booleanListener;
        state = startState;
        setListener(this::onButtonPress);

    }

    public boolean getState(){
        return state;
    }

    private void onButtonPress(){
        state = !state;
        setImage();
        booleanListener.onBoolean(state);
    }

    private void setImage(){
        if(state)
            setBackground(onDrawable);
        else
            setBackground(offDrawable);
    }

    public void setOn(){
        if(state)
            throw new RuntimeException();
        state = true;
        setImage();
    }

    public void setOff(){
        if(state)
            throw new RuntimeException();
        state = false;
        setImage();
    }


}
