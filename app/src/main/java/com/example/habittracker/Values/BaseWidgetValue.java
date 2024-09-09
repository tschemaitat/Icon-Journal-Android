package com.example.habittracker.Values;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.defaultImportPackage.ImmutableList;
import com.example.habittracker.structurePack.Structure;

public abstract class BaseWidgetValue extends WidgetValue{

    private ArrayList<RefEntryString<WidgetValueReference>> referenceStrings = new ArrayList<>();

    public BaseWidgetValue(WidgetId widgetId) {
        super(widgetId);
    }

    public abstract CachedString getDisplayCachedString();

    public abstract CachedString getStandardFormOfCachedString();

    public abstract CachedString getDebugCachedString();


    public void addReference(Structure structure, EntryId entryId, WidgetValueReference widgetValueReference){

        RefEntryString<WidgetValueReference> newReference = new RefEntryString<>(structure, entryId, widgetValueReference);
        if(referenceStrings.contains(newReference)){
            throw new RuntimeException();
        }
        referenceStrings.add(newReference);
    }
    public void removeReference(Structure structure, EntryId entryId, WidgetValueReference widgetValueReference){
        RefEntryString<WidgetValueReference> newReference = new RefEntryString<>(structure, entryId, widgetValueReference);
        if( ! referenceStrings.contains(newReference)){
            throw new RuntimeException();
        }
        referenceStrings.remove(newReference);
    }
    public ImmutableList<RefEntryString<WidgetValueReference>> getReferences(WidgetValueReference widgetValueReference){
        return new ImmutableList<>(referenceStrings);
    }
}
