package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.ViewWidgets.ListViewPackage.DynamicListView;
import com.example.habittracker.ViewWidgets.SelectionView;
import com.example.habittracker.defaultImportPackage.DefaultImportClass;
import com.example.habittracker.structurePack.Structure;

public class CategorySelectionPage implements Inflatable{
    private Context context;
    private LinearLayout linearLayout;
    public CategorySelectionPage(Context context){
        this.context = context;
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
        MainActivity.log("structures: \n" + Dictionary.getStructureDebug());
//        SelectionView selectionView = new SelectionView(context, Dictionary.getCategoryOptions(),
//                (stringValue, position, key) -> onCategorySelected((Structure) key));
//        linearLayout.addView(selectionView.getView());

        DynamicListView dynamicListView = new DynamicListView(context, Dictionary.getCategoryOptions());
        dynamicListView.setOnClickListener((cachedString, position, key) -> {
            MainActivity.log("sending click to onCategorySelected");
            onCategorySelected((Structure) key);
        });
        dynamicListView.setOnLongClickListener((cachedString, position, key) -> {
            onLongClick(position, dynamicListView);
        });
        linearLayout.addView(dynamicListView.getView());

    }

    private void onLongClick(int position, DynamicListView dynamicListView){
        MainActivity.log("outside on long click");
        dynamicListView.showCheckBoxes((checked, cachedString1, position1, key1) -> {
            onCheckBox(dynamicListView);
        });
        dynamicListView.setCheckBox(position, true);
        dynamicListView.showConfirmButton(() -> {
            onConfirm(dynamicListView);
        });
    }

    private void onCheckBox(DynamicListView dynamicListView){

    }

    private void onConfirm(DynamicListView dynamicListView){
        MainActivity.log("confirming");
        dynamicListView.hideConfirmButton();
        dynamicListView.hideCheckBoxes();
    }

    public void onCategorySelected(Structure structure){
        if( ! structure.getType().equals(Dictionary.category))
            throw new RuntimeException();
        MainActivity.changePage(new CategoryEntriesPage(context, structure));
    }

    @Override
    public boolean tryToRemove(Inflatable page) {
        return true;
    }
}
