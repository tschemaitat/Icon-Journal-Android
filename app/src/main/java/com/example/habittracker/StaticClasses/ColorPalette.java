package com.example.habittracker.StaticClasses;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;

import com.example.habittracker.R;

public class ColorPalette {



    public static void setColors(Context context){
        text = context.getColor(R.color.darkText1);
        text = context.getColor(R.color.darkText2);
        textPurple = context.getColor(R.color.purple);
        redText = context.getColor(R.color.errorTextColor);
        primary = context.getColor(R.color.dark1);
        secondary = context.getColor(R.color.dark2);
        tertiary = context.getColor(R.color.dark3);

        groupColor = context.getColor(R.color.groupColor);

        hintText = context.getColor(R.color.hintText);
    }
    public static int hintText;
    public static int text;
    public static int text2;
    public static int textPurple;
    public static int redText;
    public static int primary;
    public static int secondary;
    public static int tertiary;
    public static int groupColor;
}
