package com.example.habittracker;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;


public class DropDown extends ConstraintLayout{
    Context context;
    ArrayList<Pair<Integer, String>> optionPages;
    ConstraintLayout popUpParent;
    public DropDown(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DropDown(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public DropDown(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setPages(ArrayList<Pair<Integer, String>> optionPages){
        this.optionPages = optionPages;
        init();
    }

    public void setPopUpParent(ConstraintLayout popUpParent){
        this.popUpParent = popUpParent;
        init();
    }

    private void init(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 100);

        layoutParams.setMargins(GLib.dpToPx(context, 10), 0, 0, 0);
        //this.setBackgroundColor(Color.RED);
        this.setLayoutParams(layoutParams);
        int[] locationScreen = new int[2];


        if(optionPages == null || popUpParent == null)
            return;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getLocationOnScreen(locationScreen);
                System.out.println("location on screen: " + locationScreen[0] + ", " + locationScreen[1]);
                //DropDownPopUp popUp = new DropDownPopUp(context);
                DropDownPopUp popUp = (DropDownPopUp) GLib.inflate(context, R.layout.drop_down_pop_up_layout);
                popUpParent.addView(popUp);
                popUp.setParent(popUpParent, locationScreen[0], locationScreen[1]);
            }
        });
    }
}
