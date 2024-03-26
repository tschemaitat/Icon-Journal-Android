package com.example.habittracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class RelParam extends RelativeLayout.LayoutParams {

    public RelParam(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public RelParam(int w, int h) {
        super(w, h);
    }

    public RelParam(ViewGroup.LayoutParams source) {
        super(source);
    }

    public RelParam(ViewGroup.MarginLayoutParams source) {
        super(source);
    }

    public RelParam(RelativeLayout.LayoutParams source) {
        super(source);
    }

    public static RelParam alignLeft(int width, int height){
        RelParam nameLayoutParams = new RelParam(width, height);
        nameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        return nameLayoutParams;
    }

    public RelParam margins(int left, int top, int right, int bottom){
        setMargins(left,top,right,bottom);
        return this;
    }
}
