package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;

import java.util.ArrayList;

public class DropDownPage{
    public static int nameKeyForParent = -10;
    public DropDownPage parent = null;
    private PayloadOption payloadOption;
    public ArrayList<DropDownPage> children = new ArrayList<>();

    public DropDownPage(CachedString name, Object payload){
        this.payloadOption = new PayloadOption(name, payload);
    }

    public DropDownPage(){
        this.payloadOption = null;
    }

    public DropDownPage(PayloadOption payloadOption){
        this.payloadOption = payloadOption;
    }

    public DropDownPage put(ArrayList<PayloadOption> nameList){
        for(PayloadOption payloadOption: nameList){
            DropDownPage page = new DropDownPage(payloadOption);
            page.parent = this;
            children.add(page);
        }
        return this;
    }

    public static DropDownPage fromItemPathWithPayload(ArrayList<RefItemPath> itemPathList, ArrayList<Object> payloadList) {
        DropDownPage parentPage = new DropDownPage(null);
        int itemPathIndex = 0;
        for(RefItemPath refItemPath: itemPathList){
            ArrayList<CachedString> itemList = refItemPath.getPath();
            DropDownPage currentParentPage = parentPage;
            for(int i = 0; i < itemList.size(); i++){
                CachedString item = itemList.get(i);
                Object payload = null;
                if(i == itemList.size() - 1)
                    payload = payloadList.get(itemPathIndex);
                currentParentPage = currentParentPage.getOrAdd(new PayloadOption(item, payload));
            }
            itemPathIndex++;
        }
        return parentPage;
    }

    public static DropDownPage makeFromLiteralStrings(String name, ArrayList<String> nameList){
        ArrayList<CachedString> cachedStringList = new ArrayList<>();
        for(String string: nameList)
            cachedStringList.add(new LiteralString(string));
        return new DropDownPage(new LiteralString(name), cachedStringList);
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
        page.parent = this;
        children.add(page);
    }

    public void add(PayloadOption payloadOption){
        DropDownPage page = new DropDownPage(payloadOption);
        page.parent = this;
        children.add(page);
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
            if(page.getPayloadOption().getCachedString().equals(payloadOption.getCachedString()))
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

    public RefItemPath getRefPathToPageWithName(){
        ArrayList<CachedString> path = new ArrayList<>();
        getRefPathToPageWithName(path);
        return new RefItemPath(path);
    }

    public void getRefPathToPageWithName(ArrayList<CachedString> path){
        if(parent == null)
            return;
        parent.getRefPathToPageWithName(path);
        path.add(getCachedName());
    }

    public CachedString getCachedName() {return payloadOption.getCachedString();}

    public String getName(){
        if(payloadOption == null)
            return null;
        return payloadOption.getString();
    }


    public DropDownPage getChildPage(CachedString name) {
        for(DropDownPage child: getChildren()){
            if(child.getName().equals(name))
                return child;
        }
        throw new RuntimeException("tried to find: " + name);
    }


    public DropDownPage getChildPage(int index) {
        return children.get(index);
    }



    public ArrayList<DropDownPage> getPagePath() {
        ArrayList<DropDownPage> result = new ArrayList<>();
        getPagePathIteration(result);
        result.remove(0);
        return result;
    }

    private void getPagePathIteration(ArrayList<DropDownPage> pages){
        if(parent != null)
            parent.getPagePathIteration(pages);
        pages.add(this);
    }

    public String hierarchyString(){
        return hierarchyStringIteration(0);
    }

    private String hierarchyStringIteration(int numTabs){
        String tabString = GLib.tabs(numTabs);
        Object payload = null;
        if(payloadOption != null)
            payload = payloadOption.getPayload();
        String result = tabString + "(" + getChildren().size() + ")" +getName() +
                " -> " + payload+"\n";

        for(DropDownPage page: getChildren()){
            result += page.hierarchyStringIteration(numTabs+1);
        }
        return result;
    }
}
