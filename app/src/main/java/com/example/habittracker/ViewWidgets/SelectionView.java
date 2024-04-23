package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.PayloadOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class SelectionView {
    Context context;
    ArrayList<PayloadOption> options;
    OnSelected onSelected;
    OnAdd onAdd;
    ListView listView;
    int textViewResource;
    int color = -1;

    public static final String addString = "add";
    public SelectionView(Context context, ArrayList<PayloadOption> options, OnSelected onSelected, OnAdd onAdd){
        this.context = context;
        this.options = (ArrayList<PayloadOption>) options.clone();
        this.onSelected = onSelected;
        this.onAdd = onAdd;
        init();
    }

    public SelectionView(Context context, ArrayList<PayloadOption> options, OnSelected onSelected){
        this.context = context;
        this.options = (ArrayList<PayloadOption>) options.clone();
        this.onSelected = onSelected;
        this.onAdd = null;
        init();
    }
    public SelectionView(Context context, String[] options, OnSelected onSelected, OnAdd onAdd){
        this.context = context;
        this.options = convert(new ArrayList<>(Arrays.asList(options)));
        this.onSelected = onSelected;
        this.onAdd = onAdd;
        init();
    }
    public SelectionView(Context context, String[] options, OnSelected onSelected){
        this.context = context;
        this.options = convert(new ArrayList<>(Arrays.asList(options)));
        this.onSelected = onSelected;
        this.onAdd = null;
        init();
    }

    private static ArrayList<PayloadOption> convert(ArrayList<String> stringOptions){
        ArrayList<PayloadOption> result = new ArrayList<>();
        for(String string: stringOptions)
            result.add(new PayloadOption(new LiteralString(string), null));
        return result;
    }

    private void init(){
        color = ColorPalette.textPurple;
        createList();
    }



    public View getView(){
        return listView;
    }

    public void setColor(int textPurple) {
        //System.out.println("setting color: " + getOptions());
        color = textPurple;
        setColor();
    }

    public void resetColor(){
        color = ColorPalette.textPurple;
        setColor();
    }

    public void setText(String[] strings) {
        setText(convert(new ArrayList<>(Arrays.asList(strings))));
    }

    public void setTextString(ArrayList<CachedString> stringList) {
        setText(EnumLoop.makeList(stringList, (string)->new PayloadOption(string, null)));
    }


    public interface OnSelected {
        public void onSelected(CachedString cachedString, int position, Object key);
    }

    public interface OnAdd{
        public void onAdd();
    }

    public void setText(ArrayList<PayloadOption> payloadOptionList){
        options = payloadOptionList;
        if(onAdd != null)
            options.add(new PayloadOption(new LiteralString("add"), null));
        ArrayList<String> stringList = EnumLoop.makeList(options, (input)->input.getString());
        listView.setAdapter(new ArrayAdapter<>(context, textViewResource, stringList));
        listView.setMinimumWidth(1000);
        setColor();
    }

    private void setColor(){
        listView.post(() -> {
            for(int i = 0; i < listView.getChildCount(); i++){
                TextView child = (TextView) listView.getChildAt(i);
                child.setTextColor(color);
            }
        });
    }

    public void setText(PayloadOption[] strings){
        setText(new ArrayList<>(Arrays.asList(strings)));
    }

    private Object getKeyOfOption(String optionName){
        for(PayloadOption cachedString : options){
            if(cachedString.getString().equals(optionName))
                return cachedString.getPayload();
        }
        throw new RuntimeException("no option pair matches option name");
    }

    RelativeLayout relativeLayout;
    private void createList(){
        //System.out.println("creating list: " + options);

        int numItems = options.size();
        listView = new ListView(context);


        relativeLayout = new RelativeLayout(context);
        //RelativeLayout.LayoutParams listViewLayoutParam = new RelativeLayout.LayoutParams(-2, -2);
        //listViewLayoutParam.addRule(RelativeLayout.CENTER_IN_PARENT);
        //listView.setLayoutParams(listViewLayoutParam);
        //relativeLayout.addView(listView);
        listView.setMinimumHeight(1000);
        listView.setMinimumWidth(1000);

        if(onAdd != null){
            options.add(new PayloadOption(new LiteralString(addString), null));
        }
        ArrayList<String> optionNames = EnumLoop.makeList(options, payloadOption -> payloadOption.getString());
        //MainActivity.log("selection view optionNames: " + optionNames);
        textViewResource = android.R.layout.simple_list_item_1;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                textViewResource, optionNames);
        listView.setAdapter(adapter);
        int textViewHeight = 0;
        if(options.size() > 0)
            textViewHeight = getTextHeight(options.get(0).getString());
        //System.out.println("predicted textView hiehgt: " + textViewHeight);

        listView.setLayoutParams(new LinearLayout.LayoutParams(-1, textViewHeight * options.size()));

        setColor();

        // Set the click listener for the list items
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            //System.out.println("item selected in list: " + i);
            TextView textView = (TextView) view;
            String value = (String) textView.getText();
            int numOptions = getOptions().size();
            if(value.equals(addString)){
                onAdd.onAdd();
                return;
            }
            if(onAdd != null)
                numOptions = numOptions - 1;

            if(i < numOptions) {
                sendItemSelectedCall(i);
                return;
            }
            throw new RuntimeException("list error index selected:  \n" + i + " value: " + value + ", options: " + getOptions() + " num options: " + numOptions);


        });
        //System.out.println("finsihed creating list num views: " + listView.getChildCount() + " array: " + options);
    }

    private void sendItemSelectedCall(int position){
        PayloadOption cachedString = options.get(position);
        onSelected.onSelected(cachedString.getCachedString(), position, cachedString.getPayload());
    }

    private ArrayList<PayloadOption> getOptions(){
        return options;
    }

    public View getChild(int index){
        return listView.getChildAt(index);
    }

    public int getTextHeight(String s){
        TextView textView = (TextView) GLib.inflate(textViewResource);
        textView.setText(s);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST); // Specify the parent width if known, or a default width
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.UNSPECIFIED); // Unspecified for height
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        int desiredHeight = textView.getMeasuredHeight();
        return desiredHeight;
    }
}
