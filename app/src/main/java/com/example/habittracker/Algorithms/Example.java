package com.example.habittracker.Algorithms;

import static com.example.habittracker.defaultImportPackage.DefaultImportClass.*;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class Example {
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Integer> intList = list.convert((index, string) -> {
        return string.hashCode();
    });
}
