package com.example.habittracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("creating calendar");
        create_calendar();
    }

    public void create_calendar(){
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        // Example: Set an event click listener

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NonNull EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();

                DayStruct dayStruct = new DayStruct(clickedDayCalendar);

                System.out.println("<MainActivity> got click " + dayStruct);
            }
        });
    }
}