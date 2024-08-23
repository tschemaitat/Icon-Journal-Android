package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewLibrary.MatchParams;
import com.example.habittracker.defaultImportPackage.ArrayList;

//parent (wrap, wrap) //parent has minimum width and height
//if no child, (0, 0)
//if child (200, 400), (200, 400)


//parent.add(child, 400, 200)
//parent.add(child, match_parent, wrap_content)

//parent (wrap content)-> child (400) //the child pushes on the parent, making it bigger

//class ViewLayout extends View
//void measure:
//  for child:
//      child.measure();



public class RelLayoutSuperMeasure extends RelativeLayout {
    private boolean superMeasure = true;
    public RelLayoutSuperMeasure(Context context) {
        super(context);
    }

    public RelLayoutSuperMeasure(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelLayoutSuperMeasure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RelLayoutSuperMeasure(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!superMeasure)
            return;
        MainActivity.log("super rel onMeasure width mode: " +
                MeasureSpec.getMode(widthMeasureSpec) + ", size: " + MeasureSpec.getSize(widthMeasureSpec) + "\n" +
                MeasureSpec.getMode(heightMeasureSpec) + ", size: " + MeasureSpec.getSize(heightMeasureSpec));

        int measuredWidth = super.getMeasuredWidth();
        int measuredHeight = super.getMeasuredHeight();


        MainActivity.log("measured size: " + measuredWidth + ", " + measuredHeight);

        for(int i = 0; i < super.getChildCount(); i++){
            View view = getChildAt(i);
            MatchParams matchParams = (MatchParams) view.getLayoutParams();
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            int childMeasuredWidth = view.getMeasuredWidth();
            int childMeasuredHeight = view.getMeasuredHeight();
            boolean reMeasure = false;
            if(matchParams.getMatchWidth()){
                reMeasure = true;
                childMeasuredWidth = LayoutMeasure.getMeasureSizeMatchParent(measuredWidth,
                        lp.leftMargin, lp.rightMargin, getPaddingLeft(), getPaddingRight());
            }
            if(matchParams.getMatchHeight()){
                reMeasure = true;
                childMeasuredWidth = LayoutMeasure.getMeasureSizeMatchParent(measuredHeight,
                        lp.topMargin, lp.bottomMargin, getPaddingTop(), getPaddingBottom());
            }
            if(reMeasure){
                MainActivity.log("remeasured element");
                view.measure(MeasureSpec.makeMeasureSpec(childMeasuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childMeasuredHeight, MeasureSpec.EXACTLY));
            }

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!superMeasure)
            return;
        MainActivity.log("super rel on layout");

        int measuredWidth = super.getMeasuredWidth();
        int measuredHeight = super.getMeasuredHeight();

        for(int i = 0; i < super.getChildCount(); i++){
            View view = getChildAt(i);
            MatchParams matchParams = (MatchParams) view.getLayoutParams();
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            int left = view.getLeft();
            int top = view.getTop();
            int right = view.getRight();
            int bottom = view.getBottom();
            boolean reMeasure = false;
            if(matchParams.getMatchWidth()){
                MainActivity.log("match width, from: (" + left + ", " + right + ")");
                reMeasure = true;
                left = getPaddingLeft() + lp.leftMargin;
                right = measuredWidth - (getPaddingRight() + lp.rightMargin);
                MainActivity.log("to : (" + left + ", " + right + ")");
            }
            if(matchParams.getMatchHeight()){
                top = getPaddingTop() + lp.topMargin;
                bottom = measuredHeight - (getPaddingBottom() + lp.bottomMargin);
            }
            if(reMeasure){
                MainActivity.log("relayouted");
                view.layout(left, top, right, bottom);
            }


        }
    }
}
