package com.example.habittracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ScrollView scrollView;
    LinearLayout scrollLinearLayout;
    LinearLayout testWidgetGroup;
    ConstraintLayout constraintLayout;
    LinearLayout verticalLinearLayout;

    LinearLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        scrollLinearLayout = findViewById(R.id.scrollLinearLayout);
        constraintLayout = findViewById(R.id.constraintLayoutParent);
        testWidgetGroup = findViewById(R.id.parentWidgetGroup);

        verticalLinearLayout = findViewById(R.id.verticalLinearLayout);

        currentLayout = testWidgetGroup;
        setupLayoutButtons();
        //setOptions(dropDown);
    }

    public void setupLayoutButtons(){
        TextView editorButton = findViewById(R.id.EditorButton);
        editorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verticalLinearLayout.removeView(currentLayout);
                verticalLinearLayout.addView(inflateEditor());
            }
        });
        TextView categoryButton = findViewById(R.id.CategoriesButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verticalLinearLayout.removeView(currentLayout);
                verticalLinearLayout.addView(inflateCategories());
            }
        });
        TextView entryButton = findViewById(R.id.EntryButton);
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verticalLinearLayout.removeView(currentLayout);
                verticalLinearLayout.addView(inflateEntry());
            }
        });
        TextView testButton = findViewById(R.id.TestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verticalLinearLayout.removeView(currentLayout);
                verticalLinearLayout.addView(inflateTest());
            }
        });
    }

    public LinearLayout inflateEditor(){
        System.out.println("inflating editor");
        LinearLayout linearLayout = createVerticalLayout();
        ArrayList<String> items = Dictionary.structureKeys().getOptions();
        System.out.println("items = " + items);
        View list = createList(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = items.get(position);
                // Handle the click event for each item. You can customize this as needed.
                Toast.makeText(MainActivity.this, "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        linearLayout.addView(list);

        return linearLayout;
    }
    public LinearLayout inflateCategories(){
        return null;
    }
    public LinearLayout inflateEntry(){
        return null;
    }
    public LinearLayout inflateTest(){
        return testWidgetGroup;
    }

    public LinearLayout createVerticalLayout(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    public View createList(ArrayList<String> items, AdapterView.OnItemClickListener listener){
        ListView listView = new ListView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        listView.setLayoutParams(params);
        System.out.println("listView.getDividerHeight() = " + listView.getDividerHeight());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
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


    public void setupTestLayout(){
        System.out.println("Finding spinner");
        // Initialize the Spinner
        CustomSpinner customSpinner = findViewById(R.id.spinner);


        System.out.println("\n\n\n\nmain activity---------------");

        setOptionsSpinner(customSpinner);

        Widget list = null;
        WidgetList.ListParams childListParams = new WidgetList.ListParams(customSpinner.getData());
        WidgetList.ListParams parentParams = new WidgetList.ListParams(childListParams);

        System.out.println("parentParams = " + parentParams);
        System.out.println("childListParams = " + childListParams);

        System.out.println("making parent list: ");
        list = new WidgetList(this);
        System.out.println("setting data parent: ");
        list.setData(parentParams);
        list.setOnDataChangedListener(new Runnable() {
            @Override
            public void run() {

            }
        });




        testWidgetGroup.addView(list.getView());
        StructureWidget structureWidget = new StructureWidget(this);
        testWidgetGroup.addView(structureWidget.getView());
        System.out.println("main activity finished------------\n\n\n");
    }

    public void setOptionsSpinner(CustomSpinner customSpinner){
        CustomSpinner.DropDownParams params = new CustomSpinner.DropDownParams("default", null);
        customSpinner.setData(params);
    }




}