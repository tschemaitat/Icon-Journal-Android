package com.example.habittracker;







import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.KeyPair;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.DropDownSpinner;
import com.example.habittracker.Widgets.GroupWidget;

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
        //generateDeepStructure();
        //generateShowGenreStructure();
        generateStructure();
    }

    public static void saveStructure(Structure structure){
        if(structure == null)
            throw new RuntimeException("try to save null structure");
        if(structure.getName() == null)
            throw new RuntimeException("saved structure with null name");
        if(structure.getType() == null)
            throw new RuntimeException("saved structure with null type");
        System.out.println("saving structure with name: " + structure.getName());
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
        return entry.getHeader();
    }

    public static DropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDownSpinner.className,
                "slider"
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
        ArrayList<DataTree> data = structure.getEntries();
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

    public static void generateShowGenreStructure(){
        Object[] reLife = new Object[]{
                "ReLIFE",
                new Object[]{
                        "romance",
                        "isekai",
                        "working together"
                },
                new Object[]{
                        "composition",
                        "character progression",
                        "character",
                        "story"
                }
        };

        Object[] newGame = new Object[]{
                "NEW GAME!",
                new Object[]{
                        "working together",
                        "girls doing cute things"
                },
                new Object[]{
                        "character",
                        "dialogue"
                }
        };

        Object[] shield = new Object[]{
                "The Rising of the Shield Hero",
                new Object[]{
                        "isekai"
                },
                new Object[]{
                        "character"
                }
        };
        Object[][] objects = new Object[][]{reLife, newGame, shield};
        ArrayList<DataTree> trees = DataTree.convert(objects);

        Object[] header = new Object[]{
                "name",
                new KeyPair("genres",new Object[]{"genre"}),
                new KeyPair("attributes",new Object[]{"attribute"})
        };
        DataTree showHeader = DataTree.convertHeader(header);
        DictEntry shows = new DictEntry("shows", showHeader, trees, DictEntry.dictionary);
        //dictEntryMap.put(shows.key, shows);




    }

    public static void generateDeepStructure(){
        // ReLIFE

        Object[][] specificGenres = new Object[][]{
                new Object[]{
                        "if new game and isekai combined",
                        new Object[]{//array of specific genres
                                new Object[]{//one specific genre
                                        "isekai comedy",
                                        new Object[]{//array of sub genres
                                                new Object[]{
                                                        "isekai",
                                                        "0.4"
                                                },
                                                new Object[]{
                                                        "comedy",
                                                        "0.6"
                                                },
                                        },
                                        "0.5"
                                },
                                new Object[]{
                                        "slice of girls making games",
                                        new Object[]{
                                                new Object[]{
                                                        "slice of life",
                                                        "0.4"
                                                },
                                                new Object[]{
                                                        "girls doing cute things",
                                                        "0.6"
                                                },
                                                new Object[]{
                                                        "video games",
                                                        "0.6"
                                                },
                                        },
                                        "0.5"
                                }
                        }
                }};
        Object[] header = new Object[]{
                "name",
                new KeyPair("specific genres",new Object[]{
                        "specific genre name",
                        new KeyPair("sub genres", new Object[]{
                                "sub genre",
                                "weight"
                        }),
                        "weight"
                })
        };
        DictEntry shows = new DictEntry("specific genres", DataTree.convertHeader(header), DataTree.convert(specificGenres), DictEntry.dictionary);
        //dictEntryMap.put(shows.key, shows);
    }
}