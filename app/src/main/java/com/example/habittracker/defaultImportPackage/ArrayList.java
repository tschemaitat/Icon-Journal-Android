package com.example.habittracker.defaultImportPackage;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import androidx.annotation.NonNull;

import com.example.habittracker.structurePack.EntryInStructure;

import java.util.Arrays;
import java.util.Collection;

public class ArrayList<E> extends java.util.ArrayList<E> implements ImmutableList<E>{
    public ArrayList(E... numbers) {
        super();
        this.addAll(Arrays.asList(numbers));
    }

    public ArrayList(Collection<E> values) {
        super();
        this.addAll(values);
    }

    public ArrayList() {
        super();
    }

    public void enumerate(EnumerateFunction<E> enumerateFunction){
        for(int i = 0; i < this.size(); i++){
            enumerateFunction.enumerateIteration(i, this.get(i));
        }
    }

    public boolean containsAny(ArrayList<E> elements) {
        for(E e: elements){
            if(this.contains(e))
                return true;
        }
        return false;

    }


    public interface EnumerateFunction<E>{
        void enumerateIteration(int index, E element);
    }

    public interface IterateFunction<E>{
        void iterate(E element);
    }

    public interface ConvertFunction<Input, Output>{
        Output convert(int index, Input element);
    }

    public <T> ArrayList<T> convert(ConvertFunction<E, T> convertFunction){
        ArrayList<T> result = new ArrayList<>();
        for(int i = 0; i < this.size(); i++){
            T converted = convertFunction.convert(i, this.get(i));
            result.add(converted);
        }
        return result;
    }

    public void iter(IterateFunction<E> function){
        for(E element: this){
            function.iterate(element);
        }
    }
}
