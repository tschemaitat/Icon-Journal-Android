package com.example.habittracker.ViewLibrary.LayoutParamGenerators;

import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LinearLayoutParamGenerator implements LayoutParamGenerator{

    @Override
    public ViewGroup.LayoutParams makeLayoutParam(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }
}
