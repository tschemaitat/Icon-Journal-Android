package com.example.habittracker.StaticClasses;

import java.util.ArrayList;

public class EnumLoop {
    public static <T> void loop(ArrayList<T> list, loopFunction<T> loopFunction){
        for(int i = 0; i < list.size(); i++){
            T child = list.get(i);
            loopFunction.onLoop(i, child);
        }
    }

    public  interface loopFunction<T>{
        public void onLoop(int index, T child);
    }
}
