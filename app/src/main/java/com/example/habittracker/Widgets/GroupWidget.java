package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;

import java.util.ArrayList;

public class GroupWidget extends EntryWidget implements FocusTreeParent {
    private Context context;
    private WidgetLayout layout;

    public static final String className = "group widget";
    public GroupWidget(Context context){
        super(context);
        this.context = context;
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout.getView());
        getView().setId(R.id.groupWidget);
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<WidgetValue> result = new ArrayList<>();

        for(EntryWidget widget: getEntryWidgets()){
            result.add(widget.getValue());
        }

        return new GroupValue(result);
    }

    public ArrayList<EntryWidget> getEntryWidgets(){
        ArrayList<EntryWidget> entryWidgets = new ArrayList<>();
        for(Widget widget: layout.widgets()){
            entryWidgets.add((EntryWidget) widget);
        }
        return entryWidgets;
    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {
        GroupValue groupValue = (GroupValue) widgetValue;
        MainActivity.log("group setting values: \n" + groupValue.hierarchy());

        ArrayList<EntryWidget> entryWidgets = getEntryWidgets();
        for(int i = 0; i < entryWidgets.size(); i++){

            EntryWidget entryWidget = entryWidgets.get(i);
            MainActivity.log("widget: " + entryWidget.getName() + ", id: " + entryWidget.getWidgetId());
            entryWidget.setValue(groupValue.getWidgetValueByWidget(entryWidget.getWidgetId()));
        }
    }

    @Override
    protected void setHint(String hintString) {

    }

    @Override
    public void setParamCustom(EntryWidgetParam param) {
        GroupWidgetParam groupParams = (GroupWidgetParam) param;
        layout.inflateAll(groupParams.params, onDataChangedListener());
        for(EntryWidget widget: getEntryWidgets()){
            widget.setFocusParent(this);
        }
    }

    public WidgetLayout getWidgetLayout(){
        return layout;
    }

    public LinLayout getLinLayout() {
        return getWidgetLayout().getLinLayout();
    }


    @Override
    public EntryWidget getFirstWidget() {
        return (EntryWidget) layout.widgets().get(0);
    }

    @Override
    public EntryWidget findNextWidget(EntryWidget entryWidget){
        return FocusTreeParentHelper.findNextWidget(entryWidget, getEntryWidgets(), getFocusParent());
    }
}
