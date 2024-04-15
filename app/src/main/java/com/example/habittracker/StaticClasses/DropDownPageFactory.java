package com.example.habittracker.StaticClasses;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.DropDownPages.DropDownPage;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.structures.Entry;
import com.example.habittracker.structures.Header;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.Widgets.DropDown;

import java.util.ArrayList;
import java.util.Arrays;

public class DropDownPageFactory {
    public static DropDownPage getGroupedPages(Structure structure, WidgetId valueId, ArrayList<WidgetId> groupIdList){
        MainActivity.log("getting grouped pages");
        Header header = structure.getHeader();
        ArrayList<Entry> entries = structure.getEntries();
        ArrayList<ValueTreePath> groupPathList = header.getPathList(groupIdList);
        MainActivity.log("group paths: " + groupPathList);

        ValueTreePath valuePath = header.getPath(valueId);
        DropDownPage parentPage = new DropDownPage(null);
        MainActivity.log("value path: " + valuePath);

        //iterate through the spreadsheet's entries
        for(Entry entry: entries){
            EntryValueTree tree = entry.getEntryValueTree();
            MainActivity.log("data tree: \n" + tree.hierarchy());
            //make list of pages, starting with the parent page
            ArrayList<DropDownPage> parentPages = new ArrayList<>();
            parentPages.add(parentPage);

            //gather values, can have multiple
            ArrayList<EntryValueTree> entryValueList = tree.getValuesFromPath(valuePath);
            //gather group values, can have multiple groups and each group can have multiple values
            //ArrayList<ArrayList<CachedString>> valuesOfGroups = tree.getValueListFromPathList(groupPathList);
            //the first index, we just add items to the parent page
            //at the end of the iteration, we set parentPages to the pages we just made
            MainActivity.log("before group index loop: " + groupPathList.size() + ", " + groupIdList.size());
            for(int groupIndex = 0; groupIndex < groupPathList.size(); groupIndex++){
                //ArrayList<CachedString> valuesOfCurrentGroup = valuesOfGroups.get(groupIndex);
                ArrayList<EntryValueTree> valuesOfCurrentGroup = tree.getValuesFromPath(groupPathList.get(groupIndex));
                MainActivity.log("group index loop: " + groupIndex + "path: " + groupPathList.get(groupIndex) + "\nvalues: " + valuesOfCurrentGroup);
                //keep track of new pages so that we can set them as the parent later
                ArrayList<DropDownPage> newPages = new ArrayList<>();
                for(EntryValueTree groupValue: valuesOfCurrentGroup){
                    MainActivity.log("group value loop: " + groupValue);
                    for(DropDownPage page: parentPages){
                        //if there is already a page, get the page that matches the name
                        //if there is not a page, create it and add it to the list
                        MainActivity.log("adding new page");
                        if(groupValue.getCachedString() instanceof LiteralString){

                        }
                        newPages.add(page.getOrAdd(new PayloadOption(getRefEntryString(structure, groupIdList.get(groupIndex), entry.getId(), groupValue), null)));
                    }
                }
                parentPages = newPages;
            }
            for(DropDownPage page: parentPages){
                ArrayList<DropDownPage> pages = page.getPagePath();
                ArrayList<CachedString> valuesOfPages = new ArrayList<>();
                for(int i = 0; i < pages.size(); i++)
                    valuesOfPages.add(pages.get(i).getCachedName());
                for(EntryValueTree entryValue: entryValueList){
                    ArrayList<CachedString> copied = (ArrayList<CachedString>) valuesOfPages.clone();
                    RefEntryString entryValueRefString = getRefEntryString(structure, valueId, entry.getId(), entryValue);
                    copied.add(entryValueRefString);

                    page.add(new PayloadOption(entryValueRefString, new RefItemPath(copied)));
                }
            }

        }
        return parentPage;
    }

    public static RefEntryString getRefEntryString(Structure structure, WidgetId widgetId, Integer entryId, EntryValueTree tree){
        if(entryId == null)
            throw new RuntimeException();
        if(tree.getCachedString() instanceof LiteralString){
            return new RefEntryString(structure, widgetId, entryId, tree.getListItemIdList());
        }
        return (RefEntryString) tree.getCachedString();
    }


    public static DropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDown.className,
                "sliderfds"
        };
        ArrayList<String> typeStringList = new ArrayList<>(Arrays.asList(numbers));
        ArrayList<PayloadOption> payloadOptionList = EnumLoop.makeList(typeStringList, s -> new PayloadOption(new LiteralString(s), new LiteralString(s)));

        DropDownPage page = new DropDownPage().put(payloadOptionList);
        //MainActivity.log("type pages: \n" + page.hierarchyString());

        return page;
    }
}
