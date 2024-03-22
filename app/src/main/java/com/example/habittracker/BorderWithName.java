package com.example.habittracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.fonts.Font;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

public class BorderWithName extends View {
    private Paint paint;
    private RectF rect;
    private int textFontSize;
    private int textColor;
    private String hintText = "Hint Text";
    private ConstraintLayout parent;

    public BorderWithName(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BorderWithName(Context context, int textFontSize, int textColor, ConstraintLayout parent) {
        super(context);
        this.parent = parent;
        init();
        setCustomParameters(textFontSize, textColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Measure with the dynamically determined size
        setMeasuredDimension(parent.getMeasuredWidth(), parent.getMeasuredHeight());
    }

    private void init() {
        setId(generateViewId());
        parent.addView(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect = new RectF(); // This will be used for the rounded rectangle
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        setLayoutParams(layoutParams);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(parent);

// Assuming borderView and targetView have unique IDs
        constraintSet.connect(getId(), ConstraintSet.TOP, parent.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(getId(), ConstraintSet.BOTTOM, parent.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.connect(getId(), ConstraintSet.START, parent.getId(), ConstraintSet.START, 0);
        constraintSet.connect(getId(), ConstraintSet.END, parent.getId(), ConstraintSet.END, 0);

        constraintSet.applyTo(parent);
    }

    public void setCustomParameters(int textFontSize, int textColor) {
        this.textFontSize = textFontSize;
        this.textColor = textColor;
        paint.setTextSize(textFontSize);

        paint.setColor(textColor);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        System.out.println("fontMetrics.ascent = " + fontMetrics.ascent);
        System.out.println("fontMetrics.top = " + fontMetrics.top);
        System.out.println("fontMetrics.leading = " + fontMetrics.leading);
        invalidate(); // Redraw the view with new parameters
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Drawing the rounded rectangle
        rect.set(10, 10, getWidth() - 10, getHeight() - 10); // Adjust as necessary
        float cornerRadius = 25f; // Adjust for the desired corner roundness
        paint.setStyle(Paint.Style.STROKE); // Draw only the border
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        // Drawing the hint text
        paint.setStyle(Paint.Style.FILL);

        canvas.drawText(hintText, 30, 10 - paint.getFontMetrics().ascent/2, paint); // Adjust text position as necessary
    }
}
