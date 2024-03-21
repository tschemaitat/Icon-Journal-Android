package com.example.habittracker.Widgets;

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

import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Dictionary;
import com.example.habittracker.DropDownPage;
import com.example.habittracker.Structs.Pair;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;

public class DropDown extends androidx.appcompat.widget.AppCompatSpinner implements Widget {

    public static final String className = "drop down";
    private boolean dataSet = false;
    private Context context;
    private ArrayList<ArrayList<Pair<Integer, String>>> optionPages;
    private String structureKey = null;
    private String valueKey = null;
    private ArrayList<ItemPath> groups = new ArrayList<>();


    private String pageHeader;
    private String folder = "\uD83D\uDCC1";
    private String backCharacter = "⤴";

    private String widgetName = null;

    private DropDownPage parentPage = null;
    private DropDownPage currentPage = null;

    public static String nullValue = "select option";


    private ItemPath selectedValuePath = null;
    public DropDown(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void setName(String name){
        this.widgetName = name;
    }

    public DropDown(Context context, String widgetName){
        super(context);
        this.context = context;
        init();
        this.widgetName = widgetName;
    }

    public DropDown(Context context, int mode) {
        super(context, mode);
        this.context = context;
        init();
    }

    public DropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        super.setAdapter(adapter);
        selectedValuePath = new ItemPath(nullValue);

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
                selectedValuePath = new ItemPath(nullValue);

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
        System.out.println("drop down slected item: " + i);
        System.out.println("parent page: " + parentPage);
        System.out.println("current page: " + currentPage);

        //adapterView.clearAnimation();
        //System.out.println("currentPage = " + currentPage);
        //if user pressed select option
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
        System.out.println("<drop down>data changed: " + newValue);
        ArrayList<String> path = currentPage.getPath();
        path.add(newValue);
        selectedValuePath = new ItemPath(path);
        if(onDataChangedListener == null)
            throw new RuntimeException("<drop down: "+widgetName+">null listener set: " + setListener);
        onDataChangedListener.run();

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
    public DropDownParam getData(){
        if(!dataSet){
            throw new RuntimeException();
        }
        DropDownParam params = new DropDownParam(selectedValuePath, structureKey, valueKey, groups);
        return params;
    }

    @Override
    public DropDownValue value(){
        DropDownValue value = new DropDownValue(selectedValuePath);
        return value;
    }

    @Override
    public void setData(WidgetParam params){

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


    @Override
    public View getView() {
        return this;
    }


    public static class DropDownParam extends WidgetParam {
        public ItemPath selected;
        public String structureKey;
        public String valueKey;
        public ArrayList<ItemPath> groups;

        public DropDownParam(ItemPath selected, String structureKey, String valueKey, ArrayList<ItemPath> groups){
            if(structureKey == null)
                throw new RuntimeException();
            this.widgetClass = "drop down";
            this.selected = selected;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String structureKey, String valueKey, ArrayList<ItemPath> groups){
            if(structureKey == null)
                throw new RuntimeException();
            this.widgetClass = DropDown.className;
            this.selected = new ItemPath(nullValue);
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String structureKey, String valueKey){
            if(structureKey == null)
                throw new RuntimeException();
            this.widgetClass = DropDown.className;
            this.selected = new ItemPath(nullValue);
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = new ArrayList<>();
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

    public static class StaticDropDownParameters extends WidgetParam {
        DropDownPage page;
        public StaticDropDownParameters(DropDownPage page){
            this.widgetClass = DropDown.className;
            this.page = page;
        }

        public StaticDropDownParameters(ArrayList<String> options){
            this.widgetClass = DropDown.className;
            page = new DropDownPage("static paramters");
            for(String s: options)
                page.add(new DropDownPage(s));
        }
    }
}
