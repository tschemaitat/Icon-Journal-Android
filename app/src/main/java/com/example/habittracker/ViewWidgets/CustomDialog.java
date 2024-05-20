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

public class CustomDialog extends Dialog {

    public CustomDialog(Context context, String message) {
        super(context);
        setContentView(createContentView(context, message));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private View createContentView(Context context, String message) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Adding ScrollView for long text
        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(message);
        scrollView.addView(textView);

        layout.addView(scrollView);

        // Adding buttons
        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button button1 = new Button(context);
        button1.setText("Option 1");
        button1.setOnClickListener(v -> {
            dismiss();
            // Handle Option 1 click
        });

        Button button2 = new Button(context);
        button2.setText("Option 2");
        button2.setOnClickListener(v -> {
            dismiss();
            // Handle Option 2 click
        });

        buttonLayout.addView(button1);
        buttonLayout.addView(button2);
        layout.addView(buttonLayout);

        return layout;
    }
}
