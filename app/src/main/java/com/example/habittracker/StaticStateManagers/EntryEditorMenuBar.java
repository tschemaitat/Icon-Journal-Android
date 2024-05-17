package com.example.habittracker.StaticStateManagers;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.ViewWidgets.OnDeleteValueCancelAndConfirm;
import com.example.habittracker.Widgets.GroupWidget;

public class EntryEditorMenuBar {
    private static EntryEditorMenuBar manager;
    public static void make(LinearLayout parent, ImageButton hideButton, Context context){
        manager = new EntryEditorMenuBar(parent, hideButton, context);
    }

    public static EntryEditorMenuBar getManager(){
        return manager;
    }

    private LinearLayout parent;
    private LinearLayout entryEditorMenuBarLayout;
    private Context context;
    private Button deleteButton;
    private ImageButton hideButton;
    private boolean shown = false;
    private OnDeleteValueCancelAndConfirm deleteAndConfirm;

    private DeleteValueManager deleteValueManager;

    private boolean animateShown = true;



    private EntryEditorMenuBar(LinearLayout parent, ImageButton hideButton, Context context) {
        this.parent = parent;
        this.context = context;
        this.hideButton = hideButton;
        init();
    }

    private void init(){
        hideButton.setOnClickListener((view -> {
            if(animateShown){
                parent.setVisibility(View.GONE);
                //animateUpAndAway(parent, true);
            }else{
                parent.setVisibility(View.VISIBLE);
            }
            animateShown = !animateShown;
        }));
        entryEditorMenuBarLayout = makeMenuLinearLayout(context);
        deleteButton = makeDeleteButton(context, entryEditorMenuBarLayout);

        KeyBoardActionManager.makeNewManager(context, entryEditorMenuBarLayout);
    }

    private void animateView(View view){
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        float yStart = view.getY();
        int height = view.getHeight();

        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            float currentY = yStart - animatedValue * height;
            view.setY(currentY);
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.forceLayout();
        });
        animator.start();
    }



    public void animateUpAndAway(final LinearLayout linearLayout, boolean isUp) {
        int fromYDelta = 0;
        int toYDelta = 0;
        if(isUp){
            toYDelta = linearLayout.getHeight() * -1;
        }else{
            fromYDelta = linearLayout.getHeight() * -1;
        }

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                fromYDelta,                 // fromYDelta
                toYDelta); // toYDelta

        animate.setDuration(500);      // animation duration in milliseconds
        animate.setFillAfter(true);    // If true, the animation transformation is applied after the animation is over
        linearLayout.startAnimation(animate);

        animate.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(!isUp)
                    linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isUp)
                    linearLayout.setVisibility(View.GONE);
                //hideButton.invalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    public void show(GroupWidget groupWidget, EntryId entryId){

        parent.addView(entryEditorMenuBarLayout);
        setGroupWidget(groupWidget, entryId);
        shown = true;
    }

    public void hide(){
        parent.removeView(entryEditorMenuBarLayout);
        deleteAndConfirm = null;
        deleteButton.setOnClickListener((view)->{});
        shown = false;
        parent.setVisibility(View.VISIBLE);
        animateShown = true;
    }

    private void setGroupWidget(GroupWidget groupWidget, EntryId entryId){
        deleteAndConfirm = new OnDeleteValueCancelAndConfirm(context, this::onCancel, this::onConfirm);

        deleteButton.setOnClickListener((view)->{
            deleteValueManager = new DeleteValueManager(context, groupWidget, deleteButton, entryId);
            parent.addView(deleteAndConfirm.getView());
        });
        //MainActivity.log("parent child count: " + parent.getChildCount());

        //MainActivity.log("parent child count: " + parent.getChildCount());

    }

    private void onCancel(){
        deleteValueManager.onCancel();
    }

    private void onConfirm(){
        deleteValueManager.onConfirm();
    }

    private static Button makeDeleteButton(Context context, LinearLayout menuBarLayout){
        Button deleteButton = new Button(context);
        deleteButton.setText("delete");
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        menuBarLayout.addView(deleteButton);
        deleteButton.setOnClickListener(view -> {
            throw new RuntimeException();
        });
        return deleteButton;
    }

    private static LinearLayout makeMenuLinearLayout(Context context){
        LinearLayout menuLinearLayout = new LinearLayout(context);
        menuLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        MainActivity.log("adding menu to parent");
        //parent.addView(menuLinearLayout);
        menuLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return menuLinearLayout;
    }

    public View getView() {
        return entryEditorMenuBarLayout;
    }
}
