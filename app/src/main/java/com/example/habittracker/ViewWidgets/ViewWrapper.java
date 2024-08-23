package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.habittracker.Algorithms.HandleDeletedValues;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.StaticStateManagers.DeleteValueManager;
import com.example.habittracker.ViewLibrary.AbstractBasicElement;
import com.example.habittracker.ViewLibrary.RelativeElementLayout;

public class ViewWrapper {
    private Context context;
    private LinLayout nameLayout;
    private TextView nameTextView;
    private View view;
    private EditText invisibleEditText;
    private FillLayout borderLayout;
    private RelativeElementLayout borderLayoutRelative;
    private LinLayout widgetLayout;
    private RoundRectBorder borderView;
    private LinearLayout checkBoxLayout;
    private CheckBox checkBox;

    public ViewWrapper(Context context){
        this.context = context;
        nameLayout = createNameLayout(context);
        checkBoxLayout = createCheckBoxLayout(context, nameLayout);

//        borderLayout = new FillLayout(context);
//        borderLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
//        nameLayout.addView(borderLayout);

        borderLayoutRelative = new RelativeElementLayout(context);
        borderLayoutRelative.getView().setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        nameLayout.add(borderLayoutRelative.getView());


        checkBox = createCheckBox(context);
        
        widgetLayout = createWidgetLayout(context, borderLayoutRelative);
        borderView = createBorder(context, borderLayoutRelative);
        createInvisibleEditText();
    }

    private static LinearLayout createCheckBoxLayout(Context context, LinLayout nameLayout) {
        LinearLayout checkBoxLayout = new LinearLayout(context);
        checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
        nameLayout.add(checkBoxLayout);
        return checkBoxLayout;
    }

    public void showCheckBox(checkBoxListener checkBoxListener){
        checkBoxLayout.addView(checkBox);
        checkBox.setOnClickListener((view)->{
            boolean isChecked = checkBox.isChecked();
            checkBoxListener.onCheck(isChecked);
        });
    }


    public interface checkBoxListener{
        public void onCheck(boolean isChecked);
    }

    public void hideCheckBox(){
        checkBoxLayout.removeView(checkBox);
    }

    private static CheckBox createCheckBox(Context context) {
        CheckBox checkBox = new CheckBox(context);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(100 , 100);
        layoutParam.gravity = Gravity.CENTER_VERTICAL;
        checkBox.setLayoutParams(layoutParam);
        checkBox.setOnClickListener((view)->{
            throw new RuntimeException();
        });

        return checkBox;
    }

    public void showBorderView(){
        borderLayout.setFillerView(borderView);
    }

    public void hideBorderView(){
        borderLayout.removeFillerView();
    }

    private static RoundRectBorder createBorder(Context context, RelativeElementLayout relativeElementLayout){
        RoundRectBorder roundRectBorder = new RoundRectBorder(context);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(-1, -1);
        roundRectBorder.setLayoutParams(layoutParam);
        //borderLayout.setFillerView(roundRectBorder);
        return roundRectBorder;
    }

    private static LinLayout createNameLayout(Context context){
        LinLayout nameLayout = new LinLayout(context);
        nameLayout.getView().setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        ((LinearLayout) nameLayout.getView()).setOrientation(LinearLayout.VERTICAL);
        return nameLayout;
    }

    private static LinLayout createWidgetLayout(Context context, RelativeElementLayout relativeElementLayout){
        LinLayout widgetLayout = new LinLayout(context);
        //LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(-2, -2);
        //widgetLayout.getView().setLayoutParams(layoutParam);
        //widgetLayout.setOrientation(LinearLayout.VERTICAL);

        //borderLayout.setChildView(widgetLayout.getView());
        relativeElementLayout.addWithParam(new AbstractBasicElement() {
            @Override
            public View getView() {
                return widgetLayout.getView();
            }
        }, -2, -2).alignAllParentSides();

        return widgetLayout;
    }

    public void disable(){
        widgetLayout.disableViewsInside();
    }

    public void enable(){
        widgetLayout.enableViewsInside();
    }

    public void setName(String name){
        if(name == null)
            throw new RuntimeException();
        if(nameTextView != null)
            throw new RuntimeException();

        nameTextView = new TextView(context);
        nameTextView.setText(name);

        nameLayout.add(nameTextView, 0);

        int margin = GLib.dpToPx(20);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.setMargins(40, 0, 0, 10);
        layoutParams.gravity = Gravity.LEFT;
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        nameTextView.setLayoutParams(layoutParams);


        nameTextView.setText(name);
    }

    public void createInvisibleEditText(){

    }

    public EditText getInvisibleEditText(){
        return invisibleEditText;
    }

    public LinLayout getLinLayout(){
        return nameLayout;
    }

    public void setChildView(View view){
        if(view == null)
            throw new RuntimeException();
        this.view = view;
        widgetLayout.add(view);
    }

    public View getView(){
        return nameLayout.getView();
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
