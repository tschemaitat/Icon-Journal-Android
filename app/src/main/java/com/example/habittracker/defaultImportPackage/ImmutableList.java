package com.example.habittracker.defaultImportPackage;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import java.util.Iterator;

public class ImmutableList<E> implements ListGetterInterface<E> {
    private ArrayList<E> protectedList;
    public ImmutableList(ArrayList<E> list){
        this.protectedList = list;
    }

    @Override
    public E get(int index) {
        return protectedList.get(index);
    }

    @Override
    public int size() {
        return protectedList.size();
    }

    @Override
    public boolean containsAny(ListGetterInterface<E> elements) {
        return protectedList.containsAny(elements);
    }

    @Override
    public boolean contains(E element) {
        return protectedList.contains(element);
    }

    @Override
    @CheckResult
    public <T> ArrayList<T> convert(ArrayList.ConvertFunction<E, T> convertFunction) {
        return protectedList.convert(convertFunction);
    }

    @Override
    public void iter(ArrayList.IterateFunction<E> function) {
        protectedList.iter(function);
    }

    @Override
    public void enumerate(ArrayList.EnumerateFunction<E> enumerateFunction) {
        protectedList.enumerate(enumerateFunction);
    }

    @Override
    public boolean isEmpty() {
        return protectedList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return protectedList.iterator();
    }

    @Override
    public boolean containsAll(@NonNull ListGetterInterface<? extends E> list) {
        return protectedList.containsAll(list);
    }




}
