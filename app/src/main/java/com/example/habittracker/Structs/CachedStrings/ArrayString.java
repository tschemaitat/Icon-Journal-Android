package com.example.habittracker.Structs.CachedStrings;

import com.example.habittracker.Algorithms.Lists;
import com.example.habittracker.MainActivity;

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
        builder.append("[");
        for(int i = 0; i < stringList.size(); i++){
            builder.append(stringList.get(i).getString());
            if(i != stringList.size() - 1)
                builder.append(", ");
        }
        builder.append("]");
        result = builder.toString();
        MainActivity.log("getting string from: " + stringList + " \nresult: " + result + "\nlist with newlines: \n"  +Lists.stringNewLine(stringList));

        return result;
    }

    @Override
    public JSONObject getJSON() throws JSONException {
        throw new RuntimeException();
    }

    @Override
    public void equalsThrows(Object object) {
        if( ! (object instanceof ArrayString arrayString))
            throw new RuntimeException(object.getClass().toString());
        Lists.equalsThrowsRecursive(arrayString.stringList, stringList);
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
        return "<ArrayString, list: " + stringList.toString() + ">";
    }

    public int size() {
        return stringList.size();
    }
}
