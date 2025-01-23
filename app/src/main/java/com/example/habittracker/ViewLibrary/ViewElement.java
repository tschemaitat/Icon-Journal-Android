package com.example.habittracker.ViewLibrary;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.view.View;

import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class ViewElement extends Element{

    @Override
    protected void enableInteraction() {
        getView().setEnabled(true);
    }

    @Override
    protected void disableInteraction() {
        getView().setEnabled(false);
    }
}
