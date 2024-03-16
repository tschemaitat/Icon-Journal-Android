package com.example.habittracker;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class GLib {
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

    public static ConstraintLayout createOutlinedMarginedLayout(Context context){
        int margin = GLib.dpToPx(context, 5);
        ConstraintLayout layout = (ConstraintLayout)GLib.inflate(context, R.layout.widget_group_layout);

        //layoutParams.setMargins(margin, margin, margin, margin);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.setMargins(margin, margin, margin, margin);


        layout.setLayoutParams(layoutParams2);


        return layout;
    }

    public static void outlinedLayoutAddView(ConstraintLayout outlinedLayout, View child){
        ((ConstraintLayout)(outlinedLayout.getChildAt(0))).addView(child);
    }

    public static ConstraintLayout insertAddButton(View.OnClickListener listener, ViewGroup parent, Context context){
        ConstraintLayout addView = (ConstraintLayout) GLib.inflate(context, R.layout.add_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        addView.setLayoutParams(layoutParams);

        addView.setOnClickListener(listener);
        parent.addView(addView);
        return addView;
    }

    public static Widget inflateWidget(Context context, WidgetParams params){

        String className = params.widgetClass;
        System.out.println("inflating widget: " + className);
        Widget widget = null;
        switch (className){
            case "custom spinner":
                widget = new CustomSpinner(context);
                widget.setData(params);
                break;
            case "list":
                widget = new WidgetList(context);
                widget.setData(params);
                break;
            case "structure widget":
                widget = new StructureWidget(context);
                widget.setData(params);
                break;
            default:

                throw new RuntimeException("unknown widget class: " + className);



        }
        return widget;
    }


}
