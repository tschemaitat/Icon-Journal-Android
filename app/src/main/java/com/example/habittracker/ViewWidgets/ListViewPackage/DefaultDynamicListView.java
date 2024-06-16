package com.example.habittracker.ViewWidgets.ListViewPackage;

import android.content.Context;

import com.example.habittracker.ViewLibrary.TextElement;

public class DefaultDynamicListView{

    public static DynamicListView make(Context context) {
        ViewWithTextGenerator generator = new ViewWithTextGenerator() {
            @Override
            public TextElement makeTextElement() {
                return null;
            }
        };
        return new DynamicListView(context, generator);
    }
}
