package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public class ButtonElement implements Element{
    private Button button;
    private Context context;
    private String text;
    private Runnable listener;

    public ButtonElement(Context context, String text, Runnable listener) {
        this.context = context;
        this.listener = listener;
        button = new Button(context);
        button.setText(text);
        button.setOnClickListener((view)->listener.run());
    }

    public Button getButton(){
        return button;
    }

    @Override
    public View getView() {
        return button;
    }
}
