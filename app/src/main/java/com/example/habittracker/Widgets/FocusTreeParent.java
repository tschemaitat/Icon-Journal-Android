package com.example.habittracker.Widgets;

import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;

public interface FocusTreeParent{
    EntryWidget getFirstWidget();
    EntryWidget findNextWidget(EntryWidget entryWidget);
}
