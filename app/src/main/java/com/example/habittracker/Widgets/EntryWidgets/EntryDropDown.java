package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.DropDownPageFactory;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Values.WidgetValueStringPath;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetInStructure;

import java.util.ArrayList;

public class EntryDropDown extends BaseEntryWidget {
    private Structure structure = null;
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
        WidgetValueStringPath widgetValueStringPath = (WidgetValueStringPath) widgetValue;
        if(widgetValueStringPath.getRefItemPath() == null){
            //MainActivity.log(widgetValueStringPath.getParent().hierarchy());
            throw new RuntimeException();
        }

        setSelected(widgetValueStringPath.getRefItemPath());
    }

    @Override
    protected void setHint(String hintString) {

    }

    public void setSelected(RefItemPath itemPath){
        dropDown.setSelected(itemPath);
    }



    public RefItemPath getSelectedPath(){
        return dropDown.getSelectedPath();
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        MainActivity.log("saving value path: " + dropDown.getSelectedPath());
        return new WidgetValueStringPath(getWidgetId(), dropDown.getSelectedPath());
    }

    @Override
    public void setParamCustom(EntryWidgetParam param){
        DropDownParam dropDownParams = ((DropDownParam) param);
        dropDownPage = DropDownPageFactory.getGroupedPages(dropDownParams.structure, dropDownParams.valueKey, dropDownParams.groups);
        structure = dropDownParams.structure;
        valueId = dropDownParams.valueKey;
        groupIdList = dropDownParams.groups;
        dropDown.setDropDownPage(dropDownPage);
    }


}
