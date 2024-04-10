package com.example.habittracker.StaticClasses;

import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.DropDownPages.RefDropDownPage;
import com.example.habittracker.Structs.Header;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.TreeId;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.Widgets.DropDown;

import java.util.ArrayList;

public class DropDownPageFactory {
    public static RefDropDownPage getGroupedPages(StructureId structureId, TreeId valueId, ArrayList<TreeId> groupIdList){
        Structure structure = Dictionary.getStructure(structureId);
        Header header = structure.getHeader();
        ArrayList<EntryValueTree> data = structure.getData();
        ArrayList<ValueTreePath> groupToValue = header.getPathList(groupIdList);
        ValueTreePath valuePath = header.getPath(valueId);
        RefDropDownPage parentPage = new RefDropDownPage(null);
        //iterate through the spreadsheet's entries
        for(EntryValueTree tree: data){
            //make list of pages, starting with the parent page
            ArrayList<RefDropDownPage> parentPages = new ArrayList<>();
            parentPages.add(parentPage);

            //gather values, can have multiple
            ArrayList<CachedString> entryValueList = tree.gatherValuesFromPath(valuePath);
            //gather group values, can have multiple groups and each group can have multiple values
            ArrayList<ArrayList<CachedString>> valuesOfGroups = tree.gatherValueListFromPathList(groupToValue);
            //the first index, we just add items to the parent page
            //at the end of the iteration, we set parentPages to the pages we just made
            for(int groupIndex = 0; groupIndex < groupIdList.size(); groupIndex++){
                ArrayList<CachedString> valuesOfCurrentGroup = valuesOfGroups.get(groupIndex);
                //keep track of new pages so that we can set them as the parent later
                ArrayList<RefDropDownPage> newPages = new ArrayList<>();
                for(CachedString groupValue: valuesOfCurrentGroup){
                    for(RefDropDownPage page: parentPages){
                        //if there is already a page, get the page that matches the name
                        //if there is not a page, create it and add it to the list
                        newPages.add(page.getOrAdd(groupValue));
                    }
                }
                parentPages = newPages;
            }
            for(RefDropDownPage page: parentPages)
                for(CachedString entryValue: entryValueList)
                    page.add(entryValue);
        }
        return parentPage;
    }


    public static RefDropDownPage getTypes(){
        String[] numbers = new String[]{
                "edit text",
                "list",
                DropDown.className,
                "sliderfds"
        };
        CachedString pageName = StringMap.addStaticValue("types");
        ArrayList<CachedString> cachedStringList = new ArrayList<>();
        for(String string: numbers)
            cachedStringList.add(StringMap.addStaticValue(string));

        return new RefDropDownPage(pageName, cachedStringList);
    }
}
