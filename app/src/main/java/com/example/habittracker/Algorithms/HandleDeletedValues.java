package com.example.habittracker.Algorithms;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetInStructure;

import java.util.ArrayList;

public class HandleDeletedValues {


    public static ArrayList<ArrayList<RefEntryString>> getReferencesList(ArrayList<ArrayList<RefEntryString>> refEntryStringList){
        ArrayList<ArrayList<RefEntryString>> result = new ArrayList<>();
        for(ArrayList<RefEntryString> valuesOfWidget: refEntryStringList){
            ArrayList<RefEntryString> referencesOfWidget = getReferences(valuesOfWidget);
            result.add(referencesOfWidget);
        }
        return result;
    }

    public static ArrayList<RefEntryString> getReferences(ArrayList<RefEntryString> valuesOfWidget){
        ArrayList<RefEntryString> result = new ArrayList<>();
        ArrayList<Structure> structureList = Dictionary.getStructures();

        WidgetInStructure widgetInStructure = valuesOfWidget.get(0).getWidgetInStructure();

        for(Structure structure: structureList){
            ArrayList<RefEntryString> deleteValuePairList = structure.getReferenceLocationsOfSource(valuesOfWidget, widgetInStructure);
            result.addAll(deleteValuePairList);
        }

        return result;
    }
}
