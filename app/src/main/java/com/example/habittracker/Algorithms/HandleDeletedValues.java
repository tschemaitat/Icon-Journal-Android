package com.example.habittracker.Algorithms;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.structures.Structure;
import com.example.habittracker.structures.WidgetInStructure;

import java.util.ArrayList;

public class HandleDeletedValues {
    public static void gatherReferences(ArrayList<WidgetInStructure> widgetInStructureList){

    }

    public static void gatherReferences(WidgetInStructure widgetInStructure){

    }

    public static ArrayList<Structure.DeleteValuePair> getStructures(ArrayList<ArrayList<RefEntryString>> refEntryStringList){
        ArrayList<Structure.DeleteValuePair> result = new ArrayList<>();
        ArrayList<Structure> structureList = Dictionary.getStructures();
        for(ArrayList<RefEntryString> valuesOfWidget: refEntryStringList){
            WidgetInStructure widgetInStructure = valuesOfWidget.get(0).getWidgetInStructure();

            for(Structure structure: structureList){
                ArrayList<Structure.DeleteValuePair> deleteValuePairList = structure.getReferenceOfSource(valuesOfWidget, widgetInStructure);
                result.addAll(deleteValuePairList);
            }
        }
        return result;
    }
}
