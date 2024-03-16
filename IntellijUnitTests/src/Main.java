import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ArrayList<String> groups = new ArrayList<>();
        groups.add("body part");
        groups.add("type");
        DropDownPage pages = getPages("numbers", "name", groups);
        System.out.println(pages);
    }
    static HashMap<String, JSONObject> structures = new HashMap<>();
    static{
        generateStructures();
    }
//    public static ArrayList<Pair<String, ArrayList>> getPages(String structureKey, String valueKey, ArrayList<String> groups){
//        ArrayList<Pair<String, ArrayList>> parentPage = new ArrayList<>();
//
//        String[] header = header(structureKey);
//        ArrayList<String> headerList = new ArrayList<>(Arrays.asList(header));
//        String[][] matrix = matrix(structureKey);
//
//        ArrayList<Integer> groupIndexList = new ArrayList<>();
//        for(String groupName: groups)
//            groupIndexList.add(headerList.indexOf(groupName));
//
//        int valueIndex = headerList.indexOf(valueKey);
//
//        for(String[] entry: matrix){
//            ArrayList currentPage = parentPage;
//            System.out.println("entry: " + Arrays.toString(entry));
//            for(int i = 0; i < groupIndexList.size(); i++){
//
//                String group = entry[groupIndexList.get(i)];
//                ArrayList newPage = findPage(currentPage, group);
//                if(i < groupIndexList.size() - 1){
//                    if(newPage == null){
//                        newPage = new ArrayList<Pair<String, ArrayList>>();
//                        currentPage.add(new Pair<>(group, newPage));
//
//                    }
//                    currentPage = newPage;
//                }else{
//                    if(newPage == null){
//                        newPage = new ArrayList<Pair<String, ArrayList>>();
//                        currentPage.add(new Pair<>(group, newPage));
//                    }
//
//                    newPage.add(new Pair(entry[valueIndex], null));
//                }
//            }
//            //create page if it does not exist yet
//
//        }
//        System.out.println(parentPage);
//        traversePages(parentPage);
//
//        return parentPage;
//    }

    public static DropDownPage getPages(String structureKey, String valueKey, ArrayList<String> groups){
        DropDownPage parentPage = new DropDownPage("parent");


        String[] header = header(structureKey);
        ArrayList<String> headerList = new ArrayList<>(Arrays.asList(header));
        String[][] matrix = matrix(structureKey);

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

        return parentPage;
    }



    public static String[][] matrix(String structureKey){

        String[][] matrix = ((String[][]) structures.get(structureKey).get("matrix"));
        return matrix;


    }

    public static String[] header(String structureKey){

        String[] header = ((String[]) structures.get(structureKey).get("header"));
        return header;


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


        JSONObject json = new JSONObject();

        json.put("header", header);
        json.put("matrix", numbers);
        structures.put("numbers", json);





    }
}


//name, bodypart, type


//name, bodypart, type