package com.example.habittracker.StaticClasses;







import android.content.Context;

import com.example.habittracker.Structs.DataTree;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
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

    private static HashMap<String, Structure> structures = new HashMap<>();


    public static void addStructure(Structure structure){
        if(structure.getName() == null)
            throw new RuntimeException("structure name not set");

        structures.put(structure.getName(), structure);
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

        structures.put(structure.getName(), structure);
    }

    private static void generateStructure() {
        Structure structure = new Structure("test structure", new GroupWidget.GroupWidgetParam(null, new ArrayList<>()), "journal");
        addStructure(structure);
    }

    public static ArrayList<String> getCategoryKeys(){

        return getStructureKeys(category);
    }
    public static ArrayList<String> getJournalKeys(){
        return getStructureKeys(journal);
    }
    public static ArrayList<String> getTemplateKeys(){
        return getStructureKeys(template);
    }

    public static ArrayList<String> getStructureKeys(String structureType){
        ArrayList<String> keys = new ArrayList<>();
        for(Structure structure: structures.values()){
            if(structure.getType() == null)
                throw new RuntimeException("structure: " + structure.getName() + " has null type");
            if(structure.getType().equals(structureType)){
                keys.add(structure.getName());
            }

        }
        return keys;
    }

    public static ArrayList<String> getStructureKeys(){
        ArrayList<String> keys = new ArrayList<>(structures.keySet());
        return keys;
    }

    public static Structure getStructure(String key){
        Structure structure = structures.get(key);
        if(structure == null)
            throw new RuntimeException("structure key unknown: " + key);
        return structure;
    }


    public static DataTree header(String structureKey){
        Structure entry = structures.get(structureKey);
        if(entry == null)
            throw new NullPointerException("structureKey wrong: " + structureKey);
        DataTree header = entry.getHeader();
        if(header == null)
            throw new RuntimeException("header null structure key: " + structureKey);
        return header;
    }

    public static DropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDown.className,
                "sliderfds"
        };

        return new DropDownPage("types", new ArrayList<>(Arrays.asList(numbers)));
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

        DropDownPage parentPage = new DropDownPage(structureKey +" page for " + valueKey);


        for(DataTree tree: data){

            ArrayList<DropDownPage> currentPages = new ArrayList<>();
            currentPages.add(parentPage);
            String entryValue = tree.getString(valueIndex);
            ArrayList<ArrayList<String>> valuesOfGroups = tree.entryGroupValues(groupToValue);
            //System.out.println("valuesOfGroups = " + valuesOfGroups);

            for(int groupIndex = 0; groupIndex < groups.size(); groupIndex++){

                ArrayList<String> valuesOfCurrentGroup = valuesOfGroups.get(groupIndex);
                ArrayList<DropDownPage> newPages = new ArrayList<>();

                for(String groupValue: valuesOfCurrentGroup){

                    for(DropDownPage page: currentPages){
                        newPages.add(page.getOrAdd(groupValue));
                    }

                }

                currentPages = newPages;

            }
            for(DropDownPage page: currentPages)
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