package com.example.habittracker.ViewWidgets;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class CheckBoxElement implements Element {
    private Context context;
    private BooleanListener booleanListener;
    private CheckBox checkBox;
    private CheckBoxListener listener;

    public CheckBoxElement(Context context, BooleanListener booleanListener) {
        this.context = context;
        this.booleanListener = booleanListener;
        checkBox = new CheckBox(context);
    }

    @Override
    public View getView() {
        return checkBox;
    }

    public CheckBox getCheckBox(){
        return checkBox;
    }

    public void setOnCheckListener(CheckBoxListener listener){
        this.listener = listener;
        checkBox.setOnClickListener((view)->{
            onClicked();
        });
    }

    public void onClicked(){
        listener.onChecked(checkBox.isChecked());
    }

    public void clearOnCheckListener() {
        listener = null;
        checkBox.setOnClickListener(null);
    }

    public interface CheckBoxListener{
        void onChecked(boolean checked);
    }
}
