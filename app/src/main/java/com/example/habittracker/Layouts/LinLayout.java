package com.example.habittracker.Layouts;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.R;
import com.example.habittracker.StaticClasses.GLib;

import java.util.ArrayList;

public class LinLayout {
    private Context context;

    private InterceptLinearLayout layout;
    private View addButton = null;
    ArrayList<View> views = new ArrayList<>();

    private Margin margin;
    private Margin padding;

    public LinLayout(Context context){
        this.context = context;

        layout = new InterceptLinearLayout(context);
        layout.setId(R.id.linLayout);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    }

    public void setChildMargin(Margin margin){
        this.margin = margin;
        setChildParams();
    }



    public LinearLayout.LayoutParams childParams() {
        if(margin != null)
            return margin.getLin(-1, -2);
        return new LinearLayout.LayoutParams(-1, -2);
    }

    public void setChildParams(){
        for(View view: views){
            view.setLayoutParams(childParams());
        }
    }

    public void add(View view){
        //System.out.println("adding view");
        //System.out.println("hasButton() = " + hasButton());
        //System.out.println("(views.size() - 1) = " + (views.size() - 1));
        if(view == null)
            throw new RuntimeException();
        if(hasButton())
            layout.addView(view, views.size());
        else
            layout.addView(view);

        views.add(view);
        view.setLayoutParams(childParams());
    }

    public void addWithoutSettingParams(View view, int i){
        if(view == null)
            throw new RuntimeException();
        if(hasButton())
            layout.addView(view, i);
        else
            layout.addView(view, i);

        views.add(i, view);
    }

    public void add(View view, int i) {
        if(view == null)
            throw new RuntimeException();
        if(hasButton())
            layout.addView(view, i);
        else
            layout.addView(view, i);

        views.add(i, view);
        view.setLayoutParams(childParams());
    }

    public void remove(View view){
        if(view == null)
            throw new RuntimeException();
        views.remove(view);
        layout.removeView(view);
    }

    public void removeAt(int index){
        views.remove(index);
        layout.removeViewAt(index);
    }

    public void addButton(View.OnClickListener listener){
        addButton = GLib.getButton(listener, context);
        layout.addView(addButton);
    }

    public void removeButton(){
        layout.removeView(addButton);
        addButton = null;
    }

    public boolean hasButton(){
        return addButton != null;
    }

    public View getView() {
        return layout;
    }


}
