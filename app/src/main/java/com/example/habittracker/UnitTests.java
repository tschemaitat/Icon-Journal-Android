package com.example.habittracker;

import android.content.Context;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.ListSingleItemParam;
import com.example.habittracker.structures.WidgetId;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.ListParam;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;
import java.util.Collections;

public class UnitTests {
    public Context context;
    public UnitTests(Context context){
        this.context = context;
        makeBodyPartStructure();
        //makeMuscleStructure();
        Structure exercises = makeExerciseStructure();
        makeExerciseRoutineStructure(exercises);
        Structure genreStructure = makeGenreStructure();
        makeShowStructure(genreStructure);
    }

    private Structure makeGenreStructure() {
        String spreadSheetName = "show genres";
        EditTextParam nameEditText = new EditTextParam("name");
        nameEditText.isUniqueAttribute = true;
        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText
        });
        return Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    private void makeShowStructure(Structure genreStructure) {
        String spreadSheetName = "shows";
        EditTextParam nameEditText = new EditTextParam("name");
        nameEditText.isUniqueAttribute = true;
        WidgetId genreWidgetId = getWidgetFromStructure("name", genreStructure);
        DropDownParam exerciseDropDown = new DropDownParam("genre",
                genreStructure, genreWidgetId, new ArrayList<>());
        ListSingleItemParam genreList = new ListSingleItemParam("genres", exerciseDropDown);
        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                genreList
        });
        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    public void makeBodyPartStructure(){
        String spreadSheetName = "body part";
        EditTextParam nameEditText = new EditTextParam("name");

        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText
        });

        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    public void makeMuscleStructure(){
        String spreadSheetName = "muscleGroup";
        EditTextParam nameEditText = new EditTextParam("name");
        EditTextParam bodyPartEditText = new EditTextParam("body part");

        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                bodyPartEditText
        });
        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }


    public Structure makeExerciseStructure(){
        String spreadSheetName = "exercises";
        EditTextParam nameEditText = new EditTextParam("name");
        nameEditText.isUniqueAttribute = true;
        EditTextParam bodyPartEditText = new EditTextParam("body part");
        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                bodyPartEditText
        });
        return Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);
    }

    public void makeExerciseRoutineStructure(Structure exerciseStructure){
        String spreadSheetName = "exercise routine";
        EditTextParam nameEditText = new EditTextParam("name");
        nameEditText.isUniqueAttribute = true;
        WidgetId exerciseName = getWidgetFromStructure("name", exerciseStructure);
        WidgetId bodyPart = getWidgetFromStructure("body part", exerciseStructure);
        DropDownParam exerciseDropDown = new DropDownParam("exercise",
                exerciseStructure, exerciseName, new ArrayList<>(Collections.singleton(bodyPart)));
        exerciseDropDown.isUniqueAttribute = true;
        EditTextParam repEditText = new EditTextParam("reps");
        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                exerciseDropDown,
                repEditText
        });
        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);

    }

    public WidgetId getWidgetFromStructure(String widgetName, Structure structure){
        ArrayList<WidgetId> widgets = structure.getWidgetIdList();
        for(WidgetId widget: widgets){
            if(widgetName.equals(widget.getNameWithPath().getLast().getString())){
                return widget;
            }
        }
        MainActivity.log("name: " + widgetName + ", widgets: \n" + widgets);
        throw new RuntimeException("tried to find widget by name");
    }




    public static String[] exerciseNames = {
            "pushup",
            "pullup",
            "dip",
    };
}
