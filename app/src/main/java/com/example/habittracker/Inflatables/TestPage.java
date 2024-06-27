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
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelLP;
import com.example.habittracker.ViewLibrary.TextElement;
import com.example.habittracker.ViewWidgets.ListViewPackage.CustomListView;
import com.example.habittracker.ViewWidgets.ListViewPackage.DynamicListView;
import com.example.habittracker.ViewWidgets.RelativeLayoutNewMeasure;
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
        //addSaveAndDeleteButtons();
        //testDynamicListView();
        testCustomMeasureLayouts();
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
        DynamicListView dynamicListView = new DynamicListView(context, payloadOptions, (cachedString, index, payload)->{
            MainActivity.log("clicked: " + payload);
        }, null);
        linearLayout.addView(dynamicListView.getView());
        dynamicListView.getView().setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
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
        RelativeLayoutNewMeasure relative = new RelativeLayoutNewMeasure(context);
        relative.setBackgroundColor(Color.BLUE);
        linearLayout.addView(relative);
        TextElement textElement = new TextElement(context, "hello");
        textElement.getView().setBackgroundColor(Color.YELLOW);

        TextElement textElement2 = new TextElement(context, "hello");
        textElement2.getView().setBackgroundColor(Color.CYAN);


        relative.addWithParam(textElement.getView(), -2,-2).put(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relative.addWithParam(textElement2.getView(), -2,-2).put(RelativeLayout.RIGHT_OF, textElement.getView());
    }








}
