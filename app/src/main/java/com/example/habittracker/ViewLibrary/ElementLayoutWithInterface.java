package com.example.habittracker.ViewLibrary;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;

import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class ElementLayoutWithInterface extends ElementLayout{
    public ElementLayoutWithInterface(Context context) {
        super(context);
    }

    public final ElementLayout.MarginHelper add(Element element){
        MarginHelper marginHelper = parentOnAdd(element);
        onAdd(element);
        return marginHelper;
    }
    public final ElementLayout.MarginHelper add(int index, Element element){
        MarginHelper marginHelper = parentOnAdd(index, element);
        onAdd(index, element);
        return marginHelper;
    }
}
