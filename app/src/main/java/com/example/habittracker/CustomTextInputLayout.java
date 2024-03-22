package com.example.habittracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CustomTextInputLayout extends LinearLayout {
    private Paint paint;
    private Rect rect;
    private EditText editText;
    private float labelTextSize = 30; // Adjust as needed
    private boolean isEditTextFocused = false;

    public CustomTextInputLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Initialize your view and paint objects here
        setOrientation(VERTICAL); // Make sure the EditText is below the label

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY); // Label and border color
        paint.setTextSize(labelTextSize);

        rect = new Rect();

        editText = new EditText(getContext());
        addView(editText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setBackgroundColor(Color.TRANSPARENT); // Hide EditText background
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                invalidate(); // Redraw when text changes
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            isEditTextFocused = hasFocus;
            invalidate(); // Redraw on focus change
        });

        // Set initial dimensions and other properties as needed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the floating label
        String label = editText.getHint().toString();
        paint.getTextBounds(label, 0, label.length(), rect);
        float labelX = 0;
        float labelY = isEditTextFocused || editText.getText().length() > 0 ? 0 : editText.getTop() + rect.height();
        canvas.drawText(label, labelX, labelY, paint);

        // Draw the border around the EditText
        rect.set(0, editText.getTop(), getWidth(), editText.getBottom());
        canvas.drawRect(rect, paint);
    }
}
