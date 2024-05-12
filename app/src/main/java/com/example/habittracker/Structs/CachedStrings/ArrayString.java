package com.example.habittracker.Structs.CachedStrings;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Margin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrayString implements CachedString{
    ArrayList<CachedString> stringList;
    String result = null;
    public ArrayString(ArrayList<CachedString> stringList){
        this.stringList = stringList;
    }

    public String getString(){
        if(result != null)
            return result;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < stringList.size() - 1; i++){
            builder.append(stringList.get(i).getString()).append(", ");
        }
        builder.append(stringList.get(stringList.size()-1));
        result = builder.toString();
        MainActivity.log("getting string from: " + stringList + " \nresult: " + result);
        return result;
    }

    @Override
    public JSONObject getJSON() throws JSONException {
        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof ArrayString){
            return ((ArrayString) object).getString().equals(getString());
        }
        return false;
    }

    @Override
    public String toString(){
        return getString();
    }
}
