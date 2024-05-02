package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.ToggleView;

import java.util.ArrayList;

public class KeyBoardActionManager {
    private static KeyBoardActionManager manager = null;
    public static KeyBoardActionManager getManager(){
        if(manager == null)
            throw new RuntimeException();
        return manager;
    }

    public static boolean hasManager(){
        return manager != null;
    }

    public static void makeNewManager(Context context, LinearLayout parent){
        MainActivity.log("making new manager");
        manager = new KeyBoardActionManager(context, parent);
    }

    public static void removeManager(){
        manager.parent.removeView(manager.getView());
        manager = null;
    }

    private ToggleView toggleView;
    private Context context;
    private LinearLayout parent;
    private ArrayList<OnToggleListener> onToggleListenerList = new ArrayList<>();

    private KeyBoardActionManager(Context context, LinearLayout parent){
        this.context = context;
        toggleView = new ToggleView(context, "keyboard mode", "enter", "next",
                450, 150, parent, this::onToggle);
        this.parent = parent;
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
