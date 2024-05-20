package com.example.habittracker.Structs;

import java.util.Objects;

public class WidgetId {
    private Integer id;

    public WidgetId(Integer id) {
        this.id = id;
    }

    public Integer getInteger() {
        return id;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof WidgetId widgetId))
            return false;
        if(!Objects.equals(widgetId.id, id))
            return false;
        return true;
    }
    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
}
