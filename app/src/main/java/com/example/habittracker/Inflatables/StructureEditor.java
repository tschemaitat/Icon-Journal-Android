package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.R;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.StructureWidget;
import com.example.habittracker.Widgets.Widget;

import java.util.ArrayList;

public class StructureEditor implements Inflatable {


    Context context;
    WidgetLayout widgetLayout;
    Structure structure;
    LinLayout layout;

    CustomEditText structureKeyEditor;


    public StructureEditor(Context context, Structure structure){
        this.context = context;
        if(structure == null)
            throw new RuntimeException("structure null");

        if(structure.getType() == null)
            throw new RuntimeException("structure type null");
        this.structure = structure;

        layout = new LinLayout(context);
        Margin.setInitialLayout(layout.getView());
        layout.getView().setId(R.id.pageLayout);

        Button saveButton = setupSaveButton(layout, ()->onSave());
        structureKeyEditor = setupStructureKeyEditor(structure.getName(), layout, context);
        widgetLayout = setupWidgetLayout(layout, structure, context, ()->addStructureWidget());
    }

    private static WidgetLayout setupWidgetLayout(LinLayout layout, Structure structure, Context context, Runnable addStructureWidget) {
        WidgetLayout widgetLayout = new WidgetLayout(context);
        layout.add(widgetLayout.getView());
        if(structure.getParam() != null)
            inflateStructureUsingParam(structure, widgetLayout, context);
        widgetLayout.getLinLayout().addButton(view -> addStructureWidget.run());
        widgetLayout.getLinLayout().setChildMargin(Margin.listChildMargin());
        return widgetLayout;
    }

    private void addStructureWidget() {
        StructureWidget structureWidget = new StructureWidget(context);
        structureWidget.setOnDataChangedListener(()->{});
        widgetLayout.add(structureWidget);
        structureWidget.addDeleteButton(()->{
            widgetLayout.remove(structureWidget);
        });
    }

    private static void inflateStructureUsingParam(Structure structure, WidgetLayout widgetLayout, Context context) {
        GroupWidget.GroupWidgetParam groupWidgetParam = (GroupWidget.GroupWidgetParam) structure.getParam();
        ArrayList<EntryWidgetParam> entryWidgetParams = groupWidgetParam.params;
        for(EntryWidgetParam param: entryWidgetParams){
            StructureWidget structureWidget = new StructureWidget(context);
            structureWidget.setParam(param);
            widgetLayout.add(structureWidget);
        }
    }

    private static Button setupSaveButton(LinLayout layout, Runnable onSave) {
        Button saveButton = (Button) GLib.inflate(R.layout.button_layout);
        saveButton.setOnClickListener(view -> onSave.run());
        saveButton.setText("save template");
        layout.add(saveButton);
        LinearLayout.LayoutParams buttonLayoutParam = new LinearLayout.LayoutParams(-2, -2);
        buttonLayoutParam.gravity = Gravity.RIGHT;
        buttonLayoutParam.setMargins(0, 0, 40, 0);
        saveButton.setLayoutParams(buttonLayoutParam);
        return saveButton;
    }

    private static CustomEditText setupStructureKeyEditor(String name, LinLayout layout, Context context) {
        CustomEditText structureKeyEditor = new CustomEditText(context);
        structureKeyEditor.setText(name);
        structureKeyEditor.setOnDataChangedListener(()->{});
        structureKeyEditor.setName("spreadsheet name");
        layout.add(structureKeyEditor.getView());
        return structureKeyEditor;
    }

    public void onSave(){
        System.out.println("on save: " + structureKeyEditor.getText());

        ArrayList<EntryWidgetParam> entryWidgetParams = checkForErrorBeforeSave();
        if(entryWidgetParams == null)
            return;
        if(structure.isSpreadsheet()){
            if( ! checkForUniqueAttribute()){
                System.out.println("missing widget for unique attribute");
                MainActivity.showToast(context, "missing widget for unique attribute");
                return;
            }
        }

        EntryWidgetParam groupParam = new GroupWidget.GroupWidgetParam(structureKeyEditor.getText(), entryWidgetParams);
        System.out.println("exporting widgetParams: \n" + groupParam.hierarchyString());
        Structure newStructure = new Structure(structureKeyEditor.getText(), groupParam, structure.getType());
        Dictionary.saveStructure(newStructure);

        MainActivity.showToast(context, "saving " + structureKeyEditor.getText() + " successful");
    }

    public boolean checkForUniqueAttribute(){
        StructureWidget structureWidget = ((StructureWidget) widgetLayout.widgets().get(0));
        if(structureWidget.getType().equals(CustomEditText.className)){
            return true;
        }
        return false;
    }

    public ArrayList<EntryWidgetParam> checkForErrorBeforeSave(){
        boolean error = false;

        if(structureKeyEditor.getText() == null){
            structureKeyEditor.getViewWrapper().setNameRed();
            error = true;
        }

        ArrayList<EntryWidgetParam> entryWidgetParams = new ArrayList<>();
        for(int i = 0; i < widgetLayout.widgets().size(); i++){

            Widget widget = widgetLayout.widgets().get(i);
            EntryWidgetParam entryWidgetParam = widget.getParam();
            if(entryWidgetParam == null){
                System.out.println("error at index: " + i);
                error = true;
                break;
            }
            entryWidgetParams.add(entryWidgetParam);
        }
        if(error){
            System.out.println("<structure editor> error while gathering params");
            return null;
        }

        return entryWidgetParams;
    }


    @Override
    public View getView() {
        return layout.getView();
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

    @Override
    public boolean tryToRemove() {
        return true;
    }
}
