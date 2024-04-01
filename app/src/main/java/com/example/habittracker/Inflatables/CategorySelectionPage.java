package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.SelectionView;
import com.example.habittracker.Structs.Structure;

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
        SelectionView selectionView = new SelectionView(context, Dictionary.getCategoryKeys(), (stringValue, position) -> {
            Structure structure = Dictionary.getStructure(stringValue);
            if( ! structure.getType().equals(Dictionary.category))
                throw new RuntimeException();
            MainActivity.inflateLayout(new CategoryEntriesPage(context, structure));
        });
        linearLayout.addView(selectionView.getView());
    }
}
