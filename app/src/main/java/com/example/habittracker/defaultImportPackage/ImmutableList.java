package com.example.habittracker.defaultImportPackage;

public interface ImmutableList<E> {
    E get(int index);
    int size();
    boolean contains(E element);
    boolean containsAny(ArrayList<E> elements);
    <T> ArrayList<T> convert(ArrayList.ConvertFunction<E, T> convertFunction);
    void iter(ArrayList.IterateFunction<E> function);
    void enumerate(ArrayList.EnumerateFunction<E> enumerateFunction);
}
