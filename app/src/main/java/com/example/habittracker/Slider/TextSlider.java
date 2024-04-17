package com.example.habittracker.Slider;

import android.content.Context;

import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.structures.HeaderNode;
import com.example.habittracker.Widgets.EntryWidget;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;
import java.util.HashMap;

public class TextSlider extends EntryWidget {
    public static final String className = "text slider";
    Context context;
    SliderWithLabels sliderWithLabels;
    public TextSlider(Context context) {
        super(context);
        sliderWithLabels = new SliderWithLabels(context);
        setChild(sliderWithLabels);
        this.context = context;
        init();
    }

    private void init(){
        ArrayList<String> values = new ArrayList<>();
        values.add("morning");
        values.add("afternoon");
        values.add("night");

        ArrayList<Integer> labelIndex = new ArrayList<>();
        for(int i = 0; i < values.size(); i++)
            labelIndex.add(i);

        sliderWithLabels.setValues(values, labelIndex);
    }


    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }





    @Override
    public EntryValueTree getEntryValueTreeCustom() {
        return null;
    }



    @Override
    public void setValueCustom(EntryValueTree entryValueTree, HashMap<Integer, ValueTreePath> valueTreePathMap) {
        throw new RuntimeException();
    }

    @Override
    public void setParamCustom(EntryWidgetParam param){
        TextSliderParam casted = (TextSliderParam) param;
        String data = casted.selected;
        int index =  sliderWithLabels.valueArray.indexOf(data);

        float value = ((float)index) /  sliderWithLabels.valueArray.size();
        sliderWithLabels.slider.setValue(value);
    }

    public static class TextSliderParam extends EntryWidgetParam {
        String selected;
        public TextSliderParam(String name, String selected){
            super(name, TextSlider.className);
            this.selected = selected;
        }

        @Override
        public String hierarchyString(int numTabs) {
            return GLib.tabs(numTabs) + "slider\n";
        }

        @Override
        public HeaderNode createHeaderNode() {
            return null;
        }
    }

    public static class TextSliderValue extends WidgetValue{
        String selected;
        public TextSliderValue(String selected){
            this.selected = selected;
        }
    }
}
