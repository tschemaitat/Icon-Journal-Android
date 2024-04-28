package com.example.habittracker.Layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.Rectangle;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class InterceptLinearLayout extends LinearLayout {
    public static final int longClickTime = 1200;
    public static int idCount = 0;
    private boolean timerFinished = false;
    private Timer timer;
    private boolean enableIntercept = false;
    private int state = stateNoCurrentEvent;
    private int id;
    //private GroupWidget groupWidget;
    private Rectangle boundingBox;
    private boolean disableViewsInside = false;
    private Runnable dragListener;
    public InterceptLinearLayout(Context context) {
        super(context);
        init();
    }

    public InterceptLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterceptLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        id = idCount;
        idCount++;
    }

    public void enableIntercept(Runnable dragListener){
        MainActivity.log("enabling intercept id: " + id);
        enableIntercept = true;
        this.dragListener = dragListener;
    }

    public static final int stateDown = 1;
    public static final int stateNoCurrentEvent = 2;






    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(disableViewsInside)
            return true;

        if(!enableIntercept){
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                //MainActivity.log("cannot intercept id: " + id);
            }
            return false;
        }


        //MainActivity.log("linear layout got touch event");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(state == stateDown){
                    MainActivity.log("duplicate down event");
                    return false;
                }

                int boundingBoxSize = 100;
                boundingBox = new Rectangle((int)event.getRawX()-boundingBoxSize/2, (int)event.getRawY()-boundingBoxSize/2, 100, 100);


                MainActivity.log("action down current state: " + state);
                state = stateDown;
                MainActivity.log("action down");
                timerFinished = false;
                setTimer();
                return false;
            case MotionEvent.ACTION_CANCEL:
                if(state == stateNoCurrentEvent)
                    return false;
                MainActivity.log("action cancel");
                cancelEvent();
                break;
            case MotionEvent.ACTION_UP:
                if( ! isInsideBoundingBox(event)){
                    MainActivity.log("left bounding box");
                    cancelEvent();
                    return false;
                }
                if(timerFinished){
                    MainActivity.log("canceling action up");
                    longClickFound();
                    return true;
                }
                if(state == stateNoCurrentEvent)
                    return false;
                MainActivity.log("action up before long press");
                cancelEvent();
                return false;

            case MotionEvent.ACTION_MOVE:
                if( ! isInsideBoundingBox(event)){
                    MainActivity.log("left bounding box");
                    cancelEvent();
                    return true;
                }
                if(timerFinished){
                    MainActivity.log("cancelling action move");
                    longClickFound();
                    return true;
                }


        }

        return false;
    }

    private boolean isInsideBoundingBox(MotionEvent event){
        return boundingBox.inside((int)event.getRawX(), (int)event.getRawY());
    }

    private void cancelEvent() {
        state = stateNoCurrentEvent;
        timer.cancel();
    }


    public void longClickFound(){
        state = stateNoCurrentEvent;
        MainActivity.log("got long click");
        dragListener.run();
    }

    public void cancelTimer(){
        if(timer == null)
            return;
        timer.cancel();
        timerFinished = false;
        timer = null;
    }

    public void setTimerFinished(boolean bool){
        timerFinished = bool;
    }

    public void setTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.mainActivity.runOnUiThread(()->{
                    setTimerFinished(true);
                });
            }
        }, longClickTime);
    }

    public void disableViewsInside(){
        disableViewsInside = true;
    }

    public void enableViewsInside(){
        disableViewsInside = false;
    }
}
