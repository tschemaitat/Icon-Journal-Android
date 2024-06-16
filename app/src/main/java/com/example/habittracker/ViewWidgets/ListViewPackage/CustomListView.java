package com.example.habittracker.ViewWidgets.ListViewPackage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class CustomListView {
    private Context context;
    private int textViewResource = android.R.layout.simple_list_item_1;
    private int selectorResource = 0;
    private int dividerResource = android.R.drawable.divider_horizontal_bright;
    private Drawable dividerDrawable = new ColorDrawable(Color.DKGRAY);
    private int textColor;
    private ArrayList<String> array;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    public CustomListView(int textColor, Context context){
        this.textColor = textColor;
        this.context = context;
        scrollView = new ScrollView(context);
        linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(-1, -2);
        linearLayout.setLayoutParams(param);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
    }

    public View createDivider(){

        View view = new View(context);
//        Drawable drawable = ContextCompat.getDrawable(context, dividerResource);
//        view.setBackground(drawable);
        view.setBackground(dividerDrawable);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 3);
        view.setLayoutParams(layoutParams);


        return view;
    }

    public void setArray(ArrayList<String> array){
        this.array = (ArrayList<String>) array.clone();
        setTextViews();
    }

    public void setTextViews(){
        linearLayout.removeAllViews();
        for(int i = 0; i < array.size(); i++){
            linearLayout.addView(createTextView(array.get(i)));
            if(i != array.size() - 1)
                linearLayout.addView(createDivider());
        }
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    private Drawable getSelector(){
        return ContextCompat.getDrawable(context, selectorResource);
    }

    private Drawable createSelector() {
        int normalColor = Color.TRANSPARENT; // Normal background color of the view
        int rippleColor = Color.LTGRAY; // Light grey ripple color

        Drawable rippleDrawable = createRippleDrawable(context, rippleColor);
        return rippleDrawable;
    }

    public Drawable createRippleDrawable(Context context, int rippleColor) {
        ColorStateList rippleColorStateList = ColorStateList.valueOf(rippleColor);
        Drawable mask = new ColorDrawable(Color.WHITE);  // The mask color doesn't matter, it just needs to be opaque.

        // Create a RippleDrawable with null content (transparent background) and a mask.
        return new RippleDrawable(rippleColorStateList, null, mask);
    }

    private TextView createTextView(String s){
        TextView textView = (TextView) GLib.inflate(textViewResource);
        textView.setText(s);
        textView.setTextColor(textColor);
        textView.setForeground(createSelector());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.log("text view got clicked");
            }
        });
        return textView;
    }

    public View getView(){
        return scrollView;
    }
}
