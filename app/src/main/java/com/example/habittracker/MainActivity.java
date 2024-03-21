package com.example.habittracker;

import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.Inflatables.Inflatable;
import com.example.habittracker.Inflatables.EditorSelectionPage;
import com.example.habittracker.Inflatables.TestPage;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static Context context;
    ScrollView scrollView;
    LinearLayout scrollLinearLayout;
    ConstraintLayout constraintLayout;
    LinearLayout buttonInflateBufferLayout;
    static LinearLayout inflateLayout;

    static Inflatable currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.context = this;
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        scrollLinearLayout = findViewById(R.id.scrollLinearLayout);
        constraintLayout = findViewById(R.id.constraintLayoutParent);
        buttonInflateBufferLayout = findViewById(R.id.ButtonInflateBufferLayout);

        inflateLayout = findViewById(R.id.inflateLayout);

        //currentLayout = testWidgetGroup;
        setupLayoutButtons();
        //setOptions(dropDown);
        inflateLayout(new TestPage(context));
    }

    public static void inflateLayout(Inflatable newLayout){
        if(currentLayout != null)
            inflateLayout.removeView(currentLayout.getView());
        inflateLayout.addView(newLayout.getView());
        currentLayout = newLayout;
    }

    public void setupLayoutButtons(){
        TextView editorButton = findViewById(R.id.EditorButton);
        editorButton.setOnClickListener(view -> inflateLayout(new EditorSelectionPage(context)));
        TextView categoryButton = findViewById(R.id.CategoriesButton);
        categoryButton.setOnClickListener(view -> {

        });
        TextView entryButton = findViewById(R.id.EntryButton);
        entryButton.setOnClickListener(view -> {

        });
        TextView testButton = findViewById(R.id.TestButton);
        testButton.setOnClickListener(view -> inflateLayout(new TestPage(context)));
    }

    public static LinearLayout createVerticalLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    public static ListView createList(ArrayList<String> items, AdapterView.OnItemClickListener listener){
        ListView listView = new ListView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        listView.setLayoutParams(params);
        System.out.println("listView.getDividerHeight() = " + listView.getDividerHeight());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_expandable_list_item_1, items);
        listView.setAdapter(adapter);
        System.out.println("listView.getDividerHeight() = " + listView.getDividerHeight());
        listView.post(new Runnable() {
            @Override
            public void run() {

                int childHeight = listView.getChildAt(0).getHeight();
                System.out.println("child height = " + childHeight);
                System.out.println("listView.getDividerHeight() = " + listView.getDividerHeight());
                int listHeight = childHeight * listView.getDividerHeight();
                //listView.getLayoutParams().height = listHeight;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, listHeight);
                listView.setLayoutParams(params);
            }
        });

        // Set the click listener for the list items
        listView.setOnItemClickListener(listener);
        return listView;
    }









}