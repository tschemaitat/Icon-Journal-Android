package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.widget.Button;

import com.example.habittracker.Algorithms.HandleDeletedValues;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.ViewWidgets.CustomDialog;
import com.example.habittracker.Widgets.EntryWidgets.BaseEntryWidget;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidgets.ListWidget;
import com.example.habittracker.structurePack.EntryInStructure;
import com.example.habittracker.structurePack.ListItemId;
import com.example.habittracker.structurePack.Structure;

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
    private EntryInStructure entryInStructure;

    public DeleteValueManager(Context context, GroupWidget groupWidget, Button button, EntryInStructure entryInStructure) {
        this.entryInStructure = entryInStructure;
        this.button = button;
        this.context = context;
        this.groupWidget = groupWidget;
        init();
    }

    private void init(){
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
        if(entryInStructure != null){
            onCheckReferences();
            return;
        }
        removeWidgetsInEditorPage();
    }

    public void removeWidgetsInEditorPage(){
        ArrayList<BaseEntryWidget> widgetList = getWidgetsToDelete();
        for(BaseEntryWidget baseEntryWidget: widgetList){

        }
    }

    public ArrayList<BaseEntryWidget> getWidgetsToDelete(){
        ArrayList<EntryWidget> entryWidgetsChecked = groupWidget.gatherWidgetsChecked();
        ArrayList<ArrayList<BaseEntryWidget>> widgetGroupedByCheckedWidget = new ArrayList<>();
        for(EntryWidget entryWidget: entryWidgetsChecked){
            widgetGroupedByCheckedWidget.add(entryWidget.getWidgetsForDelete());
        }
        ArrayList<BaseEntryWidget> widgetList = new ArrayList<>();

        for(ArrayList<BaseEntryWidget> widgetsInGroup: widgetGroupedByCheckedWidget){
            widgetList.addAll(widgetsInGroup);
        }
        return widgetList;
    }

    public void onCheckReferences(){
        MainActivity.log("on confirm");

        ArrayList<BaseEntryWidget> widgetList = getWidgetsToDelete();
        ArrayList<ArrayList<RefEntryString>> refListList = new ArrayList<>();
        for(BaseEntryWidget baseEntryWidget: widgetList){
            refListList.add(baseEntryWidget.getReference(entryInStructure));
        }


        for(int w = 0; w < refListList.size(); w++){
            ArrayList<RefEntryString> refList = refListList.get(w);
            EntryWidget entryWidget = widgetList.get(w);
            MainActivity.log("checked: " + entryWidget);
            for(int r = 0; r < refList.size(); r++){
                RefEntryString refEntryString = refList.get(r);
                MainActivity.log("\t" + refEntryString.getLocationString());
            }
        }

        ArrayList<ArrayList<RefEntryString>> referencesList = HandleDeletedValues.getReferencesList(refListList);
        StringBuilder stringBuilder = new StringBuilder();
        for(int widgetIndex = 0; widgetIndex < referencesList.size(); widgetIndex++){
            BaseEntryWidget sourceWidget = widgetList.get(widgetIndex);
            ArrayList<RefEntryString> referencesOfWidget = referencesList.get(widgetIndex);
            ArrayList<RefEntryString> sourceLocationList = refListList.get(widgetIndex);
            stringBuilder.append("source widget: ").append(sourceWidget).append(", ").append(sourceWidget.getName()).append("\n");
            stringBuilder.append("\tsource values: ");
            for(RefEntryString sourceValue: sourceLocationList){
                stringBuilder.append(" [" + sourceValue + ": " + sourceValue.getString() + "]");
            }
            stringBuilder.append("\n");
            stringBuilder.append("\treference values: ");
            for(RefEntryString reference: referencesOfWidget){
                stringBuilder.append(" [" + reference + ": " + reference.getString() + "]");
            }
        }
        String dialogString = stringBuilder.toString();
        CustomDialog customDialog = new CustomDialog(context, dialogString);
        MainActivity.log(dialogString);
        customDialog.show();
    }




    public static ArrayList<BaseEntryWidget> gatherRefForDeleteWidgetsAndList(ArrayList<BaseEntryWidget> baseEntryWidgets){
        ArrayList<BaseEntryWidget> resultList = new ArrayList<>();
        for(BaseEntryWidget baseEntryWidget: baseEntryWidgets){
            if(baseEntryWidget instanceof ListWidget listWidget){
                resultList.addAll(listWidget.getWidgetsForDeleteIteration());
                continue;
            }
            resultList.add(baseEntryWidget);
        }
        return resultList;
    }
}
