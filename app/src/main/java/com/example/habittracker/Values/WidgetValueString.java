package com.example.habittracker.Values;

import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.CachedStrings.CachedString;

import org.json.JSONException;
import org.json.JSONObject;

public class WidgetValueString extends BaseWidgetValue {
    public static final String className = "widget value string";
    private CachedString cachedString;

    public WidgetValueString(Integer widgetId, CachedString cachedString){
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
        jsonObject.put("value type", "widget value string");
        jsonObject.put("widget id", getWidgetId().intValue());
        jsonObject.put("cached string", cachedString.getJSON());

        return jsonObject;
    }

    public static WidgetValue getFromJSON(JSONObject jsonObject) throws JSONException{
        JSONObject cachedStringJSON = jsonObject.getJSONObject(CachedString.className);
        int widgetId = jsonObject.getInt("widget id");
        CachedString cachedString = StructureTokenizer.getCachedString(cachedStringJSON);
        return new WidgetValueString(widgetId, cachedString);
    }
}
