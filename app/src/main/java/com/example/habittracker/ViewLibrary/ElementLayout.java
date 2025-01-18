package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class ElementLayout extends Element{
    private ArrayList<Element> elements = new ArrayList<>();
    private Context context;
    private ViewGroup viewGroup;
    private Margin childMargin = null;

    public ElementLayout(Context context){
        this.context = context;
    }

    protected void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }


    //this function should be called when adding an element
    //elementLayout tries to remove bulk "add" code
    //resets layoutParams and handles margin
    //does not add view to view layout
    protected MarginHelper parentOnAdd(Element element){
        checkElementBeforeAdd(element);
        elements.add(element);
        MarginHelper marginHelper = handleMarginAndParamsOnAdd(element);
        return marginHelper;
    }
    protected MarginHelper parentOnAdd(int index, Element element){
        if(index < 0 || index >= elements.size())
            throw new RuntimeException("incorrect index: " + index);
        checkElementBeforeAdd(element);
        elements.add(index, element);
        MarginHelper marginHelper = handleMarginAndParamsOnAdd(element);
        return marginHelper;
    }


    private MarginHelper handleMarginAndParamsOnAdd(Element element){
        //reset layout params on add to make debugging easier
        element.getView().setId(View.generateViewId());

        element.setLayoutParams(createLayoutParams());
        MarginHelper marginHelper = null;
        if(childMargin == null){
            //if layouts doesn't automatically set margin, return marginHelper to allow manual margin
            marginHelper = new MarginHelper(element, this::setMarginOfChild, this::setDimensionsOfChild);
        }else{

            //if childMargin isn't null, this layout automatically sets margin of child
            //crashes if tried to use marginHelper to manually set margin
            marginHelper = new MarginHelper(element, null, this::setDimensionsOfChild);
            setMarginOfChild(element, childMargin);
        }
        return marginHelper;
    }
    private void checkElementBeforeAdd(Element element){
        if(element == null)
            throw new RuntimeException("element null");
        if(elements.contains(element))
            throw new RuntimeException("already contains element");
    }
    private void setDimensionsOfChild(Element element, int width, int height){
        ViewGroup.LayoutParams layoutParams = element.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
    }

    public void setChildMargin(Margin margin){
        this.childMargin = margin;
    }



    private void setMarginOfChild(Element element, Margin margin){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) element.getLayoutParams();
        params.leftMargin = margin.left;
        params.bottomMargin = margin.bottom;
        params.rightMargin = margin.right;
        params.topMargin = margin.top;
    }

    protected abstract ViewGroup.LayoutParams createLayoutParams();

    protected abstract ViewGroup getViewGroup();

    protected abstract void onAdd(Element element);

    protected abstract void onAdd(int index, Element element);

    public final void remove(Element element){
        if(element == null)
            throw new RuntimeException();
        if(!elements.contains(element))
            throw new RuntimeException();
        elements.remove(element);
        onRemove(element);
    }

    public final Element remove(int index){

        Element removedElement = elements.remove(index);
        if(removedElement == null){
            throw new RuntimeException("error removing at index: " + index);
        }
        onRemove(index, removedElement);
        return removedElement;
    }



    protected abstract void onRemove(Element element);
    protected abstract void onRemove(int index, Element removedElement);

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


    public <E extends Element> ArrayList<E> getChildrenCasted() {
        return elements.convert((index, element) -> ((E) element));
    }

    public static class MarginHelper{
        private MarginFunction marginFunction;
        private DimensionsFunction dimensionsFunction;
        private Element element;
        public MarginHelper(Element element, MarginFunction marginFunction, DimensionsFunction dimensionsFunction){
            this.marginFunction = marginFunction;
            this.dimensionsFunction = dimensionsFunction;
            this.element = element;
        }

        public MarginHelper setMargin(Margin margin){
            marginFunction.marginFunction(element, margin);
            return this;
        }

        public MarginHelper setDimensions(int width, int height){
            dimensionsFunction.dimensionsFunction(element, width, height);
            return this;
        }
    }

    public static interface MarginFunction {
        void marginFunction(Element element, Margin margin);
    }
    public static interface DimensionsFunction{
        void dimensionsFunction(Element element, int width, int height);
    }
}
