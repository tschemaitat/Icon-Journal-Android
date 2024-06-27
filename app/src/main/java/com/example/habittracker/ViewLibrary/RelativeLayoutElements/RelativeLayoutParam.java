package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class RelativeLayoutParam extends ViewGroup.MarginLayoutParams {
    private Rule[] rules = new Rule[4];
    private ArrayList<View> anchorViews = new ArrayList<>();
    public RelativeLayoutParam(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public RelativeLayoutParam(int width, int height) {
        super(width, height);
    }

    public RelativeLayoutParam(ViewGroup.MarginLayoutParams source) {
        super(source);
    }

    public RelativeLayoutParam(ViewGroup.LayoutParams source) {
        super(source);
    }

    public ArrayList<View> getAnchorViews(){
        return anchorViews;
    }

    public Rule[] getRuleForEachSide(){
        return rules;
    }

    public RelativeLayoutParam addRule(int code, View view){
        addRulePrivate(code, view);
        return this;
    }

    public RelativeLayoutParam addRule(int code){
        addRulePrivate(code, null);
        return this;
    }

    private void addRulePrivate(int code, View view){
        Rule rule = new Rule(code, view);
        int index = rule.getConstrainSideCode();
        if(rules[index] != null)
            throw new RuntimeException();
        rules[index] = rule;
        createAnchorViews();
    }

    private void createAnchorViews(){
        anchorViews = new ArrayList<>();
        for(Rule rule: rules){
            if(rule.view == null)
                continue;
            anchorViews.add(rule.view);
        }

    }

//    public void checkRuleSet(){
//        boolean[] constraintSideList = {false, false, false, false};
//
//        for(Rule rule: rules){
//            int sideCode = rule.getConstrainSideCode();
//            if(constraintSideList[sideCode])
//                throw new RuntimeException();
//            constraintSideList[sideCode] = true;
//        }
//    }









    public static class Rule{
        private int code;
        private View view;

        public Rule(int code, View view) {
            this.code = code;
            if(anchorsToOtherChild() && view == null){
                throw new RuntimeException("anchors to view but has null view, code: " + code);
            }
            this.view = view;
            testConstraintFunctions();
        }

        public void testConstraintFunctions(){
            int result = -1;

            if(constraintsLeft()){
                result = 0;
            }
            if(constraintsTop()){
                if(result != -1)
                    throw new RuntimeException();
                result = 1;
            }
            if(constraintsRight()){
                if(result != -1)
                    throw new RuntimeException();
                result = 2;
            }
            if(constraintsBottom()){
                if(result != -1)
                    throw new RuntimeException();
                result = 3;
            }
        }

        public int getCode() {
            return code;
        }

        public View getView() {
            return view;
        }

        public int getConstrainSideCode(){
            if(constraintsLeft())
                return 0;
            else if (constraintsTop())
                return 1;
            else if (constraintsRight())
                return 2;
            else if (constraintsBottom())
                return 3;
            throw new RuntimeException();
        }

        public boolean constraintsLeft(){
            return isRightOf() || isAlignLeft() || isAlignParentLeft();
        }

        public boolean constraintsRight(){
            return isLeftOf() || isAlignRight() || isAlignParentRight();
        }

        public boolean constraintsTop(){
            return isBelow() || isAlignTop() || isAlignParentTop();
        }

        public boolean constraintsBottom(){
            return isAbove() || isAlignBottom() || isAlignParentBottom();
        }

        public boolean anchorsToOtherChild(){
            return ! alignsToParent();
        }

        public boolean alignsToParent(){
            return isAlignParentLeft() || isAlignParentBottom() || isAlignParentRight() || isAlignParentTop();
        }



        public boolean isLeftOf(){
            return code == RelativeLayout.LEFT_OF;
        }
        public boolean isRightOf(){
            return code == RelativeLayout.RIGHT_OF;
        }
        public boolean isAbove(){
            return code == RelativeLayout.ABOVE;
        }
        public boolean isBelow(){
            return code == RelativeLayout.BELOW;
        }

        public boolean isAlignParentBottom(){
            return code == RelativeLayout.ALIGN_PARENT_BOTTOM;
        }
        public boolean isAlignParentTop(){
            return code == RelativeLayout.ALIGN_PARENT_TOP;
        }
        public boolean isAlignParentLeft(){
            return code == RelativeLayout.ALIGN_PARENT_LEFT;
        }
        public boolean isAlignParentRight(){
            return code == RelativeLayout.ALIGN_PARENT_RIGHT;
        }

        public boolean isAlignBottom(){
            return code == RelativeLayout.ALIGN_BOTTOM;
        }
        public boolean isAlignTop(){
            return code == RelativeLayout.ALIGN_TOP;
        }
        public boolean isAlignLeft(){
            return code == RelativeLayout.ALIGN_LEFT;
        }
        public boolean isAlignRight(){
            return code == RelativeLayout.ALIGN_RIGHT;
        }


    }
}
