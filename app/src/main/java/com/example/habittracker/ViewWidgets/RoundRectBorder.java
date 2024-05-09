package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;

public class RoundRectBorder extends View {
    public int cornerSize;
    public static final int borderThickness = 3;
    public int borderColor;
    private int innerCornerSize;

    private Paint paint;
    private RectF rect;

    public RoundRectBorder(Context context) {
        super(context);
        init();
    }

    private void init() {
        cornerSize = GLib.getInnerRoundCornerSize();
        innerCornerSize = cornerSize - borderThickness;
        borderColor = ColorPalette.widgetFocusHighlightBorder;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderThickness);
        paint.setColor(borderColor);

        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        if (width >= cornerSize * 2 && height >= cornerSize * 2) {
            rect.set(paddingLeft + borderThickness / 2, paddingTop + borderThickness / 2,
                    getWidth() - paddingRight - borderThickness / 2,
                    getHeight() - paddingBottom - borderThickness / 2);
            canvas.drawRoundRect(rect, innerCornerSize, innerCornerSize, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    private boolean firstMeasure = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View view;
        if(firstMeasure){
            firstMeasure = false;
            measure(widthMeasureSpec, heightMeasureSpec);
        }else
            firstMeasure = true;


        // Measure the parent's dimensions
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Set your view's dimensions to match the parent's dimensions
        setMeasuredDimension(parentWidth, parentHeight);
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int desiredWidth = 40; // Minimum width your view wants to be
//        int desiredHeight = 20; // Minimum height your view wants to be
//
//        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
//
//        int width;
//        int height;
//
//        // Measure Width
//        if (widthMode == View.MeasureSpec.EXACTLY) {
//            width = widthSize;
//        } else if (widthMode == View.MeasureSpec.AT_MOST) {
//            width = Math.min(desiredWidth, widthSize);
//        } else {
//            width = desiredWidth;
//        }
//
//        // Measure Height
//        if (heightMode == View.MeasureSpec.EXACTLY) {
//            height = heightSize;
//        } else if (heightMode == View.MeasureSpec.AT_MOST) {
//            height = Math.min(desiredHeight, heightSize);
//        } else {
//            height = desiredHeight;
//        }
//
//        // MUST call this to store the measured width and height
//        setMeasuredDimension(width, height);
//    }


}
