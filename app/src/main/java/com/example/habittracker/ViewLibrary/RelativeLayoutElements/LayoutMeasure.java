package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.view.View;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class LayoutMeasure {
//    public void measureChildToMatchParent(View view, int childMarginLeft, int childMarginRight, int parentPaddingLeft, int parentPaddingRight,
//                             int childMarginTop, int childMarginBottom, int parentPaddingTop, int parentPaddingBottom){
//        int viewMeasureWidth = ;
//        int viewMeasureHeight;
//
//    }

    public static int getMeasureSizeMatchParent(int childMarginStart, int childMarginEnd,
                                         int parentPaddingStart, int parentPaddingEnd, int parentSize){
        return parentSize - (childMarginStart + childMarginEnd + parentPaddingStart + parentPaddingEnd);
    }
}
