package com.example.habittracker.Widgets;

import com.example.habittracker.Widgets.EntryWidgets.AbstractWidget;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.WidgetWrapper;

public interface FocusTreeParent{
    EntryWidget getFirstWidget();
    EntryWidget findNextWidget(AbstractWidget entryWidget);
}
