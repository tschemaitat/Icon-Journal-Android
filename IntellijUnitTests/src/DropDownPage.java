import java.util.ArrayList;

public class DropDownPage {
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

    public DropDownPage get(String name){
        for(DropDownPage page: children){
            if(page.name.equals(name))
                return page;
        }
        return null;
    }

    public DropDownPage get(int index){
        return children.get(index);
    }

    public String name;
    public ArrayList<DropDownPage> children;

    public String toString(){
        return stringWithTab(0);
    }

    public String stringWithTab(int tab){
        String tabs = "\t".repeat(tab);
        String result = tabs + name + "\n";
        for(DropDownPage page: children){
            result += page.stringWithTab(tab+1);
        }

        return result;
    }



}
