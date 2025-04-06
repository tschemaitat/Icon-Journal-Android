package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.ParentWidget;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;

import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.structurePack.WidgetInStructure;

import java.util.Collections;

public class CategoryEntryEditorPage extends Inflatable implements WidgetDataListener {
    private Context context;
    private Structure structure;
    private EntryInStructure entryInStructure;
    private ParentWidget parentWidget;
    private LinearLayout linearLayout;
    private boolean newEntry = false;
    boolean discarding = false;
    public CategoryEntryEditorPage(Context context, Structure spreadsheet, EntryInStructure entryInStructure){
        if(entryInStructure != null)
            MainActivity.log("entry editor: " + spreadsheet.getCachedName() + " entry id: " + entryInStructure.getId() +"\n"+ entryInStructure.getGroupValue().hierarchy());
        else
            MainActivity.log("entry editor: " + spreadsheet.getCachedName() + " entry: null");
        this.context = context;
        this.structure = spreadsheet;
        this.entryInStructure = entryInStructure;
        linearLayout = new LinearLayout(context);
    }

    public void onDataChanged(){
        System.out.println("new data: \n" + ((GroupValue) parentWidget.getValue()).hierarchy());
    }

//    private boolean checkValidForSave(Inflatable page){
//        EntryWidget firstWidget = parentWidget.getEntryWidgets().get(0);
//        CustomEditText uniqueAttributeEditor = (CustomEditText) firstWidget;
//        String uniqueAttribute = uniqueAttributeEditor.getText();
//        if(uniqueAttribute == null){
//            uniqueAttributeEditor.setTextErrorColor();
//            System.out.println("saving entry error: missing unique attribute");
//            createSaveErrorDialog(page);
//            return false;
//        }
//
//        return true;
//    }

//    private void save(){
//        GroupValue data = (GroupValue) parentWidget.getValue();
//        data.setIdOfTree();
//        MainActivity.log("group widget children: " + parentWidget.getWidgetLayout().widgets());
//        if(entryInStructure == null){
//            MainActivity.log("new entry, adding entry to structure");
//            structure.addEntry(data);
//        }else{
//            MainActivity.log("editing entry, changing dataTree");
//            structure.editEntry(entryInStructure, data);
//        }
//        MainActivity.log("saving entry successful: \n" + data.hierarchy());
//    }



//    private void createSaveErrorDialog(Inflatable page) {
//        ArrayList<String> options = new ArrayList<>(Collections.singleton("discard"));
//        ArrayList<Runnable> listener = new ArrayList<>(Collections.singleton(()->{
//            discardEntry(page);
//        }));
//        MainActivity.showPopup(context, "entry cannot be saved", options, listener);
//    }

//    private void discardEntry(Inflatable page){
//        System.out.println("discarding entry, changing page");
//        discarding = true;
//        MainActivity.changePage(page);
//    }
    @Override
    public void onWidgetChangedData(Object value, RefEntryString refEntryString){
        structure.editWidgetValue(refEntryString, (WidgetValue)value);
    }

    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {
        MenuBarManager menuBarManager = PageResources.getPageResources().getMenuBarManager();
        menuBarManager.removeEntryEditorBar();
        System.out.println("category entry editor removed");
    }

    @Override
    public void onOpened() {
        MenuBarManager menuBarManager = PageResources.getPageResources().getMenuBarManager();
        parentWidget = new ParentWidget(context, new ViewWrapper(context));
        menuBarManager.addEntryEditorBar(parentWidget, entryInStructure);
//
//
        parentWidget.setOnDataChangedListener(()->onDataChanged());
        linearLayout.addView(parentWidget.getView());
        MainActivity.log("settting param: \n" + structure.getWidgetParam());
        parentWidget.setParam(structure.getWidgetParam());
        if(entryInStructure != null){
            parentWidget.setValue(entryInStructure.getGroupValue());
            MainActivity.log("set from entry: " + entryInStructure.getGroupValue().hierarchy());
        }else{
            entryInStructure = structure.addEntry((GroupValue) parentWidget.getValue());
            newEntry = true;
        }



    }

    @Override
    public boolean canRemoveImpl(Inflatable page) {
//        System.out.println("trying to remove category entry editor");
//        if(discarding)
//            return true;
//        boolean valid = true;//checkValidForSave(page);
//        if(!valid)
//            return false;
//        save();
//        return true;
        return true;
    }




}
public interface WidgetDataListener{
    void onWidgetChangedData(Object value, RefEntryString refEntryString);
}
