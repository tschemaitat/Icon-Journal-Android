package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;

public class ButtonElement extends AbstractBasicElement{
    private Button button;
    private Context context;
    private String text;
    private Runnable listener;

    public ButtonElement(Context context, String text, Runnable listener) {
        this.context = context;
        this.listener = listener;
        //button = new Button(context);
        button = makeButton();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        button.setBackgroundResource(typedValue.resourceId);
        button.setBackgroundColor(ColorPalette.greenButtonBackground);

        button.setText(text);
        if(listener != null)
            button.setOnClickListener((view)->listener.run());
    }

    private Button makeButton(){
        ConstraintLayout layout = new ConstraintLayout(context);
        Button button =  (Button) (((ConstraintLayout) MainActivity.mainActivity.getLayoutInflater().inflate(R.layout.button_layout, layout)).getChildAt(0));
        layout.removeView(button);
        return button;
    }

    public Button getButton(){
        return button;
    }

    @Override
    public View getView() {
        return button;
    }


}
