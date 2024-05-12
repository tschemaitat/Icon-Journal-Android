package com.example.habittracker.Values;

import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.RefItemPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WidgetValueStringPath extends BaseWidgetValue{
    public static final String className = "widget value string path";
    private RefItemPath refItemPath;
    public WidgetValueStringPath(Integer widgetId, RefItemPath refItemPath){
        super(widgetId);
        this.refItemPath = refItemPath;
    }

    public RefItemPath getRefItemPath() {
        return refItemPath;
    }

    @Override
    public CachedString getDisplayCachedString() {
        return new ArrayString(refItemPath.getPath());
    }

    @Override
    public CachedString getStandardFormOfCachedString() {
        return refItemPath.getLast();
    }

    @Override
    public CachedString getDebugCachedString() {
        if(refItemPath == null)
            return null;
        return new ArrayString(refItemPath.getPath());
    }

    @Override
    protected JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value type", "widget value string");
        jsonObject.put("array size", refItemPath.size());
        JSONArray jsonArray = new JSONArray();
        for(CachedString cachedString: refItemPath){
            jsonArray.put(cachedString.getJSON());
        }
        jsonObject.put("refItemPath", jsonArray);

        return jsonObject;
    }

    public static WidgetValue getFromJSON(JSONObject jsonObject) throws JSONException{
        int widgetId = jsonObject.getInt("widget id");
        int arraySize = jsonObject.getInt("array size");
        ArrayList<CachedString> cachedStrings = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("refItemPath");
        for(int i = 0; i < arraySize; i++){
            JSONObject cachedStringJSON = jsonArray.getJSONObject(i);
            CachedString childCachedString = StructureTokenizer.getCachedString(cachedStringJSON);
            cachedStrings.add(childCachedString);
        }
        RefItemPath refItemPath = new RefItemPath(cachedStrings);
        return new WidgetValueStringPath(widgetId, refItemPath);
    }
}
