package com.example.habittracker.StaticClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.habittracker.MainActivity;
import com.example.habittracker.R;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.EntryWidgets.EntryDropDown;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.ListWidgetMultipleItems;
import com.example.habittracker.Widgets.ListWidgetSingleItem;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.ListSingleItemParam;

public class GLib {
    public static final int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    public static final int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;

    public static final int initialHorMargin = 15;
    public static final int initialVertMargin = 10;


    public static int calculateTextWidth(String text, float textSize, Typeface typeface) {


        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        return (int)paint.measureText(text);
    }

    public static int calculateTextWidth(TextView textView) {
        String text = textView.getText().toString();
        float textSize = textView.getTextSize();
        Typeface typeface = textView.getTypeface();

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        return (int)paint.measureText(text);
    }

    public static int calculateTextHeight(String text, float textSize, Typeface typeface) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    public static int calculateTextHeight(TextView textView) {
        String text = textView.getText().toString();
        float textSize = textView.getTextSize();
        Typeface typeface = textView.getTypeface();

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }

    public static View inflate(Context context, int resourceId){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourceId, null, false);
        return view;

    }

    public static int pxToDp(int px) {
        return (int)(px / (MainActivity.context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public static int dpToPx(int dp){
        return (int)(dp * (MainActivity.context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public static float pxToDp(float px) {
        return (px / (MainActivity.context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public static float dpToPx(float dp){
        return (dp * (MainActivity.context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public void setMargin(int dp){

    }

    public static void outlinedLayoutAddView(ConstraintLayout outlinedLayout, View child){
        ((ConstraintLayout)(outlinedLayout.getChildAt(0))).addView(child);
    }

    public static View getButton(View.OnClickListener listener, Context context){
        ConstraintLayout layout = new ConstraintLayout(context);
        Button button = (Button) (((ConstraintLayout) MainActivity.mainActivity.getLayoutInflater().inflate(R.layout.button_layout, layout)).getChildAt(0));
        layout.removeView(button);

        button.setOnClickListener(listener);
        return button;
    }

    public static View inflate(int id){
        ConstraintLayout layout = new ConstraintLayout(MainActivity.mainActivity);
        View view = ((ConstraintLayout) MainActivity.mainActivity.getLayoutInflater().inflate(id, layout)).getChildAt(0);
        layout.removeView(view);
        return view;
    }

    public static Widget inflateWidget(Context context, EntryWidgetParam params, Runnable onDataChange){

        String className = params.getClassName();
        //System.out.println("inflating widget: " + className);
        Widget widget = null;
        switch (className) {
            case DropDown.className -> {
                widget = new EntryDropDown(context);
                widget.setOnDataChangedListener(onDataChange);
                widget.setParam(params);
            }
            case ListWidgetMultipleItems.className -> {
                widget = new ListWidgetMultipleItems(context);
                widget.setOnDataChangedListener(onDataChange);
                widget.setParam(params);
            }
            case ListWidgetSingleItem.className -> {
                widget = new ListWidgetSingleItem(context);
                widget.setOnDataChangedListener(onDataChange);
                widget.setParam(params);
            }
            case GroupWidget.className -> {
                widget = new GroupWidget(context);
                widget.setOnDataChangedListener(onDataChange);
                widget.setParam(params);
            }
            case CustomEditText.className -> {
                widget = new CustomEditText(context);
                widget.setOnDataChangedListener(onDataChange);
                widget.setParam(params);
            }
            default -> throw new RuntimeException("unknown widget class: " + className);
        }
        return widget;
    }

    public static Drawable setBackgroundColorForView(Context context, int color) {
        //System.out.println("setting color");
        Drawable background = context.getDrawable(R.drawable.background_of_card);
        if (background instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) background;
//            int numLayers = layerDrawable.getNumberOfLayers();
//            for(int i = 0; i < numLayers; i++){
//                Drawable layer = layerDrawable.getDrawable(i);
//                System.out.println(layer);
//            }
            Drawable colorLayer = layerDrawable.getDrawable(7);
            colorLayer.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            //colorLayer.getPaint().setColor(color);
        }else{
            throw new RuntimeException();
        }
        return background;
    }


    public static String tabs(int num){
        String singleTab = "\t";
        String tabs = "";
        for(int i = 0; i < num; i++)
            tabs += singleTab;
        return tabs;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable upArrow;
    public static Drawable downArrow;
    public static Drawable starOn;
    public static Drawable starOff;
    public static Drawable starDisabled;

    public static void generateDrawables(Context context){
        Drawable rightArrow = context.getResources().getDrawable(R.drawable.arrow_right);
        Drawable leftArrow = context.getResources().getDrawable(R.drawable.arrow_left);
        upArrow = rotateDrawable(leftArrow);
        downArrow = rotateDrawable(rightArrow);
        starOn = context.getResources().getDrawable(R.drawable.star_on);
        starOff = context.getResources().getDrawable(R.drawable.star_off);
        starDisabled = starOff.getConstantState().newDrawable().mutate();
        starDisabled.setColorFilter(new PorterDuffColorFilter(Color.argb(128, 0, 0, 0), PorterDuff.Mode.SRC_ATOP));

    }

    public static Drawable rotateDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        // Convert Drawable to Bitmap
        Bitmap originalBitmap = drawableToBitmap(drawable);
        // Create a Matrix for rotation
        Matrix matrix = new Matrix();
        matrix.postRotate(90); // Rotate 90 degrees

        // Create a new Bitmap from the original using the matrix for transformation
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0,
                originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                matrix, true);

        // Convert the rotated Bitmap back to a Drawable
        return new BitmapDrawable(rotatedBitmap);
    }


}
