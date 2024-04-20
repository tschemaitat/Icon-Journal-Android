package com.example.habittracker.ANotUsing.Slider;

import java.util.Calendar;

public class DayStruct {
    public int day;
    public int month;
    public int year;
    public DayStruct(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DayStruct(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String toString(){
        return "(" + day + ", " + month + ", " + year + ")";
    }
}
