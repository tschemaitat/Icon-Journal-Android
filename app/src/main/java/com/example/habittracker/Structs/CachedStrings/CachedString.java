package com.example.habittracker.Structs.CachedStrings;

import com.example.habittracker.Algorithms.ThrowableEquals;

import org.json.JSONException;
import org.json.JSONObject;

public interface CachedString extends ThrowableEquals{
    String typeOfClassKey = "cached string type";
    String className = "cached string";

    String getString();

    JSONObject getJSON() throws JSONException;

    //void throwEquals(CachedString cachedString);

    String toString();
}
