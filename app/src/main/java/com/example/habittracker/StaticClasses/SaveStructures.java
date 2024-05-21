package com.example.habittracker.StaticClasses;

import android.content.Context;

import com.example.habittracker.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class SaveStructures {
    public static final String structuresFileName = "structures";
    public static void saveStructureFile(Context context){
        MainActivity.log("\n\n\nsaving file\n\n\n");
        JSONObject structuresJSON = StructureTokenizer.getStructureArrayToken();

        String fileContents = null;
        try {
            fileContents = structuresJSON.toString(1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        deleteFile(structuresFileName, context);
        saveFile(structuresFileName, fileContents, context);
        String contentsLoaded = loadFile(structuresFileName, context);
        MainActivity.log("\nloaded contents:\n\n" + contentsLoaded);
    }

    public static void loadStructureFromFile(Context context){
        String contents = loadFile(structuresFileName, context);
        JSONObject structuresJSON = null;
        try {
            structuresJSON = new JSONObject(contents);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        StructureTokenizer.setStructures(structuresJSON);
    }

    public static void deleteStructuresFile(Context context){
        deleteFile(structuresFileName, context);
    }

    public static boolean hasSave(Context context){
        return fileExists(structuresFileName, context);
    }

    public static boolean deleteFile(String fileName, Context context) {
        return context.deleteFile(fileName);
    }

    public static boolean fileExists(String fileName, Context context) {
        File file = context.getFileStreamPath(fileName);
        return file != null && file.exists();
    }

    public static void printStructuresContents(Context context){
        MainActivity.log("\n\n\nloading file\n\n\n");
        String contents = loadFile(structuresFileName, context);
        try {
            JSONObject json = new JSONObject(contents);
            MainActivity.log(json.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveFile(String fileName, String fileContents, Context context){



        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String loadFile(String fileName, Context context) {
        FileInputStream inputStream;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString().trim();
    }
}
