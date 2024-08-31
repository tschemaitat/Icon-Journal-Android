package com.example.habittracker.defaultImportPackage;

import java.util.Collection;

public interface ImmutableList<E> extends Collection<E> {
    E get(int index);
    int size();
    boolean contains(Object element);
    boolean containsAny(ArrayList<E> elements);
    <T> ArrayList<T> convert(ArrayList.ConvertFunction<E, T> convertFunction);
    void iter(ArrayList.IterateFunction<E> function);
    void enumerate(ArrayList.EnumerateFunction<E> enumerateFunction);

}
