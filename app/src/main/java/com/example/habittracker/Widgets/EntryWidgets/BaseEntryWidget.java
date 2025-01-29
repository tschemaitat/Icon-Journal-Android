package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.ListItemId;

import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class BaseEntryWidget extends EntryWidget{
    private ListItemIdProvider listItemIdProvider;
    public BaseEntryWidget(Context context, Element wrapperElement) {
        super(context, wrapperElement);
    }

    public void setListItemIdProvider(ListItemIdProvider listItemIdProvider) {
        this.listItemIdProvider = listItemIdProvider;
    }

    public ListItemIdProvider getListItemIdProvider(){
        return listItemIdProvider;
    }

    public ListItemId getListItemId(){
        return listItemIdProvider.getListItemId();
    }

    public abstract void setHint(String hintString);

    public ArrayList<RefEntryString> getLocation(EntryInStructure entryInStructure) {
        if(listItemIdProvider == null){
            MainActivity.log("provider null: " + this);
            throw new RuntimeException();
        }
        ArrayList<RefEntryString> resultList = new ArrayList<>();
        RefEntryString result = new RefEntryString(getWidgetInStructure(),
                entryInStructure, listItemIdProvider.getListItemIdList());
        resultList.add(result);
        return resultList;
    }

    public String getNameAndLocation(){
        ArrayList<ListItemId> itemIds = getListItemIdProvider().getListItemIdList();
        return getName() + itemIds;
    }


}
