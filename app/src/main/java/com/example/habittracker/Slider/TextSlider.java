package com.example.habittracker.Slider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;

import java.util.ArrayList;

public class TextSlider extends SliderWithLabels implements Widget {
    Context context;
    public TextSlider(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TextSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TextSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        setValues(values, labelIndex);
    }


    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public TextSliderParam getData(){
        return new TextSliderParam(selectedValue());
    }

    @Override
    public WidgetValue value() {
        return new TextSliderValue(selectedValue());
    }

    @Override
    public DataTree getDataTree() {
        return null;
    }

    @Override
    public void setData(WidgetParam params){
        TextSliderParam casted = (TextSliderParam) params;
        String data = casted.selected;
        int index = valueArray.indexOf(data);

        float value = ((float)index) / valueArray.size();
        slider.setValue(value);
    }

    @Override
    public View getView() {
        return this;
    }

    public static class TextSliderParam extends WidgetParam {
        String selected;
        public TextSliderParam(String selected){
            this.selected = selected;
        }

        @Override
        public String hierarchyString(int numTabs) {
            return GLib.tabs(numTabs) + "slider\n";
        }

        @Override
        public DataTree header() {
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
