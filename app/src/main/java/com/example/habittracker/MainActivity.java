package com.example.habittracker;

import android.os.Bundle;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("creating calendar");

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutParent);
        LinearLayout parentWidgetGroup = findViewById(R.id.parentWidgetGroup);

        DropDown dropDown = findViewById(R.id.dropDown);
        dropDown.setPopUpParent(constraintLayout);
        //setOptions(dropDown);

        ArrayList<String> spinnerOptions = new ArrayList<>();
        spinnerOptions.add("Option 1");
        spinnerOptions.add("Option 2");
        spinnerOptions.add("Option 3");

        System.out.println("Finding spinner");
        // Initialize the Spinner
        CustomSpinner customSpinner = findViewById(R.id.spinner);




        setOptionsSpinner(customSpinner);

        Widget list = null;

        WidgetList.ListParams childListParams = new WidgetList.ListParams(customSpinner.getData());

        WidgetList.ListParams parentParams = new WidgetList.ListParams(childListParams);

        list = new WidgetList(this);
        System.out.println("setting data parent: ");
        list.setData(parentParams);
        list.setOnDataChangedListener(new Runnable() {
            @Override
            public void run() {

            }
        });




        parentWidgetGroup.addView(list.getView());
        StructureWidget structureWidget = new StructureWidget(this);
        parentWidgetGroup.addView(structureWidget.getView());
    }

    public void setOptionsSpinner(CustomSpinner customSpinner){

    }




}