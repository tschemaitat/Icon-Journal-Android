package com.example.habittracker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomSpinner extends androidx.appcompat.widget.AppCompatSpinner implements Widget {

    public static final String className = "drop down";
    boolean dataSet = false;
    Context context;
    ArrayList<ArrayList<Pair<Integer, String>>> optionPages;
    String structureKey = null;
    String valueKey = null;
    ArrayList<String> groups = new ArrayList<>();


    String pageHeader;
    String folder = "\uD83D\uDCC1";
    String backCharacter = "⤴";

    DropDownPage parentPage = null;
    DropDownPage currentPage = null;

    public static String nullValue = "select option";

    String selectedValue = null;
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
        setListener();
        selectedValue = nullValue;

        //super.performClick();
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("spinner got clicked!");
//            }
//        });
    }

    private void setOptions(ArrayList<String> spinnerOptions){
        //System.out.println("setting options");
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
                int numberSpecialOptions = 1;
                if(currentPage != parentPage)
                    numberSpecialOptions = 2;
                if(position < numberSpecialOptions)
                    view.setTypeface(view.getTypeface(), Typeface.BOLD); // Make text bold
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        super.setAdapter(adapter);
        selectedValue = nullValue;

        //System.out.println("super.getAdapter().getCount() = " + super.getAdapter().getCount());

    }
    boolean dummy_selection_zero = true;


    private void setListener(){
        super.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //System.out.println("Item selected: " + i);
                processSelectedItem(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentPage = parentPage;
                setOptions(formatOptions(parentPage));
                setSelection(0);
                selectedValue = nullValue;

                //System.out.println("Nothing selected");
            }
        });
    }

//    private void processSelectedItem(AdapterView<?> adapterView, View view, int i, long l){
//        //adapterView.clearAnimation();
//        System.out.println("currentPage = " + currentPage);
//        //if user pressed select option
//        if(i == 0){
//            selectedValue = nullValue;
//
//            return;
//        }
//        //if user pressed back button
//        if( currentPage != 0 && i == 1){
//            int newPage = pageStack.remove(pageStack.size() - 1);
//            System.out.println("going back from " + currentPage + "to " + newPage);
//            currentPage = newPage;
//        }
//        //if user press a value
//        else{
//            int numberNotOptions = 1;
//            if(currentPage != 0)
//                numberNotOptions = 2;
//
//            Pair<Integer, String> selectedOption = optionPages.get(currentPage).get(i - numberNotOptions);
//            System.out.printf("selected: " + selectedOption);
//            if(selectedOption.getKey() == -1){
//                //option chosen is not a folder
//                System.out.println("option is not a folder, leaving");
//
//                dataChanged(selectedOption);
//                return;
//            }
//            //add new page to page stack
//            pageStack.add(currentPage);
//            currentPage = selectedOption.getKey();
//
//            System.out.println("new current page: " + currentPage);
//        }
//
//        int numberOptions = 1;
//        if(currentPage != 0)
//            numberOptions = 2;
//
//        setOptions(formatOptions(optionPages.get(currentPage)), numberOptions);
//
//        //adapterView.setSelection(-1);
//        dummy_selection_zero = true;
//        System.out.println("dummy_selection_zero = " + dummy_selection_zero);
//                        adapterView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                System.out.println("performing click");
//                                adapterView.performClick();
//                            }
//                        }, 0);
////        System.out.println("performing click");
////        adapterView.performClick();
//    }

    private void processSelectedItem(AdapterView<?> adapterView, View view, int i, long l){
        //adapterView.clearAnimation();
        //System.out.println("currentPage = " + currentPage);
        //if user pressed select option
        if(i == 0){
            selectedValue = nullValue;

            return;
        }
        //if user pressed back button
        if( currentPage != parentPage && i == 1){
            currentPage = currentPage.parent;
            //System.out.println("going back from " + currentPage + "to " + currentPage);
        }
        //if user press a value
        else{
            int numberNotOptions = 1;
            if(currentPage != parentPage)
                numberNotOptions = 2;
            //set option selected to the index of the page's options
            DropDownPage clickedPage = currentPage.get(i - numberNotOptions);
            //System.out.printf("selected: " + clickedPage.name);
            if(!clickedPage.hasChildren()){
                //option chosen is not a folder
                //System.out.println("option: \n" + clickedPage.name + "\n is not a folder, leaving");
                dataChanged(clickedPage.name);
                return;
            }
            //add new page to page stack

            //set currentpage
            currentPage = clickedPage;

            //System.out.println("new current page: " + currentPage);
        }
        int numSpecialButton = 1;
        if(currentPage != parentPage){
            numSpecialButton = 2;
        }

        //set options to new page
        setOptions(formatOptions(currentPage));

        dummy_selection_zero = true;
        System.out.println("dummy_selection_zero = " + dummy_selection_zero);
        doDelayedClick(adapterView);
    }

    private void doDelayedClick(AdapterView<?> adapterView){
        adapterView.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("performing click");
                adapterView.performClick();
            }
        }, 0);
    }

    private void dataChanged(String newValue){
        selectedValue = newValue;
        try{
            onDataChangedListener.run();
        }catch (Exception exception){
            throw new RuntimeException("null listener set: " + setListener);
        }

    }


    public ArrayList<String> formatOptions(DropDownPage page){
        ArrayList<String> result = new ArrayList<>();


        result.add(nullValue);
        if(currentPage != parentPage)
            result.add("Back");
        for(DropDownPage option: page.children){
            if(!option.hasChildren()){
                result.add(option.name);
                continue;
            }
            result.add(folder+" " + option.name);
        }

        //System.out.println("formatted page: \n\t" + result);

        return result;
    }

    boolean setListener = false;
    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        //System.out.println("\n\n\tcustom spinner set listener: " + runnable + "\n\n");
        setListener = true;
        onDataChangedListener = runnable;


    }

    @Override
    public DropDownParams getData(){
        if(!dataSet){
            throw new RuntimeException();
        }
        DropDownParams params = new DropDownParams(selectedValue, structureKey, valueKey, groups);
        return params;
    }

    @Override
    public DropDownValue value(){
        DropDownValue value = new DropDownValue(selectedValue);
        return value;
    }

    @Override
    public void setData(WidgetParams params){

        if(params instanceof DropDownParams){
            dataSet = true;
            DropDownParams dropDownParams = ((DropDownParams) params);
            parentPage = Dictionary.getPages(dropDownParams.structureKey, dropDownParams.valueKey, dropDownParams.groups);
            structureKey = dropDownParams.structureKey;
            valueKey = dropDownParams.valueKey;
            groups = dropDownParams.groups;


            //System.out.println("setting data: " + this);

            currentPage = parentPage;
            selectedValue = dropDownParams.selected;
            int specialOptions = 1;
            //System.out.println("setting data for spinner: \n" + parentPage);
            setOptions(formatOptions(currentPage));
            return;
        }
        StaticDropDownParameters staticParams = (StaticDropDownParameters) params;
        dataSet = true;
        parentPage = staticParams.page;
        currentPage = parentPage;
        selectedValue = nullValue;
        setOptions(formatOptions(currentPage));

    }

    public void setValue(String value){
        selectedValue = value;
    }


    @Override
    public View getView() {
        return this;
    }


    public static class DropDownParams extends WidgetParams{
        public String selected;
        public String structureKey;
        public String valueKey;
        public ArrayList<String> groups;

        public DropDownParams(String selected, String structureKey, String valueKey, ArrayList<String> groups){
            if(structureKey == null)
                throw new RuntimeException();
            this.widgetClass = "drop down";
            this.selected = selected;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParams(String structureKey, String valueKey, ArrayList<String> groups){
            if(structureKey == null)
                throw new RuntimeException();
            this.widgetClass = CustomSpinner.className;
            this.selected = nullValue;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParams(String structureKey, String valueKey){
            if(structureKey == null)
                throw new RuntimeException();
            this.widgetClass = CustomSpinner.className;
            this.selected = nullValue;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = new ArrayList<>();
        }

        public String toString(){
            return "{" + className + ", " + selected + ", " +structureKey + ", " +valueKey + ", " +groups + "}";
        }

    }

    public static class DropDownValue extends WidgetValue{
        public String selected;
        public DropDownValue(String selected){
            this.selected = selected;

        }
    }

    public static class StaticDropDownParameters extends WidgetParams{
        DropDownPage page;
        public StaticDropDownParameters(DropDownPage page){
            this.widgetClass = CustomSpinner.className;
            this.page = page;
        }

        public StaticDropDownParameters(ArrayList<String> options){
            this.widgetClass = CustomSpinner.className;
            page = new DropDownPage("static paramters");
            for(String s: options)
                page.add(new DropDownPage(s));
        }
    }
}
