package com.example.habittracker.defaultImportPackage;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public interface ListGetterInterface<E> extends Iterable{
    E get(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    boolean containsAny(ListGetterInterface<E> elements);
    boolean containsAll(@NonNull ListGetterInterface<? extends E> list);

    <T> ArrayList<T> convert(ArrayList.ConvertFunction<E, T> convertFunction);
    void iter(ArrayList.IterateFunction<E> function);
    void enumerate(ArrayList.EnumerateFunction<E> enumerateFunction);

    @NonNull
    Iterator<E> iterator();


}
