package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.ViewLibrary.ButtonElement;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.VertLayout;
import com.example.habittracker.Widgets.WidgetParams.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.Widgets.WidgetParams.ListParam;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class StructureWidgetList implements Widget{
    private Context context;
    private WidgetLayout widgetLayout;
    private LinearElementLayout parent;
    public StructureWidgetList(Context context, LinearElementLayout parent) {
        this.context = context;
        this.parent = parent;
        widgetLayout = new WidgetLayout(context);
        parent.add(widgetLayout.getElement());
        init();
    }

    private void init(){
        //vertLayout
            //baseview
            //button
        LinearElementLayout buttonLayout = new VertLayout(context);
        buttonLayout.addWithParam(widgetLayout.getElement(), -2, -2);
        ButtonElement buttonElement = new ButtonElement(context, "add", this::addStructureWidget);
        buttonLayout.addWithParam(buttonElement, -2, -2);

//        widgetLayout.getLinearElementLayout().addButton(view -> {
//            addStructureWidget();
//        });
        Margin.setStructureWidgetListLayout(widgetLayout);

//        groupWidget.getView().setBackground(new ColorDrawable(ColorPalette.tertiary));
//        Margin.setPadding(groupWidget.getView(), Margin.listPadding());
    }

    public StructureWidget addStructureWidget(){
        StructureWidget structureWidget = new StructureWidget(context, widgetLayout);
        widgetLayout.add(structureWidget);
        return structureWidget;
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {

    }

    @Override
    public Element getElement() {
        return widgetLayout;
    }

    @Override
    public void setTextErrorColor() {
        throw new RuntimeException();
    }

    @Override
    public void resetTextErrorColor() {
        throw new RuntimeException();
    }

    @Override
    public void setHint(String string) {
        throw new RuntimeException();
    }


    public EntryWidgetParam getParam() {
        ArrayList<StructureWidget> structureWidgets = EnumLoop.makeList(widgetLayout.widgets(), (widget) ->(StructureWidget) widget);
        ArrayList<EntryWidgetParam> entryWidgetParams = EnumLoop.makeList(structureWidgets, (structureWidget)->structureWidget.getWidgetInfo());
        for(EntryWidgetParam entryWidgetParam: entryWidgetParams)
            if(entryWidgetParam == null)
                return null;
        if(entryWidgetParams.size() == 0)
            return null;

        return new ListParam((String) null, new GroupWidgetParam((String) null, entryWidgetParams));




    }

    @Override
    public void setParam(EntryWidgetParam params) {
        ListParam listMultiItemParam = (ListParam) params;
        ArrayList<EntryWidgetParam> children = listMultiItemParam.cloneableWidget.params;
        for(EntryWidgetParam entryWidgetParam: children){
            StructureWidget structureWidget = addStructureWidget();
            structureWidget.setParam(entryWidgetParam);
        }
    }

    @Override
    public View getView() {
        return widgetLayout.getView();
    }

    public boolean hasUniqueAttribute() {
        ArrayList<StructureWidget> structureWidgetList = EnumLoop.makeList(widgetLayout.widgets(),
                (widget)->(StructureWidget) widget);
        for(StructureWidget structureWidget: structureWidgetList){
            if(structureWidget.hasUniqueAttribute())
                return true;
        }
        return false;
    }
}
