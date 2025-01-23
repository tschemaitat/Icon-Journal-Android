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

    public final ElementLayout.MarginHelper addWithParam(Element element, int width, int height){
        MarginHelper marginHelper = parentOnAdd(element);
        setDimensionsOfChild(element, width, height);
        onAdd(element);
        return marginHelper;
    }
    public final ElementLayout.MarginHelper addWithParam(int index, Element element, int width, int height){
        MarginHelper marginHelper = parentOnAdd(index, element);
        setDimensionsOfChild(element, width, height);
        onAdd(index, element);
        return marginHelper;
    }
    public final void remove(Element element){
        parentOnRemove(element);
    }
    public final Element remove(int index){
        return parentOnRemove(index);
    }
}
