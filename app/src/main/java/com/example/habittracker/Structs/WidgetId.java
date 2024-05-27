package com.example.habittracker.Structs;

import com.example.habittracker.Algorithms.ThrowableEqualsWithId;

import java.util.Objects;

public class WidgetId implements ThrowableEqualsWithId {
    private Integer id;

    public WidgetId(Integer id) {
        this.id = id;
    }

    public Integer getIntegerId() {
        return id;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }

    @Override
    public String toString(){
        return id.toString();
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof WidgetId widgetId))
            return false;
        if(!Objects.equals(widgetId.id, id))
            return false;
        return true;
    }


    public void equalsThrows(Object object){
        if( ! (object instanceof WidgetId widgetId))
            throw new RuntimeException(object.toString());
        if(!Objects.equals(widgetId.id, id))
            throw new RuntimeException("idFirst: " + widgetId.id + ", id second: " + id);
    }
}
