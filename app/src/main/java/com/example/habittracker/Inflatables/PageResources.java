package com.example.habittracker.Inflatables;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.habittracker.StaticStateManagers.KeyBoardActionManager;
import com.example.habittracker.StaticStateManagers.WidgetResources;

public class PageResources {
    private static PageResources staticPageResources;

    public static PageResources getPageResources(){
        return staticPageResources;
    }

    public static void setPageResources(PageResources pageResources){
        staticPageResources = pageResources;
    }

    private Context context;
    private MenuBarManager menuBarManager;
    private WidgetResources widgetResources;
    public PageResources(Context context, LinearLayout menuBarLayout, LinearLayout invisibleMenuBarLayout,
                         ImageButton menuHideButton){
        this.context = context;
        menuBarManager = new MenuBarManager(context, menuBarLayout, invisibleMenuBarLayout, menuHideButton);

    }

    public WidgetResources getWidgetResources(){
        return widgetResources;
    }

    public MenuBarManager getMenuBarManager(){
        return menuBarManager;
    }
}
