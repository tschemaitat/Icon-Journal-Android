package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

public class TextElement implements Element{
    private Context context;
    private TextView textView;

    public TextElement(Context context, String text, Runnable... clickListeners) {
        this.context = context;
        textView = new TextView(context);
        textView.setText(text);
        if(clickListeners.length == 1){
            Runnable listener = clickListeners[0];
            textView.setOnClickListener((view -> listener.run()));
        }
    }

    public TextView getTextView(){
        return textView;
    }

    public String getText(){
        return (String) textView.getText();
    }
    @Override
    public View getView(){
        return textView;
    }
}
