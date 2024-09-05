package com.example.habittracker.defaultImportPackage;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

public interface MutableListInterface<E> extends ListGetterInterface{
    boolean addAll(@NonNull ListGetterInterface<? extends E> list);
    boolean removeAll(@NonNull ListGetterInterface<? extends E> list);
    boolean remove(@Nullable E o);
}
