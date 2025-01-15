package com.example.habittracker.Inflatables;

import android.view.View;

public abstract class Inflatable {
    private boolean opened = false;
    public void remove(){
        if(!opened){
            throw new RuntimeException();
        }
        opened = false;
        onRemoved();
    }
    public void open(){
        if(opened){
            throw new RuntimeException();
        }
        opened = true;
        onOpened();
    }

    public boolean canRemove(Inflatable page){
        return canRemoveImpl(page);
    }

    public abstract View getView();
    protected abstract void onRemoved();
    protected abstract void onOpened();
    public abstract boolean canRemoveImpl(Inflatable page);
}
