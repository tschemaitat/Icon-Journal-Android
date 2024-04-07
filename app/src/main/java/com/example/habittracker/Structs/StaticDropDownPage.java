package com.example.habittracker.Structs;



import java.util.ArrayList;

public class StaticDropDownPage implements DropDownPage{
    public StaticDropDownPage parent = null;
    public String name;
    public ArrayList<StaticDropDownPage> children;

    public StaticDropDownPage(String name){
        this.name = name;
        children = new ArrayList<>();
    }

    public StaticDropDownPage(String name, ArrayList<String> stringList){
        this.name = name;
        children = new ArrayList<>();
        for(String s: stringList)
            children.add(new StaticDropDownPage(s));
    }





    public void add(StaticDropDownPage page){
        children.add(page);
    }

    public void add(String page){
        children.add(new StaticDropDownPage(page));
    }

    public void init(){
        setParent(null);
    }

    public void setParent(StaticDropDownPage parent){
        this.parent = parent;
        for(StaticDropDownPage child: children){
            child.setParent(this);
        }
    }

    public StaticDropDownPage get(String name){
        for(StaticDropDownPage page: children){
            if(page.name.equals(name))
                return page;
        }
        return null;
    }

    public StaticDropDownPage getOrAdd(String name){
        if(name == null)
            throw new RuntimeException("null name in getOrAdd function");
        for(StaticDropDownPage page: children){
            if(page.name.equals(name))
                return page;
        }
        StaticDropDownPage newPage = new StaticDropDownPage(name);
        add(newPage);
        return newPage;
    }

    public StaticDropDownPage getByIndex(int index){
        return children.get(index);
    }



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
        String result = tabs + name + "\n";
        for(StaticDropDownPage page: children){
            result += page.stringWithTab(tab+1);
        }

        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    public ArrayList<String> getOptions(){
        ArrayList<String> options = new ArrayList<>();
        for(StaticDropDownPage page: children){
            options.add(page.name);
        }
        return options;
    }

    @Override
    public ArrayList<DropDownPage> getChildren() {
        return null;
    }

    public static StaticDropDownPage fromItems(ArrayList<ItemPath> items){
        //System.out.println("getting page from items");
//        for(ItemPath item: items)
//            System.out.println("item = " + item);
        StaticDropDownPage page = new StaticDropDownPage("default");
        StaticDropDownPage currentParent;
        for(ItemPath item: items){
            currentParent = page;
            for(int level = 0; level < item.getPath().size(); level++){
                StaticDropDownPage newPage = currentParent.getOrAdd(item.getPath().get(level));
                newPage.parent = currentParent;
                currentParent = newPage;
            }
        }
        return page;
    }

    public ArrayList<String> getPath(){
        ArrayList<String> path = new ArrayList<>();
        getPath(path);
        return path;
    }

    public void getPath(ArrayList<String> path){
        if(parent == null)
            return;
        parent.getPath(path);
        path.add(name);
    }
}

