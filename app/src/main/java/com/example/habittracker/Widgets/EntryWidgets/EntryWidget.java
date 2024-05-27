package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.StaticStateManagers.InvisibleEditTextManager;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.FocusTreeParent;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.structurePack.Structure;

import java.util.ArrayList;

public abstract class EntryWidget implements Widget {
    public static int widgetDebugIdCounter = 0;
    public int widgetDebugId;
    private Runnable onDataChanged;
    private EntryWidgetParam entryWidgetParam;
    ViewWrapper viewWrapper;
    private boolean dataSet = false;


    private Context context;
    private FocusTreeParent focusParent;

    boolean deleteEnabled = false;
    private boolean listGhostEnabled = true;
    public boolean isDeleteChecked = false;



    public EntryWidget(Context context){
        this.context = context;
        widgetDebugId = widgetDebugIdCounter;
        widgetDebugIdCounter++;
        viewWrapper = new ViewWrapper(context);
        viewWrapper.getView().setId(R.id.entryWidgetWrapper);
    }

    public void setOnDataChangedListener(Runnable runnable){
        if(runnable == null)
            throw new RuntimeException();
        //MainActivity.log("set listener on: "+this+"\nrunnable: " + runnable);
        this.onDataChanged = runnable;
    }

    void disableNoVisual(){
        if(!listGhostEnabled)
            throw new RuntimeException();
        listGhostEnabled = false;
        viewWrapper.disable();
    }

    private void enableNoVisual(){
        if(listGhostEnabled)
            throw new RuntimeException();
        viewWrapper.enable();
        listGhostEnabled = true;
    }


    public void enableDelete(){
        deleteEnabled = true;
        disableNoVisual();
        viewWrapper.showCheckBox((boolean isChecked)->onDeleteCheck(isChecked));
    }



    public abstract ArrayList<BaseEntryWidget> getWidgetsForDelete();

    public final void onDeleteCheck(boolean isChecked){
        isDeleteChecked = isChecked;
    }

    public void disableDelete(){
        deleteEnabled = false;
        enableNoVisual();
        viewWrapper.hideCheckBox();
    }

    public void disableWithGrayOut(){
        if(listGhostEnabled)
            throw new RuntimeException();
        listGhostEnabled = true;
        viewWrapper.disable();
        setForegroundOfDisable();
    }

    private void setForegroundOfDisable(){
        Drawable foregroundDrawable = context.getDrawable(R.drawable.rounded_foreground_inset);
        Margin currentPadding = Margin.getPadding(getView());
        getView().setForeground(foregroundDrawable);
        Margin paddingAfter = Margin.getPadding(getView());
        if( ! currentPadding.equals(paddingAfter)){
            MainActivity.log(currentPadding.toString());
            MainActivity.log(paddingAfter.toString());
            throw new RuntimeException();
        }
    }

    public void enable(){
        if(!listGhostEnabled)
            throw new RuntimeException();
        viewWrapper.enable();
        listGhostEnabled = false;
        getView().setForeground(null);
    }

    protected final void setViewWrapperChild(View view){
        viewWrapper.setChildView(view);
    }

    public final Runnable onDataChangedListener(){
        //MainActivity.log("getting on data changed listener: " + this);
        return onDataChanged;
    }

    protected abstract WidgetValue getEntryValueTreeCustom();

    public final WidgetValue getValue(){
        WidgetValue tree = getEntryValueTreeCustom();

        return tree;
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

    public void keyListener(int keyCode){
        MainActivity.log("entry widget key listener: " + keyCode);
        if (keyCode == EditorInfo.IME_ACTION_NEXT) {

            EntryWidget nextWidget = focusParent.findNextWidget(this);
            InvisibleEditTextManager.getManager().removeFocusedWidget();
            if(nextWidget != null)
                InvisibleEditTextManager.getManager().setFocusedWidget(nextWidget);
        }
    }

    public FocusTreeParent getFocusParent(){
        return focusParent;

    }



    public WidgetInStructure getWidgetInStructure(){
        return entryWidgetParam.getWidgetInStructure();
    }

    protected abstract void setValueCustom(WidgetValue widgetValue);

    public final void setValue(WidgetValue widgetValue){
        setValueCustom(widgetValue);
    }

    protected abstract void setHint(String hintString);

    public void onFocusChange(boolean hasFocus){
        MainActivity.log("entry widget on focus change: " + this);
        if(hasFocus){
            viewWrapper.showBorderView();
        }else{
            viewWrapper.hideBorderView();
        }
    }

    public final ViewWrapper getViewWrapper(){
        return viewWrapper;
    }




    public final String getName(){
        if(entryWidgetParam == null)
            return null;
        return entryWidgetParam.name;
    }

    public final void setParam(EntryWidgetParam param){
        if(dataSet)
            throw new RuntimeException();
        dataSet = true;
        if(param.name != null)
            setHint(param.name);
        this.entryWidgetParam = param;
        setParamCustom(param);
    }

    protected abstract void setParamCustom(EntryWidgetParam param);

    public final View getView(){
        return viewWrapper.getView();
    }


    protected WidgetId getWidgetIdTracker() {
        return entryWidgetParam.getWidgetId();
    }



    public Structure getStructure(){
        return entryWidgetParam.getStructure();
    }

    public StructureId getStructureId(){
        return entryWidgetParam.getWidgetInStructure().getStructureId();
    }

    public final String toString(){
        String className = "no param";
        if(entryWidgetParam != null)
            className = entryWidgetParam.getClassName();
        return "<" + className + ": " + widgetDebugId + ">";
    }

    public abstract String getNameAndLocation();


}
