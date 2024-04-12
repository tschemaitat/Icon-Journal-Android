package com.example.habittracker.Structs;


import java.util.ArrayList;
import java.util.Arrays;

public class EntryValueTree {
    private CachedString string;
    private RefItemPath itemPath;
    private ArrayList<EntryValueTree> list = new ArrayList<>();
    public EntryValueTree(){
    }

    public EntryValueTree(RefItemPath itemPath){
        this.itemPath = itemPath;
    }

    public RefItemPath getItemPath(){
        return itemPath;
    }


    public EntryValueTree put(ArrayList<EntryValueTree> entryValueTrees){
        for(EntryValueTree entryValueTree : entryValueTrees){
            list.add(entryValueTree);
        }
        return this;
    }

    public EntryValueTree put(EntryValueTree... entryValueTrees){
        put(new ArrayList<>(Arrays.asList(entryValueTrees)));
        return this;
    }

    public EntryValueTree(CachedString string){
        this.string = string;
    }



    public EntryValueTree put(CachedString ... strings){
        for(CachedString s: strings){
            EntryValueTree entryValueTree = new EntryValueTree();
            entryValueTree.string = s;
            list.add(entryValueTree);
        }
        return this;
    }

    public ArrayList<EntryValueTree> getList(){
        return (ArrayList<EntryValueTree>) list.clone();
    }

    public void add(CachedString string){
        list.add(new EntryValueTree(string));
    }

    public void add(EntryValueTree tree){
        list.add(tree);
    }

    public CachedString getCachedString(){
        return string;
    }

    public EntryValueTree addTree(){
        EntryValueTree tree = new EntryValueTree();
        list.add(tree);
        return tree;
    }

    public CachedString getCachedString(int index){
        return (list.get(index).string);
    }

    public EntryValueTree getTree(int index){
        return ((EntryValueTree) list.get(index));
    }

    public int indexOf(CachedString value){
        int i = 0;
        for(EntryValueTree tree: list){
            if(tree.string.equals(value))
                return i;
            i++;
        }
        return -1;
    }



    public boolean hasChildren(CachedString name){
        int index = indexOf(name);
        return hasChildren(index);
    }

    public boolean hasChildren(int index){
        if(list.get(index).size() > 0)
            return true;
        return false;
    }

    public static ArrayList<EntryValueTree> gatherChildren(ArrayList<EntryValueTree> parentTrees, int treeIndex){
        ArrayList<EntryValueTree> currentTrees = new ArrayList<>();
        for(EntryValueTree tree: parentTrees){
            //System.out.println("index from tree: " + tree.nameAndLength());
            currentTrees.add(tree.getTree(treeIndex));
        }
        return currentTrees;
    }

    public static ArrayList<CachedString> gatherValues(ArrayList<EntryValueTree> parentTrees, int treeIndex){
        ArrayList<CachedString> values = new ArrayList<>();
        for(EntryValueTree tree: parentTrees){
            //System.out.println("index from tree: " + tree.nameAndLength());
            values.add(tree.getCachedString(treeIndex));
        }
        return values;
    }

    public static ArrayList<EntryValueTree> gatherTreesFromArray(ArrayList<EntryValueTree> currentTrees){
        ArrayList<EntryValueTree> gatheredTrees = new ArrayList<>();
        for(EntryValueTree tree: currentTrees){
            for(int gatherIndex = 0; gatherIndex < tree.size(); gatherIndex++){
                gatheredTrees.add(tree.getTree(gatherIndex));
            }
        }
        return gatheredTrees;
    }

    public static ArrayList<CachedString> gatherStringFromArray(ArrayList<EntryValueTree> currentTrees){
        //System.out.println("\ngathering array: " + currentTrees + " \n");
        ArrayList<CachedString> gatheredStrings = new ArrayList<>();
        for(EntryValueTree tree: currentTrees){
            for(int gatherIndex = 0; gatherIndex < tree.size(); gatherIndex++){
                gatheredStrings.add(tree.getCachedString(gatherIndex));
            }
        }
        return gatheredStrings;
    }

    public static ArrayList<EntryValueTree> processList(ArrayList<EntryValueTree> parentTrees, int treeIndex){
        ArrayList<EntryValueTree> currentTrees = gatherChildren(parentTrees, treeIndex);
        return gatherTreesFromArray(currentTrees);
    }



    //get values from group indexes. The indexes are the key for the location
    public ArrayList<CachedString> getValuesFromPath(ValueTreePath indexes){
        //System.out.println("get values from indexes");
        //System.out.println(this.hierarchy());
        //System.out.println("indexes = " + indexes);
        int lastIsTreeNum = indexes.get(indexes.size() - 1);
        boolean lastIsTree = false;
        if(lastIsTreeNum == -2)
            lastIsTree = true;
        //System.out.println("getting value of data tree from indexes");
        //System.out.println(this.hierarchy() + "\n\n");
        ArrayList<EntryValueTree> parentTrees = new ArrayList<>();
        parentTrees.add(this);

        for(int i = 0; i < indexes.size() - 2; i++){
            int treeIndex = indexes.get(i);
            //System.out.println("("+i+")current tree index: " + treeIndex);
            parentTrees = processList(parentTrees, treeIndex);
            //System.out.println(parentTrees.get(0).hierarchy());
        }
        int lastIndex = indexes.get(indexes.size() - 2);
        EntryValueTree testTree = parentTrees.get(0);
        ArrayList<CachedString> values;
        if(lastIsTree){
            ArrayList<EntryValueTree> lastTrees = gatherChildren(parentTrees, lastIndex);
            values = gatherStringFromArray(lastTrees);
        }else
            values = gatherValues(parentTrees, lastIndex);
        //System.out.println("values = " + values);
        return values;
    }

    public int size() {
        return list.size();
    }



    public ArrayList<ArrayList<CachedString>> getValueListFromPathList(ArrayList<ValueTreePath> indexes){
        ArrayList<ArrayList<CachedString>> values = new ArrayList<>();
        for(ValueTreePath index: indexes){
            values.add(getValuesFromPath(index));
        }
        return values;
    }



    public String nameAndLength(){
        return "{ " + string + ", " + list.size() + " }";
    }

    public String toString(){
        return "{ DataTree " + string + ", " + list.size() + " }";

    }

    public String hierarchy(){
        return hierarchy(0);
    }

    private String hierarchy(int tabs){
        String tab = "\t";
        String tabString = "";
        for(int i = 0; i < tabs; i++){
            tabString += tab;
        }
        String result = "";
        result += tabString + nameAndLength() + "\n";
        for(Object obj: list){
            if( obj instanceof String){
                result += tabString + tab + obj.toString() + "\n";
                continue;
            }
            if(obj instanceof EntryValueTree){
                result += ((EntryValueTree) obj).hierarchy(tabs + 1);
            }
        }
        return result;
    }


}
