package com.example.habittracker.Widgets.ListWidgets;

import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.structures.ListItemId;

public class ListWidgetItemWrapper {
    private EntryWidget entryWidget;
    private ListItemId listItemId;

    public ListWidgetItemWrapper(EntryWidget entryWidget, ListItemId listItemId) {
        this.entryWidget = entryWidget;
        this.listItemId = listItemId;
    }

    public EntryWidget getEntryWidget() {
        return entryWidget;
    }

    public ListItemId getListItemId() {
        return listItemId;
    }
}
