package com.example.habittracker.Layouts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;

public class ViewWrapper {
    private Context context;
    private LinLayout linearLayout;
    private TextView nameTextView;
    private View view;

    public ViewWrapper(Context context){
        this.context = context;
        linearLayout = new LinLayout(context);
        linearLayout.getView().setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        ((LinearLayout)linearLayout.getView()).setOrientation(LinearLayout.VERTICAL);
    }
    public void setName(String name){
        if(name == null)
            throw new RuntimeException();
        if(nameTextView != null)
            throw new RuntimeException();

        nameTextView = new TextView(context);
        nameTextView.setText(name);

        linearLayout.add(nameTextView, 0);

        int margin = GLib.dpToPx(20);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(40, 0, 0, 10);
        layoutParams.gravity = Gravity.LEFT;
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameTextView.setLayoutParams(layoutParams);


        nameTextView.setText(name);
    }

    public LinLayout getLinLayout(){
        return linearLayout;
    }

    public void setChildView(View view){
        if(view == null)
            throw new RuntimeException();
        this.view = view;
        linearLayout.add(view);
    }

    public View getView(){
        return linearLayout.getView();
    }

    public void setNameRed(){
        int colorRed = Color.RED;
        if(nameTextView != null)
            nameTextView.setTextColor(ColorPalette.redText);
    }

    public void resetNameColor(){
        int colorWhite = Color.WHITE;
        if(nameTextView != null)
            nameTextView.setTextColor(ColorPalette.text);
    }
}
