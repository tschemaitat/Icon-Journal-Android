package com.example.habittracker.Structs;

import androidx.annotation.NonNull;

import com.example.habittracker.Algorithms.Lists;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;

import com.example.habittracker.defaultImportPackage.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class RefItemPath implements Iterable<CachedString>{
    private ArrayList<CachedString> path;
    public RefItemPath(ArrayList<CachedString> path){
        this.path = path;
    }

    public RefItemPath(CachedString key){
        path = new ArrayList<>();
        path.add(key);
    }

    public ArrayList<CachedString> getPath(){
        return path;
    }

    public CachedString getLast(){
        return path.get(path.size() - 1);
    }

    public String toString(){
        return path.toString();
    }



    @NonNull
    @Override
    public Iterator<CachedString> iterator() {
        return path.iterator();
    }

    @Override
    public void forEach(@NonNull Consumer<? super CachedString> action) {
        Iterable.super.forEach(action);
        path.forEach(action);
    }

    @NonNull
    @Override
    public Spliterator<CachedString> spliterator() {
        return path.spliterator();
    }

    public ArrayList<String> getStringList() {
        return EnumLoop.makeList(path, (cachedString -> cachedString.getString()));
    }

    public int size() {
        return path.size();
    }

    public CachedString get(int i) {
        return path.get(i);
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof RefItemPath refItemPath))
            return false;

        if( !Objects.equals(refItemPath.path, path))
            return false;
        return true;
    }

    public void throwEquals(Object object) {
        if( ! (object instanceof RefItemPath refItemPath))
            throw new RuntimeException();
        Lists.equalsThrowsRecursive(path, refItemPath.path);
        if( ! this.equals(object))
            throw new RuntimeException();
    }

    public CachedString getArrayString() {
        return new ArrayString(path);
    }
}
