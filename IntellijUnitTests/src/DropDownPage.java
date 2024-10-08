

import com.example.habittracker.defaultImportPackage.ArrayList;

public class DropDownPage {
    public DropDownPage parent = null;
    public DropDownPage(String name){
        this.name = name;
        children = new ArrayList<>();
    }

    public DropDownPage(String name, ArrayList<DropDownPage> children){
        this.name = name;
        this.children = children;
    }

    public void add(DropDownPage page){
        children.add(page);
    }

    public void add(String page){
        children.add(new DropDownPage(page));
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

    public DropDownPage get(String name){
        for(DropDownPage page: children){
            if(page.name.equals(name))
                return page;
        }
        return null;
    }

    public DropDownPage getOrAdd(String name){
        for(DropDownPage page: children){
            if(page.name.equals(name))
                return page;
        }
        DropDownPage newPage = new DropDownPage(name);
        add(newPage);
        return newPage;
    }

    public DropDownPage get(int index){
        return children.get(index);
    }

    public String name;
    public ArrayList<DropDownPage> children;

    public String toString(){
        return stringWithTab(0);
    }

    public boolean hasChildren(){
        return children.size() > 0;
    }

    public String stringWithTab(int tab){
        String tabs = "\t".repeat(tab);
        String result = tabs + name + "\n";
        for(DropDownPage page: children){
            result += page.stringWithTab(tab+1);
        }

        return result;
    }

    public ArrayList<String> getOptions(){
        ArrayList<String> options = new ArrayList<>();
        for(DropDownPage page: children){
            options.add(page.name);
        }
        return options;
    }
}
