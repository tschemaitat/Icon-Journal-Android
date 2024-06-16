package com.example.habittracker.StaticStateManagers;

import android.content.Context;

public class WidgetResources {
    private Context context;
    private KeyBoardActionManager keyBoardActionManager;
    public WidgetResources(Context context, KeyBoardActionManager keyBoardActionManager){
        this.keyBoardActionManager = keyBoardActionManager;
        this.context = context;
    }

    public KeyBoardActionManager getKeyBoardActionManager(){
        return keyBoardActionManager;
    }
}
