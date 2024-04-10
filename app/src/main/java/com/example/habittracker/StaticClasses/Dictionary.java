package com.example.habittracker.StaticClasses;







import android.content.Context;

import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
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
        structures.put(structure.getId().getId(), structure);
    }

    static{


    }

    public static void generate(Context context){
        generateStructure();
        //generateDeepStructure();
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

        structures.put(structure.getId().getId(), structure);
    }

    private static void generateStructure() {
        Structure structure = new Structure("test structure", new GroupWidget.GroupWidgetParam(null, new ArrayList<>()), "journal");
        addStructure(structure);
    }

    public static ArrayList<PayloadOption> getCategoryOptions(){
        return getStructureOptions(category);
    }
    public static ArrayList<PayloadOption> getJournalOptions(){
        return getStructureOptions(journal);
    }

    public static ArrayList<PayloadOption> getStructureOptions(String structureType){
        ArrayList<PayloadOption> keyPairs = new ArrayList<>();
        for(Structure structure: structures.values()){
            if(structure.getType() == null)
                throw new RuntimeException("structure: " + structure.getName() + " has null type");
            if(structure.getType().equals(structureType)){
                PayloadOption keyPair = new PayloadOption(structure.getName(), structure.getId());
                keyPairs.add(keyPair);
            }
        }
        return keyPairs;
    }


    public static ArrayList<PayloadOption> getStructureOptions(){
        ArrayList<PayloadOption> keyPairs = new ArrayList<>();
        for(Structure structure: structures.values()){
            if(structure.getType() == null)
                throw new RuntimeException("structure: " + structure.getName() + " has null type");
            PayloadOption keyPair = new PayloadOption(structure.getName(), structure.getId());
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



    public static Structure getStructure(StructureId key){
        Structure structure = structures.get(key);
        if(structure == null)
            throw new RuntimeException("structure key unknown: " + key);
        return structure;
    }










//    public static void generateShowGenreStructure(Context context){
//
//
//        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
//                new CustomEditText.EditTextParam("name"),
//                new ListWidget.ListParam("genres", new EntryWidgetParam[]{
//                        new CustomEditText.EditTextParam("genre")     }),
//                new ListWidget.ListParam("attributes", new EntryWidgetParam[]{
//                        new CustomEditText.EditTextParam("attribute")     })
//        });
//
//        DataTree dataTree1 = new DataTree().put(
//                new DataTree("ReLIFE"),
//                new DataTree().put(
//                        new DataTree().put("romance"),
//                        new DataTree().put("isekai"),
//                        new DataTree().put("working together")),
//                new DataTree().put(
//                        new DataTree().put("composition"),
//                        new DataTree().put("character progression"),
//                        new DataTree().put("character"),
//                        new DataTree().put("story"))
//        );
//        DataTree dataTree2 = new DataTree().put(
//                new DataTree("NEW GAME!"),
//                new DataTree().put(
//                        new DataTree().put("working together"),
//                        new DataTree().put("girls doing cute things")),
//                new DataTree().put(
//                        new DataTree().put("composition"),
//                        new DataTree().put("character"),
//                        new DataTree().put("dialogue"))
//        );
//        DataTree dataTree3 = new DataTree().put(
//                new DataTree("The Rising of the Shield Hero"),
//                new DataTree().put(
//                        new DataTree().put("isekai")),
//                new DataTree().put(
//                        new DataTree().put("character"))
//        );
//
//
//        Structure structure = new Structure("shows", groupWidgetParam, category, new ArrayList<>(Arrays.asList(dataTree1, dataTree2, dataTree3)));
//        System.out.println(groupWidgetParam.hierarchyString());
//        saveStructure(structure);
//
//
//
//    }
}