package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.Rectangle;
import com.example.habittracker.ViewLibrary.MatchParams;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.LayoutMeasure;
import com.example.habittracker.ViewLibrary.ScrollElements.ScrollElement;

public class LockableScrollView extends ScrollView {



    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = true;
    private float manualScrollCounter = 0.0f;

    public LockableScrollView(Context context) {
        super(context);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollingEnabled(boolean enabled) {
        System.out.println("setting scrollable: " + enabled);
        mScrollable = enabled;
    }

    public boolean is_child_larger(){
        View child = this.getChildAt(0);
        if(child.getHeight() > this.getHeight())
            return true;
        return false;
    }

    public void scroll_down(float amount){
        manualScrollCounter = manualScrollCounter + amount;
        int intAmount = (int)manualScrollCounter;
        float difference = manualScrollCounter - intAmount;
        manualScrollCounter = difference;
        if(!is_child_larger())
            return;
        this.scrollBy(0, intAmount);
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                return mScrollable && super.onTouchEvent(ev);
            default:
                return super.onTouchEvent(ev);
        }
    }

    public void scrollToChildPublic(View view){
        Rectangle childRectangleScreen = getScreenRect(view);
        Rectangle scrollRectangleScreen = getScreenRect(this);
        Rectangle scrollParentRectangleScreen = getScreenRect((View)this.getParent());
        Rectangle childRelRectangle = getRelativeRectangle(scrollRectangleScreen, childRectangleScreen, getScrollY());
        MainActivity.log("child relative to scroll: " + childRelRectangle);
        MainActivity.log("scroll rect: " + scrollRectangleScreen);
        MainActivity.log("current scroll: " + this.getScrollY());
        Rect stupidRect = getStupidRect(childRelRectangle);
        int scrollAmount = computeScrollDeltaToGetChildRectOnScreen(stupidRect);
        MainActivity.log("scroll amount for view: " + scrollAmount);
        scrollBy(0, scrollAmount);
    }

    public Rectangle getScreenRect(View view){
        int[] screenOut = new int[2];
        view.getLocationOnScreen(screenOut);
        return new Rectangle(screenOut[0], screenOut[1], view.getWidth(), view.getHeight());
    }

    public Rectangle getRelativeRectangle(Rectangle layout, Rectangle view, int scroll){
        return new Rectangle(view.getX() - layout.getX(), view.getY() - layout.getY() + scroll, view.getWidth(), view.getHeight());
    }

    public Rect getStupidRect(Rectangle rectangle){
        return new Rect(rectangle.getX(), rectangle.getY(),
                rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        MainActivity.log("screen top and bottom before adding fading edge: " + screenTop + ", " + screenBottom);
        int fadingEdge = getVerticalFadingEdgeLength();
        int bufferFromEdge = height/5;
        // leave room for top fading edge as long as rect isn't at very top
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        int scrollYDelta = 0;


        if (rect.bottom > screenBottom - bufferFromEdge && rect.top > screenTop + bufferFromEdge) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).
            if (rect.height() > height) {
                // just enough to get screen size chunk on
                MainActivity.log("scroll amount result: (rect.top - (screenTop + bufferFromEdge)" +
                        rect.top +", " + screenTop +", " + bufferFromEdge);
                scrollYDelta += (rect.top - (screenTop + bufferFromEdge));
            } else {
                // get entire rect at bottom of screen
                MainActivity.log("scroll amount result: (rect.bottom - (screenBottom - bufferFromEdge)" +
                        rect.bottom +", " + screenBottom +", " + bufferFromEdge);
                scrollYDelta += (rect.bottom - (screenBottom - bufferFromEdge));
            }
            // make sure we aren't scrolling beyond the end of our content
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);
        } else if (rect.top < screenTop + bufferFromEdge && rect.bottom < screenBottom - bufferFromEdge) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).
            if (rect.height() > height) {
                // screen size chunk
                MainActivity.log("scroll amount result: ((screenBottom - bufferFromEdge) - rect.bottom" +
                        screenBottom +", " + bufferFromEdge +", " + rect.bottom);
                scrollYDelta -= ((screenBottom - bufferFromEdge) - rect.bottom);
            } else {
                // entire rect at top
                MainActivity.log("scroll amount result: ((screenTop + bufferFromEdge) - rect.top)" +
                        screenTop +", " + bufferFromEdge +", " + rect.top);
                scrollYDelta -= ((screenTop + bufferFromEdge) - rect.top);
            }
            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }else{
//            MainActivity.log("scroll amount result: deemed to already been shown scroll top/bot: " +
//                    screenTop + ", " + screenBottom + ", view top/bot: " +
//                    rect.top + ", " + rect.bottom);
        }
        return scrollYDelta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void remeasure(int widthMeasureSpec, int heightMeasureSpec){

//        MainActivity.log("super rel onMeasure width mode: " +
//                MeasureSpec.getMode(widthMeasureSpec) + ", size: " + MeasureSpec.getSize(widthMeasureSpec) + "\n" +
//                MeasureSpec.getMode(heightMeasureSpec) + ", size: " + MeasureSpec.getSize(heightMeasureSpec));

        int measuredWidth = super.getMeasuredWidth();
        int measuredHeight = super.getMeasuredHeight();

        //MainActivity.log("on measure scroll num children: " + getChildCount());
        //MainActivity.log("on measure scroll id: " + getId());


//        MainActivity.log("measured size: " + measuredWidth + ", " + measuredHeight);

        for(int i = 0; i < super.getChildCount(); i++) {
            View view = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            //MainActivity.log("scroll view with lp: " + lp.getClass().getName());
            if(!(lp instanceof ScrollElement.ScrollParam matchParams))
                continue;
            //MainActivity.log("found one view with matchParams in scroll");

            int childMeasuredWidth = view.getMeasuredWidth();
            int childMeasuredHeight = view.getMeasuredHeight();
            boolean reMeasure = false;
            if (matchParams.matchWidth) {
                reMeasure = true;
                childMeasuredWidth = LayoutMeasure.getMeasureSizeMatchParent(measuredWidth,
                        lp.leftMargin, lp.rightMargin, getPaddingLeft(), getPaddingRight());
            }
            if (reMeasure) {
                MainActivity.log("remeasured element");
                view.measure(MeasureSpec.makeMeasureSpec(childMeasuredWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childMeasuredHeight, MeasureSpec.EXACTLY));
            }

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return mScrollable && super.onInterceptTouchEvent(ev);
    }

}