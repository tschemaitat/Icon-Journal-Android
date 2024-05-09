package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;

public class InvisibleEditTextManager {
    private static InvisibleEditTextManager manager;
    public static InvisibleEditTextManager getManager(){
        return manager;
    }

    public static void createManager(ConstraintLayout parent, Context context){
        manager = new InvisibleEditTextManager(parent, context);
    }

    private EditText invisibleEditText;
    private EntryWidget focusedWidget;

    private InvisibleEditTextManager(ConstraintLayout parent, Context context){
        invisibleEditText = new EditText(context);
        invisibleEditText.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
        parent.addView(invisibleEditText);
        invisibleEditText.setBackgroundResource(android.R.color.transparent);
        invisibleEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        invisibleEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        invisibleEditText.setOnEditorActionListener((v, actionId, event) -> {
            return onActionListener(actionId);
        });
    }

    public void setEditableWidgetThatGotFocus(EntryWidget editableWidget){
        MainActivity.log("set focused widget from edit text press");
        if(focusedWidget != null){
            if(focusedWidget != editableWidget){
                MainActivity.log("editable widget got focus, but focused widget isn't null\n" +
                        "editable widget: " + editableWidget + "\nfocusedWidget: " + focusedWidget);
                throw new RuntimeException();
            }

        }
        focusedWidget = editableWidget;
    }

    public boolean onActionListenerFromEditText(int actionId){
        return onActionListener(actionId);
    }

    private boolean onActionListener(int actionId){
        MainActivity.log("edit text got action: " + actionId);
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            MainActivity.log("invisible edit text got action next");
            focusedWidget.keyListener(actionId);
            return true;  // Handle the next action
        }
        return false;  // Let other actions be handled normally
    }

    public void removeFocusedWidget(){
        focusedWidget.getView().clearFocus();
        invisibleEditText.clearFocus();
        if( ! (focusedWidget instanceof EditableWidget)){
            focusedWidget.onFocusChange(false);
        }
        focusedWidget = null;
    }

    public void setFocusedWidget(EntryWidget entryWidget){
        MainActivity.log("new focused widget");
        if(focusedWidget != null){
            MainActivity.log("tried to set new widget.\nold widget: " + focusedWidget + "\nnew widget: " + entryWidget);
            throw new RuntimeException();
        }
        focusedWidget = entryWidget;
        if(focusedWidget instanceof EditableWidget editableWidget){
            editableWidget.getEditText().requestFocus();
        }else{
            focusedWidget.onFocusChange(true);
            invisibleEditText.requestFocus();
        }
    }


}
