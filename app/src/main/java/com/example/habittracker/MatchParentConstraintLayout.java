package com.example.habittracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MatchParentConstraintLayout extends ConstraintLayout {

    public MatchParentConstraintLayout(@NonNull Context context) {
        super(context);
    }

    public MatchParentConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup viewGroup = (ViewGroup) getParent();
        // Measure with the dynamically determined size
        setMeasuredDimension(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredHeight());
    }
}