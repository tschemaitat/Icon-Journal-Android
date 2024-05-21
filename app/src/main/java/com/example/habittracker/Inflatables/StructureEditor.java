package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.R;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structurePack.Structure;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Widgets.StructureWidgets.StructureWidget;
import com.example.habittracker.structurePack.Structures;

import java.util.ArrayList;

public class StructureEditor implements Inflatable{


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
        MainActivity.log("editing structure: " + structure.getCachedName() + ", with id: " + structure.getId());
        this.structure = structure;
        this.structureType = structure.getType();

        init(structure.getCachedName().getString(), (GroupWidgetParam)structure.getWidgetParam());
    }

    public StructureEditor(Context context, String structureType){
        MainActivity.log("new structure: " + structureType);
        this.context = context;
        this.structureType = structureType;
        init(null, null);
    }

    private void init(String name, GroupWidgetParam groupWidgetParam){
        layout = new LinLayout(context);
        Margin.setInitialLayout(layout.getView());
        layout.getView().setId(R.id.pageLayout);

        Button saveButton = setupSaveButton(layout, ()->onSave());
        structureKeyEditor = setupStructureKeyEditor(name, layout, context);
        widgetLayout = setupWidgetLayout(layout, groupWidgetParam, context, ()->addStructureWidget());
    }

    private WidgetLayout setupWidgetLayout(LinLayout layout, GroupWidgetParam groupWidgetParam, Context context, Runnable addStructureWidget) {
        WidgetLayout widgetLayout = new WidgetLayout(context);
        layout.add(widgetLayout.getView());
        if(groupWidgetParam != null)
            inflateStructureUsingParam(groupWidgetParam, widgetLayout, context);
        widgetLayout.getLinLayout().addButton(view -> addStructureWidget.run());
        widgetLayout.getLinLayout().setChildMargin(Margin.listChildMargin());
        return widgetLayout;
    }

    private void addStructureWidget() {
        StructureWidget structureWidget = new StructureWidget(context, widgetLayout);
        structureWidget.setOnDataChangedListener(()->{});
        widgetLayout.add(structureWidget);
    }

    private void inflateStructureUsingParam(GroupWidgetParam groupWidgetParam, WidgetLayout widgetLayout, Context context) {
        ArrayList<EntryWidgetParam> entryWidgetParams = groupWidgetParam.params;
        for(EntryWidgetParam param: entryWidgetParams){
            StructureWidget structureWidget = new StructureWidget(context, widgetLayout);
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

        EditTextParam editTextParam = new EditTextParam("spreadsheet name");
        CustomEditText structureKeyEditor = (CustomEditText)GLib.inflateWidget(context, editTextParam, ()->{});
        structureKeyEditor.setText(name);
        layout.add(structureKeyEditor.getView());
        return structureKeyEditor;
    }

    public void onSave(){
        MainActivity.log("on save: " + structureKeyEditor.getText());

        GroupWidgetParam entryWidgetParams = checkForErrorBeforeSave();
        if(entryWidgetParams == null)
            return;
        if(Structures.isSpreadsheet(structureType)){
            if( ! checkForUniqueAttribute()){
                //System.out.println("missing widget for unique attribute");
                MainActivity.showToast(context, "missing widget for unique attribute");
                return;
            }
        }
        saveAfterCheck(entryWidgetParams);
        MainActivity.changePage(new EditorSelectionPage(context));
    }

    public void saveAfterCheck(GroupWidgetParam entryWidgetParams){
        //System.out.println("exporting widgetParams: \n" + groupParam.hierarchyString());
        if(structure == null){
            Dictionary.addStructure(structureKeyEditor.getText(), entryWidgetParams, structureType);
        }else{
            Dictionary.editStructure(structure, entryWidgetParams);
        }


        MainActivity.showToast(context, "saving " + structureKeyEditor.getText() + " successful");
    }

    public boolean checkForUniqueAttribute(){
        ArrayList<StructureWidget> structureWidgetList = EnumLoop.makeList(widgetLayout.widgets(),
                (widget)->(StructureWidget) widget);
        for(StructureWidget structureWidget: structureWidgetList){
            if(structureWidget.hasUniqueAttribute()){
                MainActivity.log("structure has a unqiue attribute");
                return true;
            }

        }
        MainActivity.log("doesn't have a unique attribute");
        return false;
    }

    public GroupWidgetParam checkForErrorBeforeSave(){
        boolean error = false;

        if(structureKeyEditor.getText() == null){
            structureKeyEditor.setError();
            error = true;
        }

        ArrayList<EntryWidgetParam> widgetInfoList = new ArrayList<>();
        for(int i = 0; i < widgetLayout.widgets().size(); i++){

            StructureWidget structureWidget = (StructureWidget) widgetLayout.widgets().get(i);
            EntryWidgetParam widgetInfo = structureWidget.getWidgetInfo();
            if(widgetInfo == null){
                MainActivity.log("error at index: " + i);
                error = true;
                break;
            }
            widgetInfoList.add(widgetInfo);
        }
        if(error){
            MainActivity.log("<structure editor> error while gathering params");
            return null;
        }



        return new GroupWidgetParam((String) null, widgetInfoList);
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
