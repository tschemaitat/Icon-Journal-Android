package com.example.habittracker.Widgets;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class FocusTreeParentHelper {

    public static EntryWidget findNextWidget(EntryWidget entryWidget, ArrayList<EntryWidget> entryWidgetList,
                                             FocusTreeParent currentTreeParent, FocusTreeParent currentTree){
        MainActivity.log("finding next widget: " + entryWidgetList);
        int index = entryWidgetList.indexOf(entryWidget);
        MainActivity.log("index of current widget: " + index +", list size: " + entryWidgetList.size());
        if(index == -1){
            throw new RuntimeException();
        }
        if(index == entryWidgetList.size() - 1){
            if(currentTreeParent == null){
                MainActivity.log("no next widget");
                return null;
            }
            MainActivity.log("getting parent first widget");
            return currentTreeParent.findNextWidget((EntryWidget) currentTree);
        }
        EntryWidget nextWidget = entryWidgetList.get(index + 1);
        MainActivity.log("next widget: (index= " + (index + 1) + ", widget: " + nextWidget);
        if(nextWidget instanceof FocusTreeParent focusTreeChild){
            MainActivity.log("next widget is a focusTree");
            EntryWidget firstWidgetOfChild = focusTreeChild.getFirstWidget();
            MainActivity.log("first widget of child: " + firstWidgetOfChild);
            return firstWidgetOfChild;
        }
        return entryWidgetList.get(index + 1);
    }
}
