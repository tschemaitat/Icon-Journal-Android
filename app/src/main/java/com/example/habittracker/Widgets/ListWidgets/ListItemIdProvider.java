package com.example.habittracker.Widgets.ListWidgets;


import com.example.habittracker.structurePack.ListItemId;

import com.example.habittracker.defaultImportPackage.ArrayList;

public interface ListItemIdProvider {
     ListItemId getListItemId();
     ArrayList<ListItemId> getListItemIdList();
     void setParentListItemIdProvider(ListItemIdProvider listItemIdProvider);
}
