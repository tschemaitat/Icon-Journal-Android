package com.example.habittracker.StaticClasses;







import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.Entry;
import com.example.habittracker.structurePack.Structure;

import com.example.habittracker.defaultImportPackage.ArrayList;
import java.util.HashMap;

public class Dictionary {
    public static final String category = "category";
    public static final String journal = "journal";
    public static final String template = "template";




    //static HashMap<String, DictEntry> dictEntryMap = new HashMap<>();

    private static HashMap<StructureId, Structure> structures = new HashMap<>();



    static{


    }

    public static ArrayList<String> getStructuresIdAndName(){
        ArrayList<String> strings = new ArrayList<>();
        for(Structure structure: structures.values()){
            strings.add(structure.getCachedName().getString() + structure.getId());
        }

        return strings;
    }

    public static Structure addStructureFromSave(String name, GroupWidgetParam param, String type,
                                                 ArrayList<Entry> entries, StructureId structureId){
        checkNull(name, param, type);
        Structure newStructure = new Structure(name, param, type, entries, structureId);
        checkExistingId(newStructure.getId());

        structures.put(newStructure.getId(), newStructure);
        return newStructure;
    }

    public static void checkExistingId(StructureId newId){
        if(structures.get(newId) != null){
            MainActivity.log(getStructuresIdAndName().toString());
            throw new RuntimeException("already have id");
        }
    }

    public static Structure addStructure(String name, GroupWidgetParam param, String type){
        checkNull(name, param, type);
        Structure newStructure = new Structure(name, param, type);

        MainActivity.log("saving structure with name: " + newStructure.getCachedName() + "and id: " + newStructure.getId());

        structures.put(newStructure.getId(), newStructure);
        return newStructure;
    }

    public static void checkNull(String name, GroupWidgetParam param, String type){
        if(param == null)
            throw new RuntimeException("try to save null structure");
        if(name == null)
            throw new RuntimeException("saved structure with null name");
        if(type == null)
            throw new RuntimeException("saved structure with null type");
    }

    public static void editStructure(Structure structure, GroupWidgetParam param){
        MainActivity.log("editing structure: " + structure.getCachedName());
        Structure newStructure = new Structure(structure.getCachedName().getString(), param,
                structure.getType(), structure.getEntries(), structure.getId());

        MainActivity.log("new structure: " + newStructure.getCachedName());
        structures.remove(structure.getId());
        structures.put(newStructure.getId(), newStructure);


    }

    public static String getStructureDebug(){
        StringBuilder result = new StringBuilder();
        for(Structure structure: structures.values()){
            result.append(structure.getCachedName()).append(", ").append(structure.getType()).append("\n");
        }
        return result.toString();
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
                throw new RuntimeException("structure: " + structure.getCachedName() + " has null type");
            if(structure.getType().equals(structureType)){
                PayloadOption keyPair = new PayloadOption(structure.getCachedName(), structure);
                keyPairs.add(keyPair);
            }
        }
        return keyPairs;
    }


    public static ArrayList<PayloadOption> getStructureOptions(){
        ArrayList<PayloadOption> keyPairs = new ArrayList<>();
        for(Structure structure: structures.values()){
            if(structure.getType() == null)
                throw new RuntimeException("structure: " + structure.getCachedName() + " has null type");
            PayloadOption keyPair = new PayloadOption(structure.getCachedName(), structure);
            keyPairs.add(keyPair);
        }
        return keyPairs;
    }

    public static ArrayList<CachedString> getStructureNames(){
        ArrayList<CachedString> names = new ArrayList<>();
        ArrayList<Structure> structureArrayList = (ArrayList<Structure>) structures.values();
        for(Structure structure: structureArrayList)
            names.add(structure.getCachedName());
        return names;
    }



    public static Structure getStructure(StructureId id){
        if(id == null)
            throw new RuntimeException();
        Structure structure = structures.get(id);
        if(structure == null)
            throw new RuntimeException("structure key unknown: " + id);
        return structure;
    }

    public static ArrayList<Structure> getStructures() {
        return new ArrayList<>(structures.values());
    }
}