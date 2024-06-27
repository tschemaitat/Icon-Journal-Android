package com.example.habittracker.ViewLibrary.ScrollElements;

import android.content.Context;
import android.view.View;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.LinearLayoutElements.LinearElementLayout;
import com.example.habittracker.ViewWidgets.LockableScrollView;
import com.example.habittracker.defaultImportPackage.ArrayList;

public class ScrollElement {

    private Context context;
    private LockableScrollView lockableScrollView;
    private LinearElementLayout linearElementLayout;
    private ArrayList<Element> children = new ArrayList<>();

    public ScrollElement(Context context, int orientationCode) {
        this.context = context;
        lockableScrollView = new LockableScrollView(context);
        linearElementLayout = new LinearElementLayout(context, orientationCode);
        lockableScrollView.addView(linearElementLayout.getLinearLayout());
    }

    public LockableScrollView getLockableScrollView(){
        return lockableScrollView;
    }

    public void addWithParam(Element element, int width, int height){
        MainActivity.log("adding scroll element: " + element + ", width: " + width + ", height: " + height);
        linearElementLayout.addWithParam(element, width, height);
        children.add(element);
    }

    public void add(Element element){
        linearElementLayout.add(element);
        children.add(element);
    }

    public View getView(){
        return lockableScrollView;
    }

    public <E extends Element> ArrayList<E> getChildrenCasted() {
        ArrayList<E> result = new ArrayList<>();
        for(Element element: children){
            result.add((E) element);
        }
        return result;
    }

    public void measureChildren(){
        for(Element element: children){
            forceLayout(element.getView(), 1080, 2160);
        }
    }

    public void setMinimumHeightBasedOnChildren(int numChildren){
        ArrayList<Integer> largestHeights = new ArrayList<>();
        for(Element element: children){
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

    public static void forceLayout(View view, int parentWidth, int parentHeight) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(parentHeight, View.MeasureSpec.UNSPECIFIED);

        // Measure and layout the view
        view.measure(widthMeasureSpec, heightMeasureSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }
}
