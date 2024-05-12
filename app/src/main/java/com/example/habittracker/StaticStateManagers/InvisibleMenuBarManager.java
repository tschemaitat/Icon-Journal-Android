package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;

public class InvisibleMenuBarManager {
    private static InvisibleMenuBarManager manager;
    public static void createManager(Context context, LinearLayout invisibleMenuBarLayout){
        manager = new InvisibleMenuBarManager(context, invisibleMenuBarLayout);
    }

    public static InvisibleMenuBarManager getManager(){
        return manager;
    }

    private LinearLayout invisibleMenuBarLayout;
    private Context context;

    private InvisibleMenuBarManager(Context context, LinearLayout invisibleMenuBarLayout) {
        this.invisibleMenuBarLayout = invisibleMenuBarLayout;
        this.context = context;
    }


}
