package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

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
    public SelectionView(Context context, String[] options, OnSelected onSelected, OnAdd onAdd){
        this.context = context;
        this.options = new ArrayList<>(Arrays.asList(options));
        this.onSelected = onSelected;
        this.onAdd = onAdd;
        init();
    }

    public SelectionView(Context context, String[] options, OnSelected onSelected){
        this.context = context;
        this.options = new ArrayList<>(Arrays.asList(options));
        this.onSelected = onSelected;
        this.onAdd = null;
        init();
    }

    private void init(){
        createList();
    }



    public View getView(){
        return relativeLayout;
    }


    public interface OnSelected {
        public void onSelected(String stringValue, int position);
    }

    public interface OnAdd{
        public void onAdd();
    }

    public void setText(ArrayList<String> strings){
        options = strings;
        listView.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_expandable_list_item_1, strings));
    }

    public void setText(String[] strings){
        setText(new ArrayList<>(Arrays.asList(strings)));
    }

    RelativeLayout relativeLayout;
    private void createList(){
        System.out.println("creating list: " + options);

        int numItems = options.size();
        listView = new ListView(context);
        //listView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));

        relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams listViewLayoutParam = new RelativeLayout.LayoutParams(-2, -2);
        listViewLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        listView.setLayoutParams(listViewLayoutParam);
        relativeLayout.addView(listView);
        listView.setMinimumHeight(600);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_expandable_list_item_1, options);
        listView.setAdapter(adapter);
        if(onAdd != null)
            options.add("add");
        listView.post(() -> {
            if(listView.getAdapter().getCount() == 0)
                return;
            int childHeight = listView.getChildAt(0).getHeight();
            int listHeight = childHeight * listView.getDividerHeight();
            listView.setMinimumHeight(listHeight);
            for(int i = 0; i < listView.getChildCount(); i++){
                TextView child = (TextView) listView.getChildAt(i);
                child.setTextColor(context.getColor(R.color.purple));
            }
        });

        // Set the click listener for the list items
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(i < numItems){
                onSelected.onSelected(options.get(i), i);
            }else{
                if(onAdd == null)
                    throw new IndexOutOfBoundsException("no add but but index out of bounds size: " + numItems + ", index: " + i);
                onAdd.onAdd();
            }
        });
        System.out.println("finsihed creating list: " + listView.getAdapter().getCount());
    }

    public View getChild(int index){
        return listView.getChildAt(index);
    }
}
