package com.example.habittracker.Widgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Structs.DropDownPages.RefDropDownPage;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.HeaderNode;

public class StaticDropDown extends DropDown{
    boolean dataSet = false;
    public StaticDropDown(Context context) {
        super(context);
    }

    public View getView(){

    }

    public void setParamCustom(EntryWidgetParam params){
        StaticDropDownParameters staticParams = (StaticDropDownParameters) params;
        dataSet = true;
        parentPage = staticParams.page;
        currentPage = parentPage;
        selectedValuePath = null;

    }

    public static class StaticDropDownParameters extends EntryWidgetParam {
        RefDropDownPage page;
        public StaticDropDownParameters(String name, RefDropDownPage page){
            super(name, DropDown.className);
            this.page = page;
        }


        @Override
        public String hierarchyString(int numTabs) {
            return null;
        }

        @Override
        public HeaderNode createHeaderNode() {
            throw new RuntimeException();
        }


    }
}
