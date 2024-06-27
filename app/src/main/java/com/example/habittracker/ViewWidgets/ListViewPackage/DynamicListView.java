package com.example.habittracker.ViewWidgets.ListViewPackage;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.HorLayout;
import com.example.habittracker.ViewLibrary.ScrollElements.ScrollElement;
import com.example.habittracker.ViewLibrary.ScrollElements.VertScrollElement;
import com.example.habittracker.ViewLibrary.TextElement;
import com.example.habittracker.ViewWidgets.CheckBoxElement;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.defaultImportPackage.DefaultImportClass.*;


public class DynamicListView {
    private static final String addString = "add";
    private Context context;
    private ViewWithTextGenerator viewWithTextGenerator;
    private ArrayList<PayloadOption> payloadOptions;
    private ScrollElement scrollElement;
    private OnSelected onSelected;
    private OnChecked onCheckedListener;
    private OnAdd onAdd;
    public DynamicListView(Context context, ArrayList<PayloadOption> options, OnSelected onSelected, OnAdd onAdd){
        this.context = context;
        this.payloadOptions = (ArrayList<PayloadOption>) options.clone();
        this.onSelected = onSelected;
        this.onAdd = onAdd;
        init();
    }

    public void init(){
        scrollElement = new VertScrollElement(context);
        createTextElements();
//        scrollView = new LockableScrollView(context);
//        scrollViewLinearLayout = new LinearLayout(context);
//        scrollView.addView(scrollViewLinearLayout);

    }
    //region check box
    public void openCheckBoxes(OnChecked onCheckedListener){
        if(this.onCheckedListener != null)
            throw new RuntimeException();
        this.onCheckedListener = onCheckedListener;
        iterateCheckBoxes(((index, checkBoxElement) -> {
            checkBoxElement.getCheckBox().setVisibility(View.VISIBLE);
            PayloadOption payloadOption = payloadOptions.get(index);
            checkBoxElement.setOnCheckListener((isChecked)->onCheckedListener.onChecked(isChecked,
                    payloadOption.getCachedString(), index, payloadOption.getPayload()));
        }));
    }




    private interface CheckBoxFunction{
        void checkBox(int index, CheckBoxElement checkBoxElement);
    }
    private void iterateCheckBoxes(CheckBoxFunction checkBoxFunction){
        throwIfNotInCheckMode();
        ArrayList<HorLayout> horLayouts = scrollElement.getChildrenCasted();
        horLayouts.enumerate((index, horLayout) -> {
            CheckBoxElement checkBox = horLayout.getChildCasted(1);
            checkBoxFunction.checkBox(index, checkBox);
        });
    }

    public void throwIfNotInCheckMode(){
        if(this.onCheckedListener == null)
            throw new RuntimeException();
    }

    public ArrayList<PayloadBoolPair> getCheckBoxesClicked(){
        throwIfNotInCheckMode();
        ArrayList<PayloadBoolPair> result = new ArrayList<>();
        iterateCheckBoxes((index, checkBoxElement) -> {
            PayloadOption payloadOption = payloadOptions.get(index);
            result.add(new PayloadBoolPair(payloadOption.getPayload(), checkBoxElement.getCheckBox().isChecked()));
        });
        return result;
    }

    public void closeCheckBoxes(){
        throwIfNotInCheckMode();
        this.onCheckedListener = null;
        iterateCheckBoxes(((index, checkBoxElement) -> {
            checkBoxElement.clearOnCheckListener();
            checkBoxElement.getCheckBox().setVisibility(View.INVISIBLE);
        }));


    }

    public void onCheckClicked(boolean checked, int index, PayloadOption payloadOption){
        onCheckedListener.onChecked(checked, payloadOption.getCachedString(), index, payloadOption.getPayload());
    }

    //endregion

    public void onTextClicked(int index, PayloadOption payloadOption){
        onSelected.onSelected(payloadOption.getCachedString(), index, payloadOption.getPayload());
    }

    public HorLayout makeHorLayoutElement(String text, Runnable clickListener, BooleanListener checkListener){
        HorLayout horLayout = new HorLayout(context);
        TextElement textElement = new TextElement(context, text, clickListener);
        textElement.setPadding(new Dimensions(50, 20, 20, 20));
        horLayout.addWithParam(textElement, -2, -2);

        if(checkListener != null){
            CheckBoxElement checkBoxElement = new CheckBoxElement(context, checkListener);
            checkBoxElement.getCheckBox().setVisibility(View.INVISIBLE);
            horLayout.addWithParam(checkBoxElement, 50, 50);
        }
        return horLayout;
    }

    private void createTextElements(){
        payloadOptions.enumerate((index, payloadOption)->{
            HorLayout horLayout = makeHorLayoutElement(payloadOption.getString(), ()->onTextClicked(index, payloadOption),
                    (check)-> onCheckClicked(check, index, payloadOption));
            scrollElement.addWithParam(horLayout, -2, -2);
            if(index != payloadOptions.size()-1)
                scrollElement.addWithParam(createDivisionBar(), 100, 100);
        });
        if(onAdd != null){
            HorLayout horLayout = makeHorLayoutElement(addString, ()->onAddClicked(),
                    null);
            scrollElement.addWithParam(horLayout, -2, -2);
        }
    }

    public void onAddClicked(){
        onAdd.onAdd();
    }

    public void createAddElement(){

    }

    private Element createDivisionBar() {
        Element dummyDivisionBar = new Element() {
            @Override
            public View getView() {
                View view = new View(context);
                view.setBackgroundColor(Color.DKGRAY);
                view.setMinimumHeight(2);
                return view;
            }
        };

        return dummyDivisionBar;
    }

    public View getView() {
        return scrollElement.getView();
    }
}
