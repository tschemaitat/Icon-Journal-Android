package com.example.habittracker.Widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.structures.HeaderNode;

import java.util.HashMap;

public class CustomEditText extends EntryWidget {
    public static final String className = "edit text";
    public static final String nullText = "";
    private LinLayout linLayout;
    private EditText editTextLayout;
    private Context context;

    private String currentText = nullText;

    public CustomEditText(Context context) {
        super(context);
        this.context = context;


        init();
    }

    public String getText(){
        Editable editable = editTextLayout.getText();
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
        editTextLayout.setText(newText);
    }

    private void init(){
        linLayout = new LinLayout(context);
        setChild(linLayout.getView());
        //editTextLayout = (TextInputLayout) GLib.inflate(R.layout.text_input_layout);
        editTextLayout = new EditText(context);
        editTextLayout.setPadding(10,0,0,0);
        editTextLayout.setGravity(Gravity.CENTER_VERTICAL);
        linLayout.add(editTextLayout);

        editTextLayout.setMinWidth(GLib.dpToPx(400));
        editTextLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        editTextLayout.setTextColor(ColorPalette.textPurple);
        editTextLayout.setBackground(null);
        editTextLayout.setHintTextColor(ColorPalette.hintText);
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
        editTextLayout.addTextChangedListener(new TextWatcher() {
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
    public void setValueCustom(EntryValueTree entryValueTree, HashMap<Integer, ValueTreePath> valueTreePathMap) {
        if(entryValueTree == null)
            throw new RuntimeException();

        if( ! (entryValueTree.getCachedString() instanceof LiteralString))
            throw new RuntimeException();
        String currentText = entryValueTree.getCachedString().getString();
        if(currentText == null)
            currentText = "";


        setTextState(currentText);
    }

    @Override
    public EntryValueTree getEntryValueTreeCustom() {
        //MainActivity.log("returning data tree");
        //MainActivity.log("getText() = " + getText());
        return new EntryValueTree(new LiteralString(getText()));
    }

    @Override
    public void setParamCustom(EntryWidgetParam param){
        EditTextParam casted = ((EditTextParam) param);
    }

    public void disableEdit() {
        editTextLayout.setEnabled(false);
    }

    public void setHint(String widget_name) {
        editTextLayout.setHint(widget_name);
    }

    public void showError() {
        editTextLayout.setTextColor(ColorPalette.redText);
    }

    public void setError() {
        String text = getText();
        if(text == null){
            editTextLayout.setHintTextColor(ColorPalette.redText);
            return;
        }
        editTextLayout.setTextColor(ColorPalette.redText);
    }

    public void resetError(){
        editTextLayout.setHintTextColor(ColorPalette.hintText);
        editTextLayout.setTextColor(ColorPalette.textPurple);
    }

    public EditText getEditText() {
        return editTextLayout;
    }

    public static class EditTextParam extends EntryWidgetParam {
        public EditTextParam(String name){
            super(name, CustomEditText.className);
        }

        public String hierarchyString(int numTabs){
            return GLib.tabs(numTabs) + "text\n";
        }

        @Override
        public HeaderNode createHeaderNode() {
            return new HeaderNode(name, this);
        }
    }
}
