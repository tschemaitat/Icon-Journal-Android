package com.example.habittracker.Structs.CachedStrings;

public class LiteralString implements CachedString{
    private String string;
    public LiteralString(String string){
        this.string = string;
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof LiteralString){
            return ((LiteralString) object).getString().equals(string);
        }
        return false;
    }

    @Override
    public String toString(){
        return getString();
    }
}
