package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.habittracker.ViewLibrary.EditableElementParam;
import com.example.habittracker.ViewLibrary.MatchParams;
import com.example.habittracker.defaultImportPackage.ArrayList;

import java.util.Arrays;
import java.util.Map;

public class RelParamAllowsMatch extends RelativeLayout.LayoutParams implements MatchParams, EditableElementParam {
    public boolean matchWidth = false;
    public boolean matchHeight = false;

    public RelParamAllowsMatch(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public RelParamAllowsMatch(int w, int h) {
        super(w, h);
    }
    public RelParamAllowsMatch(int w, int h, boolean matchWidth, boolean matchHeight) {
        super(w, h);
        this.matchWidth = matchWidth;
        this.matchHeight = matchHeight;
    }

    public void edit(int w, int h, boolean matchWidth, boolean matchHeight){
        this.width = w;
        this.height = h;
        this.matchWidth = matchWidth;
        this.matchHeight = matchHeight;
    }
    public static final Map<Integer, String> RULES = Map.ofEntries(
            Map.entry(RelativeLayout.ABOVE, "ABOVE"),
            Map.entry(RelativeLayout.ALIGN_BASELINE, "ALIGN_BASELINE"),
            Map.entry(RelativeLayout.ALIGN_BOTTOM, "ALIGN_BOTTOM"),
            Map.entry(RelativeLayout.ALIGN_END, "ALIGN_END"),
            Map.entry(RelativeLayout.ALIGN_LEFT, "ALIGN_LEFT"),
            Map.entry(RelativeLayout.ALIGN_PARENT_BOTTOM, "ALIGN_PARENT_BOTTOM"),
            Map.entry(RelativeLayout.ALIGN_PARENT_END, "ALIGN_PARENT_END"),
            Map.entry(RelativeLayout.ALIGN_PARENT_LEFT, "ALIGN_PARENT_LEFT"),
            Map.entry(RelativeLayout.ALIGN_PARENT_RIGHT, "ALIGN_PARENT_RIGHT"),
            Map.entry(RelativeLayout.ALIGN_PARENT_START, "ALIGN_PARENT_START"),
            Map.entry(RelativeLayout.ALIGN_PARENT_TOP, "ALIGN_PARENT_TOP"),
            Map.entry(RelativeLayout.ALIGN_RIGHT, "ALIGN_RIGHT"),
            Map.entry(RelativeLayout.ALIGN_START, "ALIGN_START"),
            Map.entry(RelativeLayout.ALIGN_TOP, "ALIGN_TOP"),
            Map.entry(RelativeLayout.BELOW, "BELOW"),
            Map.entry(RelativeLayout.CENTER_HORIZONTAL, "CENTER_HORIZONTAL"),
            Map.entry(RelativeLayout.CENTER_IN_PARENT, "CENTER_IN_PARENT"),
            Map.entry(RelativeLayout.CENTER_VERTICAL, "CENTER_VERTICAL"),
            Map.entry(RelativeLayout.END_OF, "END_OF"),
            Map.entry(RelativeLayout.LEFT_OF, "LEFT_OF"),
            Map.entry(RelativeLayout.RIGHT_OF, "RIGHT_OF"),
            Map.entry(RelativeLayout.START_OF, "START_OF")
    );
    @Override
    public String toString(){

        return "w: "+width + " , h: " + height
                + "match w: " + matchWidth + ", match h: " + matchHeight
                + ", rules: " + rulePrintOut(getRules());
    }

    public static String ruleName(int id){
        return RULES.get(id);
    }

    public static String rulePrintOut(int[] rules){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rules: \n");
        for(int i = 0; i< rules.length; i++){
            if(rules[i] != 0){
                stringBuilder.append(ruleName(i) + " id: " + rules[i] + "\n");
            }
        }
        return stringBuilder.toString();
    }

    public RelParamAllowsMatch(ViewGroup.LayoutParams source) {
        super(source);
    }

    public RelParamAllowsMatch(ViewGroup.MarginLayoutParams source) {
        super(source);
    }

    public RelParamAllowsMatch(RelativeLayout.LayoutParams source) {
        super(source);
    }

    @Override
    public boolean getMatchWidth() {
        return matchWidth;
    }

    @Override
    public boolean getMatchHeight() {
        return matchHeight;
    }


    @Override
    public void editDimensions(int x, int y) {
        if(x == -1 || y == -1)
            throw new RuntimeException();
        this.width = x;
        this.height = y;
    }

    @Override
    public void editMargin(int left, int right, int top, int bottom) {
        this.leftMargin = left;
        this.rightMargin = right;
        this.topMargin = top;
        this.bottomMargin = bottom;
    }
}
