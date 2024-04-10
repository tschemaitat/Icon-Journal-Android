package com.example.habittracker.Structs;

import androidx.annotation.NonNull;

import com.example.habittracker.StaticClasses.StringMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ItemPath implements Iterable<String>{
    private ArrayList<String> path;
    public ItemPath(ArrayList<String> path){
        this.path = path;
    }

    public ItemPath(String name){
        path = new ArrayList<>();
        path.add(name);
    }

    public ItemPath createNewPathAdd(String item){
        ArrayList<String> newList = (ArrayList<String>) path.clone();
        newList.add(item);
        return new ItemPath(newList);
    }

    public ArrayList<String> getStringPath(){
        ArrayList<String> strings = new ArrayList<>();
        for(String integer: path)
            strings.add(integer);
        return strings;
    }

    public String getName(){
        return path.get(path.size()-1);
    }


    @Override
    public boolean equals(Object object){
        if(object instanceof ItemPath){
            ItemPath item = (ItemPath) object;
            if(item.path.equals(path))
                return true;
        }
        return false;
    }

    public String toString(){
        return path.toString();
    }

    @NonNull
    @Override
    public Iterator<String> iterator() {
        return path.iterator();
    }

    @Override
    public void forEach(@NonNull Consumer<? super String> action) {
        Iterable.super.forEach(action);
        path.forEach(action);
    }

    @NonNull
    @Override
    public Spliterator<String> spliterator() {
        return path.spliterator();
    }
}
