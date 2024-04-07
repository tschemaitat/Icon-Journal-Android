package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.StringMap;

import java.util.ArrayList;

public class RefDropDownPage implements DropDownPage{
    public static int nameKeyForParent = -10;
    public RefDropDownPage parent = null;
    private int nameKey;
    public ArrayList<RefDropDownPage> children;

    public RefDropDownPage(int nameKey){
        this.nameKey = nameKey;
        children = new ArrayList<>();
    }

    public RefDropDownPage(int nameKey, ArrayList<Integer> keyList){
        this.nameKey = nameKey;
        children = new ArrayList<>();
        for(Integer integer: keyList)
            children.add(new RefDropDownPage(integer));
    }





    public void add(RefDropDownPage page){
        children.add(page);
    }

    public void add(int nameKey){
        children.add(new RefDropDownPage(nameKey));
    }

    public void init(){
        setParent(null);
    }

    public void setParent(RefDropDownPage parent){
        this.parent = parent;
        for(RefDropDownPage child: children){
            child.setParent(this);
        }
    }

    public RefDropDownPage getByKey(int nameKey){
        for(RefDropDownPage page: children){
            if(page.nameKey == nameKey)
                return page;
        }
        return null;
    }

    public RefDropDownPage getOrAdd(int name){
        if(name == StringMap.nullKeyValue)
            throw new RuntimeException("null name in getOrAdd function");
        for(RefDropDownPage page: children){
            if(page.nameKey == name)
                return page;
        }
        RefDropDownPage newPage = new RefDropDownPage(name);
        add(newPage);
        return newPage;
    }

    public RefDropDownPage getByIndex(int index){
        return children.get(index);
    }

    public String getName(){
        return StringMap.get(nameKey);
    }

    public Integer getKey() {return nameKey;}

    public String toString(){
        return stringWithTab(0);
    }

    public boolean hasChildren(){
        return children.size() > 0;
    }

    @Override
    public DropDownPage getParent() {
        return null;
    }

    public String stringWithTab(int tab){
        String tabs = "\t".repeat(tab);
        String result = tabs + getName() + "\n";
        for(RefDropDownPage page: children){
            result += page.stringWithTab(tab+1);
        }

        return result;
    }

    public ArrayList<String> getOptions(){
        ArrayList<String> options = new ArrayList<>();
        for(RefDropDownPage page: children){
            options.add(StringMap.get(page.nameKey));
        }
        return options;
    }

    @Override
    public ArrayList<DropDownPage> getChildren() {
        ArrayList<DropDownPage> result = new ArrayList<>(children);
        return result;
    }

    public ArrayList<String> getPath(){
        ArrayList<String> path = new ArrayList<>();
        getPath(path);
        return path;
    }

    public ArrayList<Integer> getKeyPath(){
        ArrayList<Integer> path = new ArrayList<>();
        getKeyPath(path);
        return path;
    }

    public void getKeyPath(ArrayList<Integer> path){
        if(parent == null)
            return;
        parent.getKeyPath(path);
        path.add(getKey());
    }

    public void getPath(ArrayList<String> path){
        if(parent == null)
            return;
        parent.getPath(path);
        path.add(getName());
    }
}
