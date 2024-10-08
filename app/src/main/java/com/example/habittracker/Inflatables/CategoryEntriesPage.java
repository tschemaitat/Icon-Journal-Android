package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.ViewWidgets.SelectionView;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.Structure;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class CategoryEntriesPage implements Inflatable{
    private Context context;
    private Structure structure;
    private LinearLayout linearLayout;
    public CategoryEntriesPage(Context context, Structure category){
        this.context = context;
        this.structure = category;
        linearLayout = new LinearLayout(context);
    }
    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {
        ArrayList<GroupValue> dataList = structure.getData();
        for(GroupValue data: dataList)
            System.out.println(data.hierarchy());

        ArrayList<EntryInStructure> entryInStructureNames = structure.getEntryList();
        ArrayList<PayloadOption> payloadOptionList = EnumLoop.makeList(entryInStructureNames, entry -> new PayloadOption(entry.getCachedName(), entry));
        SelectionView selectionView = new SelectionView(context, payloadOptionList, (stringValue, position, payload) -> {
            EntryInStructure entryInStructure = (EntryInStructure)payload;
            MainActivity.changePage(new CategoryEntryEditorPage(context, structure, entryInStructure));
        },()->{
            MainActivity.changePage(new CategoryEntryEditorPage(context, structure, null));
        });
        linearLayout.addView(selectionView.getView());
    }

    @Override
    public boolean tryToRemove(Inflatable page) {
        return true;
    }

    private ArrayList<String> getOptionsFromNames(ArrayList<ArrayList<String>> entryNames) {
        ArrayList<String> options = new ArrayList<>();
        for(ArrayList<String> name: entryNames){
            String concat = "";
            for(int i = 0; i < name.size(); i++){
                String string = name.get(i);
                concat += string;
                if(i < name.size() - 1)
                    concat += ", ";
            }
            options.add(concat);
        }
        return options;
    }
}
