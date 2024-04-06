package com.example.habittracker.Widgets;



import android.content.Context;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.Structs.DataTree;
import com.example.habittracker.ViewWidgets.CustomPopup;
import com.example.habittracker.ViewWidgets.SelectionView;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;
import java.util.Arrays;

public class DropDown extends EntryWidget {

    public static final String className = "drop down";
    private boolean dataSet = false;
    private Context context;
    private String structureKey = null;
    private String valueKey = null;
    private ArrayList<ItemPath> groups = new ArrayList<>();


    private String folder = "\uD83D\uDCC1";

    private DropDownPage parentPage = null;
    private DropDownPage currentPage = null;

    public String nullValue = "select option";

    CustomPopup customPopup;

    SelectionView buttonSelectionView;




    private ItemPath selectedValuePath = null;
    public DropDown(Context context) {
        super(context);
        this.context = context;
        init();
    }



    private void init(){

        selectedValuePath = new ItemPath(nullValue);
        createButton();

    }

    private void setOptions(String title, ArrayList<String> spinnerOptions){
        if(currentPage == parentPage){
            customPopup.setText(nullValue, spinnerOptions);
        }else
            customPopup.setText(title, spinnerOptions);

    }

    private void setOptionsOfPage(){
        setOptions(currentPage.name, formatOptions(currentPage));
    }

    private void createButton(){
        buttonSelectionView = new SelectionView(context, new String[]{"select option"}, (stringValue, position, key) -> {
            //on button pressed
            createPopUp();
        });
        buttonSelectionView.setColor(ColorPalette.textPurple);

        setChild(buttonSelectionView.getView());
    }



    private void createPopUp(){
        customPopup = new CustomPopup(context, "", new ArrayList<>(), (stringValue, position, key) -> {
            onItemSelected(position);
            }, () -> {
            onBackSelected();
        }, ()->{
            //on nothing selected
            //dataChanged(null);
            customPopup.close();
            customPopup = null;
        });
        setOptionsOfPage();
        customPopup.enableBack();
        customPopup.showPopupWindow(this.getView());
    }



    private void onItemSelected(int position){
        DropDownPage clickedPage = currentPage.get(position);
        if(!clickedPage.hasChildren()){
            //System.out.println("got final value: " + clickedPage.name);
            //System.out.println("option: \n" + clickedPage.name + "\n is not a folder, leaving");

            dataChanged(clickedPage.name);
            return;
        }
        if(currentPage == parentPage){
            customPopup.addBackIcon();
        }
        currentPage = clickedPage;
        setOptionsOfPage();
    }

    private void onBackSelected() {
        //System.out.println("back button");
        if(currentPage == parentPage){
            dataChanged(null);
            return;
        }
        if(currentPage.parent == parentPage){
            customPopup.removeBackIcon();
        }
        //on back button
        currentPage = currentPage.parent;
        setOptionsOfPage();
    }

    private void dataChanged(String newValue){
        //System.out.println("<drop down>data changed: " + newValue);
        ArrayList<String> path = currentPage.getPath();
        path.add(newValue);
        selectedValuePath = new ItemPath(path);



        if(newValue == null)
            buttonSelectionView.setText(new String[]{"select option"});
        else
            buttonSelectionView.setText(new String[]{getSelectedString()});

        customPopup.close();
        customPopup = null;

        onDataChangedListener().run();
    }


    public ArrayList<String> formatOptions(DropDownPage page){
        ArrayList<String> result = new ArrayList<>();
        if(!page.hasChildren())
            throw new RuntimeException("page doesn't have children");

        for(DropDownPage option: page.children){
            if(!option.hasChildren()){
                result.add(option.name);
                continue;
            }
            result.add(folder+" " + option.name);
        }

        //System.out.println("formatted page: \n\t" + result);
        if(result.size() == 0)
            throw new RuntimeException("result doesn't have any values");
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

    @Override
    public void setValue(DataTree dataTree) {
        setSelected(dataTree.getItemPath());
    }

    public void setSelected(String value){
        setSelected(new ItemPath(value));
    }

    public void setSelected(ItemPath itemPath){
        System.out.println("setting value of drop to: " + itemPath.getName());
        buttonSelectionView.setText(new String[]{itemPath.getName()});
        selectedValuePath = itemPath;
    }

    public String getSelectedString(){
        if(selectedValuePath == null)
            return null;
        return selectedValuePath.getName();
    }

    public ItemPath getSelectedPath(){
        ItemPath itemPath = selectedValuePath;
        if(itemPath == null)
            return null;
        ItemPath copy = new ItemPath((ArrayList<String>) itemPath.getPath().clone());
        if(getSelectedString() == null)
            return null;
        if(getSelectedString().equals(nullValue))
            return null;
        return copy;
    }

    @Override
    public DataTree getDataTree() {
        return new DataTree(selectedValuePath);
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
            //setOptionsOfPage();
            return;
        }
        StaticDropDownParameters staticParams = (StaticDropDownParameters) params;
        dataSet = true;
        parentPage = staticParams.page;
        currentPage = parentPage;
        selectedValuePath = null;
        //setOptionsOfPage();

    }

    public void resetValue(){
        selectedValuePath = null;
        currentPage = parentPage;
    }

    public void setHint(String select_type) {
        nullValue = select_type;
        buttonSelectionView.setText(new String[]{select_type});
    }

    public void setError() {
        System.out.println("<drop down> setting error");
        buttonSelectionView.setColor(ColorPalette.redText);
    }

    public void resetError() {
        buttonSelectionView.setColor(ColorPalette.textPurple);
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
            this.selected = null;
            this.structureKey = structureKey;
            this.valueKey = valueKey;
            this.groups = groups;
        }

        public DropDownParam(String name, String structureKey, String valueKey){
            super(name, DropDown.className);
            if(structureKey == null)
                throw new RuntimeException();
            this.selected = null;
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
