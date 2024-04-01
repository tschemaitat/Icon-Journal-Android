package com.example.habittracker.StaticClasses;

import static com.example.habittracker.StaticClasses.ColorPalette.*;

import android.view.View;
import android.widget.LinearLayout;


import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.Widgets.EntryWidget;

public class Margin {
    public static final int initialLeftMargin = 20;
    public static final int initialVertMargin = 20;

    public static final int listLeftPadding = 20;
    public static final int listVertMargin = 20;
    public static final int listRightPadding = 10;

    public static final int listChildMargin = 15;

    public int left;
    public int top;
    public int right;
    public int bottom;

    public Margin(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Margin subtract(Margin margin){
        return new Margin(this.left - margin.left, this.top - margin.top,
                this.right - margin.right, this.bottom - margin.bottom);
    }

    public Margin add(Margin margin){
        return new Margin(this.left + margin.left, this.top + margin.top,
                this.right + margin.right, this.bottom + margin.bottom);
    }

    public static Margin listChildMargin(){
        return new Margin(0, listChildMargin, 0, listChildMargin);
    }

    public static Margin initialPagePadding(){
        return new Margin(initialLeftMargin, initialVertMargin, initialLeftMargin, initialVertMargin);
    }

    //structureWidget, GroupWidget, StructureWidget  second, third, second

    //list group widget, each group widget containing 1 or more widgets, widget     third, second, third



    public static Margin listPadding(){
        return new Margin(listLeftPadding, listVertMargin, listRightPadding, listVertMargin);
    }

    public LinearLayout.LayoutParams getLin(int width, int height){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;
    }

    public static void setInitialLayout(View view){
        Margin padding = initialPagePadding();
        //setPaddingAndBackground(view, ColorPalette.primary, padding);
        setPadding(view, padding);
    }

    public static void setStructureWidgetLayout(LinLayout linLayout){
        Margin padding = listPadding();
        setPaddingAndBackground(linLayout.getView(), secondary, padding);
        //linLayout.setChildMargin(Margin.listChildMargin());
    }

    public static void setListLayout(LinLayout linLayout){
        Margin padding = listPadding();
        setPaddingAndBackground(linLayout.getView(), tertiary, padding);
        linLayout.setChildMargin(Margin.listChildMargin());
    }





    public static void setPaddingAndBackground(View view, int color, Margin padding){
        view.setBackground(GLib.setBackgroundColorForView(view.getContext(), color));
        setPadding(view, padding.add(getPadding(view)));
    }

    public static void setPadding(View view, Margin margin){
        view.setPadding(margin.left, margin.top, margin.right, margin.bottom);
    }

    public static Margin getPadding(View view){
        return new Margin(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }


}
