package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.ViewWidgets.SelectionView;
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
        parentLayout.setId(R.id.editorSelectionLayout);
        parentLayout.setMinimumHeight(1000);
    }

    public void createFolders(){
        System.out.println("create folders");
        ArrayList<String> items = new ArrayList<>();
        items.add("category");
        items.add("journal");
        SelectionView folderSelection = new SelectionView(context, (String[]) items.toArray(), (value, position, key) -> {
            createSelectionAfterFolder(value);
        });
        parentLayout.addView(folderSelection.getView());
    }

    public void createSelectionAfterFolder(String structureType){
        ArrayList<PayloadOption> structureKeys = Dictionary.getStructureOptions();
        parentLayout.removeAllViews();
        SelectionView structureSelection = new SelectionView(context, structureKeys, (value, position, key) -> {
            inflateStructureEditor((StructureId) key);
        }, () -> {
            MainActivity.changePage(new StructureEditor(context, new Structure(null, null, structureType)));
        });
        parentLayout.addView(structureSelection.getView());
    }

    public void inflateStructureEditor(StructureId id){
        MainActivity.changePage(new StructureEditor(context, Dictionary.getStructure(id)));
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

    @Override
    public boolean tryToRemove(Inflatable page) {
        return true;
    }
}
