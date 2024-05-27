package com.example.habittracker.Structs.CachedStrings;

import com.example.habittracker.Algorithms.Lists;

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
        return "<literalString, value: " + getString() + ">";
    }

    @Override
    public JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CachedString.typeOfClassKey, className);
        jsonObject.put("value", string);
        return jsonObject;
    }

    @Override
    public void equalsThrows(Object object) {
        if( ! (object instanceof LiteralString literalString))
            throw new RuntimeException(object.getClass().toString());
        if( ! (getString().equals(literalString.getString())))
            throw new RuntimeException("first: " + getString() + ", second: " + literalString.getString());
        if( ! this.equals(object))
            throw new RuntimeException();
    }

    public static LiteralString getFromJSON(JSONObject jsonObject) throws JSONException{
        String value = jsonObject.getString("value");
        return new LiteralString(value);
    }


}
