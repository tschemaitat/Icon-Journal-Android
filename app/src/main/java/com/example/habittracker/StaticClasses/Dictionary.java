package com.example.habittracker.StaticClasses;







import android.content.Context;

import com.example.habittracker.Structs.DataTree;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.StaticDropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.IntStringPair;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.DropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Dictionary {
    public static final String category = "category";
    public static final String journal = "journal";
    public static final String template = "template";




    //static HashMap<String, DictEntry> dictEntryMap = new HashMap<>();

    private static HashMap<Integer, Structure> structures = new HashMap<>();


    public static void addStructure(Structure structure){
        if(structure.getName() == null)
            throw new RuntimeException("structure name not set");
        structure.setNewKey();
        structures.put(structure.getKey(), structure);
    }

    static{


    }

    public static void generate(Context context){
        generateStructure();
        //generateDeepStructure();
        generateShowGenreStructure(context);
    }

    public static void saveStructure(Structure structure){
        if(structure == null)
            throw new RuntimeException("try to save null structure");
        if(structure.getName() == null)
            throw new RuntimeException("saved structure with null name");
        if(structure.getType() == null)
            throw new RuntimeException("saved structure with null type");
        System.out.println("saving structure with name: " + structure.getName());

        if(structure.getHeader() == null)
            throw new RuntimeException("saved structure couldn't make a header");

        structures.put(structure.getKey(), structure);
    }

    private static void generateStructure() {
        Structure structure = new Structure("test structure", new GroupWidget.GroupWidgetParam(null, new ArrayList<>()), "journal");
        addStructure(structure);
    }

    public static ArrayList<IntStringPair> getCategoryOptions(){
        return getStructureOptions(category);
    }
    public static ArrayList<IntStringPair> getJournalOptions(){
        return getStructureOptions(journal);
    }

    public static ArrayList<IntStringPair> getStructureOptions(String structureType){
        ArrayList<IntStringPair> keyPairs = new ArrayList<>();
        for(Structure structure: structures.values()){
            if(structure.getType() == null)
                throw new RuntimeException("structure: " + structure.getName() + " has null type");
            if(structure.getType().equals(structureType)){
                IntStringPair keyPair = new IntStringPair(structure.getKey(), structure.getName());
                keyPairs.add(keyPair);
            }
        }
        return keyPairs;
    }


    public static ArrayList<IntStringPair> getStructureOptions(){
        ArrayList<IntStringPair> keyPairs = new ArrayList<>();
        for(Structure structure: structures.values()){
            if(structure.getType() == null)
                throw new RuntimeException("structure: " + structure.getName() + " has null type");
            IntStringPair keyPair = new IntStringPair(structure.getKey(), structure.getName());
            keyPairs.add(keyPair);
        }
        return keyPairs;
    }

    public static ArrayList<String> getStructureNames(){
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Structure> structureArrayList = (ArrayList<Structure>) structures.values();
        for(Structure structure: structureArrayList)
            names.add(structure.getName());
        return names;
    }



    public static Structure getStructure(int key){
        Structure structure = structures.get(key);
        if(structure == null)
            throw new RuntimeException("structure key unknown: " + key);
        return structure;
    }


    public static DataTree header(Integer structureKey){
        Structure entry = structures.get(structureKey);
        if(entry == null)
            throw new NullPointerException("structureKey wrong: " + structureKey);
        DataTree header = entry.getHeader();
        if(header == null)
            throw new RuntimeException("header null structure key: " + structureKey);
        return header;
    }

    public static StaticDropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDown.className,
                "sliderfds"
        };

        return new StaticDropDownPage("types", new ArrayList<>(Arrays.asList(numbers)));
    }




    public static DropDownPage getGroupedPages(String structureKey, String valueKey, ArrayList<ItemPath> groups){
        Structure structure = structures.get(structureKey);

        if(structure == null){
            throw new RuntimeException("structureKey wrong: " + structureKey);
        }
        DataTree header = structure.getHeader();
        //System.out.println("header = " + header);
        ArrayList<DataTree> data = structure.getData();
        ArrayList<ArrayList<Integer>> groupToValue = getGroupIndexes(header, groups);

        int valueIndex = header.indexOf(valueKey);

        StaticDropDownPage parentPage = new StaticDropDownPage(structureKey +" page for " + valueKey);


        for(DataTree tree: data){

            ArrayList<StaticDropDownPage> currentPages = new ArrayList<>();
            currentPages.add(parentPage);
            String entryValue = tree.getString(valueIndex);
            ArrayList<ArrayList<String>> valuesOfGroups = tree.entryGroupValues(groupToValue);

            for(int groupIndex = 0; groupIndex < groups.size(); groupIndex++){

                ArrayList<String> valuesOfCurrentGroup = valuesOfGroups.get(groupIndex);
                ArrayList<StaticDropDownPage> newPages = new ArrayList<>();

                for(String groupValue: valuesOfCurrentGroup){

                    for(StaticDropDownPage page: currentPages){
                        newPages.add(page.getOrAdd(groupValue));
                    }

                }

                currentPages = newPages;

            }
            for(StaticDropDownPage page: currentPages)
                page.add(entryValue);
        }
        //System.out.println("parentPage = " + parentPage);
        return parentPage;
    }

    public static ArrayList<ArrayList<Integer>> getGroupIndexes(DataTree header, ArrayList<ItemPath> groups){
        ArrayList<ArrayList<Integer>> groupToValue = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            //System.out.println("finding ");
            groupToValue.add(header.indexOf(groups.get(i).getPath()));
        }
        return groupToValue;
    }

    public static void generateShowGenreStructure(Context context){


        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                new CustomEditText.EditTextParam("name"),
                new ListWidget.ListParam("genres", new EntryWidgetParam[]{
                        new CustomEditText.EditTextParam("genre")     }),
                new ListWidget.ListParam("attributes", new EntryWidgetParam[]{
                        new CustomEditText.EditTextParam("attribute")     })
        });

        DataTree dataTree1 = new DataTree().put(
                new DataTree("ReLIFE"),
                new DataTree().put(
                        new DataTree().put("romance"),
                        new DataTree().put("isekai"),
                        new DataTree().put("working together")),
                new DataTree().put(
                        new DataTree().put("composition"),
                        new DataTree().put("character progression"),
                        new DataTree().put("character"),
                        new DataTree().put("story"))
        );
        DataTree dataTree2 = new DataTree().put(
                new DataTree("NEW GAME!"),
                new DataTree().put(
                        new DataTree().put("working together"),
                        new DataTree().put("girls doing cute things")),
                new DataTree().put(
                        new DataTree().put("composition"),
                        new DataTree().put("character"),
                        new DataTree().put("dialogue"))
        );
        DataTree dataTree3 = new DataTree().put(
                new DataTree("The Rising of the Shield Hero"),
                new DataTree().put(
                        new DataTree().put("isekai")),
                new DataTree().put(
                        new DataTree().put("character"))
        );


        Structure structure = new Structure("shows", groupWidgetParam, category, new ArrayList<>(Arrays.asList(dataTree1, dataTree2, dataTree3)));
        System.out.println(groupWidgetParam.hierarchyString());
        saveStructure(structure);



    }
}