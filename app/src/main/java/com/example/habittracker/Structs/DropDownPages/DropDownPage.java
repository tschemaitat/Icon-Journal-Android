package com.example.habittracker.Structs.DropDownPages;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.RefItemPath;

import java.util.ArrayList;

public class DropDownPage{
    public static int nameKeyForParent = -10;
    public DropDownPage parent = null;
    private PayloadOption payloadOption;
    public ArrayList<DropDownPage> children;

    public DropDownPage(CachedString name, Object payload){
        this.payloadOption = new PayloadOption(name, payload);
        children = new ArrayList<>();
    }

    public DropDownPage(PayloadOption payloadOption){
        this.payloadOption = payloadOption;
        children = new ArrayList<>();
    }

    public DropDownPage(CachedString name, Object payload, ArrayList<PayloadOption> nameList){
        this.payloadOption = new PayloadOption(name, payload);
        children = new ArrayList<>();
        for(PayloadOption option: nameList)
            children.add(new DropDownPage(option));
    }

    public static DropDownPage fromItemPathWithPayload(ArrayList<ItemPath> itemPathList, ArrayList<Object> payloadList) {
        DropDownPage parentPage = new DropDownPage(null);
        int itemPathIndex = 0;
        for(ItemPath itemPath: itemPathList){
            ArrayList<String> itemList = itemPath.getStringPath();
            DropDownPage currentParentPage = parentPage;
            for(int i = 0; i < itemList.size(); i++){
                String item = itemList.get(i);
                Object payload = null;
                if(i == itemList.size() - 1)
                    payload = payloadList.get(itemPathIndex);
                currentParentPage = currentParentPage.getOrAdd(new PayloadOption(new CachedString(item), payload));
            }
            itemPathIndex++;
        }
        return parentPage;
    }

    public DropDownPage putList(ArrayList<PayloadOption> nameList){
        for(PayloadOption option: nameList)
            children.add(new DropDownPage(option));
        return this;
    }

    public static DropDownPage makeFromLiteralStrings(String name, ArrayList<String> nameList){
        ArrayList<CachedString> cachedStringList = new ArrayList<>();
        for(String string: nameList)
            cachedStringList.add(new CachedString(string));
        return new DropDownPage(new CachedString(name), cachedStringList);
    }

    public static DropDownPage fromItems(ArrayList<ArrayList<PayloadOption>> payloadOptionListList) {
        DropDownPage dropDownPage = new DropDownPage(null);

        for(ArrayList<PayloadOption> payloadOptionList: payloadOptionListList){
            DropDownPage currentParentPage = dropDownPage;
            for(PayloadOption currentOption: payloadOptionList){
                currentParentPage = currentParentPage.getOrAdd(currentOption);
            }
        }
        return dropDownPage;
    }


    public void add(DropDownPage page){
        children.add(page);
    }

    public void add(PayloadOption payloadOption){
        children.add(new DropDownPage(payloadOption));
    }

    public void init(){
        setParent(null);
    }

    public void setParent(DropDownPage parent){
        this.parent = parent;
        for(DropDownPage child: children){
            child.setParent(this);
        }
    }



    public DropDownPage getChild(String name){
        for(DropDownPage page: children){
            if(page.getName().equals(name))
                return page;
        }
        return null;
    }

    public PayloadOption getPayloadOption(){
        return payloadOption;
    }

    public DropDownPage getOrAdd(PayloadOption payloadOption){
        if(payloadOption == null)
            throw new RuntimeException("null name in getOrAdd function");
        for(DropDownPage page: children){
            if(page.getPayloadOption().equals(payloadOption))
                return page;
        }
        DropDownPage newPage = new DropDownPage(payloadOption);
        add(newPage);
        return newPage;
    }

    public DropDownPage getByIndex(int index){
        return children.get(index);
    }



    public String toString(){
        return stringWithTab(0);
    }

    public boolean hasChildren(){
        return children.size() > 0;
    }

    public DropDownPage getParent() {
        return parent;
    }

    public String stringWithTab(int tab){
        String tabs = "\t".repeat(tab);
        String result = tabs + payloadOption.getString() + "\n";
        for(DropDownPage page: children){
            result += page.stringWithTab(tab+1);
        }

        return result;
    }

    public ArrayList<PayloadOption> getOptions(){
        ArrayList<PayloadOption> options = new ArrayList<>();
        for(DropDownPage page: children)
            options.add(page.getPayloadOption());
        return options;
    }

    public ArrayList<DropDownPage> getRefChildren() {
        ArrayList<DropDownPage> result = new ArrayList<>(children);
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


    public ItemPath getPathToPageWithName() {
        ArrayList<String> stringList = new ArrayList<>();
        for(CachedString cachedString: getKeyPathToPageWithName())
            stringList.add(cachedString.getString());
        return new ItemPath(stringList);
    }

    public CachedString getCachedName() {return payloadOption.getCachedString();}

    public String getName(){
        return payloadOption.getString();
    }


    public DropDownPage getChildPage(String name) {
        for(DropDownPage child: getChildren()){
            if(child.getName().equals(name))
                return child;
        }
        throw new RuntimeException("tried to find: " + name);
    }


    public DropDownPage getChildPage(int index) {
        return children.get(index);
    }

    public String hierarchyString(){
        return hierarchyStringIteration(0);
    }

    private String hierarchyStringIteration(int numTabs){
        String tabString = GLib.tabs(numTabs);
        String result = tabString + getName() + "\n";
        for(DropDownPage page: getChildren()){
            result += page.hierarchyStringIteration(numTabs+1);
        }
        return result;
    }

    public ArrayList<DropDownPage> getPagePath() {
        ArrayList<DropDownPage> result = new ArrayList<>();
        getPagePathIteration(result);
        return result;
    }

    private void getPagePathIteration(ArrayList<DropDownPage> pages){
        if(parent != null)
            getPagePathIteration(pages);
        pages.add(this);
    }
}
