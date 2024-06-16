package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.ToggleView;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class KeyBoardActionManager {
    private ToggleView toggleView;
    private Context context;
    private ArrayList<OnToggleListener> onToggleListenerList = new ArrayList<>();

    public KeyBoardActionManager(Context context){
        this.context = context;
        toggleView = new ToggleView(context, "keyboard mode", "enter", "next",
                450, 150, this::onToggle);
    }

    public View getView(){
        return toggleView.getView();
    }

    public void onToggle(boolean isLeft){
        boolean isEnter = getIsEnter();
        MainActivity.log("on toggle");
        for(OnToggleListener onToggleListener: onToggleListenerList)
            onToggleListener.onToggle(isEnter);
    }

    public boolean getIsEnter(){
        return toggleView.getIsLeft();
    }

    public void addToggleListener(OnToggleListener onToggleListener) {
        onToggleListenerList.add(onToggleListener);
    }


    public interface OnToggleListener{
        void onToggle(boolean isEnter);
    }
}
