package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FillLayout extends ViewGroup {
    private View childView;
    private View fillerView;

    public FillLayout(Context context) {
        super(context);
    }

    public FillLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setChildView(View view) {
        if (childView != null) {
            removeView(childView);
        }
        childView = view;
        addView(childView);
    }

    public void removeChildView(){
        removeView(childView);
        childView = null;
    }

    public void setFillerView(View view) {
        if (fillerView != null) {
            removeView(fillerView);
        }
        fillerView = view;
        addView(fillerView);
    }

    public void removeFillerView(){
        removeView(fillerView);
        fillerView = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        if (childView != null) {
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            width = childView.getMeasuredWidth();
            height = childView.getMeasuredHeight();
        }

        if (fillerView != null) {
            measureChildWithMargins(fillerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }

        width += paddingLeft + paddingRight;
        height += paddingTop + paddingBottom;

        setMeasuredDimension(
                resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec)
        );

        if (fillerView != null) {
            fillerView.measure(
                    MeasureSpec.makeMeasureSpec(width - paddingLeft - paddingRight, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height - paddingTop - paddingBottom, MeasureSpec.EXACTLY)
            );
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (childView != null) {
            childView.layout(paddingLeft, paddingTop,
                    paddingLeft + childView.getMeasuredWidth(),
                    paddingTop + childView.getMeasuredHeight());
        }

        if (fillerView != null) {
            fillerView.layout(paddingLeft, paddingTop,
                    paddingLeft + fillerView.getMeasuredWidth(),
                    paddingTop + fillerView.getMeasuredHeight());
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}

