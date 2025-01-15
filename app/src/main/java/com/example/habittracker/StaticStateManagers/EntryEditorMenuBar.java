package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.Inflatables.MenuBarManager;
import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.OnDeleteValueCancelAndConfirm;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ParentWidget;
import com.example.habittracker.structurePack.EntryInStructure;

public class EntryEditorMenuBar {
    private Context context;
    private Button deleteButton;
    private OnDeleteValueCancelAndConfirm deleteAndConfirm;


    private LinearLayout horizontalBar;

    private MenuBarManager menuBarManager;



    public EntryEditorMenuBar(Context context, ParentWidget parentWidget, EntryInStructure entry, MenuBarManager menuBarManager) {
        this.context = context;
        this.menuBarManager = menuBarManager;
        if(parentWidget == null)
            throw new RuntimeException();
        init(parentWidget, entry);
    }

    private void init(ParentWidget parentWidget, EntryInStructure entry){

        horizontalBar = makeHorizontalBarLayout(context);
        //deleteButton = makeDeleteButton(context, horizontalBar);
    }

    private static Button makeDeleteButton(Context context, LinearLayout menuBarLayout){
        Button deleteButton = new Button(context);
        deleteButton.setText("delete");
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        menuBarLayout.addView(deleteButton);
        deleteButton.setOnClickListener(view -> {
            throw new RuntimeException();
        });
        return deleteButton;
    }

    private static LinearLayout makeHorizontalBarLayout(Context context){
        LinearLayout menuLinearLayout = new LinearLayout(context);
        menuLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        MainActivity.log("adding menu to parent");
        //parent.addView(menuLinearLayout);
        menuLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return menuLinearLayout;
    }

    public View getView() {
        return horizontalBar;
    }

    public void removeConfirmView() {
        if(deleteAndConfirm != null)
            menuBarManager.removeInvisibleBarView(deleteAndConfirm.getView());
    }
}
