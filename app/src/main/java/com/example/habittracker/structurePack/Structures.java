package com.example.habittracker.structurePack;

import com.example.habittracker.Algorithms.Lists;
import com.example.habittracker.StaticClasses.Dictionary;

public class Structures {
    public static void isStructuresSame(Structure structureFirst, Structure structureSecond){
        structureFirst.equalsThrows(structureSecond);
        structureFirst.getWidgetParam().equalsThrows(structureSecond.getWidgetParam());
        Lists.equalsThrowsAsSetsRecursive(structureFirst.getEntries(), structureSecond.getEntries());
    }

    public static boolean isSpreadsheet(String type){
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }
}
