package com.example.habittracker.ViewWidgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.habittracker.StaticClasses.ColorPalette;

public class AnimatedHighlightView extends View {
    private static int animationDuration = 100;
    private Paint backgroundPaint, highlightPaint, maskPaint;
    private RectF fullRect, highlightRect;
    private Path maskPath;
    private float highlightPosition; // 0.0 for left, 1.0 for right
    private boolean isHighlightOnLeft = false;
    private Paint bluePaint;

    public AnimatedHighlightView(Context context) {
        super(context);
        init();
    }

    public AnimatedHighlightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.WHITE); // Light gray

        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
         // Blue

        highlightPaint.setColor(Color.RED);
        highlightPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint.setColor(ColorPalette.toggleHighlight);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint.setColor(Color.TRANSPARENT);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // Clear where it draws



        fullRect = new RectF();
        highlightRect = new RectF();
        maskPath = new Path();

        setLayerType(LAYER_TYPE_SOFTWARE, null); // Required for PorterDuff mode to work correctly
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        fullRect.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
        highlightPosition = isHighlightOnLeft ? 0.0f : 1.0f;
        updateHighlightRect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 200; // Minimum width your view wants to be
        int desiredHeight = 200; // Minimum height your view wants to be

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        // MUST call this to store the measured width and height
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Your custom draw call
        onDrawCustom(canvas);
    }


    protected void onDrawCustom(Canvas canvas) {
        float cornerRadius = 15 * getResources().getDisplayMetrics().density;

        //canvas.drawRoundRect(fullRect, cornerRadius, cornerRadius, backgroundPaint);
        //canvas.drawRect(fullRect, backgroundPaint);

        Bitmap bitmap = Bitmap.createBitmap(1000, 400, Bitmap.Config.ARGB_4444);
        Canvas bitmapCanvas = new Canvas(bitmap);

        maskPath.reset();
        maskPath.addRoundRect(fullRect, cornerRadius, cornerRadius, Path.Direction.CW);

        bitmapCanvas.clipPath(maskPath);
        bitmapCanvas.clipRect(highlightRect);
        bitmapCanvas.drawColor(ColorPalette.toggleHighlight);
        //bitmapCanvas.drawPath(maskPath, maskPaint);
        //bitmapCanvas.(fullRect, cornerRadius, cornerRadius, maskPaint);
        //bitmapCanvas.drawRect(highlightRect, highlightPaint);
        canvas.drawBitmap(bitmap, 0, 0, bluePaint);
    }

    private void updateHighlightRect() {
        if (! isHighlightOnLeft) {
            highlightRect.left = (fullRect.width() / 2 * (highlightPosition));
            highlightRect.right = fullRect.width() / 2 + (fullRect.width() / 2 * highlightPosition);
        } else {
            highlightRect.left = (fullRect.width() / 2 * (highlightPosition));
            highlightRect.right = fullRect.width() / 2 + (fullRect.width() / 2 * highlightPosition);
        }

        highlightRect.top = fullRect.top;
        highlightRect.bottom = fullRect.bottom;

        invalidate();
    }

    public void toggleHighlight() {
        ValueAnimator animator = ValueAnimator.ofFloat(isHighlightOnLeft ? 0.0f : 1.0f, isHighlightOnLeft ? 1.0f : 0.0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(animationDuration);
        animator.addUpdateListener(animation -> {
            highlightPosition = (float) animation.getAnimatedValue();
            updateHighlightRect();
        });
        animator.start();
        isHighlightOnLeft = !isHighlightOnLeft;
    }

    public boolean getIsHighlightOnLeft(){
        return isHighlightOnLeft;
    }
}
