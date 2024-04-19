package com.example.habittracker.Structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class ImmutableList<T> implements Iterable<T> {
    private final ArrayList<T> list;

    public ImmutableList(Collection<T> collection) {
        this.list = new ArrayList<>(collection); // Create a copy for immutability
    }

    public T get(int index) {
        return list.get(index);
    }

    public T getLast(){
        return list.get(list.size() - 1);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    // You can add more delegation methods as needed
}