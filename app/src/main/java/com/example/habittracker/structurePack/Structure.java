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
import com.example.habittracker.Values.WidgetValueString;
import com.example.habittracker.Values.WidgetValueStringPath;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.GroupWidgetParam;

import com.example.habittracker.defaultImportPackage.ArrayList;
import com.example.habittracker.defaultImportPackage.ImmutableList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private StructureId id;
    private String name;
    private GroupWidgetParam widgetParam;
    private String type;
    private HashMap<EntryId, GroupValue> entries;
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
        if(widgetParam == null)
            throw new RuntimeException();
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
        //EntryInStructure entryInStructure = new EntryInStructure(entry.getGroupValue(), entry.getId(), this);
        entries.put(entry.getId(), entry.getGroupValue());
    }

    public void addEntry(GroupValue entryData){
        Set<EntryId> entryIdSet = entries.keySet();
        while(entryIdSet.contains(new EntryId(idCount)))
            idCount++;
        EntryInStructure entryInStructure = new EntryInStructure(new EntryId(idCount), this);
        idCount++;
        entries.put(entryInStructure.getId(), entryData);
    }

    public void editEntry(EntryInStructure entryInStructureToEdit, GroupValue data){
        EntryId entryId = entryInStructureToEdit.getId();
        if(entries.get(entryInStructureToEdit.getId()) == null)
            throw new RuntimeException();
        entries.put(entryId, data);
    }
    //assume no sources were delete (handled as delete) unconnect if it is a reference
    //disconnect references from their source
    public void disConnectValues(ImmutableList<BaseWidgetValue> baseWidgetValues){
        for(BaseWidgetValue baseWidgetValue: baseWidgetValues){
            WidgetValueString widgetValueString = (WidgetValueString) baseWidgetValue;
            CachedString cachedString = widgetValueString.getDebugCachedString();
            if(cachedString instanceof RefEntryString refEntryString){
                refEntryString.disconnectFromSource();
            }
        }
    }





    public ArrayList<EntryInStructure> getEntryList(){
        Structure thisStructure = this;
        return new ArrayList<>(entries.entrySet()).convert(new ArrayList.ConvertFunction<Map.Entry<EntryId, GroupValue>, EntryInStructure>() {
            @Override
            public EntryInStructure convert(int index, Map.Entry<EntryId, GroupValue> element) {
                return new EntryInStructure(element.getKey(), thisStructure);
            }
        });
    }

    public ArrayList<GroupValue> getData(){
        ArrayList<GroupValue> entryValueTrees = new ArrayList<>();
        for(EntryInStructure entryInStructure : getEntryList())
            entryValueTrees.add(entryInStructure.getGroupValue());
        return entryValueTrees;
    }

    public ArrayList<EntryInStructure> getEntriesInStructure(){
        return getEntryList();
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
        if(widgetParam == null)
            throw new RuntimeException();
        return widgetParam;
    }

    public boolean isSpreadsheet() {
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }




    public EntryInStructure getEntryInStructure(EntryId entryId) {
        if(entries.get(entryId) == null )
            throw new RuntimeException();
        return new EntryInStructure(entryId, this);
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

    public String getNameAndId(){
        return "<" + getCachedName().getString() + ", " + getId() + ">";
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
        ArrayList<WidgetInStructure> widgetThatRefTheSourceList = getWidgetsThatRefSource(sourceWidget);
        for(WidgetInStructure widgetThatRefTheSource: widgetThatRefTheSourceList){
            ArrayList<RefEntryString> reference = getReferenceLocationsOfWidget(sourcesToCheck, widgetThatRefTheSource);
            result.addAll(reference);
        }
        return result;
    }

    public ArrayList<WidgetInStructure> getWidgetsThatRefSource(WidgetInStructure sourceWidget){
        ArrayList<WidgetInStructure> references = new ArrayList<>();
        ArrayList<String> foundLogs = new ArrayList<>();
        for(WidgetInStructure widgetInStructure: widgetMap){
            ArrayList<WidgetInStructure> referencesOfWidget = widgetInStructure.getWidgetInfo().getReferences();
            if(referencesOfWidget.contains(sourceWidget)){
                foundLogs.add(widgetInStructure.nameAndStructure() + " references structure: " + widgetInStructure.getStructure().name +
                        ", widgets: " + EnumLoop.makeList(referencesOfWidget, (widget)->widget.getName().getString()));
                references.add(widgetInStructure);
            }

        }
        if(foundLogs.size() != 0){
            MainActivity.log("found reference to source: " + sourceWidget.nameAndStructure());
            for(String string: foundLogs)
                MainActivity.log("\t" + string);
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

    public void equalsThrows(Object object) {
        if( ! (object instanceof Structure structure))
            throw new RuntimeException();
        id.equalsThrows(structure.id);
        if( ! type.equals(structure.type))
            throw new RuntimeException();
        if( ! Objects.equals(name, structure.name))
            throw new RuntimeException();
    }

    public String getWidgetsInfoString() {
        StringBuilder stringBuilder = new StringBuilder("widget info dump from structure: " + getNameAndId());
        for(WidgetInStructure widget: widgetMap){
            stringBuilder.append(widget.getName() +", "+widget.getWidgetId() + "\n");
        }
        return stringBuilder.toString();
    }

    public GroupValue getDataFromEntryId(EntryId id) {
        if(entries.get(id) == null)
            throw new RuntimeException();
        return entries.get(id);
    }
}
