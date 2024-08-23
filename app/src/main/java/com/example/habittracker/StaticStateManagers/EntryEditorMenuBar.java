package com.example.habittracker.StaticStateManagers;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.habittracker.Inflatables.MenuBarManager;
import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewWidgets.OnDeleteValueCancelAndConfirm;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.structurePack.EntryInStructure;

public class EntryEditorMenuBar {
    private Context context;
    private Button deleteButton;
    private OnDeleteValueCancelAndConfirm deleteAndConfirm;

    private DeleteValueManager deleteValueManager;
    private LinearLayout horizontalBar;

    private MenuBarManager menuBarManager;



    public EntryEditorMenuBar(Context context, GroupWidget groupWidget, EntryInStructure entry, MenuBarManager menuBarManager) {
        this.context = context;
        this.menuBarManager = menuBarManager;
        if(groupWidget == null)
            throw new RuntimeException();
        init(groupWidget, entry);
    }

    private void init(GroupWidget groupWidget, EntryInStructure entry){

        horizontalBar = makeHorizontalBarLayout(context);
        deleteButton = makeDeleteButton(context, horizontalBar);




        deleteButton.setOnClickListener((view)->{
            deleteValueManager = new DeleteValueManager(context, groupWidget, deleteButton, entry);
            //add delete and confirm to invisible bar
            deleteAndConfirm = new OnDeleteValueCancelAndConfirm(context, this::onCancel, this::onConfirm);
            menuBarManager.addViewInvisibleLayout(deleteAndConfirm.getView());
        });
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
