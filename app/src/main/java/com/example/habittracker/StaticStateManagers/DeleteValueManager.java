package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.widget.Button;

import com.example.habittracker.Algorithms.HandleDeletedValues;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidgets.ListWidget;
import com.example.habittracker.structures.ListItemId;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;

public class DeleteValueManager {
//    private static DeleteValueManager manager;
//
//    public static void createManager(Context context, GroupWidget groupWidget, Button button){
//        manager = new DeleteValueManager(context, groupWidget, button, entryId);
//    }
//    public static DeleteValueManager getManager(){
//        return manager;
//    }
//
//    public static boolean hasManager(){
//        return manager != null;
//    }

    private Context context;
    private GroupWidget groupWidget;
    private ArrayList<RefEntryString> valuesToDelete = new ArrayList<>();
    private Button button;
    private Integer entryId;

    public DeleteValueManager(Context context, GroupWidget groupWidget, Button button, Integer entryId) {
        this.entryId = entryId;
        this.button = button;
        this.context = context;
        this.groupWidget = groupWidget;
        init();
    }

    private void init(){
        if(entryId == null)
            entryId = -1;
        groupWidget.enableDeleteValueMode();
    }

    public void onCancel() {
        MainActivity.log("on cancel");
    }

    public void addValue(RefEntryString refEntryString){
        if(valuesToDelete.contains(refEntryString)){
            throw new RuntimeException();
        }
        valuesToDelete.add(refEntryString);
    }

    public void removeValue(RefEntryString refEntryString){
        if( ! valuesToDelete.contains(refEntryString)){
            throw new RuntimeException();
        }
        valuesToDelete.remove(refEntryString);
    }
    private void changeButtonToConfirm(){
        button.setText("confirm");
        button.setOnClickListener((view -> onConfirm()));
    }

    public void onConfirm(){
        MainActivity.log("on confirm");
        ArrayList<EntryWidget> entryWidgets = groupWidget.gatherWidgetsChecked();
        ArrayList<ArrayList<RefEntryString>> refListList = new ArrayList<>();
        for(EntryWidget entryWidget: entryWidgets){
            refListList.add(entryWidget.getReferenceForDelete());
        }
        for(int w = 0; w < refListList.size(); w++){
            ArrayList<RefEntryString> refList = refListList.get(w);
            EntryWidget entryWidget = entryWidgets.get(w);
            MainActivity.log("checked: " + entryWidget);
            for(int r = 0; r < refList.size(); r++){
                RefEntryString refEntryString = refList.get(r);
                refEntryString = new RefEntryString(refEntryString.getWidgetInStructure(),
                        entryId, refEntryString.getListIdList());
                MainActivity.log("\t" + refEntryString.getLocationString());
            }
        }

        ArrayList<Structure.DeleteValuePair> references = HandleDeletedValues.getStructures(refListList);
    }




    public static void gatherRefForDeleteWidgetsAndList(ArrayList<BaseEntryWidget> baseEntryWidgets, ArrayList<RefEntryString> resultList,
                                                        Structure structure, ArrayList<ListItemId> listItemIds){
        for(BaseEntryWidget baseEntryWidget: baseEntryWidgets){
            if(baseEntryWidget instanceof ListWidget listWidget){
                listWidget.getReferenceForDeleteIteration(resultList);
                continue;
            }
            resultList.add(new RefEntryString(baseEntryWidget.getWidgetInStructure(),
                    null, listItemIds));
        }
    }
}
