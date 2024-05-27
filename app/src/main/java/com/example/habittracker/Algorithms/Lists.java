package com.example.habittracker.Algorithms;

import android.util.ArraySet;

import com.example.habittracker.MainActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Lists {
    public static <T extends ThrowableEquals>void equalsThrowsRecursive(ArrayList<T> first, ArrayList<T> second){
        if(first.equals(second))
            return;
        if(first.size() != second.size())
            throw new RuntimeException("list sizes, first: " + first.size() + ", " + second.size());
        MainActivity.log("lists not equal");
        MainActivity.log(first.toString());
        MainActivity.log(second.toString());
        for(int i = 0; i < first.size(); i++){
            T firstItem = first.get(i);
            T secondItem = second.get(i);
            if( ! firstItem.equals(secondItem)){
                firstItem.equalsThrows(secondItem);
            }
        }


    }

    public static <T>void equalsThrows(ArrayList<T> first, ArrayList<T> second){
        if(first.equals(second))
            return;

        StringBuilder debugString = new StringBuilder();
        debugString.append("original list 1: (size: "+first.size()+")");
        debugString.append(stringNewLine(first));
        debugString.append("original list 2: (size: "+second.size()+")");
        debugString.append(stringNewLine(second));
        MainActivity.log(debugString.toString());
        throw new RuntimeException();
    }

    public static <T>void equalsThrowsAsSets(ArrayList<T> first, ArrayList<T> second){
        if(equalsWithOutOrder(first, second))
            return;

        StringBuilder debugString = new StringBuilder();
        debugString.append("original list 1: (size: "+first.size()+")");
        debugString.append(stringNewLine(first));
        debugString.append("original list 2: (size: "+second.size()+")");
        debugString.append(stringNewLine(second));
        MainActivity.log(debugString.toString());
        throw new RuntimeException();
    }

    public static <T extends ThrowableEqualsWithId>void equalsThrowsAsSetsRecursive(ArrayList<T> first, ArrayList<T> second){
        if(first.size() != second.size())
            throw new RuntimeException("list sizes, first: " + first.size() + ", " + second.size());

        if(equalsWithOutOrder(first, second))
            return;


        ArrayList<T> firstCopy = new ArrayList<>(first);
        ArrayList<T> secondCopy = new ArrayList<>(second);

        for(int i = firstCopy.size() - 1; i >= 0; i--){
            T firstItem = firstCopy.get(i);
            if(secondCopy.contains(firstItem)){
                int indexFound = secondCopy.indexOf(firstItem);
                T secondItemFound = secondCopy.get(indexFound);
                boolean foundItemSame1 = firstItem.equals(secondItemFound);
                boolean foundItemSame2 = secondItemFound.equals(firstItem);
                MainActivity.log("found second item from: " + firstItem + ", at index: " + indexFound
                +"found item: " +secondItemFound + ", equals1: " + foundItemSame1 + ", equals2: " + foundItemSame2);
                secondCopy.remove(firstItem);
                firstCopy.remove(firstItem);
            }
        }
        StringBuilder debugString = new StringBuilder();

        debugString.append("different items list 1: \n");
        debugString.append(stringNewLine(firstCopy) + "\ndifferent items list 2: \n" + stringNewLine(secondCopy));

        debugString.append("\noriginal list 1: (size: "+first.size()+")\n");
        debugString.append(stringNewLine(first));
        debugString.append("original list 2: (size: "+second.size()+")\n");
        debugString.append(stringNewLine(second));
        MainActivity.log(debugString.toString());

        for(T item: firstCopy){
            T itemWithSameId = getItemWithId(secondCopy, item.getIntegerId());
            item.equalsThrows(itemWithSameId);
        }

        throw new RuntimeException("lists didn't equal");
    }

    public static <T extends ThrowableEqualsWithId> T getItemWithId(ArrayList<T> list, Integer id){
        for(T item: list){
            if(item.getIntegerId().equals(id)){
                return item;
            }
        }
        return null;
    }


    public static boolean equalsWithOutOrder(ArrayList list1, ArrayList list2){
        ArrayList list1Copy = new ArrayList(list1);
        ArrayList list2Copy = new ArrayList(list2);
        if(list1.size() != list2.size())
            return false;
        for(int i = list1Copy.size()-1; i >= 0; i--){
            Object item = list1Copy.get(i);
            if(list2Copy.contains(item))
                list2Copy.remove(item);
            else
                return false;
        }
        if(list2Copy.size() == 0)
            return true;
        return false;
    }




    public static <T>String stringNewLine(ArrayList<T> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            T item = list.get(i);
            stringBuilder.append(item.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static <T>String stringNewLine(ArrayList<T> list, ItemStringFunction<T> function){
        return stringFunctionWithBoolean(list, function, true);
    }
    public static <T>String string(ArrayList<T> list, ItemStringFunction<T> function){
        return stringFunctionWithBoolean(list, function, false);
    }

    public static <T>String stringFunctionWithBoolean(ArrayList<T> list, ItemStringFunction<T> function, boolean newLine){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            T item = list.get(i);
            //stringBuilder.append(item.toString());
            stringBuilder.append(function.stringOfItem(item));
            if(newLine)
                stringBuilder.append("\n");
            else if(i != list.size()-1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    public interface ItemStringFunction<T>{
        String stringOfItem(T item);
    }
}
