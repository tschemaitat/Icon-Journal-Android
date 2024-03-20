package com.example.habittracker;


import com.example.habittracker.Slider.TextSlider;

import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;

public class Dictionary {

    static HashMap<String, DictEntry> structures = new HashMap<>();
    static{
        generateStructures();
    }

    public static String[] header(String structureKey){
        return structures.get(structureKey).header;
    }

    public static DropDownPage getPages(String structureKey, String valueKey, ArrayList<String> groups){
        if(structureKey.equals("default")){
            ArrayList<String> temp = new ArrayList<>();
            temp.add("body part");
            temp.add("type");
            return getPages("numbers", "name", temp);
        }
        if(structureKey.equals("structure keys")){
            return structureKeys();
        }
        DropDownPage parentPage = new DropDownPage("parent");


        DictEntry structureEntry = structures.get(structureKey);
        if(structureEntry == null){
            throw new RuntimeException(structureKey + " invalid");
        }
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





    public static DictEntry entry(String key){
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

        DictEntry entry = new DictEntry("numbers", header, numbers, DictEntry.dictionary);


        structures.put("numbers", entry);

        header = new String[]{
                "type"
        };
        numbers = new String[][]{
                {CustomEditText.className},
                {WidgetList.className},
                {CustomSpinner.className},

        };

        entry = new DictEntry("types", header, numbers, DictEntry.special);
        structures.put("types", entry);

        structures.put("people", new DictEntry("people",
                new String[]{"Username", "Identifier", "One-time password", "Recovery code", "First name", "Last name", "Department", "Location"},
                new String[][]{
                {"booker12", "9012", "12se74", "rb9012", "Rachel", "Booker", "Sales", "Manchester"},
                {"grey07", "2070", "04ap67", "lg2070", "Laura", "Grey", "Depot", "London"},
                {"johnson81", "4081", "30no86", "cj4081", "Craig", "Johnson", "Depot", "London"},
                {"jenkins46", "9346", "14ju73", "mj9346", "Mary", "Jenkins", "Engineering", "Manchester"},
                {"smith79", "5079", "09ja61", "js5079", "Jamie", "Smith", "Engineering", "Manchester"}
        }, DictEntry.dictionary));




    }

    public static DropDownPage structureKeys(){
        DropDownPage page = new DropDownPage("keys");
        for(String key: structures.keySet()){
            page.add(new DropDownPage(key));
        }
        return page;
    }
}