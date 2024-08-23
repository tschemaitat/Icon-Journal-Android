package com.example.habittracker.ViewWidgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.habittracker.ViewLibrary.ButtonElement;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.HorLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.VertLayout;
import com.example.habittracker.ViewLibrary.ScrollElements.ScrollElement;
import com.example.habittracker.ViewLibrary.ScrollElements.VertScrollElement;
import com.example.habittracker.ViewLibrary.TextElement;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context, String message) {
        super(context);
        setContentView(createContentView(context, message));
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private View createContentView(Context context, String message) {

        VertLayout linearElementLayout = new VertLayout(context);

        ScrollElement scrollElement = new VertScrollElement(context);
        linearElementLayout.addWithParam(scrollElement, -1, -2);
        scrollElement.getView().setPadding(10, 10, 10, 10);

        TextElement textElement = new TextElement(context, message);
        scrollElement.addWithParam(textElement, -2, -2);

        HorLayout buttonLayout = new HorLayout(context);
        linearElementLayout.addWithParam(buttonLayout, -1, -2);

        ButtonElement button1 = new ButtonElement(context, "cancel", ()->{

        });
        ButtonElement button2 = new ButtonElement(context, "confirm", ()->{

        });

        buttonLayout.addWithParam(button1, -2, -2);
        buttonLayout.addWithParam(button2, -2, -2);


        // Adding buttons
//        LinearLayout buttonLayout = new LinearLayout(context);
//        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//        Button button1 = new Button(context);
//        button1.setText("Option 1");
//        button1.setOnClickListener(v -> {
//            dismiss();
//            // Handle Option 1 click
//        });
//
//        Button button2 = new Button(context);
//        button2.setText("Option 2");
//        button2.setOnClickListener(v -> {
//            dismiss();
//            // Handle Option 2 click
//        });
//
//        buttonLayout.addView(button1);
//        buttonLayout.addView(button2);
//        layout.addView(buttonLayout);

        return linearElementLayout.getView();
    }
}
