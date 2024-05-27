package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.widget.Button;

import com.example.habittracker.Algorithms.HandleDeletedValues;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.EnumLoop;
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
        MainActivity.log("on confirm");
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
        MainActivity.log("checks widgets: \n" + EnumLoop.makeList(entryWidgetsChecked, (widget)->widget.getNameAndLocation() + "\n"));
        ArrayList<ArrayList<BaseEntryWidget>> widgetGroupedByCheckedWidget = new ArrayList<>();
        for(EntryWidget entryWidget: entryWidgetsChecked){
            widgetGroupedByCheckedWidget.add(entryWidget.getWidgetsForDelete());
        }
        ArrayList<BaseEntryWidget> widgetList = new ArrayList<>();

        for(ArrayList<BaseEntryWidget> widgetsInGroup: widgetGroupedByCheckedWidget){
            widgetList.addAll(widgetsInGroup);
        }
        MainActivity.log("widgets set to delete: \n" + EnumLoop.makeList(widgetList, (widget)->widget.getNameAndLocation() + "\n"));

        return widgetList;
    }

    public void onCheckReferences(){


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
            stringBuilder.append("source widget: " + sourceWidget + ", " + sourceWidget.getName() + "\n");
            stringBuilder.append("\tsource values: \n");
            for(RefEntryString sourceValue: sourceLocationList){
                stringBuilder.append(" [" + sourceValue.getLocationString() + ": " + sourceValue.getString() + "]\n");
            }
            stringBuilder.append("\n");
            stringBuilder.append("\treference values: \n");
            for(RefEntryString reference: referencesOfWidget){
                stringBuilder.append(" [" + reference + ": " + reference.getString() + "]\n");
            }
            stringBuilder.append("\n");
        }
        String dialogString = stringBuilder.toString();
        CustomDialog customDialog = new CustomDialog(context, dialogString);
        MainActivity.log(dialogString);
        customDialog.show();
    }





}
