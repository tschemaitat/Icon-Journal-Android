package com.example.habittracker.Widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.example.habittracker.DataTree;
import com.example.habittracker.GLib;
import com.example.habittracker.Structs.WidgetParam;
import com.example.habittracker.Structs.WidgetValue;
import com.example.habittracker.Widgets.Widget;

import java.util.Objects;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText implements Widget {
    private String text = "null";
    public static final String className = "edit text";
    public static final String nullText = "null";
    public Runnable onDataChangedListener = null;
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
    public String text(){
        return text;
    }

    public void setValue(String newValue){
        text = newValue;
        this.setText(newValue);
    }


    private void init(){
        setValue("null");
        int MAX_CHARACTERS = 50;

        // Error handling from too many characters
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                text = s.toString();
                onDataChangedListener.run();
                if (s.length() > MAX_CHARACTERS) {
                    setError("Maximum character limit exceeded");
                } else {
                    setError(null);
                }
            }
        });
    }



    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangedListener = runnable;
    }

    @Override
    public WidgetParam getData(){
        EditTextParam params = new EditTextParam(text);

        return params;
    }

    @Override
    public WidgetValue value(){
        EditTextValue value = new EditTextValue(text);
        return value;
    }

    @Override
    public DataTree getDataTree() {
        return new DataTree(text);
    }

    @Override
    public void setData(WidgetParam params){
        EditTextParam casted = ((EditTextParam) params);
        if( ! casted.text.equals(nullText) )
            setValue(casted.text);
    }

    @Override
    public View getView() {
        return this;
    }

    public static class EditTextParam extends WidgetParam {
        public String name = "null";
        public String text;
        public EditTextParam(String text){
            this.widgetClass = "edit text";
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
