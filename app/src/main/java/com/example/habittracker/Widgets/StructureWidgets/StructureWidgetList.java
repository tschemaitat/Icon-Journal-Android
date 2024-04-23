package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.ListParam;

import java.util.ArrayList;

public class StructureWidgetList implements Widget{
    private Context context;
    private GroupWidget groupWidget;
    private LinLayout parent;
    public StructureWidgetList(Context context, LinLayout parent) {
        this.context = context;
        this.parent = parent;
        groupWidget = new GroupWidget(context);
        parent.add(groupWidget.getView());
        init();
    }

    private void init(){
        groupWidget.getWidgetLayout().getLinLayout().addButton(view -> {
            addStructureWidget();
        });
        Margin.setListLayout(groupWidget.getLinLayout());

//        groupWidget.getView().setBackground(new ColorDrawable(ColorPalette.tertiary));
//        Margin.setPadding(groupWidget.getView(), Margin.listPadding());
    }

    public StructureWidget addStructureWidget(){
        StructureWidget structureWidget = new StructureWidget(context, groupWidget.getWidgetLayout());
        groupWidget.getWidgetLayout().add(structureWidget);
        return structureWidget;
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }


    public EntryWidgetParam getParam() {
        ArrayList<StructureWidget> structureWidgets = EnumLoop.makeList(groupWidget.getWidgetLayout().widgets(), (widget) ->(StructureWidget) widget);
        ArrayList<EntryWidgetParam> entryWidgetParams = EnumLoop.makeList(structureWidgets, (structureWidget)->structureWidget.getWidgetInfo());
        for(EntryWidgetParam entryWidgetParam: entryWidgetParams)
            if(entryWidgetParam == null)
                return null;

        return new ListParam(null, new GroupWidgetParam(null, entryWidgetParams));
    }

    @Override
    public void setParam(EntryWidgetParam params) {
        ListParam listParam = (ListParam) params;
        ArrayList<EntryWidgetParam> children = listParam.cloneableWidget.params;
        for(EntryWidgetParam entryWidgetParam: children){
            StructureWidget structureWidget = addStructureWidget();
            structureWidget.setParam(entryWidgetParam);
        }
    }

    @Override
    public View getView() {
        return groupWidget.getView();
    }

    public boolean hasUniqueAttribute() {
        ArrayList<StructureWidget> structureWidgetList = EnumLoop.makeList(groupWidget.getWidgetLayout().widgets(),
                (widget)->(StructureWidget) widget);
        for(StructureWidget structureWidget: structureWidgetList){
            if(structureWidget.hasUniqueAttribute())
                return true;
        }
        return false;
    }
}
