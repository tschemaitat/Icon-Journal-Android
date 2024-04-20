package com.example.habittracker.Inflatables;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.ViewWidgets.CustomPopup;

import java.util.ArrayList;
import java.util.Arrays;

public class TestPage implements Inflatable {
    private LinearLayout linearLayout;
    private Context context;

    public TestPage(Context context){
        this.context = context;
        linearLayout = MainActivity.createVerticalLayout();
        linearLayout.setId(R.id.pageLayout);
        //setupTestLayout();
        //testDataTreeValueExport();
        //testNewEditText();
        //testCard();
        //testPopup();
        testLinLayoutButton();
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

    public void testCard(){
        MainActivity.constraintLayout.setBackgroundColor(Color.WHITE);

        CardView cardView = (CardView) GLib.inflate(R.layout.card_view);
        RelativeLayout.LayoutParams cardViewLayoutParams = new RelativeLayout.LayoutParams(200, 400);
        cardViewLayoutParams.setMargins(100, 100, 100, 100);
        cardViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cardView.setLayoutParams(cardViewLayoutParams);
        //cardView.setCardElevation(-10);
        //cardView.setCardBackgroundColor(context.getColor(R.color.gray3));
        //cardView.setCardBackgroundColor(Color.WHITE);
        //cardView.setRadius(20);
        linearLayout.addView(cardView);
        cardView.setCardElevation(0);
        cardView.setBackground(context.getDrawable(R.drawable.background_of_card));
        cardView.setPadding(20, 20, 20, 20);
    }

    public void testLinLayoutButton(){
        LinLayout linLayout = new LinLayout(context);
        linearLayout.addView(linLayout.getView());
        linLayout.addButton((view)->{});
        TextView textView = new TextView(context);
        textView.setText("hello");
        linLayout.add(textView);

    }

    public void testPopup(){
        int blue1 = context.getColor(R.color.blue1);
        int blue2 = context.getColor(R.color.blue2);
        int blue3 = context.getColor(R.color.blue3);
        int blue4 = context.getColor(R.color.blue4);
        int blue5 = context.getColor(R.color.blue5);
        MainActivity.constraintLayout.setBackgroundColor(context.getColor(R.color.dark1));
        //linearLayout.setBackground(new ColorDrawable(Color.WHITE));

        LinLayout layout = new LinLayout(context);
        linearLayout.addView(layout.getView());
        CustomEditText customEditText = new CustomEditText(context);
        layout.add(customEditText.getView());
        CustomPopup customPopup = new CustomPopup(context, "title", new ArrayList<>(Arrays.asList("hi", "hello")), null, null, null);


        linearLayout.post(()->{
            customPopup.showPopupWindow(customEditText.getView());
        });
    }





}
