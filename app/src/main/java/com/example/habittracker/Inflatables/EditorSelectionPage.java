package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.Dictionary;
import com.example.habittracker.MainActivity;
import com.example.habittracker.SelectionView;
import com.example.habittracker.Structs.Structure;

import java.util.ArrayList;

public class EditorSelectionPage implements Inflatable {
    private LinearLayout parentLayout;
    private Context context;
    public EditorSelectionPage(Context context){
        this.context = context;
        initLayout();
        createFolders();
        //inflateEditor();
    }

    public void initLayout(){
        parentLayout = new LinearLayout(context);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        parentLayout.setLayoutParams(params);
    }

    public void createFolders(){
        System.out.println("create folders");
        ArrayList<String> items = new ArrayList<>();
        items.add("category");
        items.add("journal");
        SelectionView folderSelection = new SelectionView(context, items, (value, position) -> {
            createSelectionAfterFolder(value);
        });
        parentLayout.addView(folderSelection.getView());
    }

    public void createSelectionAfterFolder(String structureType){
        ArrayList<String> structureKeys = Dictionary.getStructureKeys(structureType);
        parentLayout.removeAllViews();
        SelectionView structureSelection = new SelectionView(context, structureKeys, (value, position) -> {
            inflateStructureEditor(value);
        }, () -> {
            MainActivity.inflateLayout(new StructureEditor(context, new Structure(null, null, structureType)));
        });
        parentLayout.addView(structureSelection.getView());
    }

    public void inflateStructureEditor(String structureKey){
        MainActivity.inflateLayout(new StructureEditor(context, Dictionary.getStructure(structureKey)));
    }



    @Override
    public View getView() {
        return parentLayout;
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {

    }
}
