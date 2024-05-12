package com.example.habittracker.Structs.CachedStrings;

import org.json.JSONException;
import org.json.JSONObject;

public class LiteralString implements CachedString{
    public static final String className = "literal";
    private String string;
    public LiteralString(String string){
        this.string = string;
    }



    @Override
    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof LiteralString){
            return ((LiteralString) object).getString().equals(string);
        }
        return false;
    }

    @Override
    public String toString(){
        return getString();
    }

    @Override
    public JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CachedString.typeOfClassKey, className);
        jsonObject.put("value", string);
        return jsonObject;
    }

    public static LiteralString getFromJSON(JSONObject jsonObject) throws JSONException{
        String value = jsonObject.getString("value");
        return new LiteralString(value);
    }


}
