package com.example.habittracker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

public class DropDownPopUp extends ConstraintLayout {
    Context context;
    ConstraintLayout parent;
    public DropDownPopUp(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DropDownPopUp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DropDownPopUp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    public void setParent(ConstraintLayout parent, int x, int y){
        System.out.println("x = " + x);
        System.out.println("y = " + y);
        this.parent = parent;
        this.setX(x);
        this.setY(y);
        constraint();
    }

    private void init(){

    }

    private void constraint(){
        if(parent == null)
            return;
        //ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(200, 200);
//        layoutParams.leftToLeft = 500;
//        layoutParams.topToTop = 500;
//
//        this.setLayoutParams(layoutParams);
        //this.setLayoutParams(constraintLeftTop(200, 200, 500, 500));
        System.out.println("Drop down pop up: setting constraints");
        //setLayoutParams(constraintLeftTop(500, 500, 200, 200));
        this.getLayoutParams().width = 200;
        this.getLayoutParams().height = 200;
        //this.setX(200);
        System.out.println();
        //this.setBackgroundColor(Color.BLUE);
    }

    public ConstraintLayout.LayoutParams constraintLeftTop(int left, int top, int width, int height) {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);

        layoutParams.startToStart = left;
        layoutParams.topToTop = top;

        return layoutParams;
    }
}
