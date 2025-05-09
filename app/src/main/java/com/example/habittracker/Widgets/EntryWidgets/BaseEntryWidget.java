package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.ListItemId;

import com.example.habittracker.defaultImportPackage.ArrayList;

public abstract class BaseEntryWidget extends EntryWidget{
    private ListItemIdProvider listItemIdProvider;
    public BaseEntryWidget(Context context, Element wrapperElement, EntryWidgetResources entryWidgetResources) {
        super(context, wrapperElement, entryWidgetResources);
    }

    public void setListItemIdProvider(ListItemIdProvider listItemIdProvider) {
        this.listItemIdProvider = listItemIdProvider;
    }

    //TODO: i need to make this more clear
    //this should only be call when making an edit, so it shouldn't be call by listwidget?
    //because the child of list widget would make the call, list does not call the edit
    @Override
    protected void onDataChanged(){

        entryWidgetResources.entryOnDataChange.onBaseEntryDataChange(
                getLocation(entryWidgetResources.entryInStructure).get(0), (WidgetValue) getEntryValueTreeCustom());
    }



    public ListItemIdProvider getListItemIdProvider(){
        return listItemIdProvider;
    }

    public ListItemId getListItemId(){
        return listItemIdProvider.getListItemId();
    }

    public abstract void setHint(String hintString);



    public String getNameAndLocation(){
        ArrayList<ListItemId> itemIds = getListItemIdProvider().getListItemIdList();
        return getName() + itemIds;
    }


}
