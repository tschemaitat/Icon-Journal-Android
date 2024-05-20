package com.example.habittracker.structurePack;

import com.example.habittracker.StaticClasses.Dictionary;

import java.util.ArrayList;
import java.util.Objects;

public class Structures {
    public static boolean isStructuresSame(Structure structureFirst, Structure structureSecond){
        if( ! Objects.equals(structureFirst, structureSecond)){
            return false;
        }
        if( ! Objects.equals(structureFirst.getWidgetParam(), structureSecond.getWidgetParam())){
            return false;
        }
        if( ! Objects.equals(structureFirst.getEntries(), structureSecond.getEntries())){
            return false;
        }
        return true;
    }

    public static boolean isSpreadsheet(String type){
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }
}
