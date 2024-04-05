package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.Structs.DataTree;
import com.example.habittracker.Structs.Entry;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;

public class CategoryEntryEditorPage implements Inflatable{
    private Context context;
    private Structure structure;
    private Entry entry;
    private GroupWidget groupWidget;
    private LinearLayout linearLayout;
    public CategoryEntryEditorPage(Context context, Structure spreadsheet, Entry entry){
        this.context = context;
        this.structure = spreadsheet;
        this.entry = entry;
        linearLayout = new LinearLayout(context);
    }

    public void onDataChanged(){
        System.out.println("new data: \n" + groupWidget.getDataTree().hierarchy());
    }

    public boolean trySave(){
        EntryWidget firstWidget = groupWidget.entryWidgets().get(0);
        CustomEditText uniqueAttributeEditor = (CustomEditText) firstWidget;
        String uniqueAttribute = uniqueAttributeEditor.getText();
        if(uniqueAttribute == null){
            uniqueAttributeEditor.showError();
            System.out.println("saving entry error: missing unique attribute");
            return false;
        }
        DataTree data = groupWidget.getDataTree();
        if(entry == null){
            System.out.println("new entry, adding entry to structure");
            structure.addEntry(data);
        }else{
            System.out.println("editing entry, changing dataTree");
            entry.dataTree = data;
        }
        System.out.println("saving entry successful: \n" + data.hierarchy());
        return true;
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
        groupWidget.setParam(structure.getParam());

        if(entry != null)
            groupWidget.setValue(entry.dataTree);
    }

    @Override
    public boolean tryToRemove() {
        System.out.println("trying to remove category entry editor");
        return trySave();
    }


}
