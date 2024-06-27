package com.example.habittracker.ViewWidgets;

import static com.example.habittracker.ViewLibrary.MeasureFunctions.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;



import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelativeLayoutParam;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelativeLayoutParam.*;
import com.example.habittracker.defaultImportPackage.ArrayList;

import java.util.HashMap;

public class RelativeLayoutNewMeasure extends RelativeLayout {
    private HashMap<View, MutablePosition> mutablePositions = new HashMap<>();
    private ArrayList<View> notMeasured = new ArrayList<>();
    private ArrayList<View> anchoredViews = new ArrayList<>();
    private ArrayList<View> children = new ArrayList<>();
    private int[] bordersFromConstraints = new int[4];
    public RelativeLayoutNewMeasure(Context context) {
        super(context);
    }

    public RelativeLayoutNewMeasure(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelativeLayoutNewMeasure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RelativeLayoutNewMeasure(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        mutablePositions.put(child, new MutablePosition());
        children.add(child);
        if(mutablePositions.size() != getChildCount() || children.size() != getChildCount()){
            throw new RuntimeException();
        }
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        mutablePositions.remove(child);
        children.remove(child);
        if(mutablePositions.size() != getChildCount() || children.size() != getChildCount()){
            throw new RuntimeException();
        }
    }

    public void resetOnMeasureDataStructures(){
        for(MutablePosition mutablePosition: mutablePositions.values())
            mutablePosition.reset();
        notMeasured.clear();
        notMeasured.addAll(children);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int layoutSpecWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int layoutSpecHeightSize = MeasureSpec.getSize(heightMeasureSpec);




        int maxChildWidth = -3;
        int maxChildHeight = -3;

        for(int measureIteration = 0; measureIteration < 2; measureIteration++){
            boolean measuredAtLeastOneView = true;
            resetOnMeasureDataStructures();
            while(measuredAtLeastOneView && notMeasured.size() != 0){
                measuredAtLeastOneView = false;
                for(View view: children){
                    RelativeLayoutParam lp = (RelativeLayoutParam) view.getLayoutParams();
                    ArrayList<View> anchorViews = lp.getAnchorViews();
                    if(children.containsAny(anchorViews))
                        continue;
                    measuredAtLeastOneView = true;
                    notMeasured.remove(view);

                    boolean[] constrained = new boolean[4];

                    for(int i: bordersFromConstraints)
                        i = 0;



                    Rule[] rules = lp.getRuleForEachSide();
//                for(int i = 0; i < rules.length; i++){
//                    Rule rule = rules[i];
//                    View anchorView = rule.getView();
//                    bordersFromConstraints[i] = getPositionBorderFromDimensionIndex(anchorView, i);
//                }
                    MarginLayoutParams currentAnchorLp;
                    View currentAnchorView;
                    //left
//                int leftBorder = getPositionBorderFromDimensionIndex(rules[0].getView(), rules[0].getCode());
                    int leftBorder = 0;

                    if(rules[0] != null) {
                        currentAnchorView = rules[0].getView();
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[0].isRightOf())
                            leftBorder = mutablePositions.get(currentAnchorView).x + view.getMeasuredWidth() + currentAnchorLp.rightMargin;// + lp.leftMargin;
                        else //align left
                            leftBorder = mutablePositions.get(currentAnchorView).x;
                    }
                    //top
                    int topBorder = 0;

                    if(rules[1] != null){
                        currentAnchorView = rules[1].getView();
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[1].isBelow())
                            topBorder = mutablePositions.get(currentAnchorView).y + view.getMeasuredHeight() + currentAnchorLp.bottomMargin;// + lp.topMargin;
                        else //align top
                            topBorder = mutablePositions.get(currentAnchorView).y;
                    }
                    //right
                    int rightBorder = layoutSpecWidthSize;

                    if(rules[2] != null){
                        currentAnchorView = rules[2].getView();
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[2].isLeftOf())
                            rightBorder = mutablePositions.get(currentAnchorView).x - currentAnchorLp.leftMargin;// - lp.rightMargin;
                        else //align right
                            rightBorder = mutablePositions.get(currentAnchorView).x + view.getMeasuredWidth();
                    }
                    //bottom
                    int bottomBorder = layoutSpecHeightSize;

                    if(rules[3] != null) {
                        currentAnchorView = rules[3].getView();
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[3].isAbove())
                            bottomBorder = mutablePositions.get(currentAnchorView).y - currentAnchorLp.topMargin;// - lp.bottomMargin;
                        else //align bottom
                            bottomBorder = mutablePositions.get(currentAnchorView).y + view.getMeasuredHeight();
                    }
                    int measureMaxWidth = rightBorder - leftBorder;
                    int measureMaxHeight = bottomBorder - topBorder;

                    if(measureIteration == 0){
                        measureIgnoreMatch(view, lp.width, measureMaxWidth, lp.height, measureMaxHeight);
                        maxChildWidth = Math.max(maxChildWidth, getMeasuredWidthAndMargin(view));
                        maxChildHeight = Math.max(maxChildHeight, getMeasuredHeightAndMargin(view));
                    }else{
                        customMeasure(view, lp.width, measureMaxWidth, lp.height, measureMaxHeight);
                    }


                    MutablePosition mutablePosition = mutablePositions.get(view);
                    mutablePosition.x = leftBorder + lp.leftMargin;
                    mutablePosition.y = topBorder + lp.bottomMargin;

                }
            }
        }








    }

    public int getSizeWithMarginFromDimensionIndex(View view, int dimensionIndex){
        if(dimensionIndex == 0 || dimensionIndex == 2){
            return getMeasuredWidthAndMargin(view);
        }
        if(dimensionIndex == 1 || dimensionIndex == 3){
            return getMeasuredHeightAndMargin(view);
        }
        throw new RuntimeException();
    }

    public int getPositionBorderFromDimensionIndex(View view, int dimensionIndex){
        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
        MutablePosition mutablePosition = mutablePositions.get(view);
        if(dimensionIndex == 0){
            return mutablePosition.x - lp.leftMargin;
        }
        if(dimensionIndex == 1){
            return mutablePosition.y - lp.topMargin;
        }
        if(dimensionIndex == 3){
            return mutablePosition.x + view.getMeasuredWidth() + lp.rightMargin;
        }
        if(dimensionIndex == 4){
            return mutablePosition.y + view.getMeasuredHeight() + lp.bottomMargin;
        }
        throw new RuntimeException();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    public static class MutablePosition {
        public int x = -3;
        public int y = -3;
        public MutablePosition(){

        }

        public void reset(){
            x = -3;
            y = -3;
        }
    }
}
