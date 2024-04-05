package com.example.habittracker;

import android.content.Context;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.GroupWidget;

public class UnitTests {
    public Context context;
    public UnitTests(Context context){
        this.context = context;
        makeBodyPartStructure();
        //makeMuscleStructure();
        makeExercisesStructure();
    }

    public void makeBodyPartStructure(){
        String spreadSheetName = "body part";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");

        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText
        });
        Structure structure = new Structure(spreadSheetName, groupWidgetParam, Dictionary.category);
        Dictionary.saveStructure(structure);
    }

    public void makeMuscleStructure(){
        String spreadSheetName = "muscleGroup";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");
        CustomEditText.EditTextParam bodyPartEditText = new CustomEditText.EditTextParam("body part");

        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                bodyPartEditText
        });
        Structure structure = new Structure(spreadSheetName, groupWidgetParam, Dictionary.category);
        Dictionary.saveStructure(structure);
    }


    public void makeExercisesStructure(){
        String spreadSheetName = "exercises";
        CustomEditText.EditTextParam nameEditText = new CustomEditText.EditTextParam("name");
        CustomEditText.EditTextParam bodyPartEditText = new CustomEditText.EditTextParam("body part");

        GroupWidget.GroupWidgetParam groupWidgetParam = new GroupWidget.GroupWidgetParam(null, new EntryWidgetParam[]{
                nameEditText,
                bodyPartEditText
        });
        Structure structure = new Structure(spreadSheetName, groupWidgetParam, Dictionary.category);
        Dictionary.saveStructure(structure);



    }




    public static String[] exerciseNames = {
            "pushup",
            "pullup",
            "dip",
    };
}
