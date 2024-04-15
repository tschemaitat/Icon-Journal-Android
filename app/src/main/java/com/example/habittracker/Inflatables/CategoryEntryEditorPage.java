package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.structures.Entry;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryEntryEditorPage implements Inflatable{
    private Context context;
    private Structure structure;
    private Entry entry;
    private GroupWidget groupWidget;
    private LinearLayout linearLayout;
    boolean discarding = false;
    public CategoryEntryEditorPage(Context context, Structure spreadsheet, Entry entry){
        this.context = context;
        this.structure = spreadsheet;
        this.entry = entry;
        linearLayout = new LinearLayout(context);
    }

    public void onDataChanged(){
        System.out.println("new data: \n" + groupWidget.getEntryValueTree().hierarchy());
    }

    private boolean checkValidForSave(Inflatable page){
        EntryWidget firstWidget = groupWidget.entryWidgets().get(0);
        CustomEditText uniqueAttributeEditor = (CustomEditText) firstWidget;
        String uniqueAttribute = uniqueAttributeEditor.getText();
        if(uniqueAttribute == null){
            uniqueAttributeEditor.showError();
            System.out.println("saving entry error: missing unique attribute");
            createSaveErrorDialog(page);
            return false;
        }

        return true;
    }

    private void save(){
        EntryValueTree data = groupWidget.getEntryValueTree();
        data.setIdOfTree();
        MainActivity.log("group widget children: " + groupWidget.getWidgetLayout().widgets());
        if(entry == null){
            MainActivity.log("new entry, adding entry to structure");
            structure.addEntry(data);
        }else{
            MainActivity.log("editing entry, changing dataTree");
            structure.setData(entry, data);
        }
        MainActivity.log("saving entry successful: \n" + data.hierarchy());
    }



    private void createSaveErrorDialog(Inflatable page) {
        ArrayList<String> options = new ArrayList<>(Collections.singleton("discard"));
        ArrayList<Runnable> listener = new ArrayList<>(Collections.singleton(()->{
            discardEntry(page);
        }));
        MainActivity.showPopup(context, "entry cannot be saved", options, listener);
    }

    private void discardEntry(Inflatable page){
        System.out.println("discarding entry, changing page");
        discarding = true;
        MainActivity.changePage(page);
    }


    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {
        System.out.println("category entry editor removed");
    }

    @Override
    public void onOpened() {
        groupWidget = new GroupWidget(context);
        groupWidget.setOnDataChangedListener(()->onDataChanged());
        linearLayout.addView(groupWidget.getView());
        MainActivity.log("settting param: \n" + structure.getParam());
        groupWidget.setParam(structure.getParam());

        if(entry != null)
            groupWidget.setValue(entry.getEntryValueTree());
    }

    @Override
    public boolean tryToRemove(Inflatable page) {
        System.out.println("trying to remove category entry editor");
        if(discarding)
            return true;
        boolean valid = checkValidForSave(page);
        if(!valid)
            return false;
        save();
        return true;
    }


}
