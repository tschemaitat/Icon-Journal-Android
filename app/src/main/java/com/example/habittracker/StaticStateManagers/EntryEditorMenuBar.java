package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.ToggleView;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidget;
import com.example.habittracker.Widgets.GroupWidget;

public class EntryEditorMenuBar {
    private static EntryEditorMenuBar manager;
    public static void make(LinearLayout parent, Context context){
        manager = new EntryEditorMenuBar(parent, context);
    }

    public static EntryEditorMenuBar get(){
        return manager;
    }

    private LinearLayout parent;
    private LinearLayout menuBarLayout;
    private Context context;
    private Button deleteButton;

    private DeleteValueManager deleteValueManager;



    private EntryEditorMenuBar(LinearLayout parent, Context context) {
        this.parent = parent;
        this.context = context;
        init();
    }

    private void init(){
        menuBarLayout = makeMenuLinearLayout(parent, context);
        deleteButton = makeDeleteButton(context, menuBarLayout);
        KeyBoardActionManager.makeNewManager(context, menuBarLayout);
    }

    public void setGroupWidget(GroupWidget groupWidget){
        deleteButton.setOnClickListener((view)->{
            deleteValueManager = new DeleteValueManager(context, groupWidget, deleteButton);
        });
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

    private static LinearLayout makeMenuLinearLayout(LinearLayout parent, Context context){
        LinearLayout menuLinearLayout = new LinearLayout(context);
        menuLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        MainActivity.log("adding menu to parent");
        parent.addView(menuLinearLayout);
        menuLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return menuLinearLayout;
    }

    public View getView() {
        return menuBarLayout;
    }
}
