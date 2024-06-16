package com.example.habittracker.ViewLibrary.ScrollElements;

import android.content.Context;
import android.view.View;

import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewWidgets.LockableScrollView;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class ScrollElement {

    private Context context;
    private LockableScrollView lockableScrollView;
    private LinearElementLayout linearElementLayout;
    private ArrayList<Element> children = new ArrayList<>();

    public ScrollElement(Context context, int orientationCode) {
        this.context = context;
        lockableScrollView = new LockableScrollView(context);
        linearElementLayout = new LinearElementLayout(context, orientationCode);
        lockableScrollView.addView(linearElementLayout.getLinearLayout());
    }

    public LockableScrollView getLockableScrollView(){
        return lockableScrollView;
    }

    public void addWithParam(Element element, int width, int height){
        linearElementLayout.addWithParam(element, width, height);
        children.add(element);
    }

    public void add(Element element){
        linearElementLayout.add(element);
        children.add(element);
    }

    public View getView(){
        return lockableScrollView;
    }

    public <E extends Element> ArrayList<E> getChildrenCasted() {
        ArrayList<E> result = new ArrayList<>();
        for(Element element: children){
            result.add((E) element);
        }
        return result;
    }
}
