package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;

public class Drag {
    private View view;
    private ListWidgetGhostManager listWidgetGhostManager;
    private boolean actionUp = false;
    private int cursorX = -10000;
    private int cursorY = -10000;
    private Context context;
    private boolean doneInitialPost = false;
    private RelativeLayout relativeLayout;
    private RelativeLayout.LayoutParams shadowParam;
    private int topBorder;
    private int bottomBorder;
    private int scrollFactor = 1;


    public Drag(int shadowWidth, int shadowHeight, ListWidgetGhostManager listWidgetGhostManager, Context context){
        this.listWidgetGhostManager = listWidgetGhostManager;
        this.context = context;
        relativeLayout = MainActivity.popUpLayout;
        setLayoutBorderValues();
        MainActivity.mainActivity.setDragListener(this);
        initShadow(shadowWidth, shadowHeight);
        postFunction();
    }

    public void setLayoutBorderValues(){
        int[] temp = new int[2];
        relativeLayout.getLocationOnScreen(temp);
        topBorder = temp[1];
        bottomBorder = topBorder + relativeLayout.getHeight();
    }

    public boolean inBetween(int value, int low, int high){
        if(value > low && value < high)
            return true;
        return false;
    }

    public int magnitude(int value, int low, int high){
        return (int)(scrollFactor*(value - low) / (float)(high-low));
    }

    public void tryToScroll(){
        int layoutHeight = bottomBorder - topBorder;
        int areaToScroll = layoutHeight/4;
        int sign = 0;
        float magnitude = 0;
        if(inBetween(cursorY, topBorder, topBorder + areaToScroll)){
            sign = -1;
            magnitude = magnitude(cursorY, topBorder, topBorder + layoutHeight);
        }
        if(inBetween(cursorY, bottomBorder - areaToScroll, bottomBorder)){
            sign = 1;
            magnitude = magnitude(cursorY, bottomBorder - areaToScroll, bottomBorder);
        }
        MainActivity.scrollView.scroll_down(sign*magnitude);
    }
    public interface DragIterate{
        void iterate(int x, int y);
    }
    private DragIterate iterateFunction;
    public void setIterateFunction(DragIterate iterateFunction){
        this.iterateFunction = iterateFunction;
    }
    private Runnable onActionUp;
    public void setOnActionUpListener(Runnable runnable){
        this.onActionUp = runnable;
    }


    private void initShadow(int shadowWidth, int shadowHeight){
        view = new View(context);
        view.setBackground(new ColorDrawable(ColorPalette.dragShadowBackground));
        shadowParam = new RelativeLayout.LayoutParams(shadowWidth, shadowHeight);
        relativeLayout.addView(view);
    }

    private void updateShadowLocation(){
        shadowParam.leftMargin = cursorX;
        shadowParam.topMargin = cursorY;
    }

    private void initialPost(){
        MainActivity.log("initial post of drag");

    }

    private void iterate(){
        //MainActivity.log("iterate of drag");
        if(!doneInitialPost){
            initialPost();
            doneInitialPost = true;
        }
        tryToScroll();
        updateShadowLocation();
        iterateFunction.iterate(cursorX, cursorY);
    }

    private void finalIteration(){
        MainActivity.log("final iteration of drag");
        relativeLayout.removeView(view);
        onActionUp.run();
    }

    private void postFunction(){

        view.post(()->{
            MainActivity.mainActivity.runOnUiThread(()->{
                if(isActionUp()){
                    finalIteration();
                    return;
                }
                iterate();
                postFunction();
            });
        });
    }

    public boolean isActionUp(){
        return actionUp;
    }

    public void actionUp() {
        actionUp = true;
    }

    public void updateCursorPosition(float rawX, float rawY) {
       // MainActivity.log("updating cursor position: " + (int)rawX + ", "  + (int)rawY);
        cursorX = (int) rawX;
        cursorY = (int) rawY;
    }
}
