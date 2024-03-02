package com.example.habittracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class CustomSpinner extends androidx.appcompat.widget.AppCompatSpinner {
    Context context;
    ArrayList<ArrayList<Pair<Integer, String>>> optionPages;

    ArrayList<Integer> pageStack = new ArrayList<>();
    int currentPage = 0;
    String pageHeader;
    String folder = "\uD83D\uDCC1";
    String backCharacter = "⤴";
    public CustomSpinner(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
        this.context = context;
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){
        //super.performClick();
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("spinner got clicked!");
//            }
//        });
    }

    private void setOptions(ArrayList<String> spinnerOptions, int numberSpecialOptions){
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerOptions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerOptions) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                // Customize your item here (for the selected item shown in the spinner)
                //view.setTextColor(Color.BLUE); // Set text color
                //view.setTypeface(view.getTypeface(), Typeface.BOLD); // Make text bold
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                // Customize your dropdown item here
                // Example: Set different colors for odd and even rows
//                if (position % 2 == 0) {
//                    view.setTextColor(Color.RED); // Set text color for even rows
//                } else {
//                    view.setTextColor(Color.GREEN); // Set text color for odd rows
//                }
                if(position < numberSpecialOptions)
                view.setTypeface(view.getTypeface(), Typeface.BOLD); // Make text bold
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        super.setAdapter(adapter);
    }
    boolean dummy_selection_zero = true;
    public void setPages(ArrayList<ArrayList<Pair<Integer, String>>> optionPages) {
        this.optionPages = optionPages;

        setOptions(formatOptions(optionPages.get(0)), 1);
        int page_count = 0;
        for(ArrayList<Pair<Integer, String>> page: optionPages){
            System.out.println("page: " + page_count);
            for(Pair<Integer, String> option: page){
                System.out.println("\t" + option);
            }
            page_count++;
        }


        super.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Item selected: " + i);
                processSelectedItem(adapterView, view, i, l);
//                adapterView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        processSelectedItem(adapterView, view, i, l);
//                    }
//                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentPage = 0;
                setOptions(formatOptions(optionPages.get(0)), 1);
                setSelection(0);
                System.out.println("Nothing selected");
            }
        });
    }

    private void processSelectedItem(AdapterView<?> adapterView, View view, int i, long l){
        //adapterView.clearAnimation();
        System.out.println("currentPage = " + currentPage);
        if(i == 0){
            return;
        }
        if( currentPage != 0 && i == 1){
            int newPage = pageStack.remove(pageStack.size() - 1);
            System.out.println("going back from " + currentPage + "to " + newPage);
            currentPage = newPage;
        }else{
            int numberNotOptions = 1;
            if(currentPage != 0)
                numberNotOptions = 2;
            Pair<Integer, String> selectedOption = optionPages.get(currentPage).get(i - numberNotOptions);
            System.out.printf("selected: " + selectedOption);
            if(selectedOption.getKey() == -1){
                System.out.println("option is not a folder, leaving");

                return;
            }
            pageStack.add(currentPage);
            currentPage = selectedOption.getKey();
            pageHeader = selectedOption.getValue();
            System.out.println("new current page: " + currentPage);
        }




        int numberOptions = 1;
        if(currentPage != 0)
            numberOptions = 2;

        setOptions(formatOptions(optionPages.get(currentPage)), numberOptions);

        //adapterView.setSelection(-1);
        dummy_selection_zero = true;
        System.out.println("dummy_selection_zero = " + dummy_selection_zero);
                        adapterView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("performing click");
                                adapterView.performClick();
                            }
                        }, 0);
//        System.out.println("performing click");
//        adapterView.performClick();
    }



    public ArrayList<String> formatOptions(ArrayList<Pair<Integer, String>> options){
        ArrayList<String> result = new ArrayList<>();

        result.add("Select Option");
        if(currentPage != 0)
            result.add("Back");
        for(Pair<Integer, String> pair: options){
            if(pair.getKey() == -1){
                result.add(pair.getValue());
                continue;
            }
            result.add(folder+" " + pair.getValue());
        }



        return result;
    }
}
