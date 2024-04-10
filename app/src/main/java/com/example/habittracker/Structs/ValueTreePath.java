package com.example.habittracker.Structs;

import java.util.ArrayList;

public class ValueTreePath {
    private ArrayList<Integer> intList;
    public ValueTreePath(ArrayList<Integer> intList){
        this.intList = (ArrayList<Integer>)intList.clone();
    }


    public int get(int level){
        return intList.get(level);
    }

    public int getLast(){
        return intList.get(intList.size() - 1);
    }

    public int size(){
        return intList.size();
    }


}
