package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.ViewWidgets.LockableScrollView;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;

import java.util.ArrayList;

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
    private StringBuilder logList = new StringBuilder();

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
        logList.append("set focused that got focus: " + editableWidget.widgetDebugId + " , name: " + editableWidget.getName() + "\n");
        MainActivity.log("set focused widget from edit text press");
//        if(focusedWidget != null){
//            if(focusedWidget != editableWidget){
//                MainActivity.log("editable widget got focus, but focused widget isn't null\n" +
//                        "editable widget: " + editableWidget + "\nfocusedWidget: " + focusedWidget);
//                MainActivity.log(logList.toString());
//                throw new RuntimeException();
//            }
//
//        }
        focusedWidget = editableWidget;
    }

    public boolean onActionListenerFromEditText(int actionId){
        return onActionListener(actionId);
    }

    private boolean onActionListener(int actionId){
        MainActivity.log("edit text got action: " + actionId);
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            MainActivity.log("invisible edit text got action next");
            logList.append("send next action to: " + focusedWidget.widgetDebugId + " , name: " + focusedWidget.getName() + "\n");
            focusedWidget.keyListener(actionId);
            return true;  // Handle the next action
        }
        return false;  // Let other actions be handled normally
    }

    public void removeFocusedWidget(){
        String debug = "removed focused widget: ";
        if(focusedWidget == null)
            debug += "focused widget was null\n";
        else
            debug += "focused widget was " + focusedWidget.widgetDebugId + " , name: " + focusedWidget.getName() + "\n";
        logList.append(debug);
        focusedWidget.getView().clearFocus();
        invisibleEditText.clearFocus();
        if(focusedWidget != null){
            if( ! (focusedWidget instanceof EditableWidget)){
                focusedWidget.onFocusChange(false);
            }
            focusedWidget = null;
        }

    }

    public void setFocusedWidget(EntryWidget entryWidget){
        logList.append("set focused widget widgetDebugId: " + entryWidget.widgetDebugId + " , name: " + entryWidget.getName() + "\n");
        MainActivity.log("new focused widget");
        if(focusedWidget != null){
            MainActivity.log("tried to set new widget.\nold widget: " + focusedWidget + "\nnew widget: " + entryWidget);
            MainActivity.log(logList.toString());
            throw new RuntimeException();
        }
        LockableScrollView scrollView = MainActivity.scrollView;
        scrollView.scrollToChildPublic(entryWidget.getView());
        focusedWidget = entryWidget;
        if(focusedWidget instanceof EditableWidget editableWidget){
            editableWidget.getEditText().requestFocus();
        }else{
            focusedWidget.onFocusChange(true);
            invisibleEditText.requestFocus();
        }
    }


    public boolean hasFocusedWidget() {
        return focusedWidget != null;
    }
}
