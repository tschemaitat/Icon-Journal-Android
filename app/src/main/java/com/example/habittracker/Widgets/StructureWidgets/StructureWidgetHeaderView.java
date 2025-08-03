package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.StaticStateManagers.InvisibleEditTextManager;
import com.example.habittracker.StaticStateManagers.KeyBoardActionManager;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ImageButtonElement;
import com.example.habittracker.ViewLibrary.ImageToggleButtonElement;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelativeElementLayout;
import com.example.habittracker.ViewLibrary.ViewElement;
import com.example.habittracker.ViewWidgets.ViewWrapper;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidgets.EntryWidgetResources;

public class StructureWidgetHeaderView {
    public RelativeElementLayout relativeLayout;
    public CustomEditText nameEditor;
    public ImageButtonElement deleteButton;
    public ImageToggleButtonElement starButton;
    public ImageButtonElement upButton;
    public ImageButtonElement downButton;
    private boolean starEnabled = true;
    private Runnable pressedStarListener;
    private Context context;
    public StructureWidgetHeaderView(Context context, Runnable onTextChange, Runnable onDelete, Runnable moveUp, Runnable moveDown, Runnable pressStar){
        this.context = context;
        relativeLayout = new RelativeElementLayout(context);
        Margin.setStructureWidgetHeader(this);
        addNameEditor(null, onTextChange);
        MainActivity.log("nameEditor element params: " + nameEditor.getElement().getLayoutParams());
        addDeleteButton(onDelete);
        addMoveButtons(moveUp, moveDown);
        pressedStarListener = pressStar;
        addStar();
        relativeLayout.addRule(starButton).below(downButton);
        relativeLayout.addRule(downButton).rightOf(starButton);
        relativeLayout.addRule(upButton).rightOf(downButton);
        MainActivity.log("nameEditor element params: " + nameEditor.getElement().getLayoutParams());
        relativeLayout.addRule(nameEditor.getElement()).rightOf(starButton);
        relativeLayout.addRule(nameEditor.getElement()).below(downButton);
//        align(starButton, RelativeLayout.BELOW, downButton);
//        align(downButton, RelativeLayout.RIGHT_OF, starButton);
//        align(upButton, RelativeLayout.RIGHT_OF, downButton);
//
//        align(nameEditor.getView(), RelativeLayout.RIGHT_OF, starButton);
//        align(nameEditor.getView(), RelativeLayout.BELOW, downButton);
    }

    private void align(View first, int rule, View second){
        RelativeLayout.LayoutParams param = ((RelativeLayout.LayoutParams) first.getLayoutParams());
        param.addRule(rule, second.getId());
        //first.setLayoutParams(param);

    }

    public void disableStar(){
        if(starButton.getState())
            throw new RuntimeException();
        if( ! starEnabled)
            throw new RuntimeException();
        starEnabled = false;

        starButton.setBackground(GLib.starDisabled);
    }
    public void enableStar(){
        if(starButton.getState())
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
        return starButton.getState();
    }
    private boolean setStarOn = false;
    public void setStarOn(boolean starOn){
        if(setStarOn)
            throw new RuntimeException();
        setStarOn = true;
        starButton.setOn();
    }

    private void addStar(){
        int size = 100;
        starButton = new ImageToggleButtonElement(context, this::starPressed, GLib.starOff, GLib.starOn, false);
        relativeLayout.addWithParam(starButton, size, size).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        starButton.setBackground(GLib.starOff);
//        RelativeLayout.LayoutParams starParam = new RelativeLayout.LayoutParams(size, size);
//        starParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        starButton.setLayoutParams(starParam);
//        relativeLayout.addView(starButton);
//        starButton.setOnClickListener(view -> starPressed());
    }

    private void starPressed(boolean state){
        if( ! starEnabled)
            return;
        pressedStarListener.run();
    }

    private void addMoveButtons(Runnable moveUp, Runnable moveDown) {
        int size = 100;
        int horMargin = 20;
        downButton = new ImageButtonElement(context, GLib.downArrow, moveDown);
        upButton = new ImageButtonElement(context, GLib.upArrow, moveUp);
        relativeLayout.addWithParam(upButton, size, size).alignParentTop();
        relativeLayout.addWithParam(downButton, size, size).leftOf(upButton).alignParentTop();





//        downButton = new View(context);
//        downButton.setId(View.generateViewId());
//        downButton.setBackground(GLib.downArrow);
//        downButton.setOnClickListener(view -> moveDown.run());
//        RelativeLayout.LayoutParams downParam = new RelativeLayout.LayoutParams(size, size);
//        //downParam.addRule(RelativeLayout.LEFT_OF, upButton.getId());
//        //downParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        downParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        downParam.setMargins(horMargin, 0, horMargin, 0);
//        downButton.setLayoutParams(downParam);
//        relativeLayout.addView(downButton);
//
//        upButton = new View(context);
//        upButton.setId(View.generateViewId());
//        upButton.setBackground(GLib.upArrow);
//        upButton.setOnClickListener(view -> moveUp.run());
//        RelativeLayout.LayoutParams upParam = new RelativeLayout.LayoutParams(size, size);
//        //upParam.addRule(RelativeLayout.LEFT_OF, deleteButton.getId());
//
//        upParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        upParam.setMargins(horMargin, 0, 2*horMargin, 0);
//        upButton.setLayoutParams(upParam);
//        relativeLayout.addView(upButton);
//
//        ((RelativeLayout.LayoutParams) nameEditor.getView().getLayoutParams()).addRule(RelativeLayout.BELOW, downButton.getId());
    }

    private void addNameEditor(String name, Runnable onTextChange){

        nameEditor = new CustomEditText(context, new ViewWrapper(context), null);
        nameEditor.setHint("widget name");
        if(name != null)
            nameEditor.setText(name);
        relativeLayout.addWithParam(nameEditor.getElement(), -2, -2);
        MainActivity.log("nameEditor element params right after add: " + nameEditor.getElement().getLayoutParams());
//        relativeLayout.addView(nameEditor.getView());
        nameEditor.setOnDataChangedListener((prevData, data)->onTextChange.run());
        MainActivity.log("nameEditor element params right after add: " + nameEditor.getElement().getLayoutParams());
        Margin.setStructureWidgetHeader(this);
        MainActivity.log("nameEditor element params right after add: " + nameEditor.getElement().getLayoutParams());
//        nameEditor.getView().setId(View.generateViewId());
    }

    private void addDeleteButton(Runnable runnable){
        deleteButton = new ImageButtonElement(context, GLib.delete, runnable);
//        deleteButton = (ImageButton) GLib.inflate(R.layout.delete_button);
//        //deleteButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
//        deleteButton.setId(View.generateViewId());
//        relativeLayout.addView(deleteButton);
//
//        deleteButton.setOnClickListener((view)->runnable.run());
//        Margin.setStructureWidgetHeader(this);

    }

    public View getView(){
        return relativeLayout.getView();
    }

    public Element getElement(){
        return new ViewElement() {
            @Override
            public View getView() {
                return getView();
            }
        };
    }
}
