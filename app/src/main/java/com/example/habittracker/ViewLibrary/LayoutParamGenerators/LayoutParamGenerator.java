package com.example.habittracker.ViewLibrary.LayoutParamGenerators;

import android.view.ViewGroup;

public interface LayoutParamGenerator {
    ViewGroup.LayoutParams makeLayoutParam(int width, int height);
}
