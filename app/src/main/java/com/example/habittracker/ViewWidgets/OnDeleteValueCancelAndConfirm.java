package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewLibrary.ButtonElement;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.HorLayout;

public class OnDeleteValueCancelAndConfirm {
    private Context context;
    private HorLayout buttonLayout;
    private Integer entryId;
    public OnDeleteValueCancelAndConfirm(Context context, Runnable onCancel, Runnable onConfirm){
        this.context = context;
        init(onCancel, onConfirm);
    }

    private void init(Runnable onCancel, Runnable onConfirm){
        buttonLayout = showDeleteValueButtons(context, onCancel, onConfirm);
    }



    public static HorLayout showDeleteValueButtons(Context context, Runnable onCancel, Runnable onConfirm){
        MainActivity.log("showing delete value cancel and confirm buttons");

        HorLayout buttonLayout = new HorLayout(context);
        buttonLayout.getLinearLayout().setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

        ButtonElement cancelButton = new ButtonElement(context, "cancel", onCancel);
        buttonLayout.addWithParam(cancelButton, -2, -2);

        ButtonElement confirmButton = new ButtonElement(context, "confirm", onConfirm);
        buttonLayout.addWithParam(confirmButton, -2, -2);
        return buttonLayout;
    }


    public View getView(){
        return buttonLayout.getView();
    }
}
