package com.example.habittracker.StaticClasses;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Values.BaseWidgetValue;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.structures.Entry;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetInStructure;
import com.example.habittracker.structures.WidgetPath;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;

import java.util.ArrayList;
import java.util.Arrays;

public class DropDownPageFactory {
    public static DropDownPage getGroupedPages(Structure structure, WidgetInStructure valueId, ArrayList<WidgetInStructure> groupIdList){
        //MainActivity.log("getting grouped pages");
        ArrayList<Entry> entries = structure.getEntries();
        ArrayList<WidgetPath> groupPathList = EnumLoop.makeList(groupIdList, (widgetId)->widgetId.getWidgetInfo().getWidgetPath());
        //MainActivity.log("group paths: " + groupPathList);

        WidgetPath valuePath = valueId.getWidgetInfo().getWidgetPath();
        DropDownPage parentPage = new DropDownPage(null);
        //MainActivity.log("value path: " + valuePath);

        //iterate through the spreadsheet's entries
        for(Entry entry: entries){
            GroupValue entryValueTree = entry.getGroupValue();
            //MainActivity.log("data tree: \n" + tree.hierarchy());
            //make list of pages, starting with the parent page
            ArrayList<DropDownPage> parentPages = new ArrayList<>();
            parentPages.add(parentPage);

            //gather values, can have multiple
            ArrayList<BaseWidgetValue> entryValueList = entryValueTree.getValuesFromWidgetPath(valuePath);
            //gather group values, can have multiple groups and each group can have multiple values
            //ArrayList<ArrayList<CachedString>> valuesOfGroups = tree.getValueListFromPathList(groupPathList);
            //the first index, we just add items to the parent page
            //at the end of the iteration, we set parentPages to the pages we just made
            //MainActivity.log("before group index loop: " + groupPathList.size() + ", " + groupIdList.size());
            for(int groupIndex = 0; groupIndex < groupPathList.size(); groupIndex++){
                //ArrayList<CachedString> valuesOfCurrentGroup = valuesOfGroups.get(groupIndex);
                ArrayList<BaseWidgetValue> valuesOfCurrentGroup = entryValueTree.getValuesFromWidgetPath(groupPathList.get(groupIndex));
                //MainActivity.log("group index loop: " + groupIndex + "path: " + groupPathList.get(groupIndex) + "\nvalues: " + valuesOfCurrentGroup);
                //keep track of new pages so that we can set them as the parent later
                ArrayList<DropDownPage> newPages = new ArrayList<>();
                for(BaseWidgetValue baseWidgetValue: valuesOfCurrentGroup){
                    //MainActivity.log("group value loop: " + groupValue);
                    for(DropDownPage page: parentPages){
                        //if there is already a page, get the page that matches the name
                        //if there is not a page, create it and add it to the list
                        //MainActivity.log("adding new page");
                        if(baseWidgetValue.getStandardFormOfCachedString() instanceof LiteralString){

                        }
                        newPages.add(page.getOrAdd(new PayloadOption(getRefEntryString(structure, groupIdList.get(groupIndex), entry.getId(), baseWidgetValue), null)));
                    }
                }
                parentPages = newPages;
            }
            for(DropDownPage page: parentPages){
                ArrayList<DropDownPage> pages = page.getPagePath();
                ArrayList<CachedString> valuesOfPages = new ArrayList<>();
                for(int i = 0; i < pages.size(); i++)
                    valuesOfPages.add(pages.get(i).getCachedName());
                for(BaseWidgetValue baseWidgetValue: entryValueList){
                    ArrayList<CachedString> copied = (ArrayList<CachedString>) valuesOfPages.clone();
                    RefEntryString entryValueRefString = getRefEntryString(structure, valueId, entry.getId(), baseWidgetValue);
                    copied.add(entryValueRefString);

                    page.add(new PayloadOption(entryValueRefString, new RefItemPath(copied)));
                }
            }

        }
        return parentPage;
    }

    public static RefEntryString getRefEntryString(Structure structure, WidgetInStructure widgetInStructure, Integer entryId,
                                                   BaseWidgetValue tree){
        if(entryId == null)
            throw new RuntimeException();
        if(tree.getStandardFormOfCachedString() instanceof LiteralString){
            return new RefEntryString(structure, widgetInStructure, entryId, tree.getListItemIds());
        }
        return (RefEntryString) tree.getStandardFormOfCachedString();
    }


    public static DropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDown.className,
                "sliderfds"
        };
        ArrayList<String> typeStringList = new ArrayList<>(Arrays.asList(numbers));
        ArrayList<PayloadOption> payloadOptionList = EnumLoop.makeList(typeStringList, s -> new PayloadOption(new LiteralString(s), s));

        DropDownPage page = new DropDownPage().put(payloadOptionList);
        //MainActivity.log("type pages: \n" + page.hierarchyString());

        return page;
    }
}
