package com.example.habittracker.Slider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.habittracker.SliderWithLabels;
import com.example.habittracker.Widget;
import com.example.habittracker.WidgetParams;
import com.example.habittracker.WidgetValue;

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

    @Override
    public Widget widgetClone() {
        TextSlider clone = new TextSlider(context);
        ArrayList<Integer> labelIndex = new ArrayList<>();
        for(int i = 0; i < valueArray.size(); i++)
            labelIndex.add(i);
        clone.setValues(valueArray, labelIndex);
        return clone;

    }
    public Runnable onDataChangedListener;
    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public TextSliderParams getData(){
        return new TextSliderParams(selectedValue());
    }

    @Override
    public WidgetValue value() {
        return new TextSliderValue(selectedValue());
    }

    @Override
    public void setData(WidgetParams params){
        TextSliderParams casted = (TextSliderParams) params;
        String data = casted.selected;
        int index = valueArray.indexOf(data);

        float value = ((float)index) / valueArray.size();
        slider.setValue(value);
    }

    @Override
    public View getView() {
        return this;
    }

    public static class TextSliderParams extends WidgetParams{
        String selected;
        public TextSliderParams(String selected){
            this.selected = selected;
        }
    }

    public static class TextSliderValue extends WidgetValue{
        String selected;
        public TextSliderValue(String selected){
            this.selected = selected;
        }
    }
}
