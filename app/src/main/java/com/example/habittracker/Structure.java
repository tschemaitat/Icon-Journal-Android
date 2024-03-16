package com.example.habittracker;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;

public class Structure {

    static HashMap<String, StructureEntry> structures = new HashMap<>();
    static{
        generateStructures();
    }

    public static DropDownPage getPages(String structureKey, String valueKey, ArrayList<String> groups){
        DropDownPage parentPage = new DropDownPage("parent");


        StructureEntry structureEntry = structures.get(structureKey);
        ArrayList<String> headerList = new ArrayList<>(Arrays.asList(structureEntry.header));
        String[][] matrix = structureEntry.data;

        ArrayList<Integer> groupIndexList = new ArrayList<>();
        for(String groupName: groups)
            groupIndexList.add(headerList.indexOf(groupName));

        int valueIndex = headerList.indexOf(valueKey);

        for(String[] entry: matrix){
            DropDownPage currentPage = parentPage;
            System.out.println("entry: " + Arrays.toString(entry));
            for(int i = 0; i < groupIndexList.size(); i++){

                String group = entry[groupIndexList.get(i)];
                DropDownPage newPage = currentPage.get(group);

                if(newPage == null){
                    newPage = new DropDownPage(group);
                    currentPage.add(newPage);
                }
                currentPage = newPage;

            }
            currentPage.add(new DropDownPage(entry[valueIndex]));
        }

        System.out.println(parentPage);

        parentPage.init();
        return parentPage;
    }





    public static StructureEntry entry(String key){
        return structures.get(key);
    }

    public static void generateStructures(){

        String[] header = new String[]{
                "name", "body part", "type"
        };
        String[][] numbers = new String[][]{
                {"pushup", "arms", "anarobic"},
                {"squat", "legs", "anarobic"},
                {"running", "legs", "arobic"},
                {"bike", "legs", "arobic"},
                {"pullup", "arms", "anarobic"}
        };

        StructureEntry entry = new StructureEntry("numbers", header, numbers);


        structures.put("numbers", entry);

        header = new String[]{
                "type"
        };
        numbers = new String[][]{
                {"edit text"},
                {"list"},
                {"custom spinner"},
                {"slider"}
        };

        entry = new StructureEntry("types", header, numbers);
        structures.put("types", entry);








    }
}