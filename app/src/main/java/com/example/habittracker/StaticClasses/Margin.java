package com.example.habittracker.StaticClasses;

import static com.example.habittracker.StaticClasses.ColorPalette.*;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.Widgets.StructureWidgets.StructureWidgetHeaderView;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;

public class Margin {
    public static final int initialLeftMargin = 20;
    public static final int initialVertMargin = 20;

    public static final int listLeftPadding = 10;
    public static final int listVertMargin = 20;
    public static final int listRightPadding = 0;

    public static final int listChildMargin = 15;

    public int left;
    public int top;
    public int right;
    public int bottom;

    public static Size structureWidgetNameEditorSize;

    public static void setup(Context context){
        structureWidgetNameEditorSize = new Size(context, 200, 50);
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public Margin(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Margin(){
        this.left = 0;
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
    }

    public Margin(int num){
        this.left = num;
        this.top = num;
        this.right = num;
        this.bottom = num;
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
        setPaddingAndBackground(linLayout.getView(), groupColor, padding);
        linLayout.setChildMargin(Margin.listChildMargin());
    }
    public static void setEditTextLayout(CustomEditText customEditText){


        setEditText(customEditText.getEditText());
        //linLayout.setChildMargin(Margin.listChildMargin());
    }

    public static void setEditText(EditText editText){
        setPaddingAndBackground(editText, tertiary, new Margin(20, 5, 20, 5));
        int verticalPadding = editText.getPaddingBottom() + editText.getPaddingTop();
        editText.setLayoutParams(new LinearLayout.LayoutParams(700, 100 + verticalPadding));
    }

    public static void setStructureWidgetGroupLayout(LinLayout linLayout){
        setPaddingAndBackground(linLayout.getView(), tertiary, new Margin());
        linLayout.setChildMargin(Margin.listChildMargin());

    }


    public static void setStructureWidgetHeader(StructureWidgetHeaderView headerView){
        if(headerView.nameEditor != null){
            CustomEditText nameView = headerView.nameEditor;
            RelativeLayout.LayoutParams nameParam = getRelativeParam(-2, -2, new Margin(0));
            nameView.getView().setLayoutParams(nameParam);
            setEditText(nameView.getEditText());
        }
        if(headerView.deleteButton != null){
            ImageButton deleteButton = headerView.deleteButton;
            RelativeLayout.LayoutParams deleteParam = getRelativeParam(20, 20, new Margin(20));
            deleteParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            deleteButton.setLayoutParams(deleteParam);
        }
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

    public LinearLayout.LayoutParams getLin(int width, int height){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;
    }

    public static RelativeLayout.LayoutParams getRelativeParam(int width, int height, Margin margin){
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(GLib.dpToPx(width), GLib.dpToPx(height));
        param.setMargins(margin.getLeft(), margin.getTop(), margin.getRight(), margin.getBottom());
        return param;
    }



    public static class Size{
        private int width;
        private int height;
        private Context context;
        public Size(Context context, int width, int height){
            this.context = context;
            this.width = width;
            this.height = height;
        }

        public int getWidthPixel(){
            return GLib.dpToPx(width);
        }

        public int getHeightPixel(){
            return GLib.dpToPx(height);
        }
    }


}
