package com.example.habittracker;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.DropDownSpinner;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.StructureWidget;
import com.example.habittracker.Widgets.Widget;

public class GLib {
    public static final int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    public static final int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;

    public static final int initialHorMargin = 15;
    public static final int initialVertMargin = 10;

    public static GroupWidget createInitialGroupWidget(Context context){
        GroupWidget groupWidget = new GroupWidget(context);
        groupWidget.setMargin(initialHorMargin, initialVertMargin);
        return groupWidget;
    }


    public static int calculateTextWidth(String text, float textSize, Typeface typeface) {


        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        return (int)paint.measureText(text);
    }

    public static int calculateTextWidth(TextView textView) {
        String text = textView.getText().toString();
        float textSize = textView.getTextSize();
        Typeface typeface = textView.getTypeface();

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        return (int)paint.measureText(text);
    }

    public static int calculateTextHeight(String text, float textSize, Typeface typeface) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    public static int calculateTextHeight(TextView textView) {
        String text = textView.getText().toString();
        float textSize = textView.getTextSize();
        Typeface typeface = textView.getTypeface();

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    public static View inflate(Context context, int resourceId){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourceId, null, false);
        return view;

    }

    public static int pxToDp(Context context, int px) {
        return (int)(px / (context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public static int dpToPx(Context context, int dp){
        return (int)(dp * (context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public void setMargin(int dp){

    }

    public static void outlinedLayoutAddView(ConstraintLayout outlinedLayout, View child){
        ((ConstraintLayout)(outlinedLayout.getChildAt(0))).addView(child);
    }

    public static View getButton(View.OnClickListener listener, Context context){
        ConstraintLayout layout = new ConstraintLayout(context);
        Button button = (Button) (((ConstraintLayout) MainActivity.mainActivity.getLayoutInflater().inflate(R.layout.button_layout, layout)).getChildAt(0));
        layout.removeView(button);

        button.setOnClickListener(listener);
        return button;
    }

    public static View inflate(int id){
        ConstraintLayout layout = new ConstraintLayout(MainActivity.mainActivity);
        View view = ((ConstraintLayout) MainActivity.mainActivity.getLayoutInflater().inflate(id, layout)).getChildAt(0);
        layout.removeView(view);
        return view;
    }

    public static Widget inflateWidget(Context context, EntryWidgetParam params){

        String className = params.className;
        System.out.println("inflating widget: " + className);
        Widget widget = null;
        switch (className){
            case DropDownSpinner.className:
                widget = new DropDownSpinner(context);
                widget.setParam(params);
                break;
            case "list":
                widget = new ListWidget(context);
                widget.setParam(params);
                break;
            case "structure widget":
                widget = new StructureWidget(context);
                widget.setParam(params);
                break;
            case GroupWidget.className:
                widget = new GroupWidget(context);
                widget.setParam(params);
                break;
            case CustomEditText.className:
                widget = new CustomEditText(context);
                widget.setParam(params);
                break;

            default:

                throw new RuntimeException("unknown widget class: " + className);



        }
        return widget;
    }


    public static String tabs(int num){
        String singleTab = "\t";
        String tabs = "";
        for(int i = 0; i < num; i++)
            tabs += singleTab;
        return tabs;
    }


}
