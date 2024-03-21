package com.example.habittracker;







import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.KeyPair;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.DropDown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Dictionary {

    static HashMap<String, DictEntry> dictEntryMap = new HashMap<>();

    private static HashMap<String, Structure> categoryStructures = new HashMap<>();
    private static HashMap<String, Structure> journalStructures = new HashMap<>();
    private static HashMap<String, Structure> templateStructures = new HashMap<>();
    private static HashMap<String, Structure> structures = new HashMap<>();

    static{
        generateDeepStructure();
        generateShowGenreStructure();
    }

    public static ArrayList<String> getCategoryKeys(){
        return new ArrayList<>(categoryStructures.keySet());
    }
    public static ArrayList<String> getJournalKeys(){
        return new ArrayList<>(journalStructures.keySet());
    }

    public static Structure getStructure(String key){
        return structures.get(key);
    }


    public static DataTree header(String structureKey){
        DictEntry entry = dictEntryMap.get(structureKey);
        if(entry == null)
            throw new NullPointerException("structureKey wrong: " + structureKey);
        return entry.header;
    }

    public static DropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDown.className,
                "slider"
        };

        return new DropDownPage("types", new ArrayList<>(Arrays.asList(numbers)));
    }

    public static DropDownPage getStructureKeys(){
        DropDownPage page = new DropDownPage("keys");
        for(String s: dictEntryMap.keySet()){
            page.add(s);
        }
        return page;
    }


    public static DropDownPage getGroupedPages(String structureKey, String valueKey, ArrayList<ItemPath> groups){
        DictEntry dictEntry = dictEntryMap.get(structureKey);

        if(dictEntry == null){
            throw new RuntimeException("structureKey wrong: " + structureKey);
        }
        DataTree header = dictEntry.header;
        System.out.println("header = " + header);
        ArrayList<DataTree> data = dictEntry.entries;
        ArrayList<ArrayList<Integer>> groupToValue = getGroupIndexes(header, groups);

        int valueIndex = header.indexOf(valueKey);

        DropDownPage parentPage = new DropDownPage(structureKey +" page for " + valueKey);


        for(DataTree tree: data){

            ArrayList<DropDownPage> currentPages = new ArrayList<>();
            currentPages.add(parentPage);
            String entryValue = tree.getString(valueIndex);
            ArrayList<ArrayList<String>> valuesOfGroups = tree.entryGroupValues(groupToValue);
            System.out.println("valuesOfGroups = " + valuesOfGroups);

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
        System.out.println("parentPage = " + parentPage);
        return parentPage;
    }

    public static ArrayList<ArrayList<Integer>> getGroupIndexes(DataTree header, ArrayList<ItemPath> groups){
        ArrayList<ArrayList<Integer>> groupToValue = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            System.out.println("finding ");
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
        dictEntryMap.put(shows.key, shows);




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
        dictEntryMap.put(shows.key, shows);
    }




    public static DictEntry entry(String key){
        return dictEntryMap.get(key);
    }

    public static DropDownPage structureKeys(){
        DropDownPage page = new DropDownPage("keys");
        for(String key: dictEntryMap.keySet()){
            page.add(new DropDownPage(key));
        }
        return page;
    }
}