package com.example.habittracker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BorderView {
    private int color;
    private ConstraintLayout parent;
    private Context context;
    private MatchParentConstraintLayout border;


    public BorderView(Context context, ConstraintLayout parent) {
        this.context = context;
        this.parent = parent;
        //this.color = color;
        init();
    }

    private void init(){
        View border = MainActivity.mainActivity.getLayoutInflater().inflate(R.layout.border_view_layout, parent);
        Drawable drawable = border.getBackground();
        border = new MatchParentConstraintLayout(context);
        border.setBackground(drawable);
        parent.addView(border);

    }

    public View getView(){
        return border;
    }








}
