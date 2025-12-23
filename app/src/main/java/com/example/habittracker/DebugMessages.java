package com.example.habittracker;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class DebugMessages {
    private ArrayList<String> debugMessages = new ArrayList<>();
    public void add(String message){
        debugMessages.add(message);
    }

    public void printDebugMessages(){
        for(String message: debugMessages){
            MainActivity.log(message);
        }
    }
}
