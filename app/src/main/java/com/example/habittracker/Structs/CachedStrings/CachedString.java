package com.example.habittracker.Structs.CachedStrings;

import org.json.JSONException;
import org.json.JSONObject;

public interface CachedString {
    String typeOfClassKey = "cached string type";
    String className = "cached string";

    String getString();

    JSONObject getJSON() throws JSONException;
}
