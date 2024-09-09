package com.example.habittracker.Values;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import com.example.habittracker.StaticClasses.StructureTokenizer;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.defaultImportPackage.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class WidgetValueReference extends BaseWidgetValue{
    public static final String className = "widget reference string";
    private RefEntryString refEntryString;

    public WidgetValueReference(WidgetId widgetId, RefEntryString refEntryString) {
        super(widgetId);
        this.refEntryString = refEntryString;
    }

    public BaseWidgetValue getSource(){
        return refEntryString.getBaseWidgetValue();
    }

    @Override
    public CachedString getDisplayCachedString() {
        return refEntryString;
    }

    @Override
    public CachedString getStandardFormOfCachedString() {
        return refEntryString;
    }

    @Override
    public CachedString getDebugCachedString() {
        return refEntryString;
    }

    @Override
    protected JSONObject getJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value type", className);
        jsonObject.put("widget id", getWidgetId().getIntegerId().intValue());
        jsonObject.put("cached string", refEntryString.getJSON());

        return jsonObject;
    }

    public static WidgetValue getFromJSON(JSONObject jsonObject) throws JSONException{
        JSONObject cachedStringJSON = jsonObject.getJSONObject(CachedString.className);
        int widgetId = jsonObject.getInt("widget id");
        RefEntryString cachedString = (RefEntryString)StructureTokenizer.getCachedString(cachedStringJSON);
        return new WidgetValueReference(new WidgetId(widgetId), cachedString);
    }


    @Override
    public void equalsThrows(Object object) {

    }
}
