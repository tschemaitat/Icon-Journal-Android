package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.view.View;

public abstract class AbstractBasicElement implements Element{
    private Context context;
    private ElementShadowDetails elementShadowDetails;
    private ElementDimensions elementDimensions;
    private View extendedView;
    private int cornerRadius;
    private ElementLayout parent;


    public void addClickAnimation(){

    }

    public void setBackground(){

    }

    public void setForeground(){

    }

    public void addShadow(int shadowRadius){
        this.elementShadowDetails = new ElementShadowDetails(shadowRadius);
    }

    public void setCoreView(View view){
        this.extendedView = view;
    }

    public void setParent(ElementLayout elementLayout){
        this.parent = elementLayout;
    }

    public ElementLayout getParent(ElementLayout elementLayout){
        return parent;
    }

    protected void removeParent() {
        this.parent = null;
    }

    public View getView(){
        return extendedView;
    }
    public int getHeight(){
        return elementDimensions.getHeight();
    }
    public int getWidth(){
        return elementDimensions.getWidth();
    }
    public int getX(){
        return elementDimensions.getX();
    }
    public int getY(){
        return elementDimensions.getY();
    }
    public int getCornerRadius(){
        return cornerRadius;
    }




    public boolean hasShadow(){
        return elementShadowDetails != null;
    }
    public ElementShadowDetails getShadowDetails(){
        return elementShadowDetails;
    }


}
