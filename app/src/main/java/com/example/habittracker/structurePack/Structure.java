package com.example.habittracker.structurePack;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.CachedStrings.RefEntryString;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Structs.StructureId;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Values.BaseWidgetValue;
import com.example.habittracker.Values.GroupValue;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private StructureId id;
    private String name;
    private GroupWidgetParam widgetParam;
    private String type;
    private HashMap<EntryId, EntryInStructure> entries;
    private Set<WidgetInStructure> widgetMap;


    public Structure(String name, GroupWidgetParam widgetParam, String type){
        createId();
        initVariables(name, widgetParam, type, null, null);
    }



    public Structure(String name, GroupWidgetParam widgetParam, String type,
                     ArrayList<Entry> entries, StructureId id){
        initVariables(name, widgetParam, type, entries, id);
    }
    public Structure(String type){
        initVariables(null, null, type, null, null);
    }

    public void initVariables(String name, GroupWidgetParam widgetParam, String type,
                              ArrayList<Entry> entries, StructureId id){
        if(id != null)
            this.id = id;
        this.name = name;
        this.widgetParam = widgetParam;
        this.type = type;
        this.entries = new HashMap();

        if(entries == null)
            this.entries = new HashMap<>();
        else{
            for(Entry data: entries)
                insertOldEntry(data);
        }


        //MainActivity.log("log param while init structure: \n" + widgetParam.hierarchyString());

        init();
    }

    private void init(){
        MainActivity.log("\ninit name: " + name + ", id: " + id);
        Header header = new Header(widgetParam, this);
        widgetMap = header.widgetMap;
        MainActivity.log("set structure's map: " + widgetMap);
    }

    public void createId(){
        MainActivity.log("set new key for: " + getCachedName());
        if(id != null)
            throw new RuntimeException("tried to set new key of structure twice");
        this.id = new StructureId(structureKeyCount);
        structureKeyCount++;
    }

    private void insertOldEntry(Entry entry){
        EntryInStructure entryInStructure = new EntryInStructure(entry.getGroupValue(), entry.getId(), this);
        entries.put(entryInStructure.getId(), entryInStructure);
    }

    public void addEntry(GroupValue entryData){
        Set<EntryId> entryIdSet = entries.keySet();
        while(entryIdSet.contains(new EntryId(idCount)))
            idCount++;
        EntryInStructure entryInStructure = new EntryInStructure(entryData, new EntryId(idCount), this);
        idCount++;
        entries.put(entryInStructure.getId(), entryInStructure);
    }

    public void editEntry(EntryInStructure entryInStructureToEdit, GroupValue data){
        EntryId entryId = entryInStructureToEdit.getId();
        EntryInStructure found = null;
        for(EntryInStructure entryInStructure : entries.values()){
            if(entryInStructure.getId() == entryId){
                found = entryInStructure;
                break;
            }
        }
        EntryInStructure newEntryInStructure = new EntryInStructure(data, entryId, this);
        entries.put(entryId, newEntryInStructure);
    }



    public ArrayList<EntryInStructure> getEntryList(){
        return new ArrayList<>(entries.values());
    }

    public ArrayList<GroupValue> getData(){
        ArrayList<GroupValue> entryValueTrees = new ArrayList<>();
        for(EntryInStructure entryInStructure : entries.values())
            entryValueTrees.add(entryInStructure.getGroupValue());
        return entryValueTrees;
    }

    public ArrayList<EntryInStructure> getEntriesInStructure(){
        return new ArrayList<>(entries.values());
    }

    public ArrayList<Entry> getEntries(){
        return EnumLoop.makeList(getEntriesInStructure(), (entryInStructure -> entryInStructure.getEntry()));
    }



    public CachedString getCachedName(){
        return new LiteralString(name);
    }

    public String getType(){
        return type;
    }





    public GroupWidgetParam getWidgetParam(){
        return widgetParam;
    }

    public boolean isSpreadsheet() {
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }


    @Override
    public boolean equals(Object object){
        if( ! (object instanceof Structure structure))
            return false;
        if( ! Objects.equals(id, structure.id))
            return false;
        if( ! Objects.equals(type, structure.type))
            return false;
        if( ! Objects.equals(name, structure.name))
            return false;
        return true;
    }

    public EntryInStructure getEntryInStructure(EntryId entryId) {
        return entries.get(entryId);
    }

    public StructureId getId() {
        if(id == null){
            MainActivity.log("structure: " + getCachedName() +", has no id");
            throw new RuntimeException();
        }

        return id;
    }

    public String toString(){
        return "<structure> " + getCachedName().getString() + ", id: " + getId();
    }

    protected CachedString getEntryName(EntryInStructure entryInStructure) {
        MainActivity.log("getting entry name");

        ArrayList<WidgetInStructure> importantWidgetList = getImportantWidgets();
        ArrayList<CachedString> arrayStringList = new ArrayList<>();
        MainActivity.log("important widgets: " + importantWidgetList);
        for(WidgetInStructure widgetInStructure : importantWidgetList){

            ArrayList<BaseWidgetValue> widgetValueList = entryInStructure.getGroupValue().getValuesFromWidgetPath(widgetInStructure.getWidgetPath());
            ArrayString string = new ArrayString(EnumLoop.makeList(widgetValueList, (widgetValue)->widgetValue.getDisplayCachedString()));
            MainActivity.log("getting values from widgetId: " + widgetValueList);
            arrayStringList.add(string);
        }
        return new ArrayString(arrayStringList);
    }

    public WidgetInStructure getWidgetInStructureFromId(WidgetId widgetId){
        for(WidgetInStructure widgetInStructure: widgetMap){
            if(widgetInStructure.getWidgetId().equals(widgetId))
                return widgetInStructure;
        }
        throw new RuntimeException();
    }

    public ArrayList<WidgetInStructure> getImportantWidgets(){
        ArrayList<WidgetInStructure> result = new ArrayList<>();
        for(WidgetInStructure widgetInStructure : widgetMap){
            if(widgetInStructure.getWidgetParam().isUniqueAttribute)
                result.add(widgetInStructure);
        }
        return result;
    }

    public ArrayList<WidgetInStructure> getWidgetIdList() {
        ArrayList<WidgetInStructure> widgetInStructures = new ArrayList<>(widgetMap);
        for(WidgetInStructure widgetInStructure: widgetInStructures){
            if(widgetInStructure == null)
                throw new RuntimeException("widgetMap key set has null key(s)");
        }
        return widgetInStructures;
    }

    public ArrayList<RefEntryString> getReferenceLocationsOfSource(ArrayList<RefEntryString> sourcesToCheck, WidgetInStructure sourceWidget){
        ArrayList<RefEntryString> result = new ArrayList<>();
        ArrayList<WidgetInStructure> widgetThatRefTheSourceList = references(sourceWidget);
        for(WidgetInStructure widgetThatRefTheSource: widgetThatRefTheSourceList){
            ArrayList<RefEntryString> reference = getReferenceLocationsOfWidget(sourcesToCheck, widgetThatRefTheSource);
            result.addAll(reference);
        }
        return result;
    }

    public ArrayList<WidgetInStructure> references(WidgetInStructure sourceWidget){
        ArrayList<WidgetInStructure> references = new ArrayList<>();
        for(WidgetInStructure widgetInStructure: widgetMap){
            WidgetInStructure reference = widgetInStructure.getWidgetInfo().getReference();
            if(reference.equals(sourceWidget))
                references.add(reference);
        }
        return references;
    }

    public ArrayList<RefEntryString> getReferenceLocationsOfWidget(ArrayList<RefEntryString> sourcesToCheck, WidgetInStructure widgetThatRefTheSource){
        ArrayList<RefEntryString> referenceValues = getReferenceValuesOfWidget(widgetThatRefTheSource, getEntriesInStructure());
        ArrayList<RefEntryString> matchedReferenceValues = extractReferencesWhoseValuesMatch(sourcesToCheck, referenceValues);
        return referenceValues;
    }



    private static ArrayList<RefEntryString> getReferenceValuesOfWidget(WidgetInStructure widget, ArrayList<EntryInStructure> entries){
        if( ! (widget.getWidgetParam() instanceof DropDownParam))
            throw new RuntimeException();

        ArrayList<RefEntryString> result = new ArrayList<>();
        for(EntryInStructure entryInStructure : entries){
            ArrayList<BaseWidgetValue> valuesFromWidget = entryInStructure.getGroupValue().getValuesFromWidgetPath(widget.getWidgetPath());
            for(BaseWidgetValue baseWidgetValue: valuesFromWidget)
                result.add(baseWidgetValue.getReference(entryInStructure));
        }
        return result;
    }

    private static ArrayList<RefEntryString> extractReferencesWhoseValuesMatch(ArrayList<RefEntryString> sourceValues,
                                                                               ArrayList<RefEntryString> references){
        ArrayList<RefEntryString> referenceLocationResult = new ArrayList<>();
        for(RefEntryString reference: references){
            ArrayList<RefEntryString> valuesOfReference = reference.getRefEntryStringListOfValue();
            for(RefEntryString referencedValue: valuesOfReference){
                if(sourceValues.contains(referencedValue)){
                    referenceLocationResult.add(reference);
                    break;
                }
            }
        }
        return referenceLocationResult;
    }



}
