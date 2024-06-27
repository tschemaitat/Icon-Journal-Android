package com.example.habittracker.ViewLibrary.LinearLayoutElements;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.habittracker.ViewLibrary.Element;

import com.example.habittracker.ViewLibrary.ElementLayout;
import com.example.habittracker.ViewWidgets.CheckBoxElement;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class LinearElementLayout extends ElementLayout {
    private Context context;
    private LinearLayout linearLayout;
    private int orientationCode;
    public LinearElementLayout(Context context, int orientationCode){
        super(context);
        this.context = context;
        this.orientationCode = orientationCode;
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientationCode);
    }

    public void addWithParam(Element element, int width, int height){
        View elementView = element.getView();
//        if(elementView.getLayoutParams() != null)
//            throw new RuntimeException();
        elementView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        add(element);
    }

    @Override
    protected void onAdd(Element element) {
        linearLayout.addView(element.getView());
        ViewGroup.LayoutParams lp = element.getLayoutParams();
        if(orientationCode == LinearLayout.VERTICAL){
            if(lp.height == -1)
                throw new RuntimeException();
        }else{
            if(lp.width == -1)
                throw new RuntimeException();
        }
    }

    @Override
    protected void onRemove(Element element) {
        linearLayout.removeView(element.getView());
    }

    public ArrayList<View> getViewChildren(){
        return getElements().convert((index, element) -> element.getView());
    }

    public void verticalOnMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        final int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        final int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        //measure spec can be at_most, unspecified, or exactly
        getElements().iter(element -> {
            ViewGroup.LayoutParams lp = element.getLayoutParams();
            if(lp.height == -1){
                throw new RuntimeException();
            }
        });


    }

    public LinearLayout getLinearLayout(){
        return linearLayout;
    }
    @Override
    public View getView(){
        return linearLayout;
    }



}
