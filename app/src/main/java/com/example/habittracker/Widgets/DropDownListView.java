package com.example.habittracker.Widgets;



import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.example.habittracker.DataTree;
import com.example.habittracker.SelectionView;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Dictionary;
import com.example.habittracker.DropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;
import java.util.Arrays;

public class DropDownListView extends EntryWidget {

    public static final String className = "drop down";
    private boolean dataSet = false;
    private Context context;
    private String structureKey = null;
    private String valueKey = null;
    private ArrayList<ItemPath> groups = new ArrayList<>();


    private String folder = "\uD83D\uDCC1";

    private DropDownPage parentPage = null;
    private DropDownPage currentPage = null;

    public static String nullValue = "select option";

    CustomPopup customPopup;




    private ItemPath selectedValuePath = null;
    public DropDownListView(Context context) {
        super(context);
        this.context = context;
        init();
    }



    private void init(){
        createPopUp();
        selectedValuePath = new ItemPath(nullValue);

        //super.performClick();
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("spinner got clicked!");
//            }
//        });
    }

    private void setOptions(String title, ArrayList<String> spinnerOptions){
        customPopup.setText(title, spinnerOptions);
        selectedValuePath = null;
    }

    private void setOptionsOfPage(){
        setOptions(currentPage.name, formatOptions(currentPage));
    }


    private void createPopUp(){
        customPopup = new CustomPopup(context, "", new ArrayList<>(), (stringValue, position) -> {
            DropDownPage clickedPage = currentPage.get(position);
            if(!clickedPage.hasChildren()){
                //option chosen is not a folder
                //System.out.println("option: \n" + clickedPage.name + "\n is not a folder, leaving");
                customPopup.close();
                createPopUp();
                dataChanged(clickedPage.name);

                return;
            }

            setOptionsOfPage();


        }, () -> {
            //on back button
            currentPage = currentPage.parent;
            setOptionsOfPage();
        }, ()->{
            //on nothing selected


            selectedValuePath = null;
            customPopup.close();
            createPopUp();
        });
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

    public void setSelected(String value){
        ArrayList<String> pathStack = new ArrayList<>();

        selectedValuePath = new ItemPath(value);
    }

    public void setSelected(ItemPath itemPath){
        selectedValuePath = itemPath;
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
            setOptionsOfPage();
            return;
        }
        StaticDropDownParameters staticParams = (StaticDropDownParameters) params;
        dataSet = true;
        parentPage = staticParams.page;
        currentPage = parentPage;
        selectedValuePath = new ItemPath(nullValue);
        setOptionsOfPage();

    }

    public void resetValue(){
        selectedValuePath = null;
        currentPage = parentPage;
    }


    public static class DropDownParam extends EntryWidgetParam {
        public ItemPath selected;
        public String structureKey;
        public String valueKey;
        public ArrayList<ItemPath> groups;
        public String name = "null";

        public DropDownParam(String name, ItemPath selected, String structureKey, String valueKey, ArrayList<ItemPath> groups){
            super(name, DropDownSpinner.className);
            if(structureKey == null)
                throw new RuntimeException();
            this.selected = selected;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String name, String structureKey, String valueKey, ArrayList<ItemPath> groups){
            super(name, DropDownSpinner.className);
            if(structureKey == null)
                throw new RuntimeException();
            this.selected = new ItemPath(nullValue);
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String name, String structureKey, String valueKey){
            super(name, DropDownSpinner.className);
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
            super(name, DropDownSpinner.className);
            this.page = page;
        }

        public StaticDropDownParameters(String name, ArrayList<String> options){
            super(name, DropDownSpinner.className);
            page = new DropDownPage("static paramters");
            for(String s: options)
                page.add(new DropDownPage(s));
        }

        public StaticDropDownParameters(String name, String[] optionsArray){
            super(name, DropDownSpinner.className);
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
