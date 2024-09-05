package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

public class LayoutMeasureFunction {
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
