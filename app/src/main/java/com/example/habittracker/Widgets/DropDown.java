package com.example.habittracker.Widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.habittracker.CustomLinearLayout;
import com.example.habittracker.DataTree;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Dictionary;
import com.example.habittracker.DropDownPage;
import com.example.habittracker.Structs.Pair;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;
import java.util.Arrays;

public class DropDown extends EntryWidget {

    public static final String className = "drop down";
    private boolean dataSet = false;
    private Context context;
    private ArrayList<ArrayList<Pair<Integer, String>>> optionPages;
    private String structureKey = null;
    private String valueKey = null;
    private ArrayList<ItemPath> groups = new ArrayList<>();


    private String folder = "\uD83D\uDCC1";

    private DropDownPage parentPage = null;
    private DropDownPage currentPage = null;

    public static String nullValue = "select option";

    AppCompatSpinner spinner;




    private ItemPath selectedValuePath = null;
    public DropDown(Context context) {
        super(context);
        spinner = new AppCompatSpinner(context);
        setChild(spinner);
        this.context = context;
        init();
    }



    private void init(){
        setListener();
        selectedValuePath = new ItemPath(nullValue);

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
        spinner.setAdapter(adapter);
        selectedValuePath = new ItemPath(nullValue);

        //System.out.println("super.getAdapter().getCount() = " + super.getAdapter().getCount());

    }
    boolean dummy_selection_zero = true;


    private void setListener(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //System.out.println("Item selected: " + i);
                processSelectedItem(adapterView, view, i, l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currentPage = parentPage;
                setOptions(formatOptions(parentPage));
                spinner.setSelection(0);
                selectedValuePath = new ItemPath(nullValue);

                //System.out.println("Nothing selected");
            }
        });
    }

    private void processSelectedItem(AdapterView<?> adapterView, View view, int i, long l){
        if(i == 0){
            selectedValuePath = new ItemPath(nullValue);

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
        //System.out.println("dummy_selection_zero = " + dummy_selection_zero);
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
        System.out.println("<drop down>data changed: " + newValue);
        ArrayList<String> path = currentPage.getPath();
        path.add(newValue);
        selectedValuePath = new ItemPath(path);

        onDataChangedListener().run();

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

    @Override
    public DropDownParam getParam(){
        if(!dataSet){
            throw new RuntimeException();
        }
        DropDownParam params = new DropDownParam(getName(), selectedValuePath, structureKey, valueKey, groups);
        return params;
    }

    public String getSelectedString(){
        String selected = selectedValuePath.getName();
        if(selected.equals(nullValue))
            return null;
        return selected;
    }

    public ItemPath getSelectedPath(){
        ItemPath itemPath = selectedValuePath;
        ItemPath copy = new ItemPath((ArrayList<String>) itemPath.getPath().clone());
        if(getSelectedString() == null)
            return null;
        return copy;
    }

    @Override
    public DataTree getDataTree() {
        return new DataTree(selectedValuePath.getName());
    }

    @Override
    public void setParamCustom(EntryWidgetParam params){
        if(params instanceof DropDownParam){
            dataSet = true;
            DropDownParam dropDownParams = ((DropDownParam) params);
            parentPage = Dictionary.getGroupedPages(dropDownParams.structureKey, dropDownParams.valueKey, dropDownParams.groups);
            structureKey = dropDownParams.structureKey;
            valueKey = dropDownParams.valueKey;
            groups = dropDownParams.groups;


            //System.out.println("setting data: " + this);

            currentPage = parentPage;
            selectedValuePath = dropDownParams.selected;
            int specialOptions = 1;
            //System.out.println("setting data for spinner: \n" + parentPage);
            setOptions(formatOptions(currentPage));
            return;
        }
        StaticDropDownParameters staticParams = (StaticDropDownParameters) params;
        dataSet = true;
        parentPage = staticParams.page;
        currentPage = parentPage;
        selectedValuePath = new ItemPath(nullValue);
        setOptions(formatOptions(currentPage));

    }

    public void resetValue(){
        selectedValuePath = new ItemPath(nullValue);
        currentPage = parentPage;
    }


    public static class DropDownParam extends EntryWidgetParam {
        public ItemPath selected;
        public String structureKey;
        public String valueKey;
        public ArrayList<ItemPath> groups;
        public String name = "null";

        public DropDownParam(String name, ItemPath selected, String structureKey, String valueKey, ArrayList<ItemPath> groups){
            super(name, DropDown.className);
            if(structureKey == null)
                throw new RuntimeException();
            this.selected = selected;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String name, String structureKey, String valueKey, ArrayList<ItemPath> groups){
            super(name, DropDown.className);
            if(structureKey == null)
                throw new RuntimeException();
            this.selected = new ItemPath(nullValue);
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String name, String structureKey, String valueKey){
            super(name, DropDown.className);
            if(structureKey == null)
                throw new RuntimeException();
            this.selected = new ItemPath(nullValue);
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = new ArrayList<>();
        }

        @Override
        public String hierarchyString(int numTabs){
            String singleTab = "\t";
            String tabs = "";
            for(int i = 0; i < numTabs; i++)
                tabs += singleTab;
            return tabs + "drop down\n"
                    + tabs + "\tstructure: " + structureKey + "\n"
                    + tabs + "\tvalue: " + valueKey + "\n"
                    + tabs + "\tgroups: " + groups + "\n";
        }

        @Override
        public DataTree header() {
            return new DataTree(name);
        }

        public String toString(){
            return "{" + className + ", " + selected + ", " +structureKey + ", " +valueKey + ", " +groups + "}";
        }

    }

    public static class DropDownValue extends WidgetValue {
        public ItemPath selected;
        public DropDownValue(ItemPath selected){
            this.selected = selected;

        }
    }

    public static class StaticDropDownParameters extends EntryWidgetParam {
        DropDownPage page;
        public StaticDropDownParameters(String name, DropDownPage page){
            super(name, DropDown.className);
            this.page = page;
        }

        public StaticDropDownParameters(String name, ArrayList<String> options){
            super(name, DropDown.className);
            page = new DropDownPage("static paramters");
            for(String s: options)
                page.add(new DropDownPage(s));
        }

        public StaticDropDownParameters(String name, String[] optionsArray){
            super(name, DropDown.className);
            ArrayList<String> options = new ArrayList<>(Arrays.asList(optionsArray));
            page = new DropDownPage("static paramters");
            for(String s: options)
                page.add(new DropDownPage(s));
        }


        @Override
        public String hierarchyString(int numTabs) {
            return null;
        }

        @Override
        public DataTree header() {
            throw new RuntimeException();
        }
    }
}
