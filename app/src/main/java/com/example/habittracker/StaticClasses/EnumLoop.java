package com.example.habittracker.StaticClasses;

import java.util.ArrayList;

public class EnumLoop {
    public static <T> void loop(ArrayList<T> list, loopFunction<T> loopFunction){
        for(int i = 0; i < list.size(); i++){
            T child = list.get(i);
            loopFunction.onLoop(i, child);
        }
    }

    public static <Result, Input> ArrayList<Result> makeList(ArrayList<Input> inputList, ConvertFunc<Result, Input> convertFunc){
        ArrayList<Result> resultList = new ArrayList<>();
        for(Input input: inputList){
            resultList.add(convertFunc.convert(input));
        }
        return resultList;
    }

    public interface ConvertFunc<Result, Input>{
        public Result convert(Input input);
    }

    public  interface loopFunction<T>{
        public void onLoop(int index, T child);
    }
}
