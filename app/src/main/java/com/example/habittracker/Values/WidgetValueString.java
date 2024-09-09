package com.example.habittracker.Values;

import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.defaultImportPackage.ImmutableList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class WidgetValueString extends BaseWidgetValue {
    public static final String className = "widget value string";
    private LiteralString cachedString;


    public WidgetValueString(WidgetId widgetId, LiteralString cachedString){
        super(widgetId);
        this.cachedString = cachedString;
    }




    public CachedString getStandardFormOfCachedString(){
        return cachedString;
    }

    @Override
    public CachedString getDebugCachedString() {
        return cachedString;
    }

    public CachedString getDisplayCachedString() {
        return cachedString;
    }


    @Override
    protected JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value type", className);
        jsonObject.put("widget id", getWidgetId().getIntegerId().intValue());
        jsonObject.put("cached string", cachedString.getJSON());

        return jsonObject;
    }



    public static WidgetValue getFromJSON(JSONObject jsonObject) throws JSONException{
        JSONObject cachedStringJSON = jsonObject.getJSONObject(CachedString.className);
        int widgetId = jsonObject.getInt("widget id");
        LiteralString cachedString = (LiteralString)StructureTokenizer.getCachedString(cachedStringJSON);
        return new WidgetValueString(new WidgetId(widgetId), cachedString);
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof WidgetValueString widgetValueString))
            return false;
        if( ! Objects.equals(getWidgetId(), widgetValueString.getWidgetId()))
            return false;
        if( ! Objects.equals(cachedString, widgetValueString.cachedString))
            return false;
        return true;
    }

    @Override
    public void equalsThrows(Object object) {
        if( ! (object instanceof WidgetValueString widgetValueString))
            throw new RuntimeException(object.toString());
        getWidgetId().equalsThrows(widgetValueString.getWidgetId());
        cachedString.equalsThrows(widgetValueString.cachedString);
        if( ! this.equals(object))
            throw new RuntimeException();
    }

    @Override
    public String toString(){
        return "WidgetValueString, id: " + getIntegerId() + ", value: " + cachedString + ">";
    }
}
