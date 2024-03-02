package com.example.habittracker;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {
    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        int MAX_CHARACTERS = 50;

        // Error handling from too many characters
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > MAX_CHARACTERS) {
                    setError("Maximum character limit exceeded");
                } else {
                    setError(null);
                }
            }
        });

        // Responding to the user focusing on the EditText
        setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // EditText is focused
            } else {
                // EditText lost focus
            }
        });

        // Responding to the user changing the text (using TextWatcher)

        // Responding to the user exiting focus from the EditText (using OnFocusChangeListener)

    }
}
