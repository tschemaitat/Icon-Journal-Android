package com.example.habittracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;

public class SliderWithLabels extends ConstraintLayout {

    private CustomSlider slider;
    private ArrayList<String> valueArray = new ArrayList<>();
    private ArrayList<Integer> labelIndexList = new ArrayList<>();
    private ArrayList<TextView> labels = new ArrayList<>();

    private Context context;

    public SliderWithLabels(Context context) {
        super(context);
        init(context);
    }

    public SliderWithLabels(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SliderWithLabels(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        System.out.println("sliderWithLabels init");
        System.out.println("this = " + this);


        this.context = context;
        // Initialize the slider
        slider = new CustomSlider(context);
        slider.setId(View.generateViewId());
        addView(slider, new ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT));



        slider.setLabelFormatter(value -> {
            // Customize label based on the value
            // Example: converting value to a custom string
            return "Value: " + value;
        });


        //setLabelNumbers();
        //setLabelText();


        // Add static labels below the slider
        //addStaticLabels(context);

        //createLabelViews(context);

    }

    public void setNumberValues(int numberValues){
        slider.setNumberValues(numberValues);
    }

    public void setValues(ArrayList<String> values, ArrayList<Integer> labelIndexList){
        System.out.println("set values");
        System.out.println("\t" + values);
        this.labelIndexList = labelIndexList;
        this.valueArray = values;
        createLabelViews(context);
        constraintSliderAndLabels();
        slider.setNumberValues(values.size());
    }

    public void createLabelViews(Context context){
        System.out.println("\tcreating label views");
        for (Integer index : labelIndexList){
            String value = valueArray.get(index);
            TextView label = createTextView(context, value);
            addView(label);
            labels.add(label);

        }


    }


    private void constraintSliderAndLabels(){
        System.out.println("\nconstraintSliderAndLabels");
        int labelLayoutMargin = 0;
        int margin = 50;
        int numberValues = valueArray.size();
        int numberLabel = labels.size();

        int textMaxWidth = 0;
        int maxTextHeight = 0;
        int labelMargin = 20;
        for(int i = 0; i < labelIndexList.size(); i++){
            TextView label = labels.get(i);
            int textWidth = GLib.calculateTextWidth(label);
            textMaxWidth = Math.max(textMaxWidth, textWidth);

            int textHeight = GLib.calculateTextHeight(label);
            maxTextHeight = Math.max(textHeight, maxTextHeight);
        }
        System.out.print("\tlabelMargin = " + labelMargin);
        System.out.print(", textMaxWidth = " + textMaxWidth);
        System.out.println(", numberValues = " + numberValues);
        int widthSum = (labelMargin + textMaxWidth) * (numberValues) + labelLayoutMargin*2;
        System.out.println("\twidthSum = " + widthSum);
        this.setMinWidth(widthSum);

        int trackStart = textMaxWidth/2 + labelLayoutMargin;
        constraintSlider(trackStart, 10, trackStart);

        int finalTextMaxWidth = textMaxWidth;
        post(new Runnable() {
            @Override
            public void run() {
                System.out.println("post constraint");
                constraintLabels(margin, numberValues, trackStart, getWidth() - trackStart);
                printAllConstraints();


                System.out.println("\ttrackMargin: " + trackMargin());
                constraintSlider(trackStart - trackMargin(), 10, trackStart - trackMargin());
                System.out.println("end post constraint");
            }
        });
        System.out.println("\nend constraintSliderAndLabels\n");
    }

    private void printAllConstraints(){

    }

    private int trackMargin(){
        return (slider.getWidth() - slider.getTrackWidth()) / 2;
    }



    private void constraintLabels(int trackToLabelMargin, int numberValues, int trackStart, int trackEnd){
        System.out.println("\tConstraint Labels");
        int trackWidth = slider.getTrackWidth();
        int trackHeight = slider.getTrackHeight();
        int layoutWidth = getWidth();
        int layoutHeight = getHeight();
        int bottomTrack = layoutHeight/2 + trackHeight/2;


        for (int i = 0; i < labelIndexList.size(); i++){
            Integer indexOfValues = labelIndexList.get(i);
            String value = valueArray.get(indexOfValues);

            System.out.println("\t\tvalue " + value + ", indexOfValues = " + indexOfValues);
            TextView label = labels.get(i);
            int textWidth = (int)GLib.calculateTextWidth(label.getText().toString(), label.getTextSize(), label.getTypeface());
            //System.out.print("calculated textWidth = " + textWidth);
            //System.out.println(", actual textWidth = " + label.getWidth());
            int labelX = trackStart + (int)( ((float)indexOfValues / (numberValues - 1)) * (trackEnd - trackStart) );
            constraint(label, context, labelX, bottomTrack + trackToLabelMargin);


        }
        System.out.println("\tend constraint labels");
    }

    private void constraintSlider(int marginLeft, int marginTop, int marginRight){
        System.out.println("\tconstraint slider");
        System.out.println("\t\t" + "left: " + marginLeft + ", top: " + marginTop + ", right: " + marginRight);
        slider.setX(marginLeft);
        slider.setY(marginTop);
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(this);
//        constraintSet.connect(slider.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, marginTop);
//        constraintSet.connect(slider.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, marginLeft);
//        constraintSet.connect(slider.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, marginRight);
//
//        constraintSet.applyTo(this);
        System.out.println("\tend constraint slider");
    }

    private void constraint(View view, Context context, int x, int y){

        System.out.print("\tx = " + x);
        System.out.println(", y = " + y);
        int left = x - view.getWidth()/2;
        int top = y - view.getHeight()/2;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top);
        constraintSet.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, left);

        constraintSet.applyTo(this);
    }

    private TextView createTextView(Context context, String label){
        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        textView.setText(String.valueOf(label));
        return textView;
    }
}
