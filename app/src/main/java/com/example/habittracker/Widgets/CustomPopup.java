package com.example.habittracker.Widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.habittracker.GLib;
import com.example.habittracker.R;
import com.example.habittracker.RelParam;
import com.example.habittracker.SelectionView;

import java.util.ArrayList;


public class CustomPopup {
    private Context context;
    private PopupWindow popupWindow;
    private SelectionView optionsSelectionView;
    private ArrayAdapter<String> adapter;
    SelectionView.OnSelected onItemSelected;
    RelativeLayout nameLayout;
    SelectionView name;
    ImageView backIcon;

    boolean backEnabled = false;
    Runnable onBack;


    public CustomPopup(Context context, String title, ArrayList<String> options, SelectionView.OnSelected onItemSelected, Runnable onBack, Runnable onNothingSelected) {
        this.context = context;
        // Inflate the popup Layout

        this.onBack = onBack;

        this.onItemSelected = onItemSelected;

        RelativeLayout colorBackground = new RelativeLayout(context);
        colorBackground.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));

        colorBackground.setBackground(context.getDrawable(R.drawable.background_of_card));

        //linearlayout, params: linearlayout (idk what the popup parent layout is)
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        colorBackground.addView(linearLayout);
        //linearLayout.setBackgroundColor(context.getColor(R.color.gray3));
        linearLayout.setBackgroundColor(context.getColor(R.color.dark3));



        //relative layout with LinearLayout params added to linearLayout
        nameLayout = new RelativeLayout(context);
        nameLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout.addView(nameLayout);

        //name with RelativeLayout params added to nameLayout
        name = new SelectionView(context, new String[]{title}, (stringValue, position) -> {titlePressed();});;
        name.getView().setLayoutParams(RelParam.alignLeft(-2, -2));
        nameLayout.addView(name.getView());

        backIcon = (ImageView) GLib.inflate(R.layout.back_image_view);
        backIcon.setLayoutParams(RelParam.alignLeft(100,100));






        //options with LinearLayout params added to linearLayout
        optionsSelectionView = new SelectionView(context, options, (value, index)->{onSelection(value, index);});
        optionsSelectionView.getView().setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        linearLayout.addView(optionsSelectionView.getView());

        // Create the popup window
        popupWindow = new PopupWindow(colorBackground, 600, 800);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                onNothingSelected.run();
            }
        });
        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize the ListView and Adapter


    }
    boolean closed = false;

    public void close(){
        closed = true;
        popupWindow.dismiss();
    }

    public void onSelection(String value, int index){
        if(!closed)
            onItemSelected.onSelected(value, index);
    }

    public void titlePressed(){
        if(backEnabled && !closed)
            onBack.run();
    }

    public void setText(String title, ArrayList<String> options){
        name.setText(new String[]{title});
        optionsSelectionView.setText(options);
    }

    public void enableBack(){
        backEnabled = true;
        nameLayout.addView(backIcon);

        backIcon.post(()->{
            TextView textView = (TextView) name.getChild(0);
            int startOfTextView = textView.getLeft(); // Relative to parent
            int startOfTextInTextView = startOfTextView + textView.getPaddingLeft(); // Assuming text starts after padding
            backIcon.setLayoutParams(RelParam.alignLeft(100,100).margins(startOfTextInTextView - backIcon.getDrawable().getMinimumWidth() - 10, 0, 0, 0));

        });
    }

    public void disableBack(){
        backEnabled = false;
        nameLayout.removeView(backIcon);
    }



    public void showPopupWindow(View anchorView) {
        // Set location of popup (for example, top left corner)
        // Can adjust the x, y offsets as needed
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    public Drawable scaleDrawable(Drawable drawable, Resources resources, int width, int height) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), width, height, true);
        } else {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return new BitmapDrawable(resources, bitmap);
    }
}
