package com.example.habittracker.Widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.HeaderNode;
import com.example.habittracker.Structs.RefItemPath;

public class CustomEditText extends EntryWidget {
    private String currentText = null;
    public static final String className = "edit text";
    public static final String nullText = "";
    private LinLayout linLayout;
    private EditText editTextLayout;
    private Context context;
    public CustomEditText(Context context) {
        super(context);
        this.context = context;


        init();
    }

    private void updateEditTextValue(){
        editTextLayout.setText(currentText);
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




    private void init(){
        linLayout = new LinLayout(context);
        setChild(linLayout.getView());
        //editTextLayout = (TextInputLayout) GLib.inflate(R.layout.text_input_layout);
        editTextLayout = new EditText(context);
        linLayout.add(editTextLayout);

        editTextLayout.setMinWidth(GLib.dpToPx(context, 400));
        editTextLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        editTextLayout.setTextColor(ColorPalette.textPurple);
        editTextLayout.setBackground(null);
        Margin.setEditTextLayout(linLayout);

        setTextListener();
        int MAX_CHARACTERS = 50;

        // Error handling from too many characters

    }

    public void setText(String text){
        currentText = text;
        updateEditTextValue();
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

                String newText = s.toString();
                //System.out.println("\t\tafter text changed listener: " + newText);
                boolean textChanged = !currentText.equals(newText);
                if(!textChanged)
                    return;
                String before = currentText;
                onTextChange(before, newText);
            }
        });
    }





    @Override
    public EntryWidgetParam getParam(){
        EditTextParam params = new EditTextParam(getName());
        return params;
    }

    @Override
    public void setValue(EntryValueTree entryValueTree) {
        if(entryValueTree == null){
            currentText = "";
        }else{
            if( ! entryValueTree.getCachedString().isLiteral())
                throw new RuntimeException();
            currentText = entryValueTree.getCachedString().getString();
        }

        updateEditTextValue();
    }

    @Override
    public EntryValueTree getEntryValueTree() {
        System.out.println("returning data tree");
        System.out.println("currentText = " + currentText);
        System.out.println("getText() = " + getText());
        return new EntryValueTree(new CachedString(currentText));
    }

    @Override
    public void setParamCustom(EntryWidgetParam params){
        EditTextParam casted = ((EditTextParam) params);
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
        editTextLayout.setHintTextColor(ColorPalette.textPurple);
        editTextLayout.setTextColor(ColorPalette.textPurple);
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
            return new HeaderNode(name);
        }
    }
}
