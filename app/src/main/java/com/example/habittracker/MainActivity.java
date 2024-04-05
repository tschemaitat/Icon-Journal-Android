package com.example.habittracker;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.habittracker.Inflatables.CategorySelectionPage;
import com.example.habittracker.Inflatables.Inflatable;
import com.example.habittracker.Inflatables.EditorSelectionPage;
import com.example.habittracker.Inflatables.JournalPage;
import com.example.habittracker.Inflatables.TestPage;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.Dictionary;


public class MainActivity extends AppCompatActivity {
    public static Context context;
    ScrollView scrollView;
    LinearLayout scrollLinearLayout;
    public static ConstraintLayout constraintLayout;
    LinearLayout buttonInflateBufferLayout;
    public static LinearLayout inflateLayout;
    public static MainActivity mainActivity;

    static Inflatable currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        MainActivity.context = this;
        Dictionary.generate(context);
        ColorPalette.setColors(context);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        scrollLinearLayout = findViewById(R.id.scrollLinearLayout);
        constraintLayout = findViewById(R.id.constraintLayoutParent);
        buttonInflateBufferLayout = findViewById(R.id.ButtonInflateBufferLayout);

        inflateLayout = findViewById(R.id.inflateLayout);
        inflateLayout.setMinimumHeight(1000);

        //currentLayout = testWidgetGroup;
        setupLayoutButtons();

        UnitTests unitTests = new UnitTests(context);
        //setOptions(dropDown);
        inflateLayout(new TestPage(context));


    }

    public static void inflateLayout(Inflatable newLayout){
        System.out.println("inflating: " + newLayout);
        if(currentLayout != null){

            boolean removeSuccess = currentLayout.tryToRemove();
            if(!removeSuccess){
                System.out.println("remove: " + currentLayout + " failed");
                return;
            }

            inflateLayout.removeView(currentLayout.getView());
            currentLayout.onRemoved();
        }

        inflateLayout.addView(newLayout.getView());
        currentLayout = newLayout;
        currentLayout.onOpened();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void setupLayoutButtons(){
        TextView editorButton = findViewById(R.id.EditorButton);
        editorButton.setOnClickListener(view -> inflateLayout(new EditorSelectionPage(context)));
        TextView categoryButton = findViewById(R.id.CategoriesButton);
        categoryButton.setOnClickListener(view -> {
            inflateLayout(new CategorySelectionPage(context));
        });
        TextView journalButton = findViewById(R.id.JournalButton);
        journalButton.setOnClickListener(view -> {
            inflateLayout(new JournalPage(context, "test structure"));
        });
        TextView testButton = findViewById(R.id.TestButton);
        testButton.setOnClickListener(view -> inflateLayout(new TestPage(context)));
    }

    public static LinearLayout createVerticalLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }











}