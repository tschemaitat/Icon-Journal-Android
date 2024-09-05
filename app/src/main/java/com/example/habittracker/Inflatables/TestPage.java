package com.example.habittracker.Inflatables;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.SaveStructures;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.ViewLibrary.BasicElement;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelativeElementLayout;
import com.example.habittracker.ViewLibrary.TextElement;
import com.example.habittracker.ViewWidgets.ListViewPackage.CustomListView;
import com.example.habittracker.ViewWidgets.ListViewPackage.DynamicListView;
import com.example.habittracker.ViewWidgets.RoundRectBorder;
import com.example.habittracker.ViewWidgets.ToggleView;

import com.example.habittracker.defaultImportPackage.ArrayList;

import java.util.List;

public class TestPage implements Inflatable {
    private LinearLayout linearLayout;
    private Context context;

    public TestPage(Context context){
        this.context = context;
        linearLayout = MainActivity.createVerticalLayout();
        linearLayout.setId(R.id.pageLayout);
        //testCustomListView();
        //testAnimatedToggle();
        //testRoundRectBorder();
        addSaveAndDeleteButtons();
        //testDynamicListView();
        //testCustomMeasureLayouts();
    }




    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {

    }

    @Override
    public boolean tryToRemove(Inflatable page) {
        return true;
    }

    private void testDynamicListView() {
        ArrayList<String> options = new ArrayList<>("item 1", "item 2", "item 3");
        ArrayList<PayloadOption> payloadOptions = options.convert((index, element) -> {
            return new PayloadOption(new LiteralString(element), null);
        });
        DynamicListView dynamicListView = new DynamicListView(context, payloadOptions);
        linearLayout.addView(dynamicListView.getView());
        dynamicListView.getView().setLayoutParams(new LinearLayout.LayoutParams(-1, -2));



        dynamicListView.setOnClickListener((cachedString, position, key) -> {

        });
        dynamicListView.setOnLongClickListener((cachedString, position, key) -> {
            onLongClick(dynamicListView);
        });
    }

    private void onLongClick(DynamicListView dynamicListView){
        MainActivity.log("outside on long click");
        dynamicListView.showCheckBoxes((checked, cachedString1, position1, key1) -> {
            onCheckBox(dynamicListView);
        });
        dynamicListView.showConfirmButton(() -> {
            onConfirm(dynamicListView);
        });
    }

    private void onCheckBox(DynamicListView dynamicListView){

    }

    private void onConfirm(DynamicListView dynamicListView){
        MainActivity.log("confirming");
        dynamicListView.hideConfirmButton();
        dynamicListView.hideCheckBoxes();
    }

    private void addSaveAndDeleteButtons() {
        Button saveButton = new Button(context);
        saveButton.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        saveButton.setText("save structures");
        saveButton.setOnClickListener((view)->{
            SaveStructures.saveStructureFile(context);
        });
        linearLayout.addView(saveButton);
        Button deleteButton = new Button(context);
        deleteButton.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        deleteButton.setText("delete structures");
        deleteButton.setOnClickListener((view)->{
            SaveStructures.deleteStructuresFile(context);
        });
        linearLayout.addView(deleteButton);
    }

    private void testCustomListView(){
        ArrayList<String> array = new ArrayList<>(List.of(
                "option1",
                "option2",
                "option3"
        ));
        CustomListView customListView = new CustomListView(ColorPalette.textPurple, context);
        customListView.setArray(array);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(400, 400);
        customListView.getView().setLayoutParams(param);
        linearLayout.addView(customListView.getView());
    }

    private void testAnimatedToggle(){
        ToggleView toggleView = new ToggleView(context, "keyboard mode", "enter", "next",
                450, 150, (isLeft -> {MainActivity.log("highlight is left: " + isLeft);}));
    }


    private void testRoundRectBorder() {
        RoundRectBorder roundRectBorder = new RoundRectBorder(context);
        roundRectBorder.setLayoutParams(new LinearLayout.LayoutParams(400, 200));
        linearLayout.addView(roundRectBorder);
    }


    private void testCustomMeasureLayouts() {
        RelativeElementLayout relative = new RelativeElementLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
        lp.leftMargin = 10;
        lp.topMargin = 10;
        lp.rightMargin = 10;
        lp.bottomMargin = 10;
        relative.getView().setLayoutParams(lp);
        relative.getView().setPadding(10, 10, 10, 10);
        relative.getView().setBackgroundColor(Color.BLUE);
        linearLayout.addView(relative.getView());
        TextElement textElement = new TextElement(context, "hello");
        textElement.getView().setBackgroundColor(Color.YELLOW);

        TextElement textElement2 = new TextElement(context, "hello");
        textElement2.getView().setBackgroundColor(Color.CYAN);

        BasicElement basicElement = new BasicElement(context);
        basicElement.getView().setBackgroundColor(Color.RED);


        relative.addWithParam(textElement, -2,250).alignParentLeft();
        relative.addWithParam(textElement2, -2,-2).rightOf(textElement).alignParentBottom();
        relative.addWithParam(basicElement, -1, 40);
        //relative.addWithParam(basicElement, -2, 40).matchWidth().matchHeight();

        MainActivity.log("textElement1, id: " + textElement.getView().getId() + ", " + describeLayoutRules(((RelativeLayout.LayoutParams) textElement.getLayoutParams())));
        MainActivity.log("textElement2, id: " + textElement2.getView().getId() + ", " + describeLayoutRules(((RelativeLayout.LayoutParams) textElement2.getLayoutParams())));
    }

    public String describeLayoutRules(RelativeLayout.LayoutParams params) {
        int[] rules = params.getRules();
        StringBuilder description = new StringBuilder("Rules applied: ");

        if (rules[RelativeLayout.LEFT_OF] != 0) {
            description.append("left of element ID ").append(rules[RelativeLayout.LEFT_OF]).append(", ");
        }
        if (rules[RelativeLayout.RIGHT_OF] != 0) {
            description.append("right of element ID ").append(rules[RelativeLayout.RIGHT_OF]).append(", ");
        }
        if (rules[RelativeLayout.ABOVE] != 0) {
            description.append("above element ID ").append(rules[RelativeLayout.ABOVE]).append(", ");
        }
        if (rules[RelativeLayout.BELOW] != 0) {
            description.append("below element ID ").append(rules[RelativeLayout.BELOW]).append(", ");
        }
        if (rules[RelativeLayout.ALIGN_BASELINE] != 0) {
            description.append("align baseline with element ID ").append(rules[RelativeLayout.ALIGN_BASELINE]).append(", ");
        }
        if (rules[RelativeLayout.ALIGN_LEFT] != 0) {
            description.append("align left with element ID ").append(rules[RelativeLayout.ALIGN_LEFT]).append(", ");
        }
        if (rules[RelativeLayout.ALIGN_TOP] != 0) {
            description.append("align top with element ID ").append(rules[RelativeLayout.ALIGN_TOP]).append(", ");
        }
        if (rules[RelativeLayout.ALIGN_RIGHT] != 0) {
            description.append("align right with element ID ").append(rules[RelativeLayout.ALIGN_RIGHT]).append(", ");
        }
        if (rules[RelativeLayout.ALIGN_BOTTOM] != 0) {
            description.append("align bottom with element ID ").append(rules[RelativeLayout.ALIGN_BOTTOM]).append(", ");
        }
        if (rules[RelativeLayout.ALIGN_PARENT_LEFT] == RelativeLayout.TRUE) {
            description.append("align parent left, ");
        }
        if (rules[RelativeLayout.ALIGN_PARENT_TOP] == RelativeLayout.TRUE) {
            description.append("align parent top, ");
        }
        if (rules[RelativeLayout.ALIGN_PARENT_RIGHT] == RelativeLayout.TRUE) {
            description.append("align parent right, ");
        }
        if (rules[RelativeLayout.ALIGN_PARENT_BOTTOM] == RelativeLayout.TRUE) {
            description.append("align parent bottom, ");
        }
        if (rules[RelativeLayout.CENTER_IN_PARENT] == RelativeLayout.TRUE) {
            description.append("center in parent, ");
        }
        if (rules[RelativeLayout.CENTER_HORIZONTAL] == RelativeLayout.TRUE) {
            description.append("center horizontally, ");
        }
        if (rules[RelativeLayout.CENTER_VERTICAL] == RelativeLayout.TRUE) {
            description.append("center vertically, ");
        }

        // Remove trailing comma and space if any rules were added
        if (description.length() > 14) {
            description.setLength(description.length() - 2);
        } else {
            description.append("none");
        }

        return description.toString();
    }








}
