package com.example.habittracker.ViewLibrary.RelativeLayoutElements;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.habittracker.MainActivity;
import com.example.habittracker.ViewLibrary.Element;
import com.example.habittracker.ViewLibrary.ElementLayout;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelLayoutSuperMeasure;
import com.example.habittracker.ViewLibrary.RelativeLayoutElements.RelParamAllowsMatch;

public class RelativeElementLayout extends ElementLayout {
    private RelLayoutSuperMeasure relativeLayout;
    private Context context;

    public RelativeElementLayout(Context context) {
        super(context);
        relativeLayout = new RelLayoutSuperMeasure(context);
        super.setViewGroup(relativeLayout);
        this.context = context;

    }

    @Override
    protected ViewGroup getViewGroup() {
        return relativeLayout;
    }

    @Override
    protected void onAdd(Element element) {

    }

    @Override
    protected void onRemove(Element element) {

    }

    public ParamHelper addWithParam(Element element, int width, int height){
        int paramWidth = width;
        int paramHeight = height;
        boolean matchWidth = false;
        boolean matchHeight = false;
        if(width == -1){
            matchWidth = true;
            paramWidth = -2;
        }
        if(height == -1){
            matchHeight = true;
            paramHeight = -2;
        }
        RelParamAllowsMatch layoutParams = new RelParamAllowsMatch(width, height, matchWidth, matchHeight);
        element.getView().setLayoutParams(layoutParams);
        relativeLayout.addView(element.getView());
        super.add(element);
        return new ParamHelper(layoutParams);
    }



    @Override
    public View getView() {
        return relativeLayout;
    }


    public static class ParamHelper{
        private RelParamAllowsMatch lp;
        public ParamHelper(RelParamAllowsMatch lp){
            this.lp = lp;
        }

//        public ParamHelper matchWidth(){
//            lp.matchWidth = true;
//            return this;
//        }
//        public ParamHelper matchHeight(){
//            lp.matchHeight = true;
//            return this;
//        }

        public void addRuleWithSubject(int code, Element element){
            int id = element.getView().getId();
            if(id == View.NO_ID){
                int new_id = View.generateViewId();
                MainActivity.log("new id generated: " + new_id);
                element.getView().setId(new_id);
            }else{
                MainActivity.log("view already has id: " + id);
            }

            lp.addRule(code, element.getView().getId());
        }

        public void addRule(int code){
            lp.addRule(code);
        }

        //region nextToElement

        public ParamHelper leftOf(Element element){
            addRuleWithSubject(RelativeLayout.LEFT_OF, element);
            return this;
        }

        public ParamHelper rightOf(Element element){
            addRuleWithSubject(RelativeLayout.RIGHT_OF, element);
            return this;
        }

        public ParamHelper above(Element element){
            addRuleWithSubject(RelativeLayout.ABOVE, element);
            return this;
        }

        public ParamHelper below(Element element){
            addRuleWithSubject(RelativeLayout.BELOW, element);
            return this;
        }
        //endregion
        //region alignParent
        public ParamHelper alignParentTop(){
            addRule(RelativeLayout.ALIGN_PARENT_TOP);
            return this;
        }

        public ParamHelper alignParentLeft(){
            addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            return this;
        }

        public ParamHelper alignParentRight(){
            addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            return this;
        }

        public ParamHelper alignParentBottom(){
            addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            return this;
        }
        //endregion alignParent
        //region center
        public ParamHelper centerInParent(){
            addRule(RelativeLayout.CENTER_IN_PARENT);
            return this;
        }

        public ParamHelper centerHorizontal(){
            addRule(RelativeLayout.CENTER_HORIZONTAL);
            return this;
        }

        public ParamHelper centerVertical(){
            addRule(RelativeLayout.CENTER_VERTICAL);
            return this;
        }
        //endregion
        //region alignElement
        public ParamHelper alignLeft(Element element){
            addRuleWithSubject(RelativeLayout.ALIGN_LEFT, element);
            return this;
        }

        public ParamHelper alignRight(Element element){
            addRuleWithSubject(RelativeLayout.ALIGN_RIGHT, element);
            return this;
        }
        public ParamHelper alignBottom(Element element){
            addRuleWithSubject(RelativeLayout.ALIGN_BOTTOM, element);
            return this;
        }
        public ParamHelper alignTop(Element element){
            addRuleWithSubject(RelativeLayout.ALIGN_TOP, element);
            return this;
        }

        public ParamHelper alignAllParentSides() {
            alignParentLeft().alignParentTop().alignParentRight().alignParentBottom();
            return this;
        }
        //endregion




    }



}
