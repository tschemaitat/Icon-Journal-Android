package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Widgets.ListWidgets.ListItemIdProvider;
import com.example.habittracker.structures.ListItemId;

import java.util.ArrayList;

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

    @Override
    public ArrayList<RefEntryString> getReferenceForDelete() {
        if(listItemIdProvider == null){
            MainActivity.log("provider null: " + this);
            throw new RuntimeException();
        }
        ArrayList<RefEntryString> resultList = new ArrayList<>();
        RefEntryString result = new RefEntryString(getStructureId(), getWidgetIdTracker(),
                    null, listItemIdProvider.getListItemIdList());
        resultList.add(result);
        return resultList;
    }



//    public void onDeleteCheck(boolean isChecked){
//        DeleteValueManager deleteValueManager = DeleteValueManager.getManager();
//        if(isChecked){
//            WidgetValue widgetValue = this.getValue();
//            deleteValueManager.addValue(new RefEntryString(getStructure(), getWidgetId(),
//                    null, listItemIdProvider.getListItemIdList()));
//        }
//    }


}
