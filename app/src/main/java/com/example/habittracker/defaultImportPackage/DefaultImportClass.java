package com.example.habittracker.defaultImportPackage;

import android.widget.LinearLayout;

import com.example.habittracker.Structs.CachedStrings.CachedString;

public class DefaultImportClass {
    public static final int Horizontal = LinearLayout.HORIZONTAL;
    public static final int Vertical = LinearLayout.VERTICAL;

    public static LinearLayout.LayoutParams linearParam(int width, int height){
        return new LinearLayout.LayoutParams(width, height);
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
