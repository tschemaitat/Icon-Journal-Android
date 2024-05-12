package com.example.habittracker.StaticClasses;

import android.content.Context;

import com.example.habittracker.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class SaveStructures {
    public static void save(Context context){
        MainActivity.log("\n\n\nsaving file\n\n\n");
        JSONArray structuresJSON = StructureTokenizer.getStructureArrayToken();

        String fileName = "structures";
        String fileContents = structuresJSON.toString();
        saveFile(fileName, fileContents, context);
    }

    public static void load(Context context){
        MainActivity.log("\n\n\nloading file\n\n\n");
        String fileName = "structures";
        String contents = loadFile(fileName, context);
        try {
            JSONArray jsonArray = new JSONArray(contents);
            MainActivity.log(jsonArray.toString());
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
