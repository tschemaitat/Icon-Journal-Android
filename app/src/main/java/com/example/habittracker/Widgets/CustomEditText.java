package com.example.habittracker.Widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.R;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.google.android.material.textfield.TextInputLayout;

public class CustomEditText extends EntryWidget {
    private String text = "null";
    public static final String className = "edit text";
    public static final String nullText = "null";
    public Runnable onDataChangedListener = null;
    private TextInputLayout editTextLayout;
    public CustomEditText(Context context) {
        super(context);
        editTextLayout = (TextInputLayout) GLib.inflate(R.layout.text_input_layout);
        editTextLayout.setMinWidth(GLib.dpToPx(context, 400));
        //editText.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        setChild(editTextLayout);
        init();
    }


    public String text(){
        return text;
    }

    public void setValue(String newValue){
        text = newValue;
        editTextLayout.getEditText().setText(newValue);
    }


    private void init(){
        setValue("null");
        int MAX_CHARACTERS = 50;

        // Error handling from too many characters
        editTextLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                text = s.toString();
                onDataChangedListener.run();
                if (s.length() > MAX_CHARACTERS) {
                    editTextLayout.setError("Maximum character limit exceeded");
                } else {
                    editTextLayout.setError(null);
                }
            }
        });
    }



    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public EntryWidgetParam getParam(){
        EditTextParam params = new EditTextParam(name, text);

        return params;
    }

    @Override
    public DataTree getDataTree() {
        return new DataTree(text);
    }

    @Override
    public void setParamCustom(EntryWidgetParam params){
        EditTextParam casted = ((EditTextParam) params);
        if( ! casted.text.equals(nullText) )
            setValue(casted.text);
    }

    public static class EditTextParam extends EntryWidgetParam {
        public String text;
        public EditTextParam(String name, String text){
            super(name, CustomEditText.className);
            this.text = text;
        }

        public String hierarchyString(int numTabs){
            return GLib.tabs(numTabs) + "text\n";
        }

        @Override
        public DataTree header() {
            return new DataTree(name);
        }
    }

    public static class EditTextValue extends WidgetValue{
        public String text;
        public EditTextValue(String text){
            this.text = text;
        }
    }
}
