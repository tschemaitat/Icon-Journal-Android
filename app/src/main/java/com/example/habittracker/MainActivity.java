package com.example.habittracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;


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
        changePage(new TestPage(context));


    }

    public static void changePage(Inflatable newLayout){
        log("inflating: " + newLayout.getClass().getName());
        if(currentLayout != null){

            boolean removeSuccess = currentLayout.tryToRemove(newLayout);
            if(!removeSuccess){
                log("remove: " + currentLayout.getClass().getName() + " failed");
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
        editorButton.setOnClickListener(view -> changePage(new EditorSelectionPage(context)));
        TextView categoryButton = findViewById(R.id.CategoriesButton);
        categoryButton.setOnClickListener(view -> {
            changePage(new CategorySelectionPage(context));
        });
        TextView journalButton = findViewById(R.id.JournalButton);
        journalButton.setOnClickListener(view -> {
            //changePage(new JournalPage(context, "test structure"));
        });
        TextView testButton = findViewById(R.id.TestButton);
        testButton.setOnClickListener(view -> changePage(new TestPage(context)));
    }

    public static void showPopup(Context context, String prompt, ArrayList<String> optionNames, ArrayList<Runnable> onClickOptions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(prompt);

        CharSequence[] items = optionNames.toArray(new CharSequence[0]);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickOptions.get(which).run();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static LinearLayout createVerticalLayout(){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        return linearLayout;
    }

    public static void log(String message) {
        // Getting the current stack trace element of this method call (log)
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println("stackTraceElements = " + Arrays.toString(stackTraceElements));
        // Adjust index as needed to find the correct caller info (might need fine-tuning)
        StackTraceElement element = stackTraceElements[3];

        String fullClassName = element.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        int lineNumber = element.getLineNumber();
        String methodName = element.getMethodName();

        Log.d(className + "." + methodName + "():" + lineNumber, "<CustLogzzz> " + message);
    }











}