package com.example.habittracker.ViewLibrary;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class RelativeElementLayout extends ElementLayout{
    private RelativeLayout relativeLayout;
    private Context context;

    public RelativeElementLayout(Context context) {
        super(context);
        this.context = context;
        relativeLayout = new RelativeLayout(context);
    }

    public void add(Element element){
        super.add(element);
    }

    public static void test(Context context){
        RelativeElementLayout relativeElementLayout = new RelativeElementLayout(context);
        TextElement textElement = new TextElement(context, "");
        relativeElementLayout.addWithParam(textElement, -2, -2);
        relativeElementLayout.addWithParam(textElement, -2, -2, Direction.Top, Direction.Left);
    }

    public void addWithParam(Element element, int width, int height, Direction... directions){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        for(Direction direction: directions){
            layoutParams.addRule(direction.getCode());
        }
        element.getView().setLayoutParams(layoutParams);
        super.add(element);
    }



    @Override
    public View getView() {
        return relativeLayout;
    }
}
