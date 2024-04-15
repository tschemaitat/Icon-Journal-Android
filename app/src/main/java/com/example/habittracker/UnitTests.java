package com.example.habittracker;

import android.content.Context;
import android.icu.text.UnicodeSet;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.EntryDropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class UnitTests {
    public Context context;
    public UnitTests(Context context){
        this.context = context;
        makeBodyPartStructure();
        //makeMuscleStructure();
        Structure exercises = makeExerciseStructure();
        makeExerciseRoutineStructure(exercises);
    }

    public void makeBodyPartStructure(){
        String spreadSheetName = "body part";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");

        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText
        });

        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    public void makeMuscleStructure(){
        String spreadSheetName = "muscleGroup";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");
        CustomEditText.EditTextParam bodyPartEditText = new CustomEditText.EditTextParam("body part");

        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                bodyPartEditText
        });
        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }


    public Structure makeExerciseStructure(){
        String spreadSheetName = "exercises";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");
        CustomEditText.EditTextParam bodyPartEditText = new CustomEditText.EditTextParam("body part");

        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                bodyPartEditText
        });
        return Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    public void makeExerciseRoutineStructure(Structure exerciseStructure){
        String spreadSheetName = "exercise routine";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");
        WidgetId exerciseName = getWidgetFromStructure("name", exerciseStructure);
        WidgetId bodyPart = getWidgetFromStructure("body part", exerciseStructure);
        EntryDropDown.DropDownParam exerciseDropDown = new EntryDropDown.DropDownParam("exercise", null,
                exerciseStructure, exerciseName, new ArrayList<>(Collections.singleton(bodyPart)));
        CustomEditText.EditTextParam repEditText = new CustomEditText.EditTextParam("name");
        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                exerciseDropDown,
                repEditText
        });
        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    public WidgetId getWidgetFromStructure(String widgetName, Structure structure){
        ArrayList<WidgetId> widgets = structure.getHeader().getWidgetIdList();
        for(WidgetId widget: structure.getHeader().getWidgetIdList()){
            if(widgetName.equals(widget.getNameWithPath().getName())){
                return widget;
            }
        }
        throw new RuntimeException();
    }




    public static String[] exerciseNames = {
            "pushup",
            "pullup",
            "dip",
    };
}
