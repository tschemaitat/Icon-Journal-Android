package com.example.habittracker.Structs.DropDownPages;

import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.RefItemPath;

import java.util.ArrayList;

public class RefDropDownPage implements DropDownPage{
    public static int nameKeyForParent = -10;
    public RefDropDownPage parent = null;
    private CachedString name;
    public ArrayList<RefDropDownPage> children;

    public RefDropDownPage(CachedString name){
        this.name = name;
        children = new ArrayList<>();
    }

    public RefDropDownPage(CachedString name, ArrayList<CachedString> nameList){
        this.name = name;
        children = new ArrayList<>();
        for(CachedString cachedString: nameList)
            children.add(new RefDropDownPage(cachedString));
    }





    public void add(RefDropDownPage page){
        children.add(page);
    }

    public void add(CachedString nameKey){
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

    public RefDropDownPage getByKey(CachedString nameKey){
        for(RefDropDownPage page: children){
            if(page.name == nameKey)
                return page;
        }
        return null;
    }

    public RefDropDownPage getOrAdd(CachedString name){
        if(name == null)
            throw new RuntimeException("null name in getOrAdd function");
        for(RefDropDownPage page: children){
            if(page.name == name)
                return page;
        }
        RefDropDownPage newPage = new RefDropDownPage(name);
        add(newPage);
        return newPage;
    }

    public RefDropDownPage getByIndex(int index){
        return children.get(index);
    }



    public String toString(){
        return stringWithTab(0);
    }

    public boolean hasChildren(){
        return children.size() > 0;
    }

    public RefDropDownPage getParent() {
        return parent;
    }

    public String stringWithTab(int tab){
        String tabs = "\t".repeat(tab);
        String result = tabs + name.getString() + "\n";
        for(RefDropDownPage page: children){
            result += page.stringWithTab(tab+1);
        }

        return result;
    }

    public ArrayList<PayloadOption> getOptions(){
        ArrayList<PayloadOption> options = new ArrayList<>();
        for(RefDropDownPage page: children)
            options.add(new PayloadOption(page.getName(), page.getKeyPathToPageWithName()));
        return options;
    }

    public ArrayList<RefDropDownPage> getRefChildren() {
        ArrayList<RefDropDownPage> result = new ArrayList<>(children);
        return result;
    }

    public ArrayList<DropDownPage> getChildren() {
        ArrayList<DropDownPage> result = new ArrayList<>(children);
        return result;
    }

    public RefItemPath getKeyPathToPageWithName(){
        ArrayList<CachedString> path = new ArrayList<>();
        getKeyPathToPageWithName(path);
        return new RefItemPath(path);
    }

    public void getKeyPathToPageWithName(ArrayList<CachedString> path){
        if(parent == null)
            return;
        parent.getKeyPathToPageWithName(path);
        path.add(getCachedName());
    }

    @Override
    public ItemPath getPathToPageWithName() {
        ArrayList<String> stringList = new ArrayList<>();
        for(CachedString cachedString: getKeyPathToPageWithName())
            stringList.add(cachedString.getString());
        return new ItemPath(stringList);
    }

    public CachedString getCachedName() {return name;}

    public String getName(){
        return name.getString();
    }

    @Override
    public DropDownPage getChildPage(String name) {
        for(DropDownPage child: getChildren()){
            if(child.getName().equals(name))
                return child;
        }
        throw new RuntimeException("tried to find: " + name);
    }

    @Override
    public DropDownPage getChildPage(int index) {
        return children.get(index);
    }
}
