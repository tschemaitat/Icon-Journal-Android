package com.example.habittracker.Algorithms;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetId;

import java.util.ArrayList;

public class HandleDeletedValues {
    public static void gatherReferences(ArrayList<WidgetId> widgetIdList){

    }

    public static void gatherReferences(WidgetId widgetId){

    }

    public static ArrayList<Structure.DeleteValuePair> getStructures(ArrayList<ArrayList<RefEntryString>> refEntryStringList){
        ArrayList<Structure.DeleteValuePair> result = new ArrayList<>();
        ArrayList<Structure> structureList = Dictionary.getStructures();
        for(ArrayList<RefEntryString> valuesOfWidget: refEntryStringList){
            WidgetId widgetId = valuesOfWidget.get(0).getWidgetId();

            for(Structure structure: structureList){
                ArrayList<Structure.DeleteValuePair> deleteValuePairList = structure.getReferenceLocations(valuesOfWidget, widgetId);
                result.addAll(deleteValuePairList);
            }
        }
        return result;
    }
}
