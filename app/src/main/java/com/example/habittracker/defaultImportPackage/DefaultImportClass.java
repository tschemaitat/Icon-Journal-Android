package com.example.habittracker.defaultImportPackage;

import android.widget.LinearLayout;

import com.example.habittracker.Structs.CachedStrings.CachedString;

public class DefaultImportClass {
    public static final int Horizontal = LinearLayout.HORIZONTAL;
    public static final int Vertical = LinearLayout.VERTICAL;

    public static LinearLayout.LayoutParams linearParam(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
    }

    public enum Direction{
        Left(9),
        Top(10),
        Right(11),
        Bottom(12);

        private int code;
        Direction(int code){
            this.code = code;
        }

        public int getCode(){
            return code;
        }
    }

    public enum MatchCode {
        MatchWidth,
        MatchHeight;
    }

    public static class Dimensions{
        private int left;
        private int top;
        private int right;
        private int bottom;

        public Dimensions(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }

        public int getRight() {
            return right;
        }

        public int getBottom() {
            return bottom;
        }

        public int getWidth(){
            return left - right;
        }

        public int getHeight(){
            return bottom - top;
        }
    }

    public interface BooleanListener{
        void onBoolean(boolean bool);
    }

    public interface PayloadClickListener{
        void onClick(Object payload);
    }

    public static class PayloadBoolPair{
        private Object payload;
        private boolean bool;

        public PayloadBoolPair(Object payload, boolean bool) {
            this.payload = payload;
            this.bool = bool;
        }

        public Object getPayload() {
            return payload;
        }

        public boolean getBool() {
            return bool;
        }
    }

    public interface OnSelected {
        public void onSelected(CachedString cachedString, int position, Object key);
    }

    public interface OnChecked {
        public void onChecked(boolean checked, CachedString cachedString, int position, Object key);
    }

    public interface OnAdd{
        public void onAdd();
    }
}
