package com.example.habittracker.ViewWidgets.ListViewPackage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.ViewLibrary.ButtonElement;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.HorLayout;
import com.example.habittracker.ViewLibrary.RelativeElementLayout;
import com.example.habittracker.ViewLibrary.ScrollElements.ScrollElement;
import com.example.habittracker.ViewLibrary.ScrollElements.VertScrollElement;
import com.example.habittracker.ViewLibrary.TextElement;
import com.example.habittracker.ViewWidgets.CheckBoxElement;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.defaultImportPackage.DefaultImportClass.*;
import com.example.habittracker.defaultImportPackage.ImmutableList;


public class DynamicListView {
    private static final String addString = "add";
    private Context context;
    private ViewWithTextGenerator viewWithTextGenerator;
    private ArrayList<PayloadOption> payloadOptions;
    private RelativeElementLayout relativeElementLayout;
    private ScrollElement scrollElement;
    private ButtonElement confirmButton;

    private OnSelected onSelected;
    private OnChecked onCheckedListener;
    private OnSelected onLongClickListener;
    private OnAdd onAdd;
    private HorLayout addHorLayout;

    public DynamicListView(Context context, ArrayList<PayloadOption> options){
        this.context = context;
        this.payloadOptions = (ArrayList<PayloadOption>) options.clone();
        init();
    }

    public void init(){
        relativeElementLayout = new RelativeElementLayout(context);
        scrollElement = new VertScrollElement(context);
        relativeElementLayout.addWithParam(scrollElement, -1, -2).alignAllParentSides().matchWidth();
        Drawable drawable = new ColorDrawable(Color.DKGRAY);//context.getResources().getDrawable(android.R.drawable.line);
        LinearLayout linearLayout = scrollElement.getLinearLayout().getLinearLayout();
        linearLayout.setDividerDrawable(drawable);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        //linearLayout.setDividerPadding(30);
        createTextElements();
        confirmButton = createButtonElement(context);
        confirmButton.getButton().setVisibility(View.GONE);
        relativeElementLayout.addWithParam(confirmButton, -2, -2).alignParentBottom().alignParentRight();
    }

    //region create views
    public static ButtonElement createButtonElement(Context context){
        return new ButtonElement(context, "confirm", null);
    }
    public HorLayout makeHorLayoutElement(String text, Runnable clickListener, Runnable longClickListener, boolean addCheckBox){
        HorLayout horLayout = new HorLayout(context);
        if(clickListener != null)
            horLayout.getView().setOnClickListener(view->clickListener.run());
        if(longClickListener != null){
            horLayout.getView().setOnLongClickListener(view -> {
                longClickListener.run();
                return true;
            });
        }

        TextElement textElement = new TextElement(context, text);
        textElement.setPadding(new Dimensions(50, 20, 20, 20));
        TextView textView = textElement.getTextView();
        textView.setTextColor(ColorPalette.textPurple);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        horLayout.getView().setBackgroundResource(typedValue.resourceId);
        //textView.setBackgroundResource(android.R.drawable.list_selector_background);
        horLayout.addWithParam(textElement, -2, -2);

        if(addCheckBox){
            CheckBoxElement checkBoxElement = new CheckBoxElement(context);
            checkBoxElement.getCheckBox().setVisibility(View.GONE);
            horLayout.addWithParam(checkBoxElement, -2, -2);
        }


        return horLayout;
    }
    private void createTextElements(){
        payloadOptions.enumerate((index, payloadOption)->{
            HorLayout horLayout = makeHorLayoutElement(payloadOption.getString(), ()->onClick(index, payloadOption),
                    null, true);
            scrollElement.addWithParam(horLayout, -1, -2);
        });

        createAddElement();

    }
    private void createAddElement(){
        addHorLayout = makeHorLayoutElement(addString, ()->onAddClicked(), null, false);
        scrollElement.addWithParam(addHorLayout, -1, -2);
        addHorLayout.getView().setVisibility(View.GONE);

    }
    //endregion



    //region open/close
    public void showAddButton(Runnable runnable){
        addHorLayout.getView().setVisibility(View.VISIBLE);
        addHorLayout.getView().setOnClickListener(view -> {
            runnable.run();
        });
    }
    public void showConfirmButton(Runnable runnable){
        confirmButton.getButton().setOnClickListener(view -> {
            runnable.run();
        });
        confirmButton.getButton().setVisibility(View.VISIBLE);
    }
    public void showCheckBoxes(OnChecked onCheckedListener){
        if(this.onCheckedListener != null)
            throw new RuntimeException();
        this.onCheckedListener = onCheckedListener;
        iterateCheckBoxes(((index, checkBoxElement) -> {
            checkBoxElement.getCheckBox().setVisibility(View.VISIBLE);
            PayloadOption payloadOption = payloadOptions.get(index);
            checkBoxElement.setOnCheckListener((isChecked)-> {
                MainActivity.log("internal onCheck");
                onCheckedListener.onChecked(isChecked,
                        payloadOption.getCachedString(), index, payloadOption.getPayload());

            });
        }));
    }
    public void hideConfirmButton() {
        confirmButton.getButton().setVisibility(View.GONE);
        confirmButton.getButton().setOnClickListener(null);
    }
    public void hideCheckBoxes() {
        throwIfNotInCheckMode();
        this.onCheckedListener = null;
        iterateCheckBoxes(((index, checkBoxElement) -> {
            checkBoxElement.getCheckBox().setVisibility(View.GONE);
            checkBoxElement.setOnCheckListener(null);
        }));
    }
    //endregion

    //region abstract functions
    private interface CheckBoxFunction{
        void checkBox(int index, CheckBoxElement checkBoxElement);
    }
    private ImmutableList<CheckBoxElement> getCheckBoxes(){
        ArrayList<CheckBoxElement> checkBoxElements = new ArrayList<>();
        ArrayList<HorLayout> horLayouts = scrollElement.getChildrenCasted();
        horLayouts.enumerate((index, horLayout) -> {
            if(index == horLayouts.size() - 1)
                return;
            CheckBoxElement checkBox = horLayout.getChildCasted(1);
            checkBoxElements.add(checkBox);
        });
        return checkBoxElements;
    }
    private void iterateCheckBoxes(CheckBoxFunction checkBoxFunction){
        ArrayList<HorLayout> horLayouts = scrollElement.getChildrenCasted();
        horLayouts.enumerate((index, horLayout) -> {
            if(index == horLayouts.size() - 1)
                return;
            CheckBoxElement checkBox = horLayout.getChildCasted(1);
            checkBoxFunction.checkBox(index, checkBox);
        });
    }
    private interface OptionViewFunction{
        void optionView(int index, HorLayout horLayout, TextElement textElement, PayloadOption payloadOption);
    }
    private void iterateOptionViews(OptionViewFunction optionViewFunction){
        ArrayList<HorLayout> horLayouts = scrollElement.getChildrenCasted();
        MainActivity.log("total horLayouts: " + horLayouts.size());
        int endIndex = horLayouts.size();
        if(addHorLayout != null){
            endIndex--;
        }
        MainActivity.log("num non-add layouts: " + endIndex);
        if(payloadOptions.size() != endIndex)
            throw new RuntimeException("num horLayouts: " + horLayouts.size() + ", num payloads: " + payloadOptions.size()+
                    " addHorLayout is null: " + (addHorLayout == null) + ", end index calculated: " + endIndex);
        for(int i = 0; i < endIndex; i++){
            HorLayout horLayout = horLayouts.get(i);
            TextElement textElement = horLayout.getChildCasted(0);
            optionViewFunction.optionView(i, horLayout, textElement, payloadOptions.get(i));
        }
    }
    //endregion

    //region getter functions
    public ArrayList<PayloadBoolPair> getCheckBoxesClicked(){
        throwIfNotInCheckMode();
        ArrayList<PayloadBoolPair> result = new ArrayList<>();
        iterateCheckBoxes((index, checkBoxElement) -> {
            PayloadOption payloadOption = payloadOptions.get(index);
            result.add(new PayloadBoolPair(payloadOption.getPayload(), checkBoxElement.getCheckBox().isChecked()));
        });
        return result;
    }
    //endregion

    public void throwIfNotInCheckMode(){
        if(this.onCheckedListener == null)
            throw new RuntimeException();
    }

    public void setCheckBox(int position, boolean b) {
        ImmutableList<CheckBoxElement> checkBoxElements = getCheckBoxes();
        CheckBoxElement checkBoxElement = checkBoxElements.get(position);
        checkBoxElement.getCheckBox().setChecked(b);
    }



    public void setOnLongClickListener(OnSelected onSelected){
        this.onLongClickListener = onSelected;
        iterateOptionViews((index, horLayout, textElement, payloadOption) -> {
            horLayout.getView().setOnLongClickListener(view -> {
                MainActivity.log("internal onLongClick");
                onSelected.onSelected(payloadOption.getCachedString(), index, payloadOption.getPayload());
                return true;
            });
        });
    }

    public void setOnClickListener(OnSelected onSelected){
        this.onSelected = onSelected;

//        iterateOptionViews((index, horLayout, textElement, payloadOption) -> {
//            MainActivity.log("setting on click on view: " + index);
//            horLayout.getView().setOnClickListener(view -> {
//                //MainActivity.log("internal onClick");
//                onClick(index, payloadOption);
//            });
//        });
    }

    private void onClick(int position, PayloadOption payloadOption) {
        MainActivity.log("dynamic onClick method");
        if(isCheckBoxesOpen()){
            CheckBoxElement checkBoxElement = getCheckBoxes().get(position);
            checkBoxElement.getCheckBox().toggle();
        }else{
            MainActivity.log("going to onSelected listener because not in check boxes");
            if(onSelected != null){
                onSelected.onSelected(payloadOption.getCachedString(), position, payloadOption.getPayload());
            }else{
                MainActivity.log("onSelected is null");
            }

        }

    }

    private boolean isCheckBoxesOpen() {
        return onCheckedListener != null;
    }

    public void onAddClicked(){
        onAdd.onAdd();
    }



    public View getView() {
        return relativeElementLayout.getView();
    }
}
