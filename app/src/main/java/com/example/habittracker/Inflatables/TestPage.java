package com.example.habittracker.Inflatables;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.DropDownPageFactory;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.ViewWidgets.AnimatedHighlightView;
import com.example.habittracker.ViewWidgets.CustomListView;
import com.example.habittracker.ViewWidgets.RoundRectBorder;
import com.example.habittracker.ViewWidgets.ToggleView;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.ViewWidgets.CustomPopup;

import java.util.ArrayList;
import java.util.Arrays;
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
        testRoundRectBorder();
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
                450, 150, linearLayout, (isLeft -> {MainActivity.log("highlight is left: " + isLeft);}));
    }


    private void testRoundRectBorder() {
        RoundRectBorder roundRectBorder = new RoundRectBorder(context);
        roundRectBorder.setLayoutParams(new LinearLayout.LayoutParams(400, 200));
        linearLayout.addView(roundRectBorder);
    }










}
