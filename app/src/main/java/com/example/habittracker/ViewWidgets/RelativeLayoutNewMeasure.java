package com.example.habittracker.ViewWidgets;

import static com.example.habittracker.ViewLibrary.MeasureFunctions.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelLP;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelLP.*;
import com.example.habittracker.defaultImportPackage.ArrayList;

import java.util.HashMap;

public class RelativeLayoutNewMeasure extends ViewGroup {
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

    public RelLP addWithParam(View child, int width, int height){
        super.addView(child);
        RelLP lp = new RelLP(width, height);
        child.setLayoutParams(lp);
        return lp;
    }

    @Override
    public void onViewAdded(View child) {
        MainActivity.log("before super, child added, their param:  " + child.getLayoutParams().getClass());
        super.onViewAdded(child);

        MainActivity.log("child added, their param:  " + child.getLayoutParams().getClass());
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
        int layoutSpecWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int layoutSpecHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        MainActivity.log("on measure children count: " + children.size());
        for(View view: children){
            MainActivity.log("child class: " + view.getClass());
        }


        int maxChildWidth = 0;
        int maxChildHeight = 0;

        int layoutWidth = layoutSpecWidthSize;
        int layoutHeight = layoutSpecHeightSize;



        for(int measureIteration = 0; measureIteration < 2; measureIteration++){
            if(measureIteration == 1){
                if(layoutSpecWidthMode == MeasureSpec.AT_MOST){
                    layoutWidth = Math.min(maxChildWidth, layoutSpecWidthSize);
                }
                if(layoutSpecHeightMode == MeasureSpec.AT_MOST){
                    layoutHeight = Math.min(maxChildHeight, layoutSpecHeightSize);
                }
            }


            boolean measuredAtLeastOneView = true;
            resetOnMeasureDataStructures();
            while(measuredAtLeastOneView){
                MainActivity.log("measuring while loop num child not measured: " + notMeasured.size());
                measuredAtLeastOneView = false;
                for(int notMeasuredIndex = notMeasured.size() - 1; notMeasuredIndex >= 0; notMeasuredIndex--){
                    View view = notMeasured.get(notMeasuredIndex);
                    RelLP lp = (RelLP) view.getLayoutParams();
                    ArrayList<View> anchorViews = lp.getAnchorViews();
                    if(notMeasured.containsAny(anchorViews)){
                        MainActivity.log("child index: " + children.indexOf(view) + ", cannot be measured");
                        MainActivity.log("children not measured: " + notMeasured.convert((index, element) -> children.indexOf(element)));
                        MainActivity.log("anchored views: " + anchorViews.convert((index, element) -> children.indexOf(element)));
                        continue;
                    }

                    measuredAtLeastOneView = true;
                    notMeasured.remove(view);

                    boolean leftConstrained = false, topConstrained = false, rightConstrained = false, bottomConstrained = false;

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
                    ruleNotNull:
                    if(rules[0] != null) {
                        leftConstrained = true;
                        currentAnchorView = rules[0].getView();
                        if(currentAnchorView == null){
                            break ruleNotNull;
                        }
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[0].isRightOf())
                            leftBorder = mutablePositions.get(currentAnchorView).x + view.getMeasuredWidth() + currentAnchorLp.rightMargin;// + lp.leftMargin;
                        else //align left
                            leftBorder = mutablePositions.get(currentAnchorView).x;
                    }
                    //top
                    int topBorder = 0;
                    ruleNotNull:
                    if(rules[1] != null){
                        topConstrained = true;
                        currentAnchorView = rules[1].getView();
                        if(currentAnchorView == null){
                            break ruleNotNull;
                        }
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[1].isBelow())
                            topBorder = mutablePositions.get(currentAnchorView).y + view.getMeasuredHeight() + currentAnchorLp.bottomMargin;// + lp.topMargin;
                        else //align top
                            topBorder = mutablePositions.get(currentAnchorView).y;
                    }
                    //right
                    int rightBorder = layoutWidth;
                    ruleNotNull:
                    if(rules[2] != null){
                        rightConstrained = true;
                        currentAnchorView = rules[2].getView();
                        if(currentAnchorView == null){
                            break ruleNotNull;
                        }
                        currentAnchorLp = (MarginLayoutParams) currentAnchorView.getLayoutParams();
                        if(rules[2].isLeftOf())
                            rightBorder = mutablePositions.get(currentAnchorView).x - currentAnchorLp.leftMargin;// - lp.rightMargin;
                        else //align right
                            rightBorder = mutablePositions.get(currentAnchorView).x + view.getMeasuredWidth();
                    }
                    //bottom
                    int bottomBorder = layoutHeight;
                    ruleNotNull:
                    if(rules[3] != null) {
                        bottomConstrained = true;
                        currentAnchorView = rules[3].getView();
                        if(currentAnchorView == null){
                            break ruleNotNull;
                        }

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

            if(notMeasured.size() != 0){
                MainActivity.log("children: " + children);
                MainActivity.log("not measured: " + notMeasured);
                MainActivity.log("not measured index's: " + notMeasured.convert((index, element)->children.indexOf(element)));
                MainActivity.log("\n");

                children.enumerate((index, view) -> {
                    RelLP lp = (RelLP) view.getLayoutParams();
                    ArrayList<View> anchorViews = lp.getAnchorViews();
                    ArrayList<Integer> anchorViewIndexList = anchorViews.convert((indexAnchor, viewAnchor)->children.indexOf(viewAnchor));
                    MainActivity.log("index: " + index +", anchors: " + anchorViewIndexList);
                });

                throw new RuntimeException();
            }
        }


        setMeasuredDimension(maxChildWidth, maxChildHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(View view: children){
            MutablePosition mutablePosition = mutablePositions.get(view);
            view.layout(mutablePosition.x, mutablePosition.y,
                    mutablePosition.x + view.getMeasuredWidth(), mutablePosition.y + view.getMeasuredHeight());
        }
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
