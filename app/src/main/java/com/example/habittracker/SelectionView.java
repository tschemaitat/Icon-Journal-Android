package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class SelectionView {
    Context context;
    ArrayList<String> options;
    OnSelected onSelected;
    OnAdd onAdd;
    ListView listView;
    public SelectionView(Context context, ArrayList<String> options, OnSelected onSelected, OnAdd onAdd){
        this.context = context;
        this.options = (ArrayList<String>) options.clone();
        this.onSelected = onSelected;
        this.onAdd = onAdd;
        init();
    }

    public SelectionView(Context context, ArrayList<String> options, OnSelected onSelected){
        this.context = context;
        this.options = (ArrayList<String>) options.clone();
        this.onSelected = onSelected;
        this.onAdd = null;
        init();
    }

    private void init(){
        listView = createList(context, options, onSelected, onAdd);
    }



    public View getView(){
        return listView;
    }


    public interface OnSelected {
        public void onSelected(String stringValue, int position);
    }

    public interface OnAdd{
        public void onAdd();
    }


    private static ListView createList(Context context, ArrayList<String> items, OnSelected onSelected, OnAdd onAdd){
        int numItems = items.size();
        ListView listView = new ListView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        listView.setLayoutParams(params);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_expandable_list_item_1, items);
        listView.setAdapter(adapter);
        if(onAdd != null)
            items.add("add");
        listView.post(() -> {
            int childHeight = listView.getChildAt(0).getHeight();
            int listHeight = childHeight * listView.getDividerHeight();
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(-1, listHeight);
            listView.setLayoutParams(params1);
        });

        // Set the click listener for the list items
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(i < numItems){
                onSelected.onSelected(items.get(i), i);
            }else{
                if(onAdd == null)
                    throw new IndexOutOfBoundsException("no add but but index out of bounds size: " + numItems + ", index: " + i);
                onAdd.onAdd();
            }
        });
        return listView;
    }
}
