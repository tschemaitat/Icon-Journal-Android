package com.example.habittracker.Widgets.ListWidgets;


import com.example.habittracker.structures.ListItemId;

import java.util.ArrayList;

public interface ListItemIdProvider {
     ListItemId getListItemId();
     ArrayList<ListItemId> getListItemIdList();
     void setParentListItemIdProvider(ListItemIdProvider listItemIdProvider);
}
