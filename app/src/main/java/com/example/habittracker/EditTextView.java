package com.example.habittracker;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EditTextView extends androidx.appcompat.widget.AppCompatEditText {

    public EditTextView(Context context) {
        super(context);
        init();
    }

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize your view here
        // For example, set listeners, customize appearance, etc.
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Additional drawing commands, if needed
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Hide keyboard when the back button is pressed
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
        return super.onKeyPreIme(keyCode, event);
    }

    // Additional methods for your custom view
}