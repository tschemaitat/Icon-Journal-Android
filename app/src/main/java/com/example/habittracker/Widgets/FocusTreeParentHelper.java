package com.example.habittracker.Widgets;

import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;

import java.util.ArrayList;

public class FocusTreeParentHelper {

    public static EntryWidget findNextWidget(EntryWidget entryWidget, ArrayList<EntryWidget> entryWidgetList, FocusTreeParent parent){
        int index = entryWidgetList.indexOf(entryWidget);
        if(index == -1){
            throw new RuntimeException();
        }
        if(index == entryWidgetList.size() - 1){
            if(parent == null)
                return null;
            return parent.getFirstWidget();
        }
        return entryWidgetList.get(index + 1);
    }
}
