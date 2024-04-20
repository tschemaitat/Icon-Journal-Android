package com.example.habittracker.ANotUsing.Slider.Sliders;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.google.android.material.slider.Slider;

public class CustomSlider extends Slider {

    public CustomSlider(Context context) {
        super(context);
        init();
    }

    public CustomSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize your custom slider here
        // For example, set a custom LabelFormatter



        //changeRange(0, 100, 5);


    }

    // Override methods to change behavior


    public void setNumberValues(int numberValues) {
        //number
        this.setValueFrom(0);
        this.setValueTo(numberValues - 1);
        this.setStepSize(1);
    }
}
