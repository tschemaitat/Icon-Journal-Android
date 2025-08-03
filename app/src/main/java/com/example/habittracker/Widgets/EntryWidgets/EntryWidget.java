package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.EditorInfo;


import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementProvider;
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

import java.util.List;

public abstract class EntryWidget implements ElementProvider, FocusTreeParent.Focusable {

    private EntryWidgetResources entryWidgetResources;
    private AbstractWidget abstractWidget;
    private FocusTreeParent focusTreeParent;


    public EntryWidget(AbstractWidget abstractWidget,
                       EntryWidgetResources entryWidgetResources){
        this.abstractWidget = abstractWidget;


        this.entryWidgetResources = entryWidgetResources;

        abstractWidget.setOnDataChangedListener((prevData, data) -> {
            entryWidgetResources.entryOnDataChange.onBaseEntryDataChange(
                    getLocation(), (WidgetValue) abstractWidget.getValue());
        });

    }
    public RefEntryString getLocation(){
        return abstractWidget.getLocation(entryWidgetResources.entryInStructure);
    }

    public void setWidgetResources(EntryWidgetResources entryWidgetResources){
        this.entryWidgetResources = entryWidgetResources;
    }

    @Override
    public void setFocusParent(FocusTreeParent focusTreeParent){
        this.focusTreeParent = focusTreeParent;
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


    //returns widget value or group value







//    public final Runnable onDataChangedListener(){
//        //MainActivity.log("getting on data changed listener: " + this);
//        return onDataChanged;
//    }







    public abstract String getNameAndLocation();

    public AbstractWidget getAbstractWidget() {
        return abstractWidget;
    }
}
