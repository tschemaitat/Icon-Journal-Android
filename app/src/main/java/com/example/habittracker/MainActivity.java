package com.example.habittracker;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.sql.SQLOutput;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("creating calendar");

        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayoutParent);

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

    }

    public void setOptionsSpinner(CustomSpinner customSpinner){
        ArrayList<ArrayList<Pair<Integer, String>>> optionPages = new ArrayList<>();
        ArrayList<Pair<Integer, String>> page1 = new ArrayList<>();
        ArrayList<Pair<Integer, String>> page2 = new ArrayList<>();
        ArrayList<Pair<Integer, String>> page3 = new ArrayList<>();
        ArrayList<Pair<Integer, String>> page4 = new ArrayList<>();

        page1.add(new Pair<>(1, "option won"));
        page1.add(new Pair<>(2, "option too"));
        page1.add(new Pair<>(-1, "option tree"));
        page1.add(new Pair<>(-1, "option floor"));

        optionPages.add(page1);
        optionPages.add(page2);
        optionPages.add(page3);
        optionPages.add(page4);

        page2.add(new Pair<>(-1, "option 5"));
        page2.add(new Pair<>(-1, "option 6"));

        page3.add(new Pair<>(-1, "option 10"));
        page3.add(new Pair<>(-1, "option 11"));
        page3.add(new Pair<>(3, "option 12"));

        page4.add(new Pair<>(-1, "option wow"));
        customSpinner.setPages(optionPages);
    }

    public void setOptions(DropDown dropDown){
        ArrayList<Pair<Integer, String>> optionPages = new ArrayList<>();

        optionPages.add(new Pair<>(-1, "option won"));
        optionPages.add(new Pair<>(-1, "option too"));
        optionPages.add(new Pair<>(-1, "option tree"));
        optionPages.add(new Pair<>(-1, "option floor"));
        dropDown.setPages(optionPages);
    }
}