package com.example.habittracker.ViewLibrary.ScrollElements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementLayout;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewWidgets.LockableScrollView;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class ScrollElement extends ElementLayout{

    private Context context;
    private LockableScrollView lockableScrollView;
    private LinearElementLayout linearElementLayout;

    public ScrollElement(Context context, int orientationCode) {
        super(context);
        this.context = context;
        lockableScrollView = new LockableScrollView(context);
        if(lockableScrollView.getId() == -1)
            lockableScrollView.setId(View.generateViewId());
        linearElementLayout = new LinearElementLayout(context, orientationCode);
        ScrollParam scrollParam = new ScrollParam(-2, -2);
        scrollParam.matchWidth = true;
        linearElementLayout.getLinearLayout().setLayoutParams(scrollParam);
        MainActivity.log("linear layout param before add: " + scrollParam.getClass().getName());
        lockableScrollView.addView(linearElementLayout.getLinearLayout());

        MainActivity.log("linear layout param after add: " + scrollParam.getClass().getName());
        MainActivity.log("my scroll id: " + lockableScrollView.getId());
    }

    public LinearElementLayout getLinearLayout(){
        return linearElementLayout;
    }

    public LockableScrollView getLockableScrollView(){
        return lockableScrollView;
    }

    public void addWithParam(Element element, int width, int height){
        MainActivity.log("adding scroll element: " + element + ", width: " + width + ", height: " + height);
        linearElementLayout.addWithParam(element, width, height);
        super.add(element);
    }

    @Override
    protected ViewGroup getViewGroup() {
        return lockableScrollView;
    }

    @Override
    protected void onAdd(Element element) {

    }

    @Override
    protected void onRemove(Element element) {

    }

    public View getView(){
        return lockableScrollView;
    }

//    public <E extends Element> ArrayList<E> getChildrenCasted() {
//        ArrayList<E> result = new ArrayList<>();
//        for(Element element: children){
//            result.add((E) element);
//        }
//        return result;
//    }
//
//    public void measureChildren(){
//        for(Element element: children){
//            forceLayout(element.getView(), 1080, 2160);
//        }
//    }

    public void setMinimumHeightBasedOnChildren(int numChildren){
        ArrayList<Integer> largestHeights = new ArrayList<>();
        for(Element element: getElements()){
            int elementHeight = element.getView().getHeight();
            if(largestHeights.size() < numChildren)
                largestHeights.add(elementHeight);
            else{
                int min = 100000;
                for(Integer integer: largestHeights){
                    if(integer < min)
                        min = integer;
                }
                if(elementHeight < min){
                    largestHeights.remove(min);
                    largestHeights.add(elementHeight);
                }
            }
        }
        int sum = 0;
        for(Integer integer: largestHeights)
            sum += integer;
        lockableScrollView.setMinimumHeight(sum);
    }


    public static class ScrollParam extends FrameLayout.LayoutParams{
        public boolean matchWidth;

        public ScrollParam(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public ScrollParam(int width, int height) {
            super(width, height);
        }

        public ScrollParam(ViewGroup.LayoutParams p) {
            super(p);
        }

        public ScrollParam(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public ScrollParam(LinearLayout.LayoutParams source) {
            super(source);
        }
    }

//    public static void forceLayout(View view, int parentWidth, int parentHeight) {
//        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY);
//        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentHeight, View.MeasureSpec.UNSPECIFIED);
//
//        // Measure and layout the view
//        view.measure(widthMeasureSpec, heightMeasureSpec);
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//    }
}
