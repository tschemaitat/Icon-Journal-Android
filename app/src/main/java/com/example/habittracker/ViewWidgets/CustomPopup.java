package com.example.habittracker.ViewWidgets;

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

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.RelParam;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.ViewWidgets.SelectionView;

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
        nameLayout.setBackgroundColor(context.getColor(R.color.dark2));
        linearLayout.addView(nameLayout);

        //name with RelativeLayout params added to nameLayout
        name = new SelectionView(context, new String[]{title}, (stringValue, position, key) -> {titlePressed();});;
        name.getView().setLayoutParams(RelParam.alignLeft(-2, -2));
        nameLayout.addView(name.getView());

        backIcon = (ImageView) GLib.inflate(R.layout.back_image_view);
        backIcon.setLayoutParams(RelParam.alignLeft(100,100));






        //options with LinearLayout params added to linearLayout
        optionsSelectionView = new SelectionView(context, options.toArray(new String[0]), (value, index, key)->{onSelection(value, index, key);});
        optionsSelectionView.getView().setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        linearLayout.addView(optionsSelectionView.getView());

        // Create the popup window
        popupWindow = new PopupWindow(colorBackground, 600, 800);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(closed){
                    //System.out.println("pop was manually dismissed");
                }else{
                    //System.out.println("pop up was dismissed");
                    onNothingSelected.run();
                }

            }
        });
        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Initialize the ListView and Adapter


    }
    private boolean closed = false;

    public void close(){
        //System.out.println("manually closing pop up");
        closed = true;
        popupWindow.dismiss();
    }

    public void onSelection(String value, int index, Object key){
        MainActivity.log("thing selected: " + value + ", " + key);
        //System.out.println("value selected of popup");
        if(!closed)
            onItemSelected.onSelected(value, index, key);
    }

    public void titlePressed(){
        if(backEnabled && !closed)
            onBack.run();
    }

    public void setText(String title, ArrayList<PayloadOption> options){
        //System.out.println("popup: setting text. title: " + title + ", options: " + options);
        name.setText(new String[]{title});
        optionsSelectionView.setText(options);
    }

    public void enableBack(){
        backEnabled = true;
    }

    public void disableBack(){
        backEnabled = false;
    }

    public void addBackIcon(){
        //System.out.println("addBackIcon");
        nameLayout.addView(backIcon);
        TextView textView = (TextView) name.getChild(0);
        int startOfTextView = textView.getLeft(); // Relative to parent
        int startOfTextInTextView = startOfTextView + textView.getPaddingLeft(); // Assuming text starts after padding
        backIcon.setLayoutParams(RelParam.alignLeft(100,100).margins(startOfTextInTextView - backIcon.getDrawable().getMinimumWidth() - 10, 0, 0, 0));
    }

    public void removeBackIcon(){
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
