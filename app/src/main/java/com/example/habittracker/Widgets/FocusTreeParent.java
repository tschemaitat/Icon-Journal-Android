package com.example.habittracker.Widgets;

import com.example.habittracker.Widgets.EntryWidgets.AbstractWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;


public interface FocusTreeParent{
    AbstractWidget getFirstWidget();
    AbstractWidget findNextWidget(AbstractWidget entryWidget);

    AbstractWidget getWidget();

    interface Focusable{
        void setFocusParent(FocusTreeParent focusTreeParent);

    }
}
