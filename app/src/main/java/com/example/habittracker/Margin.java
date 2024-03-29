package com.example.habittracker;

import android.widget.LinearLayout;

public class Margin {
    public static final int initialLeftMargin = 40;
    public static final int initialVertMargin = 40;

    public static final int listLeftPadding = 40;
    public static final int listVertMargin = 40;
    public static final int listRightPadding = 30;
    int left;
    int top;
    int right;
    int bottom;

    public Margin(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public static Margin listChildMargin(){
        return new Margin(0, listVertMargin, 0, listVertMargin);
    }

    public static Margin initialPagePadding(){
        return new Margin(initialLeftMargin, initialVertMargin, initialLeftMargin, initialVertMargin);
    }

    public static Margin listPadding(){
        return new Margin(listLeftPadding, listVertMargin, listRightPadding, listVertMargin);
    }

    public LinearLayout.LayoutParams getLin(int width, int height){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;
    }
}
