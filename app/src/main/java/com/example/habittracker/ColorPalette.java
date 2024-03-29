package com.example.habittracker;

import android.content.Context;
import android.graphics.Color;

public class ColorPalette {

    public static void setColors(Context context){
        text = context.getColor(R.color.dark1);
        redText = Color.RED;
        primary = context.getColor(R.color.dark1);
        secondary = context.getColor(R.color.dark2);
        tertiary = context.getColor(R.color.dark3);
    }

    public static int text;
    public static int redText;
    public static int primary;
    public static int secondary;
    public static int tertiary;
}
