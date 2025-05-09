package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;


import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ViewElement;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticStateManagers.InvisibleEditTextManager;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.FocusTreeParent;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.structurePack.Structure;

public abstract class EntryWidget implements Widget {
    public static int widgetDebugIdCounter = 0;
    public int widgetDebugId;
    private Runnable onDataChanged;
    private EntryWidgetParam entryWidgetParam;
    ViewWrapper viewWrapper;
    private boolean dataSet = false;

    private Context context;
    private FocusTreeParent focusParent;

    protected EntryWidgetResources entryWidgetResources;


    public EntryWidget(Context context, Element wrapperElement,
                       EntryWidgetResources entryWidgetResources){
        this.context = context;
        widgetDebugId = widgetDebugIdCounter;
        widgetDebugIdCounter++;
        this.viewWrapper = (ViewWrapper) wrapperElement;
        this.viewWrapper.getView().setId(R.id.entryWidgetWrapper);
        this.entryWidgetResources = entryWidgetResources;
    }

    public void setWidgetResources(EntryWidgetResources entryWidgetResources){
        this.entryWidgetResources = entryWidgetResources;
    }

//    private void setForegroundOfDisable(){
//        Drawable foregroundDrawable = context.getDrawable(R.drawable.rounded_foreground_inset);
//        Margin currentPadding = Margin.getPadding(getView());
//        getView().setForeground(foregroundDrawable);
//        Margin paddingAfter = Margin.getPadding(getView());
//        if( ! currentPadding.equals(paddingAfter)){
//            MainActivity.log(currentPadding.toString());
//            MainActivity.log(paddingAfter.toString());
//            throw new RuntimeException();
//        }
//    }

    protected abstract void setValueCustom(Object widgetValue);
    public final void setValue(Object widgetValue){
        setValueCustom(widgetValue);
    }
    public final Object getValue(){
        Object tree = getEntryValueTreeCustom();
        return tree;
    }
    //returns widget value or group value
    protected abstract Object getEntryValueTreeCustom();

    public void onFocusChange(boolean hasFocus){
        MainActivity.log("entry widget on focus change: " + this);
        if(hasFocus){
            viewWrapper.showBorderView();
        }else{
            viewWrapper.hideBorderView();
        }
    }
    public void keyListener(int keyCode){
        MainActivity.log("entry widget key listener: " + keyCode);
        if (keyCode == EditorInfo.IME_ACTION_NEXT) {
            if(focusParent == null){

                MainActivity.log("focus parent null");
                return;
            }

            EntryWidget nextWidget = focusParent.findNextWidget(this);
            InvisibleEditTextManager.getManager().removeFocusedWidget();
            if(nextWidget != null)
                InvisibleEditTextManager.getManager().setFocusedWidget(nextWidget);
        }
    }

    public final void setParam(EntryWidgetParam param){
        if(dataSet)
            throw new RuntimeException();
        dataSet = true;
        if(this instanceof BaseEntryWidget baseEntryWidget){
            if(param.name != null)
                baseEntryWidget.setHint(param.name);
        }

        this.entryWidgetParam = param;
        setParamCustom(param);
    }
    protected abstract void setParamCustom(EntryWidgetParam param);

    public void setOnDataChangedListener(Runnable runnable){
        if(runnable == null)
            throw new RuntimeException();
        //MainActivity.log("set listener on: "+this+"\nrunnable: " + runnable);
        this.onDataChanged = runnable;
    }
//    public final Runnable onDataChangedListener(){
//        //MainActivity.log("getting on data changed listener: " + this);
//        return onDataChanged;
//    }
    protected void onDataChanged(){

    }

    public void setFocusParent(FocusTreeParent parent){
        this.focusParent = parent;
        this.getView().setFocusable(true);
        this.getView().setFocusableInTouchMode(true);
//        this.viewWrapper.getInvisibleEditText().setOnKeyListener((v, keyCode, event) -> {
//            MainActivity.log("got key in action key listener: " + keyCode);
//            if(event.getAction() == KeyEvent.ACTION_DOWN){
//                keyListener(keyCode);
//            }
//            return true;
//        });

    }
    public FocusTreeParent getFocusParent(){
        return focusParent;

    }

    public final View getView(){
        return viewWrapper.getView();
    }

    public final Element getElement(){
        Element element = new ViewElement() {
            @Override
            public View getView() {

                return EntryWidget.this.getView();
            }
        };
        return element;
    }
    public final ViewWrapper getViewWrapper(){
        return viewWrapper;
    }
//    protected final void setViewWrapperChild(View view){
//        viewWrapper.setChildView(view);
//    }
    protected final void setViewWrapperChild(Element element){
        viewWrapper.setChildView(element);
    }

    protected WidgetId getWidgetId() {
        return entryWidgetParam.getWidgetId();
    }
    public WidgetInStructure getWidgetInStructure(){
        return entryWidgetParam.getWidgetInStructure();
    }
    public Structure getStructure(){
        return entryWidgetParam.getStructure();
    }
    public StructureId getStructureId(){
        return entryWidgetParam.getWidgetInStructure().getStructureId();
    }

    public final String getName(){
        if(entryWidgetParam == null)
            return null;
        return entryWidgetParam.name;
    }
    public final String toString(){
        String className = "no param";
        if(entryWidgetParam != null)
            className = entryWidgetParam.getClassName();
        return "<" + className + ": " + widgetDebugId + ">";
    }
    public abstract String getNameAndLocation();
}
