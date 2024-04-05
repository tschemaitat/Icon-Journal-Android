package com.example.habittracker.Inflatables;

import android.view.View;

public interface Inflatable {
    public View getView();
    public void onRemoved();
    public void onOpened();
    public boolean tryToRemove();
}
