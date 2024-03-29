package com.example.habittracker.Inflatables;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.ColorPalette;
import com.example.habittracker.Dictionary;
import com.example.habittracker.GLib;
import com.example.habittracker.LinLayout;
import com.example.habittracker.Margin;
import com.example.habittracker.R;
import com.example.habittracker.Structs.Structure;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.WidgetLayout;
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

    CustomEditText nameEditor;


    public StructureEditor(Context context, Structure structure){
        this.context = context;
        if(structure == null)
            throw new RuntimeException("structure null");

        if(structure.getType() == null)
            throw new RuntimeException("structure type null");

        this.structure = structure;

        layout = new LinLayout(context);
        layout.setPadding(Margin.initialPagePadding());
        layout.getView().setId(R.id.pageLayout);

        Button saveButton = (Button) GLib.inflate(R.layout.button_layout);
        saveButton.setOnClickListener(view -> onSave());
        saveButton.setText("save template");
        layout.add(saveButton);
        LinearLayout.LayoutParams buttonLayoutParam = new LinearLayout.LayoutParams(-2, -2);
        buttonLayoutParam.gravity = Gravity.RIGHT;
        buttonLayoutParam.setMargins(0, 0, 40, 0);
        saveButton.setLayoutParams(buttonLayoutParam);

        nameEditor = new CustomEditText(context);
        String name = structure.getName();
        nameEditor.setText(name);
        nameEditor.setOnDataChangedListener(()->{});
        nameEditor.setName("spreadsheet name");
        layout.add(nameEditor.getView());

        widgetLayout = new WidgetLayout(context);
        layout.add(widgetLayout.getView());


        if(structure.getParam() != null){
            GroupWidget.GroupWidgetParam groupWidgetParam = (GroupWidget.GroupWidgetParam) structure.getParam();
            ArrayList<EntryWidgetParam> entryWidgetParams = groupWidgetParam.params;
            for(EntryWidgetParam param: entryWidgetParams){
                StructureWidget structureWidget = new StructureWidget(context);
                structureWidget.setParam(param);
                widgetLayout.add(structureWidget);
            }
        }




        widgetLayout.getLinLayout().addButton(view -> {
            StructureWidget structureWidget = new StructureWidget(context);
            structureWidget.setOnDataChangedListener(()->{});
            widgetLayout.add(structureWidget);
            structureWidget.addDeleteButton(()->{
                widgetLayout.remove(structureWidget);
            });
        });

        widgetLayout.getLinLayout().setChildMargin(Margin.listChildMargin());

        for(Widget widget: widgetLayout.widgets()){
            StructureWidget structureWidget = (StructureWidget) widget;

        }
    }

    public void onSave(){
        System.out.println("on save: " + nameEditor.getText());

        boolean error = false;

        if(nameEditor.getText() == null){
            nameEditor.getViewWrapper().setNameRed();
            error = true;
        }

        ArrayList<EntryWidgetParam> entryWidgetParamArrayList = new ArrayList<>();
        for(int i = 0; i < widgetLayout.widgets().size(); i++){

            Widget widget = widgetLayout.widgets().get(i);
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

        EntryWidgetParam groupParam = new GroupWidget.GroupWidgetParam(nameEditor.getText(), entryWidgetParamArrayList);
        System.out.println("exporting widgetParams: \n" + groupParam.hierarchyString());
        Structure newStructure = new Structure(nameEditor.getText(), groupParam, structure.getType());
        Dictionary.saveStructure(newStructure);

        //System.out.println("new set of structures: " + Dictionary.getStructureKeys());

        //System.out.println("set of categories: " + Dictionary.getStructureKeys("category"));
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
}
