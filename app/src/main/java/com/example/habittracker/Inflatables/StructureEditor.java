package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.Dictionary;
import com.example.habittracker.GLib;
import com.example.habittracker.R;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.StructureWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureEditor implements Inflatable {


    Context context;
    GroupWidget groupWidget;
    Structure structure;
    LinearLayout linearLayout;

    CustomEditText nameEditor;


    public StructureEditor(Context context, Structure structure){
        this.context = context;
        if(structure == null)
            throw new RuntimeException("structure null");

        if(structure.getType() == null)
            throw new RuntimeException("structure type null");

        this.structure = structure;

        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(GLib.initialHorMargin, GLib.initialVertMargin, GLib.initialHorMargin, GLib.initialVertMargin);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button saveButton = (Button) GLib.inflate(R.layout.button_layout);
        saveButton.setOnClickListener(view -> onSave());
        saveButton.setText("save template");
        linearLayout.addView(saveButton);
        LinearLayout.LayoutParams buttonLayoutParam = new LinearLayout.LayoutParams(-2, -2);
        buttonLayoutParam.gravity = Gravity.RIGHT;
        buttonLayoutParam.setMargins(0, 0, 40, 0);
        saveButton.setLayoutParams(buttonLayoutParam);

        groupWidget = GLib.createInitialGroupWidget(context);
        linearLayout.addView(groupWidget.getView());

        nameEditor = new CustomEditText(context);
        String name = structure.getName();
        nameEditor.setText(name);
        nameEditor.setOnDataChangedListener(()->{});
        nameEditor.setName("spreadsheet name");
        groupWidget.addWidget(nameEditor);
        if(structure.getParam() != null){
            GroupWidget.GroupWidgetParam groupWidgetParam = (GroupWidget.GroupWidgetParam) structure.getParam();
            ArrayList<EntryWidgetParam> entryWidgetParams = groupWidgetParam.params;
            for(EntryWidgetParam param: entryWidgetParams){
                StructureWidget structureWidget = new StructureWidget(context);
                structureWidget.setParam(param);
                groupWidget.addWidget(structureWidget);
            }
        }




        groupWidget.addButton(view -> {
            StructureWidget structureWidget = new StructureWidget(context);
            structureWidget.setOnDataChangedListener(()->{});
            groupWidget.addWidget(structureWidget);
            structureWidget.addDeleteButton(()->{
                groupWidget.removeWidget(structureWidget);
            });
        });
    }

    public void onSave(){
        System.out.println("on save: " + nameEditor.text());

        boolean error = false;

        if(nameEditor.text() == null){
            nameEditor.setNameRed();
            error = true;
        }

        ArrayList<EntryWidgetParam> entryWidgetParamArrayList = new ArrayList<>();
        for(int i = 0; i < groupWidget.widgets().size(); i++){
            if(i == 0)
                continue;
            Widget widget = groupWidget.widgets().get(i);
            EntryWidgetParam entryWidgetParam = widget.getParam();
            if(entryWidgetParam == null){
                error = true;
                break;
            }
            entryWidgetParamArrayList.add(entryWidgetParam);
        }
        if(error)
            return;

        //System.out.println("starting save");

        EntryWidgetParam groupParam = new GroupWidget.GroupWidgetParam(nameEditor.text(), entryWidgetParamArrayList);
        System.out.println("exporting widgetParams: \n" + groupParam.hierarchyString());
        Structure newStructure = new Structure(nameEditor.text(), groupParam, structure.getType());
        Dictionary.saveStructure(newStructure);

        //System.out.println("new set of structures: " + Dictionary.getStructureKeys());

        //System.out.println("set of categories: " + Dictionary.getStructureKeys("category"));
    }


    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {
//        System.out.println("structure editor being removed");
//        EntryWidgetParam widgetParam = groupWidget.getParam();
//        System.out.println("exporting widgetParams: \n" + widgetParam.hierarchyString(0));
//        Structure newStructure = new Structure(structure.getName(), widgetParam, structure.getType());
//        Dictionary.saveStructure(newStructure);
    }

    @Override
    public void onOpened() {

    }
}
