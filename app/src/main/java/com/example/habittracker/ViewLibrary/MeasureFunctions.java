package com.example.habittracker.ViewLibrary;

import android.view.View;
import android.view.ViewGroup;

public class MeasureFunctions {

    public static int generateMeasureSpec(int layoutParam, int parentSize){
        int measureSpecSize = -3;
        int measureSpecMode = -3;
        if(layoutParam == -2){
            measureSpecMode = View.MeasureSpec.AT_MOST;
            measureSpecSize = parentSize;
        } else if(layoutParam == -1){
            measureSpecMode = View.MeasureSpec.EXACTLY;
            measureSpecSize = parentSize;
        }else if(layoutParam > -1){
            measureSpecMode = View.MeasureSpec.EXACTLY;
            measureSpecSize = layoutParam;
        }else{
            throw new RuntimeException("incorrect layout param: " + layoutParam);
        }
        return View.MeasureSpec.makeMeasureSpec(measureSpecSize, measureSpecMode);
    }

    public static int genSpecIgnoreMatch(int layoutParam, int parentSpecSize){
        int measureSpecSize = -3;
        int measureSpecMode = -3;
        if(layoutParam == -2 || layoutParam == -1){
            measureSpecMode = View.MeasureSpec.AT_MOST;
            measureSpecSize = parentSpecSize;
        }else if(layoutParam > -1){
            measureSpecMode = View.MeasureSpec.EXACTLY;
            measureSpecSize = layoutParam;
        }else{
            throw new RuntimeException("incorrect layout param: " + layoutParam);
        }
        return View.MeasureSpec.makeMeasureSpec(measureSpecSize, measureSpecMode);
    }

    public static void measureIgnoreMatch(View view, int paramWidth, int parentWidthSize, int paramHeight, int parentHeightSize){
        ViewGroup.MarginLayoutParams lp = ((ViewGroup.MarginLayoutParams) view.getLayoutParams());

        int widthMinusMargin = parentWidthSize - (lp.leftMargin + lp.rightMargin);
        int heightMinusMargin = parentHeightSize - (lp.topMargin + lp.bottomMargin);
        view.measure(genSpecIgnoreMatch(paramWidth, widthMinusMargin), genSpecIgnoreMatch(paramHeight, heightMinusMargin));
    }

    public static void customMeasure(View view, int paramWidth, int parentWidthSize, int paramHeight, int parentHeightSize){
        ViewGroup.MarginLayoutParams lp = ((ViewGroup.MarginLayoutParams) view.getLayoutParams());
        int widthMinusMargin = parentWidthSize - (lp.leftMargin + lp.rightMargin);
        int heightMinusMargin = parentHeightSize - (lp.topMargin + lp.bottomMargin);
        view.measure(generateMeasureSpec(paramWidth, widthMinusMargin), generateMeasureSpec(paramHeight, heightMinusMargin));
    }

    public static int getMeasuredWidthAndMargin(View view){
        ViewGroup.MarginLayoutParams lp = ((ViewGroup.MarginLayoutParams) view.getLayoutParams());
        return view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    public static int getMeasuredHeightAndMargin(View view){
        ViewGroup.MarginLayoutParams lp = ((ViewGroup.MarginLayoutParams) view.getLayoutParams());
        return view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
    }
}
