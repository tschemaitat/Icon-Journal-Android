package com.example.habittracker.Widgets.EntryWidgets;

import com.example.habittracker.StaticStateManagers.InvisibleEditTextManager;
import com.example.habittracker.StaticStateManagers.KeyBoardActionManager;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.ListItemId;

public class EntryWidgetResources {
    public InvisibleEditTextManager invisibleEditTextManager;
    public EntryOnDataChange entryOnDataChange;
    public KeyBoardActionManager keyBoardActionManager;
    public EntryInStructure entryInStructure;

    //parent for invisible edit text
    //callback for saving data edit


    public EntryWidgetResources(KeyBoardActionManager keyBoardActionManager,
                                InvisibleEditTextManager invisibleEditTextManager,
                                EntryOnDataChange entryOnDataChange,
                                EntryInStructure entryInStructure) {
        this.entryInStructure = entryInStructure;
        this.keyBoardActionManager = keyBoardActionManager;
        this.invisibleEditTextManager = invisibleEditTextManager;
        this.entryOnDataChange = entryOnDataChange;
    }

    public interface EntryOnDataChange{
        //this takes object because i haven't made a interface to group group and widget value
        //current GroupValue is a standalone class
        void onBaseEntryDataChange(RefEntryString refEntryString, WidgetValue widgetValue);
        void onListItemCreated(RefEntryString refEntryString, GroupValue groupValue, ListIdCallBack listIdCallBack);
    }

    public interface ListIdCallBack{
        void giveListId(ListItemId listItemId);
    }

}
