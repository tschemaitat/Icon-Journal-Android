package com.example.habittracker;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class LinLayout {
    private Context context;

    private LinearLayout layout;
    private View addButton = null;
    ArrayList<View> views = new ArrayList<>();

    private Margin margin;
    private Margin padding;

    public LinLayout(Context context){
        this.context = context;

        layout = new LinearLayout(context);
        layout.setId(R.id.linLayout);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    }

    public void setChildMargin(Margin margin){
        this.margin = margin;
        setChildParams();
    }

    public void setPadding(Margin margin){
        this.padding = margin;
        layout.setPadding(margin.left, margin.top, margin.right, margin.bottom);
    }

    public LinearLayout.LayoutParams childParams() {
        return margin.getLin(-1, -2);
    }

    public void setChildParams(){
        for(View view: views){
            view.setLayoutParams(childParams());
        }
    }

    public void add(View view){
        System.out.println("adding view");
        System.out.println("hasButton() = " + hasButton());
        System.out.println("(views.size() - 1) = " + (views.size() - 1));
        if(view == null)
            throw new RuntimeException();
        if(hasButton())
            layout.addView(view, views.size());
        else
            layout.addView(view);

        views.add(view);

    }

    public void remove(View view){
        if(view == null)
            throw new RuntimeException();
        views.remove(view);
        layout.removeView(view);
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
