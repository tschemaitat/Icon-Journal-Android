package com.example.habittracker.Widgets.EntryWidgets;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.StaticStateManagers.KeyBoardActionManager;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Values.WidgetValue;
import com.example.habittracker.Values.WidgetValueString;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;

public class CustomEditText extends EntryWidget {
    public static final String className = "edit text";
    public static final String nullText = "";
    private LinLayout linLayout;
    private EditText editText;
    private Context context;

    private String currentText = nullText;

    public CustomEditText(Context context) {
        super(context);
        this.context = context;


        init();
    }

    public void setModeNext(){
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void setModeEnter(){
        editText.setImeOptions(EditorInfo.IME_ACTION_NONE);
    }

    public String getText(){
        Editable editable = editText.getText();
        if(editable == null)
            return null;
        String result = editable.toString();
        if(result.equals(""))
            return null;
        String spaceCharacter = " ";
        String copy = result.replace(String.valueOf(spaceCharacter), "");
        if(copy.equals("")){
            System.out.println("text of widget only has spaces");
            return null;
        }
        return result;
    }

    private void setTextState(String newText){
        currentText = newText;
        editText.setText(newText);
    }

    private void setModeFromToggle(boolean isEnter){
        if(isEnter)
            setModeEnter();
        else
            setModeNext();
        if(editText.hasFocus())
            resetKeyboard();
    }

    private void resetKeyboard(){
        // Force the keyboard to close
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        // Force the keyboard to open again
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
    }

    private void init(){


        linLayout = new LinLayout(context);
        setViewWrapperChild(linLayout.getView());
        //editTextLayout = (TextInputLayout) GLib.inflate(R.layout.text_input_layout);
        editText = new EditText(context);
        editText.setPadding(10,0,0,0);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        linLayout.add(editText);

        editText.setMinWidth(GLib.dpToPx(400));
        editText.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        editText.setTextColor(ColorPalette.textPurple);
        editText.setBackground(null);
        editText.setHintTextColor(ColorPalette.hintText);


        setModeFromToggle(KeyBoardActionManager.getManager().getIsEnter());
        KeyBoardActionManager.getManager().addToggleListener((isEnter)->{
            setModeFromToggle(isEnter);
        });

        //setModeEnter();
        //editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        Margin.setEditTextLayout(this);

        setTextListener();
        int MAX_CHARACTERS = 50;

        // Error handling from too many characters

    }

    public void setText(String text){
        String temp = text;
        if(text == null)
            temp = "";
        setTextState(temp);
    }

    public void onTextChange(String before, String newText){
        getViewWrapper().resetNameColor();
        onDataChangedListener().run();
    }

    private void setTextListener(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newText = "";
                if(s != null)
                    newText = s.toString();
                String before = currentText;
                currentText = newText;
                //System.out.println("\t\tafter text changed listener: " + newText);
                boolean textChanged = !before.equals(newText);
                if(!textChanged)
                    return;
                onTextChange(before, newText);
            }
        });
    }

    @Override
    public void setValueCustom(WidgetValue widgetValue) {
        WidgetValueString widgetValueString = (WidgetValueString)widgetValue;
        if(widgetValue == null)
            throw new RuntimeException();

        if( ! (widgetValueString.getStandardFormOfCachedString() instanceof LiteralString))
            throw new RuntimeException();
        String currentText = widgetValueString.getStandardFormOfCachedString().getString();
        if(currentText == null)
            currentText = "";


        setTextState(currentText);
    }

    @Override
    public WidgetValue getEntryValueTreeCustom() {
        //MainActivity.log("returning data tree");
        //MainActivity.log("getText() = " + getText());
        return new WidgetValueString(getWidgetId(), new LiteralString(getText()));
    }

    @Override
    public void setParamCustom(EntryWidgetParam param){
        EditTextParam casted = ((EditTextParam) param);
    }

    public void disableEdit() {
        editText.setEnabled(false);
    }

    public void setHint(String widget_name) {
        editText.setHint(widget_name);
    }

    public void setError() {
        String text = getText();
        if(text == null){
            editText.setHintTextColor(ColorPalette.redText);
            return;
        }
        editText.setTextColor(ColorPalette.redText);
    }

    public void resetError(){
        editText.setHintTextColor(ColorPalette.hintText);
        editText.setTextColor(ColorPalette.textPurple);
    }

    public EditText getEditText() {
        return editText;
    }


}
