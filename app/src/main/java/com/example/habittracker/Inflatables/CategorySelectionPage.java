package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.habittracker.Dictionary;
import com.example.habittracker.MainActivity;
import com.example.habittracker.SelectionView;
import com.example.habittracker.Structs.Structure;

public class CategorySelectionPage implements Inflatable{
    private Context context;
    public CategorySelectionPage(Context context){
        this.context = context;
    }
    @Override
    public View getView() {
        return null;
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {
        SelectionView selectionView = new SelectionView(context, Dictionary.getCategoryKeys(), (stringValue, position) -> {
            Structure structure = Dictionary.getStructure(stringValue);

        });
    }
}
