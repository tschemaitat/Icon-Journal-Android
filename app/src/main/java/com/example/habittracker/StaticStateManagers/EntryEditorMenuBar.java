package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.OnDeleteValueCancelAndConfirm;
import com.example.habittracker.Widgets.GroupWidget;

public class EntryEditorMenuBar {
    private static EntryEditorMenuBar manager;
    public static void make(LinearLayout parent, Context context){
        manager = new EntryEditorMenuBar(parent, context);
    }

    public static EntryEditorMenuBar getManager(){
        return manager;
    }

    private LinearLayout parent;
    private LinearLayout menuBarLayout;
    private Context context;
    private Button deleteButton;
    private boolean shown = false;
    private OnDeleteValueCancelAndConfirm deleteAndConfirm;

    private DeleteValueManager deleteValueManager;



    private EntryEditorMenuBar(LinearLayout parent, Context context) {
        this.parent = parent;
        this.context = context;
        init();
    }

    private void init(){
        menuBarLayout = makeMenuLinearLayout(context);
        deleteButton = makeDeleteButton(context, menuBarLayout);

        KeyBoardActionManager.makeNewManager(context, menuBarLayout);
    }

    public void show(GroupWidget groupWidget, Integer entryId){

        parent.addView(menuBarLayout);
        setGroupWidget(groupWidget, entryId);
        shown = true;
    }

    public void hide(){
        parent.removeView(menuBarLayout);
        shown = false;
    }

    private void setGroupWidget(GroupWidget groupWidget, Integer entryId){
        deleteAndConfirm = new OnDeleteValueCancelAndConfirm(context, this::onCancel, this::onConfirm);

        deleteButton.setOnClickListener((view)->{
            deleteValueManager = new DeleteValueManager(context, groupWidget, deleteButton, entryId);
            parent.addView(deleteAndConfirm.getView());
        });
        //MainActivity.log("parent child count: " + parent.getChildCount());

        //MainActivity.log("parent child count: " + parent.getChildCount());

    }

    private void onCancel(){
        deleteValueManager.onCancel();
    }

    private void onConfirm(){
        deleteValueManager.onConfirm();
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

    private static LinearLayout makeMenuLinearLayout(Context context){
        LinearLayout menuLinearLayout = new LinearLayout(context);
        menuLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        MainActivity.log("adding menu to parent");
        //parent.addView(menuLinearLayout);
        menuLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return menuLinearLayout;
    }

    public View getView() {
        return menuBarLayout;
    }
}
