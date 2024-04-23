package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;

public class StructureWidgetHeaderView {
    public RelativeLayout relativeLayout;
    public CustomEditText nameEditor;
    public ImageButton deleteButton;
    public View starButton;
    public View upButton;
    public View downButton;
    private boolean starOn = false;
    private boolean starEnabled = true;
    private Runnable pressedStarListener;
    private Context context;
    public StructureWidgetHeaderView(Context context, Runnable onTextChange, Runnable onDelete, Runnable moveUp, Runnable moveDown, Runnable pressStar){
        this.context = context;
        relativeLayout = new RelativeLayout(context);
        Margin.setStructureWidgetHeader(this);
        addNameEditor(null, onTextChange);
        addDeleteButton(onDelete);
        addMoveButtons(moveUp, moveDown);
        pressedStarListener = pressStar;
        addStar();
        align(starButton, RelativeLayout.BELOW, downButton);
        align(downButton, RelativeLayout.RIGHT_OF, starButton);
        align(upButton, RelativeLayout.RIGHT_OF, downButton);

        align(nameEditor.getView(), RelativeLayout.RIGHT_OF, starButton);
        align(nameEditor.getView(), RelativeLayout.BELOW, downButton);
    }

    private void align(View first, int rule, View second){
        RelativeLayout.LayoutParams param = ((RelativeLayout.LayoutParams) first.getLayoutParams());
        param.addRule(rule, second.getId());
        //first.setLayoutParams(param);

    }

    public void disableStar(){
        if(starOn)
            throw new RuntimeException();
        if( ! starEnabled)
            throw new RuntimeException();
        starEnabled = false;
        starButton.setBackground(GLib.starDisabled);
    }
    public void enableStar(){
        if(starOn)
            throw new RuntimeException();
        if(starEnabled)
            throw new RuntimeException();
        MainActivity.log("enabling star");
        starEnabled = true;
        starButton.setBackground(GLib.starOff);
    }

    public boolean isStarEnabled(){
        return starEnabled;
    }


    public boolean getStarOn(){
        return starOn;
    }
    private boolean setStarOn = false;
    public void setStarOn(boolean starOn){
        if(setStarOn)
            throw new RuntimeException();
        setStarOn = true;
        this.starOn = starOn;
        if(starEnabled)
            setStarImage();
    }

    private void setStarImage(){
        MainActivity.log("setting star image enabled: " + starEnabled);
        if(starOn)
            starButton.setBackground(GLib.starOn);
        else
            starButton.setBackground(GLib.starOff);
    }

    private void addStar(){
        int size = 100;
        starButton = new View(context);
        starButton.setBackground(GLib.starOff);
        starButton.setId(View.generateViewId());
        RelativeLayout.LayoutParams starParam = new RelativeLayout.LayoutParams(size, size);
        starParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        starButton.setLayoutParams(starParam);
        relativeLayout.addView(starButton);
        starButton.setOnClickListener(view -> starPressed());
    }

    private void starPressed(){
        if( ! starEnabled)
            return;
        if(starOn)
            starOn = false;
        else
            starOn = true;

        setStarImage();
        pressedStarListener.run();
    }

    private void addMoveButtons(Runnable moveUp, Runnable moveDown) {
        int size = 100;
        int horMargin = 20;


        downButton = new View(context);
        downButton.setId(View.generateViewId());
        downButton.setBackground(GLib.downArrow);
        downButton.setOnClickListener(view -> moveDown.run());
        RelativeLayout.LayoutParams downParam = new RelativeLayout.LayoutParams(size, size);
        //downParam.addRule(RelativeLayout.LEFT_OF, upButton.getId());
        //downParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        downParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        downParam.setMargins(horMargin, 0, horMargin, 0);
        downButton.setLayoutParams(downParam);
        relativeLayout.addView(downButton);

        upButton = new View(context);
        upButton.setId(View.generateViewId());
        upButton.setBackground(GLib.upArrow);
        upButton.setOnClickListener(view -> moveUp.run());
        RelativeLayout.LayoutParams upParam = new RelativeLayout.LayoutParams(size, size);
        //upParam.addRule(RelativeLayout.LEFT_OF, deleteButton.getId());

        upParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upParam.setMargins(horMargin, 0, 2*horMargin, 0);
        upButton.setLayoutParams(upParam);
        relativeLayout.addView(upButton);

        ((RelativeLayout.LayoutParams) nameEditor.getView().getLayoutParams()).addRule(RelativeLayout.BELOW, downButton.getId());
    }

    private void addNameEditor(String name, Runnable onTextChange){
        nameEditor = new CustomEditText(context);
        nameEditor.setHint("widget name");
        if(name != null)
            nameEditor.setText(name);

        relativeLayout.addView(nameEditor.getView());
        nameEditor.setOnDataChangedListener(()->onTextChange.run());
        Margin.setStructureWidgetHeader(this);
        nameEditor.getView().setId(View.generateViewId());
    }

    private void addDeleteButton(Runnable runnable){
        deleteButton = (ImageButton) GLib.inflate(R.layout.delete_button);
        //deleteButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        deleteButton.setId(View.generateViewId());
        relativeLayout.addView(deleteButton);

        deleteButton.setOnClickListener((view)->runnable.run());
        Margin.setStructureWidgetHeader(this);

    }

    public View getView(){
        return relativeLayout;
    }
}
