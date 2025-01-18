package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

public class CheckBoxElement {
    private Context context;
    private CheckBox checkBox;
    private CheckBoxListener checkBoxListener;

    public CheckBoxElement(Context context, CheckBoxListener... checkBoxListeners) {
        this.context = context;
        if(checkBoxListeners.length == 0){
            checkBoxListeners = null;
        }else if(checkBoxListeners.length == 1){
            this.checkBoxListener = checkBoxListeners[0];
        }else{
            throw new RuntimeException();
        }

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
        this.checkBoxListener = listener;
        checkBox.setOnClickListener((view)->{
            onClicked();
        });
    }

    public void onClicked(){
        checkBoxListener.onChecked(checkBox.isChecked());
    }

    public void clearOnCheckListener() {
        checkBoxListener = null;
        checkBox.setOnClickListener(null);
    }

    public interface CheckBoxListener{
        void onChecked(boolean checked);
    }
}
