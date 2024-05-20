package com.example.habittracker;

import android.content.Context;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.WidgetParams.ListMultiItemParam;
import com.example.habittracker.structurePack.WidgetInStructure;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.structurePack.WidgetPath;

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
        WidgetInStructure genreWidgetInStructure = getWidgetFromStructure("name", genreStructure);
        DropDownParam exerciseDropDown = new DropDownParam("genre",
                genreStructure.getId(), genreWidgetInStructure.getWidgetId(), new ArrayList<>());
        EditTextParam genreDesc = new EditTextParam("genreDesc");
        ListMultiItemParam genreList = new ListMultiItemParam("genres", new GroupWidgetParam(null, new EntryWidgetParam[]{
                exerciseDropDown,
                genreDesc,
        }));
        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                genreList,
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
        WidgetInStructure exerciseName = getWidgetFromStructure("name", exerciseStructure);
        WidgetInStructure bodyPart = getWidgetFromStructure("body part", exerciseStructure);
        DropDownParam exerciseDropDown = new DropDownParam("exercise",
                exerciseStructure.getId(), exerciseName.getWidgetId(), new ArrayList<>(Collections.singleton(bodyPart.getWidgetId())));
        exerciseDropDown.isUniqueAttribute = true;
        EditTextParam repEditText = new EditTextParam("reps");
        GroupWidgetParam groupWidgetParam = new GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                exerciseDropDown,
                repEditText
        });
        Dictionary.addStructure(spreadSheetName, groupWidgetParam, Dictionary.category);

    }

    public WidgetInStructure getWidgetFromStructure(String widgetName, Structure structure){
        ArrayList<WidgetInStructure> widgets = structure.getWidgetIdList();
        for(WidgetInStructure widget: widgets){
            if(widgetName.equals(widget.getNameWithPath().getLast().getString())){
                return widget;
            }
        }
        MainActivity.log("name: " + widgetName + ", widgets: \n" + widgets);
        throw new RuntimeException("tried to find widget by name");
    }

    public void addValues(ArrayList<String> values, WidgetInStructure widgetInStructure, Structure structure){
        GroupWidgetParam groupWidgetParam = structure.getWidgetParam();
        GroupWidget groupWidget = (GroupWidget)GLib.inflateWidget(context, groupWidgetParam, ()->{});
        GroupValue groupValue = (GroupValue)groupWidget.getValue();
        WidgetPath widgetPath = widgetInStructure.getWidgetInfo().getWidgetPath();
        WidgetValue currentValue = groupValue;
        for(int i = 0; i < widgetPath.size(); i++){
            WidgetInStructure currentWidgetInStructure = widgetPath.get(i);

        }
    }




    public static String[] exerciseNames = {
            "pushup",
            "pullup",
            "dip",
    };
}
