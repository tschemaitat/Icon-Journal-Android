package com.example.habittracker.Inflatables;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.Widgets.CustomEditText;
import com.example.habittracker.Widgets.StructureWidget;

public class TestPage implements Inflatable {
    private LinearLayout linearLayout;
    private Context context;

    public TestPage(Context context){
        this.context = context;
        linearLayout = MainActivity.createVerticalLayout();
        setupTestLayout();
    }

    @Override
    public View getView() {
        return linearLayout;
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onOpened() {

    }


    public void setupTestLayout(){
        StructureWidget structureWidget = new StructureWidget(context);
        structureWidget.setOnDataChangedListener(()->{});
        linearLayout.addView(structureWidget.getView());

        CustomEditText editText = new CustomEditText(context);
        linearLayout.addView(editText.getView());
    }
}
