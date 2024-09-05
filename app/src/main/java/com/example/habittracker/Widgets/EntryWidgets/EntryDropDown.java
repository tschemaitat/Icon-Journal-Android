package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.DropDownPageFactory;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Values.WidgetValueString;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class EntryDropDown extends BaseEntryWidget {
    private Structure referenceStructure = null;
    private WidgetInStructure valueId = null;
    private ArrayList<WidgetInStructure> groupIdList = new ArrayList<>();

    private DropDown dropDown;

    private DropDownPage dropDownPage = null;
    private Context context;

    public EntryDropDown(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){

        dropDown = new DropDown(context, (refItemPath, payload, prevRefItemPath, prevPayload) -> onDataChanged((RefItemPath)payload));
        setViewWrapperChild(dropDown.getView());
    }

    private void onDataChanged(RefItemPath refItemPath){
        MainActivity.log("on data changed: " + refItemPath);
        onDataChangedListener().run();
    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {

//        if(widgetValueStringPath.getRefItemPath() == null){
//            //MainActivity.log(widgetValueStringPath.getParent().hierarchy());
//            throw new RuntimeException();
//        }

        //WidgetValueStringPath widgetValueSet = (WidgetValueStringPath) widgetValue;
        WidgetValueString widgetValueSet = (WidgetValueString) widgetValue;

//        if(widgetValueSet.getRefItemPath() != null)
//            setSelected(widgetValueSet.getRefItemPath());

        setSelected(widgetValueSet.getStandardFormOfCachedString());
    }

    @Override
    protected void setHint(String hintString) {

    }

    public void setSelected(CachedString item){
        dropDown.setSelectedNonPath(item);
        //dropDown.setSelectedPath(itemPath);
    }



    public RefItemPath getSelectedPath(){
        return dropDown.getSelectedPath();
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        MainActivity.log("saving value path: " + dropDown.getSelectedPath());
        if(dropDown.getSelectedPath() == null)
            return null;
        //return new WidgetValueStringPath(getWidgetInStructure().getWidgetId(), dropDown.getSelectedPath());
        return new WidgetValueString(getWidgetInStructure().getWidgetId(), dropDown.getSelectedPath().getLast());
    }

    @Override
    public void setParamCustom(EntryWidgetParam param){
        DropDownParam dropDownParams = ((DropDownParam) param);
        referenceStructure = dropDownParams.getReferenceStructure();
        valueId = dropDownParams.getValueWidget();
        valueId.getWidgetInfo();
        groupIdList = dropDownParams.getGroupWidgets();
        dropDownPage = DropDownPageFactory.getGroupedPages(referenceStructure, valueId, groupIdList);


        dropDown.setDropDownPage(dropDownPage);
    }


}
