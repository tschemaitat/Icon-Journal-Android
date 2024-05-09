package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;

import com.example.habittracker.StaticStateManagers.DeleteValueManager;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.structures.ListItemId;

public abstract class BaseEntryWidget extends EntryWidget{
    private ListItemIdProvider listItemIdProvider;
    public BaseEntryWidget(Context context) {
        super(context);
    }

    public void setListItemIdProvider(ListItemIdProvider listItemIdProvider) {
        this.listItemIdProvider = listItemIdProvider;
    }

    public ListItemIdProvider getListItemIdProvider(){
        return listItemIdProvider;
    }

    public ListItemId getListItemId(){
        return listItemIdProvider.getListItemId();
    }



    public void onDeleteCheck(boolean isChecked){
        DeleteValueManager deleteValueManager = DeleteValueManager.getManager();
        if(isChecked){
            WidgetValue widgetValue = this.getValue();
            deleteValueManager.addValue(new RefEntryString(getStructure(), getWidgetId(),
                    null, listItemIdProvider.getListItemIdList()));
        }
    }


}
