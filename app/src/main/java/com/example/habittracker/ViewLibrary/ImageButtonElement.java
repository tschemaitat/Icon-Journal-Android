package com.example.habittracker.ViewLibrary;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class ImageButtonElement extends ButtonElement{
    public ImageButtonElement(Context context, Drawable image, Runnable listener) {
        super(context, "", listener);
        setImage(image);
    }

    public void setImage(Drawable drawable){
        setBackground(drawable);
    }
}
