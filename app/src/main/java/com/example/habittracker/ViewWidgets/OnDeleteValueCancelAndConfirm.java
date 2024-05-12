package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;

public class OnDeleteValueCancelAndConfirm {
    private Context context;
    private LinearLayout buttonLayout;
    private Integer entryId;
    public OnDeleteValueCancelAndConfirm(Context context, Runnable onCancel, Runnable onConfirm){
        this.context = context;
        init(onCancel, onConfirm);
    }

    private void init(Runnable onCancel, Runnable onConfirm){
        buttonLayout = showDeleteValueButtons(context, onCancel, onConfirm);
    }



    public static LinearLayout showDeleteValueButtons(Context context, Runnable onCancel, Runnable onConfirm){
        MainActivity.log("showing delete value cancel and confirm buttons");
        LinearLayout buttonLayout = new LinearLayout(context);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button cancelButton = new Button(context);
        cancelButton.setText("cancel");
        cancelButton.setOnClickListener((view -> onCancel.run()));
        cancelButton.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));

        Button confirmButton = new Button(context);
        confirmButton.setText("confirm");
        confirmButton.setOnClickListener((view -> onConfirm.run()));
        confirmButton.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));

        buttonLayout.addView(cancelButton);
        buttonLayout.addView(confirmButton);
        return buttonLayout;
    }


    public View getView(){
        return buttonLayout;
    }
}
