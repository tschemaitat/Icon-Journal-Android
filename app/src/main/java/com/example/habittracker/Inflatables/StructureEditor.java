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
import com.example.habittracker.structures.Structure;
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
    Structure structure = null;
    LinLayout layout;

    String structureType = null;

    CustomEditText structureKeyEditor;

    boolean newStructure = false;


    public StructureEditor(Context context, Structure structure){

        this.context = context;
        if(structure == null)
            throw new RuntimeException("structure null");

        if(structure.getType() == null)
            throw new RuntimeException("structure type null");
        MainActivity.log("editing structure: " + structure.getCachedName());
        this.structure = structure;
        this.structureType = structure.getType();

        init(structure.getCachedName().getString(), (GroupWidget.GroupWidgetParam)structure.getParam());
    }

    public StructureEditor(Context context, String structureType){
        MainActivity.log("new structure: " + structureType);
        this.context = context;
        this.structureType = structureType;
        init(null, null);
    }

    private void init(String name, GroupWidget.GroupWidgetParam groupWidgetParam){
        layout = new LinLayout(context);
        Margin.setInitialLayout(layout.getView());
        layout.getView().setId(R.id.pageLayout);

        Button saveButton = setupSaveButton(layout, ()->onSave());
        structureKeyEditor = setupStructureKeyEditor(name, layout, context);
        widgetLayout = setupWidgetLayout(layout, groupWidgetParam, context, ()->addStructureWidget());
    }

    private static WidgetLayout setupWidgetLayout(LinLayout layout, GroupWidget.GroupWidgetParam groupWidgetParam, Context context, Runnable addStructureWidget) {
        WidgetLayout widgetLayout = new WidgetLayout(context);
        layout.add(widgetLayout.getView());
        if(groupWidgetParam != null)
            inflateStructureUsingParam(groupWidgetParam, widgetLayout, context);
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

    private static void inflateStructureUsingParam(GroupWidget.GroupWidgetParam groupWidgetParam, WidgetLayout widgetLayout, Context context) {
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
        MainActivity.log("on save: " + structureKeyEditor.getText());

        ArrayList<EntryWidgetParam> entryWidgetParams = checkForErrorBeforeSave();
        if(entryWidgetParams == null)
            return;
        if(Structure.isSpreadsheet(structureType)){
            if( ! checkForUniqueAttribute()){
                //System.out.println("missing widget for unique attribute");
                MainActivity.showToast(context, "missing widget for unique attribute");
                return;
            }
        }
        saveAfterCheck(entryWidgetParams);
        MainActivity.changePage(new EditorSelectionPage(context));
    }

    public void saveAfterCheck(ArrayList<EntryWidgetParam> entryWidgetParams){
        EntryWidgetParam groupParam = new GroupWidget.GroupWidgetParam(structureKeyEditor.getText(), entryWidgetParams);
        //System.out.println("exporting widgetParams: \n" + groupParam.hierarchyString());
        if(structure == null){
            Dictionary.addStructure(structureKeyEditor.getText(), groupParam, structureType);
        }else{
            Dictionary.editStructure(structure, groupParam);
        }


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
    public boolean tryToRemove(Inflatable page) {
        return true;
    }
}
