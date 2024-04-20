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
import com.example.habittracker.structures.WidgetId;

import java.util.ArrayList;

public class EntryDropDown extends EntryWidget {
    private Structure structure = null;
    private WidgetId valueId = null;
    private ArrayList<WidgetId> groupIdList = new ArrayList<>();

    private RefItemPath selectedValuePath = null;

    private DropDown dropDown;

    private DropDownPage dropDownPage = null;
    private Context context;

    public EntryDropDown(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){

        dropDown = new DropDown(context, new DropDown.DropDownOnSelected() {
            @Override
            public void onSelected(ArrayList<String> itemPath, Object payload) {
                onDataChanged((RefItemPath)payload);
            }
        });
        setViewWrapperChild(dropDown.getView());
    }

    private void onDataChanged(RefItemPath refItemPath){
        MainActivity.log("on data changed: " + refItemPath);
        selectedValuePath = refItemPath;
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

    public void setSelected(RefItemPath itemPath){
        dropDown.setSelected(itemPath.getStringList());
        selectedValuePath = itemPath;
    }



    public RefItemPath getSelectedPath(){
        return selectedValuePath;
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        MainActivity.log("saving value path: " + selectedValuePath);
        return new WidgetValueStringPath(getWidgetId(), selectedValuePath);
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
