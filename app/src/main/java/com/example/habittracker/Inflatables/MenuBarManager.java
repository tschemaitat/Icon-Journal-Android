package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.habittracker.StaticStateManagers.EntryEditorMenuBar;
import com.example.habittracker.StaticStateManagers.KeyBoardActionManager;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.structurePack.EntryInStructure;

public class MenuBarManager {
    private Context context;
    private LinearLayout menuBarVerticalLayout;
    private LinearLayout invisibleMenuBarVerticalLayout;
    private ImageButton hideButton;
    private boolean animateShown = true;

    private EntryEditorMenuBar entryEditorMenuBar;

    public MenuBarManager(Context context, LinearLayout menuBarVerticalLayout, LinearLayout invisibleMenuBarVerticalLayout, ImageButton hideButton) {
        this.menuBarVerticalLayout = menuBarVerticalLayout;
        this.invisibleMenuBarVerticalLayout = invisibleMenuBarVerticalLayout;
        this.context = context;
        this.hideButton = hideButton;
        init();
        //scroll needs to add a view with height same as hidden menu bar,
    }

    public void addEntryEditorBar(GroupWidget groupWidget, EntryInStructure entry){
        entryEditorMenuBar = new EntryEditorMenuBar(context, groupWidget, entry, this);
        menuBarVerticalLayout.addView(entryEditorMenuBar.getView());
    }

    public void removeEntryEditorBar(){
        menuBarVerticalLayout.removeView(entryEditorMenuBar.getView());
        entryEditorMenuBar.removeConfirmView();
    }

    public KeyBoardActionManager getKeyBoardActionManager(){
        return entryEditorMenuBar.getKeyBoardActionManager();
    }


    private void init(){
        hideButton.setOnClickListener((view -> {
            if(animateShown){
                menuBarVerticalLayout.setVisibility(View.GONE);
                //animateUpAndAway(parent, true);
            }else{
                menuBarVerticalLayout.setVisibility(View.VISIBLE);
            }
            animateShown = !animateShown;
        }));
    }


    public void addViewInvisibleLayout(View view) {
        invisibleMenuBarVerticalLayout.addView(view);
    }

    public void removeInvisibleBarView(View view) {
        invisibleMenuBarVerticalLayout.removeView(view);
    }
}
