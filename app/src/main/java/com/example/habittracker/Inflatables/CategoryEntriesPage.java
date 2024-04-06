package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.Structs.DataTree;
import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.SelectionView;
import com.example.habittracker.Structs.Entry;
import com.example.habittracker.Structs.Structure;

import java.util.ArrayList;

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
        ArrayList<DataTree> dataList = structure.getData();
        for(DataTree data: dataList)
            System.out.println(data.hierarchy());

        ArrayList<ArrayList<String>> entryNames = structure.IdAttributes();
        ArrayList<String> options = getOptionsFromNames(entryNames);
        SelectionView selectionView = new SelectionView(context, (String[])options.toArray(), (stringValue, position, key) -> {
            Entry entry = structure.getEntry(entryNames.get(position));
            MainActivity.changePage(new CategoryEntryEditorPage(context, structure, entry));
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
