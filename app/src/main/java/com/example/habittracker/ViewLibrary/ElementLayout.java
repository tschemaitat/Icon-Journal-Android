package com.example.habittracker.ViewLibrary;

import android.content.Context;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class ElementLayout extends BasicElement{
    private ArrayList<AbstractBasicElement> elements = new ArrayList<>();
    private Context context;

    public ElementLayout(Context context){
        this.context = context;
    }

    public void addElement(AbstractBasicElement element){
        elements.add(element);
        element.setParent(this);
    }

    public void removeElement(AbstractBasicElement element){
        elements.add(element);
        element.removeParent();
    }

    public AbstractBasicElement getElement(int index){
        return elements.get(index);
    }

    public int getElementSize(){
        return elements.size();
    }
}
