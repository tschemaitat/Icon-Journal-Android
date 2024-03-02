package com.example.habittracker.Slider;

import android.content.Context;
import android.util.AttributeSet;

import com.example.habittracker.SliderWithLabels;

import java.util.ArrayList;

public class TextSlider extends SliderWithLabels {
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
}
