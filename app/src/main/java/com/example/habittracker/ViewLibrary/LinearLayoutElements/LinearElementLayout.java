package com.example.habittracker.ViewLibrary.LinearLayoutElements;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.ViewLibrary.Element;

import com.example.habittracker.ViewWidgets.CheckBoxElement;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class LinearElementLayout implements Element{
    private Context context;
    private ArrayList<Element> elements = new ArrayList<>();
    private LinearLayout linearLayout;
    public LinearElementLayout(Context context, int orientationCode){
        this.context = context;
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientationCode);
    }

    public void addWithParam(Element element, int width, int height){
        View elementView = element.getView();
        if(elementView.getLayoutParams() != null)
            throw new RuntimeException();
        elementView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        linearLayout.addView(element.getView());
        elements.add(element);
    }

    public void add(Element element){
        elements.add(element);
        linearLayout.addView(element.getView());
    }

    public LinearLayout getLinearLayout(){
        return linearLayout;
    }
    @Override
    public View getView(){
        return linearLayout;
    }

    public <E extends Element> E getChildCasted(int i) {
        return (E)elements.get(i);
    }
}
