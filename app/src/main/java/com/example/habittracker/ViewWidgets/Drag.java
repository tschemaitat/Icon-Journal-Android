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
    private float scrollFactor = 100f;
    private int postDelayedNumber = 20;


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

    public float magnitudeBot(int value, int low, int high){
        float linNumber = (value - low) / (float)(high-low);
        return scrollFactor * (float)Math.pow(linNumber, 1);
    }

    public float magnitudeTop(int value, int low, int high){
        float height = high-low;
        float linNumber = (height - (value - low) )  / height;
        return scrollFactor * (float)Math.pow(linNumber, 1);
    }

    public void tryToScroll(){
        int layoutHeight = bottomBorder - topBorder;
        int areaToScroll = (int)(layoutHeight/2.3);
        int sign = 0;
        float magnitude = 0;
        if(inBetween(cursorY, topBorder, topBorder + areaToScroll)){
            sign = -1;
            magnitude = magnitudeTop(cursorY, topBorder, topBorder + areaToScroll);
        }
        if(inBetween(cursorY, bottomBorder - areaToScroll, bottomBorder)){
            sign = 1;
            magnitude = magnitudeBot(cursorY, bottomBorder - areaToScroll, bottomBorder);
        }
        MainActivity.scrollView.scroll_down(sign*magnitude);
    }
    public interface DragIterate{
        void iterate(int x, int y, int scrollAmount);
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
        iterateFunction.iterate(cursorX, cursorY, MainActivity.scrollView.getScrollY());
    }

    private void finalIteration(){
        MainActivity.log("final iteration of drag");
        relativeLayout.removeView(view);
        onActionUp.run();
    }

    private void postFunction(){

        view.postDelayed(()->{
            MainActivity.mainActivity.runOnUiThread(()->{
                if(isActionUp()){
                    finalIteration();
                    return;
                }
                iterate();
                postFunction();
            });
        }, postDelayedNumber);
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
