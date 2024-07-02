package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.habittracker.ViewLibrary.MatchParams;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class RelParamAllowsMatch extends RelativeLayout.LayoutParams implements MatchParams {
    public boolean matchWidth = false;
    public boolean matchHeight = false;

    public RelParamAllowsMatch(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public RelParamAllowsMatch(int w, int h) {
        super(w, h);
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
}
