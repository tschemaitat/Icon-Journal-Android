package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;

public class ToggleView {
    private Context context;
    private int width;
    private int height;
    private OnToggleListener onToggleListener;


    private RelativeLayout toggleRelativeLayout;
    private TextView toggleTitle;
    private RelativeLayout toggleTextRelLayout;
    private AnimatedHighlightView animatedHighlightView;
    private TextView leftText;
    private TextView rightText;

    public ToggleView(Context context, String title, String left, String right, int width, int height,
                      OnToggleListener onToggleListener){
        this.context = context;
        this.width = width;
        this.height = height;
        this.onToggleListener = onToggleListener;
        init(title, left, right);
    }

    public boolean getIsLeft(){
        return animatedHighlightView.getIsHighlightOnLeft();
    }

    private void init(String title, String left, String right){

        toggleRelativeLayout = makeToggleRelativeLayout(context, width, height);
        toggleTitle = makeToggleTitle(toggleRelativeLayout, context);
        toggleTextRelLayout = makeToggleTextRelLayout(toggleRelativeLayout, toggleTitle, context);
        animatedHighlightView = makeAnimatedHighlightView(toggleTextRelLayout, onToggleListener, context);
        leftText = makeLeftText(toggleTextRelLayout, context, width, left);
        rightText = makeRightText(toggleTextRelLayout, context, width, right);
    }


    private static RelativeLayout makeToggleRelativeLayout(Context context, int width, int height){
        RelativeLayout toggleRelativeLayout = new RelativeLayout(context);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(width, height);
        layoutParam.gravity = Gravity.CENTER_VERTICAL;
        toggleRelativeLayout.setLayoutParams(layoutParam);
        return toggleRelativeLayout;
    }
    private static TextView makeToggleTitle(RelativeLayout toggleRelativeLayout, Context context){
        TextView toggleTitle = new TextView(context);
        toggleTitle.setText("keyboard mode");
        MainActivity.log("title text size: " + toggleTitle.getTextSize());
        toggleTitle.setTextSize(GLib.pxToDp(toggleTitle.getTextSize()) * 0.7f);
        MainActivity.log("title text size: " + toggleTitle.getTextSize());
        RelativeLayout.LayoutParams toggleTitleParam = new RelativeLayout.LayoutParams(-2, -2);
        toggleTitleParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        toggleTitleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        toggleTitle.setId(View.generateViewId());
        toggleRelativeLayout.addView(toggleTitle);
        toggleTitle.setLayoutParams(toggleTitleParam);
        return toggleTitle;
    }
    private static RelativeLayout makeToggleTextRelLayout(RelativeLayout toggleRelativeLayout, TextView toggleTitle, Context context){
        RelativeLayout toggleTextRelativeLayout = new RelativeLayout(context);
        toggleTextRelativeLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams toggleTextRelativeLayoutParam = new RelativeLayout.LayoutParams(-1, -1);
        toggleTextRelativeLayoutParam.addRule(RelativeLayout.BELOW, toggleTitle.getId());
        toggleTextRelativeLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        toggleTextRelativeLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        toggleTextRelativeLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        toggleTextRelativeLayout.setLayoutParams(toggleTextRelativeLayoutParam);
        toggleRelativeLayout.addView(toggleTextRelativeLayout);
        return toggleTextRelativeLayout;
    }
    private static AnimatedHighlightView makeAnimatedHighlightView(RelativeLayout toggleTextRelativeLayout, OnToggleListener onToggleListener, Context context){
        AnimatedHighlightView animatedHighlightView = new AnimatedHighlightView(context, 15);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        animatedHighlightView.setLayoutParams(layoutParams);
        //animatedHighlightView.setPadding(10,10,10,10);
        toggleTextRelativeLayout.addView(animatedHighlightView);
        animatedHighlightView.setOnClickListener((view)->{
            animatedHighlightView.toggleHighlight();
            onToggleListener.onToggle(animatedHighlightView.getIsHighlightOnLeft());
        });
        return animatedHighlightView;
    }
    private static TextView makeLeftText(RelativeLayout toggleTextRelativeLayout, Context context, int width, String left){
        TextView leftText = new TextView(context);
        leftText.setId(View.generateViewId());
        RelativeLayout.LayoutParams leftTextParam = new RelativeLayout.LayoutParams(width/2, -1);
        leftText.setGravity(Gravity.CENTER);
        leftTextParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftText.setLayoutParams(leftTextParam);
        toggleTextRelativeLayout.addView(leftText);
        leftText.setText(left);
        return leftText;
    }
    private static TextView makeRightText(RelativeLayout toggleTextRelativeLayout, Context context, int width, String right){
        TextView rightText = new TextView(context);
        rightText.setId(View.generateViewId());
        RelativeLayout.LayoutParams rightTextParam = new RelativeLayout.LayoutParams(width/2, -1);
        rightText.setGravity(Gravity.CENTER);
        rightTextParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightText.setLayoutParams(rightTextParam);
        toggleTextRelativeLayout.addView(rightText);
        rightText.setText(right);
        return rightText;
    }

    public View getView() {
        return toggleRelativeLayout;
    }

    public interface OnToggleListener{
        void onToggle(boolean isLeft);
    }
}
