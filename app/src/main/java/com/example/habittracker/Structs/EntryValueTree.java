package com.example.habittracker.Structs;


import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.CachedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EntryValueTree {
//    private CachedString string;
//    private RefItemPath itemPath;
//    private ListItemId listItemId;
//    private EntryValueTree parent = null;
//    private boolean setIds = false;
//    private boolean setIdFunction = false;
//    private ArrayList<EntryValueTree> list = new ArrayList<>();
//    public EntryValueTree(){
//    }
//
//    public ListItemId getListItemId(){
//        return listItemId;
//    }
//    public EntryValueTree(RefItemPath itemPath){
//        this.itemPath = itemPath;
//    }
//
//    public RefItemPath getItemPath(){
//        return itemPath;
//    }
//
//
//    public EntryValueTree put(ArrayList<EntryValueTree> entryValueTrees){
//        for(EntryValueTree entryValueTree : entryValueTrees){
//            list.add(entryValueTree);
//            entryValueTree.parent = this;
//        }
//        return this;
//    }
//
//    public EntryValueTree put(EntryValueTree... entryValueTrees){
//        return put(new ArrayList<>(Arrays.asList(entryValueTrees)));
//    }
//
//    public EntryValueTree(CachedString string){
//        this.string = string;
//    }
//
//
//
//    public EntryValueTree put(CachedString... strings){
//        for(CachedString s: strings){
//            EntryValueTree entryValueTree = new EntryValueTree();
//            entryValueTree.string = s;
//            entryValueTree.parent = this;
//            list.add(entryValueTree);
//        }
//        return this;
//    }
//
//    public ArrayList<EntryValueTree> getList(){
//        return (ArrayList<EntryValueTree>) list.clone();
//    }
//
//    public void add(CachedString string){
//        list.add(new EntryValueTree(string));
//    }
//
//    public void add(EntryValueTree tree){
//        list.add(tree);
//    }
//
//    public CachedString getCachedString(){
//        return string;
//    }
//
//    public EntryValueTree addTree(){
//        EntryValueTree tree = new EntryValueTree();
//        list.add(tree);
//        return tree;
//    }
//
//    public CachedString getCachedString(int index){
//        return (list.get(index).string);
//    }
//
//    public EntryValueTree getTree(int index){
//        return list.get(index);
//    }
//
//    public EntryValueTree getTree(ListItemId id){
//        for(EntryValueTree child: list){
//            if(child.getListItemId().equals(id)){
//                return child;
//            }
//        }
//        throw new RuntimeException();
//    }
//
//    public int indexOf(CachedString value){
//        int i = 0;
//        for(EntryValueTree tree: list){
//            if(tree.string.equals(value))
//                return i;
//            i++;
//        }
//        return -1;
//    }
//
//
//
//    public boolean hasChildren(CachedString name){
//        int index = indexOf(name);
//        return hasChildren(index);
//    }
//
//    public boolean hasChildren(int index){
//        if(list.get(index).size() > 0)
//            return true;
//        return false;
//    }
//
//    public static ArrayList<EntryValueTree> gatherChildren(ArrayList<EntryValueTree> parentTrees, int treeIndex){
//        ArrayList<EntryValueTree> currentTrees = new ArrayList<>();
//        for(EntryValueTree tree: parentTrees){
//            //System.out.println("index from tree: " + tree.nameAndLength());
//            currentTrees.add(tree.getTree(treeIndex));
//        }
//        return currentTrees;
//    }
//
//    public static ArrayList<CachedString> gatherValues(ArrayList<EntryValueTree> parentTrees, int treeIndex){
//        ArrayList<CachedString> values = new ArrayList<>();
//        for(EntryValueTree tree: parentTrees){
//            //System.out.println("index from tree: " + tree.nameAndLength());
//            values.add(tree.getCachedString(treeIndex));
//        }
//        return values;
//    }
//
//    public static ArrayList<EntryValueTree> gatherTreesFromArray(ArrayList<EntryValueTree> currentTrees){
//        ArrayList<EntryValueTree> gatheredTrees = new ArrayList<>();
//        for(EntryValueTree tree: currentTrees){
//            for(int gatherIndex = 0; gatherIndex < tree.size(); gatherIndex++){
//                gatheredTrees.add(tree.getTree(gatherIndex));
//            }
//        }
//        return gatheredTrees;
//    }
//
//    public static ArrayList<CachedString> gatherStringFromArray(ArrayList<EntryValueTree> currentTrees){
//        //System.out.println("\ngathering array: " + currentTrees + " \n");
//        ArrayList<CachedString> gatheredStrings = new ArrayList<>();
//        for(EntryValueTree tree: currentTrees){
//            for(int gatherIndex = 0; gatherIndex < tree.size(); gatherIndex++){
//                gatheredStrings.add(tree.getCachedString(gatherIndex));
//            }
//        }
//        return gatheredStrings;
//    }
//
//    public static ArrayList<EntryValueTree> processList(ArrayList<EntryValueTree> parentTrees, int treeIndex){
//        ArrayList<EntryValueTree> currentTrees = gatherChildren(parentTrees, treeIndex);
//        return gatherTreesFromArray(currentTrees);
//    }
//
//
//
//    //get values from group indexes. The indexes are the key for the location
//    public ArrayList<EntryValueTree> getValuesFromPath(ValueTreePath indexes){
//        //MainActivity.log("indexes: \n" + indexes);
//        if(indexes.size() == 0){
//            MainActivity.log(this.hierarchy());
//            throw new RuntimeException();
//        }
//
//
//        if(indexes.size() == 1){
//            EntryValueTree cachedString = list.get(indexes.get(0));
//            if(cachedString == null){
//                //MainActivity.log(indexes + "\n" + this.hierarchy());
//            }
//            return new ArrayList<>(Collections.singleton(cachedString));
//        }
//
//
//        ArrayList<EntryValueTree> parentTrees = new ArrayList<>();
//        parentTrees.add(this);
//
//        for(int i = 0; i < indexes.size(); i++){
//            int treeIndex = indexes.get(i);
//            parentTrees = processList(parentTrees, treeIndex);
//        }
//
//        return parentTrees;
//    }
//
//    public int size() {
//        return list.size();
//    }
//
//
//
//    public ArrayList<ArrayList<EntryValueTree>> getValueListFromPathList(ArrayList<ValueTreePath> indexes){
//        ArrayList<ArrayList<EntryValueTree>> values = new ArrayList<>();
//        for(ValueTreePath index: indexes){
//            values.add(getValuesFromPath(index));
//        }
//        return values;
//    }
//
//
//
//    public String nameAndLength(){
//        String name;
//        if(string != null)
//            name = string.getString();
//        else if(itemPath != null)
//            name = itemPath.toString();
//        else
//            name = "null item";
//
//        return "{ " + name + ", " + list.size() + ", id: "+listItemId+" }";
//    }
//
//    public String toString(){
//        return nameAndLength();
//
//    }
//
//    public String hierarchy(){
//        return hierarchy(0);
//    }
//
//    private String hierarchy(int tabs){
//        String tab = "\t";
//        String tabString = "";
//        for(int i = 0; i < tabs; i++){
//            tabString += tab;
//        }
//        String result = "";
//        result += tabString + nameAndLength() + "\n";
//        for(Object obj: list){
//            if( obj instanceof String){
//                result += tabString + tab + obj.toString() + "\n";
//                continue;
//            }
//            if(obj instanceof EntryValueTree){
//                result += ((EntryValueTree) obj).hierarchy(tabs + 1);
//            }
//        }
//        return result;
//    }
//
//
//    public void setIdOfTree() {
//        if(setIds)
//            throw new RuntimeException();
//        setIdIteration();
//    }
//
//    private void setIdIteration(){
//        setIds = true;
//        int max = -1000;
//        for(EntryValueTree entryValueTree: list){
//            ListItemId id = entryValueTree.getListItemId();
//            if(id != null)
//                max = Math.max(max, id.getId());
//        }
//        max++;
//        for(EntryValueTree entryValueTree: list){
//            ListItemId id = entryValueTree.getListItemId();
//            if(id == null){
//                entryValueTree.listItemId = new ListItemId(max);
//                max++;
//            }
//        }
//        for(EntryValueTree entryValueTree: list)
//            entryValueTree.setIdIteration();
//    }
//
//    public ArrayList<ListItemId> getListItemIdList(){
//        ArrayList<ListItemId> list = new ArrayList<>();
//        getListItemIdListIteration(list);
//        return list;
//    }
//
//    private void getListItemIdListIteration(ArrayList<ListItemId> list){
//        if(parent != null)
//            parent.getListItemIdListIteration(list);
//        list.add(listItemId);
//    }
//
//    public EntryValueTree getValue(ValueTreePath path, ArrayList<ListItemId> listIdList) {
//        if(path.size() != listIdList.size()){
//            MainActivity.log(path + "\n" + listIdList);
//            throw new RuntimeException();
//        }
//        if(path.size() == 1){
//            return list.get(path.get(0));
//        }
//        EntryValueTree parentTree = this;
//        for(int i = 0; i < path.size(); i++){
//            parentTree = parentTree.getTree(path.get(i));
//            parentTree = parentTree.getTree(listIdList.get(i));
//        }
//        return parentTree;
//    }
//
//    public EntryValueTree getParent() {
//        return parent;
//    }
//
//    public void setId(ListItemId id) {
//        if(setIdFunction)
//            throw new RuntimeException();
//        setIdFunction = true;
//        this.listItemId = id;
//    }
//
//    public CachedString generateCachedStringForDisplay() {
//        if(string != null)
//            return string;
//        if(itemPath != null)
//            return itemPath.getLast();
//        throw new RuntimeException();
//    }
//
//
}
