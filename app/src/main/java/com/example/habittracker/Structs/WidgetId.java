package com.example.habittracker.Structs;

import java.util.ArrayList;

public class WidgetId {
    private int id;
    private Structure structure;
    public WidgetId(int id, Structure structure){
        this.id = id;
        this.structure = structure;
    }

    public int getId(){
        return id;
    }

    public String toString(){
        return "tree id: " + id;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof WidgetId){
            WidgetId widgetId = ((WidgetId) object);
            if(widgetId.id == id)
                return true;
        }
        return false;
    }

    public ItemPath getNameWithPath(){
        return structure.getHeader().getWidgetNamePath(this);
    }
}
