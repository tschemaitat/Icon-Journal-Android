package com.example.habittracker.ViewLibrary;

import android.content.Context;

import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class ElementLayout extends AbstractBasicElement{
    private ArrayList<Element> elements = new ArrayList<>();
    private Context context;

    public ElementLayout(Context context){
        this.context = context;
    }

    public final void add(Element element){
        if(element == null)
            throw new RuntimeException();
        if(elements.contains(element))
            throw new RuntimeException();

        elements.add(element);
        onAdd(element);
    }

    protected abstract void onAdd(Element element);

    public final void remove(Element element){
        if(element == null)
            throw new RuntimeException();
        if(!elements.contains(element))
            throw new RuntimeException();
        elements.remove(element);
        onRemove(element);
    }

    protected abstract void onRemove(Element element);

    public final Element getElement(int index){
        return elements.get(index);
    }

    public final int getElementSize(){
        return elements.size();
    }

    public final ArrayList<Element> getElements(){
        return elements;
    }

    public <E extends Element> E getChildCasted(int i) {
        return (E)elements.get(i);
    }
}
