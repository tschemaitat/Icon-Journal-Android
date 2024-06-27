package com.example.habittracker.ViewWidgets;

import static com.example.habittracker.ViewLibrary.MeasureFunctions.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.habittracker.MainActivity;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class LinearLayoutNewMeasure extends LinearLayout {
    public LinearLayoutNewMeasure(Context context) {
        super(context);
    }

    public LinearLayoutNewMeasure(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutNewMeasure(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinearLayoutNewMeasure(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ArrayList<View> getViews(){
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < getChildCount(); ++i) {
            final View child = getChildAt(i);
            views.add(child);
        }
        return views;
    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        boolean isHorizontal = getOrientation() == LinearLayout.HORIZONTAL;
        int childrenCount = getChildCount();

        int widthSizePadded = widthSize - (getPaddingLeft() + getPaddingRight());
        int heightSizePadded = heightSize - (getPaddingTop() + getPaddingBottom());

        boolean widthChildMatchesParent = false;
        boolean heightChildMatchesParent = false;

        for(int i = 0; i < childrenCount; i++){
            View view = getChildAt(i);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if(lp == null){
                MainActivity.log("layout params null");
            }
            if((lp.height == -1 && !isHorizontal) || (lp.width == -1 && isHorizontal)){
                MainActivity.log("important dimension of view is match parent");
                throw new RuntimeException();
            }
            if(lp.height == -1)
                heightChildMatchesParent = true;
            if(lp.width == -1)
                widthChildMatchesParent = true;
        }
        int maxWidth = Integer.MIN_VALUE;
        int maxHeight = Integer.MIN_VALUE;

        for(int i = 0; i < childrenCount; i++){
            //measure each child ignoring match parent
            //so if a number, exactly with that number
            //if wrap content, then at most with specSize
            View view = getChildAt(i);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            measureIgnoreMatch(view, lp.width, widthSizePadded, lp.height, heightSizePadded);
            maxWidth = Math.max(getMeasuredWidthAndMargin(view), maxWidth);
            maxHeight = Math.max(getMeasuredHeightAndMargin(view), maxHeight);
        }



        if(isHorizontal){
            int totalWidth = 0;


            for(int i = 0; i < childrenCount; i++){
                //measure each child ignoring match parent
                //so if a number, exactly with that number
                //if wrap content, then at most with specSize
                View view = getChildAt(i);
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                customMeasure(view, lp.width, maxWidth, lp.height, maxHeight);
                totalWidth += getMeasuredWidthAndMargin(view);
            }
            setMeasuredDimension(totalWidth, maxHeight);
        }else{
            int totalHeight = 0;


            for(int i = 0; i < childrenCount; i++){
                //measure each child ignoring match parent
                //so if a number, exactly with that number
                //if wrap content, then at most with specSize
                View view = getChildAt(i);
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                customMeasure(view, lp.width, maxWidth, lp.height, maxHeight);
                totalHeight += getMeasuredHeightAndMargin(view);
            }
            setMeasuredDimension(maxWidth, totalHeight);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(getOrientation() == HORIZONTAL){
            horizontalLayout(t, b);
        }else{
            verticalLayout(l, r);
        }
    }



    private void horizontalLayout(int t, int b){
        int childLeft = 0;

        int height = b-t;

        for (View child : getViews()) {
            if (child.getVisibility() == GONE)
                continue;
            MarginLayoutParams lp = ((MarginLayoutParams) child.getLayoutParams());
            int verticalSpace = height - getMeasuredHeightAndMargin(child);
            int verticalShift = 0;

            if(getGravity() == Gravity.TOP){
                verticalShift = 0;
            }else if(getGravity() == Gravity.BOTTOM){
                verticalShift = verticalSpace;
            }else if(getGravity() == Gravity.CENTER || getGravity() == Gravity.CENTER_VERTICAL){
                verticalShift = verticalSpace/2;
            }

            childLeft += lp.leftMargin;

            int left = childLeft;
            int top = lp.topMargin + verticalShift;
            int right = childLeft + child.getMeasuredWidth();
            int bottom = child.getMeasuredHeight() + lp.topMargin + verticalShift;
            child.layout(left, top, right, bottom);

            childLeft += child.getMeasuredWidth() + lp.rightMargin;
        }
    }

    private void verticalLayout(int l, int r){
        int childTop = 0;

        int width = r-l;

        for (View child : getViews()) {
            if (child.getVisibility() == GONE)
                continue;
            MarginLayoutParams lp = ((MarginLayoutParams) child.getLayoutParams());
            int horizontalSpace = width - getMeasuredWidthAndMargin(child);
            int horizontalShift = 0;

            if(getGravity() == Gravity.TOP){
                horizontalShift = 0;
            }else if(getGravity() == Gravity.BOTTOM){
                horizontalShift = horizontalSpace;
            }else if(getGravity() == Gravity.CENTER || getGravity() == Gravity.CENTER_VERTICAL){
                horizontalShift = horizontalSpace/2;
            }
            childTop += lp.topMargin;

            int left = lp.leftMargin + horizontalShift;
            int top = childTop;
            int right = child.getMeasuredWidth() + lp.leftMargin + horizontalShift;
            int bottom = childTop + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);

            childTop += child.getMeasuredHeight() + lp.bottomMargin;
        }
    }


}
