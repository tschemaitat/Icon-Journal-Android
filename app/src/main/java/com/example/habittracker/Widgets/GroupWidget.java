package com.example.habittracker.Widgets;

import android.content.Context;

import com.example.habittracker.Layouts.InterceptLinearLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.structures.Structure;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupWidget extends EntryWidget {
    private Context context;
    private WidgetLayout layout;
    private ListWidget listWidgetParent;

    public static final String className = "group widget";
    public GroupWidget(Context context){
        super(context);
        this.context = context;
        layout = new WidgetLayout(context);
        setViewWrapperChild(layout.getView());
        getView().setId(R.id.groupWidget);
    }

    public void setListParent(ListWidget listWidgetParent){
        this.listWidgetParent = listWidgetParent;
        MainActivity.log("group widget enabling intercept");
        ((InterceptLinearLayout) getView()).enableIntercept(this);
    }

    public void startDrag() {
        listWidgetParent.startDrag(this);
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        ArrayList<WidgetValue> result = new ArrayList<>();

        for(EntryWidget widget: entryWidgets()){
            result.add(widget.getValue());
        }

        return new GroupValue(result);
    }

    public ArrayList<EntryWidget> entryWidgets(){
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

        ArrayList<EntryWidget> entryWidgets = entryWidgets();
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
    }

    public WidgetLayout getWidgetLayout(){
        return layout;
    }

    public LinLayout getLinLayout() {
        return getWidgetLayout().getLinLayout();
    }




}
