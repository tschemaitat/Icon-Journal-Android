package com.example.habittracker.ANotUsing.Slider.Sliders;

import android.content.Context;
import android.util.AttributeSet;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class LinearSlider extends SliderWithLabels {
    int start;
    int end;
    int difference;
    public LinearSlider(Context context) {
        super(context);
        init(context);
    }

    public LinearSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinearSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setRange(0, 200, 50);
    }

    public void setRange(int start, int end, int difference){
        ArrayList<String> values = new ArrayList<>();
        ArrayList<Integer> labelIndexList = new ArrayList<>();
        int value;
        for(int i = start; i <= end/difference; i++){
            //labelIndexList.add(i);
            value = start + i * difference;
            values.add( String.valueOf(value) );
            System.out.println("value = " + value);
        }

        labelIndexList.add(0);
        labelIndexList.add(values.size()-1);

        setValues(values, labelIndexList);
    }
}
