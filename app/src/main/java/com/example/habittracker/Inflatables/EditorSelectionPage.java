package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.habittracker.Dictionary;
import com.example.habittracker.MainActivity;
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
        items.add("new category");
        items.add("new journal");
        System.out.println("items = " + items);
        ListView list = MainActivity.createList(items, (parent, view, position, id) -> {
            String selectedItem = items.get(position);
            if(selectedItem.equals("new category")){
                setListItems(Dictionary.getCategoryKeys());
                return;
            }
            if(selectedItem.equals("new journal")){
                setListItems(Dictionary.getJournalKeys());
                return;
            }
        });
        parentLayout.addView(list);
    }

    public void setListItems(ArrayList<String> items){
        items.add("add");
        System.out.println("items = " + items);

        View list = MainActivity.createList(items, (parent, view, position, id) -> {
            String selectedItem = items.get(position);
            Structure structure;
            if(selectedItem.equals("add")){
                structure = new Structure();
            }
            structure = Dictionary.getStructure(selectedItem);

            MainActivity.inflateLayout(new StructureEditor(context, structure));
        });
        parentLayout.removeViewAt(0);
        parentLayout.addView(list);
    }

    @Override
    public View getView() {
        return parentLayout;
    }
}
