package com.example.datemanager;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Scanner;

public class FileManager {
    public static String resourcePath = ViewManager.getInstance().mainActivity.getDir("dataManager", Context.MODE_PRIVATE).getAbsolutePath();

    public static <T> T loadFromJson(Type typeOfData, String path) {
        T output;
        StringBuilder jsonData = new StringBuilder();
        try (Scanner file = new Scanner(new FileReader(resourcePath + path))) {
            while (file.hasNext())
                jsonData.append(file.next());
        } catch (IOException e) {
            System.out.println("File_System : load file from " + resourcePath + path + " fail");
            return null;
        }

        output = new Gson().fromJson(jsonData.toString(), typeOfData);
        return output;
    }

    public static <T> void saveAsJson(T saveObject , String path) {
        String jsonObject = new Gson().toJson(saveObject);
        System.out.println(jsonObject);
        PrintWriter printWriter = null;
        try {
            File file = new File(resourcePath + path);
            if (!file.exists()) {
                file.createNewFile();
            }
            printWriter = new PrintWriter(file);
            printWriter.write(jsonObject);
            printWriter.flush();
        } catch (IOException e) {
            System.out.println("File_System : save file to " + resourcePath + path + " fail , please check the folder");
        } finally {
            printWriter.close();
        }
    }
}
