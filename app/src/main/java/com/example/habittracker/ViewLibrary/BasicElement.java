package com.example.habittracker.ViewLibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.habittracker.MainActivity;

public class BasicElement extends AbstractBasicElement{
    private Context context;
    private View view;

    public BasicElement(Context context){
        this.context = context;
        view = new ViewSuperMeasure(context);
    }


    @Override
    public View getView() {
        return view;
    }


    public static class ViewSuperMeasure extends View{

        public ViewSuperMeasure(Context context) {
            super(context);
        }

        public ViewSuperMeasure(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public ViewSuperMeasure(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public ViewSuperMeasure(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);


        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);

            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            int width = (widthMode == MeasureSpec.AT_MOST) ? 0 : widthSize;
            int height = (heightMode == MeasureSpec.AT_MOST) ? 0 : heightSize;

            setMeasuredDimension(width, height);

//            MainActivity.log("view onMeasure width mode: " +
//                    MeasureSpec.getMode(widthMeasureSpec) + ", size: " + MeasureSpec.getSize(widthMeasureSpec) + "\n" +
//                    MeasureSpec.getMode(heightMeasureSpec) + ", size: " + MeasureSpec.getSize(heightMeasureSpec));
//
//            MainActivity.log("measured size: " + getMeasuredWidth() + ", " + getMeasuredHeight());
        }
    }
}
