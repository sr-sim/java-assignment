/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class TextFile {
    
    //additional file check, opy is using it now but later remove ba
    private static boolean checkFileExists(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            return true;
        }
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            return file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error: Failed to create file at " + filepath);
            return false;
        }
    }
    
    public static void appendTo(String fileName, String appendWord){
        try {
            File file = new File(fileName);
            boolean isempty = file.length() == 0;
            FileWriter myWriter = new FileWriter(file, true);
            if (!isempty){
                 myWriter.write(System.lineSeparator());
            }
            myWriter.write(appendWord);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file");
        }
    }

    public static List<String> readFile(String fileName) {
        List<String> list = new ArrayList<>();
        File data = new File(fileName);
        if(!data.exists()){
            System.out.println("file does not exist");
            return list;
        }
        try {
            Scanner lines = new Scanner(data);
            while (lines.hasNextLine()){
                String line = lines.nextLine().strip();
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void replaceLine(String fileName, String oldLine, String newLine){
        List<String> list = readFile(fileName);
        int size = list.size();
        int i = 1;
        try {
            FileWriter myWriter = new FileWriter(fileName);
            for (String line : list){
                if (line.equals(oldLine)){
                    myWriter.write(newLine);
                }
                else {
                    myWriter.write(line);
                }
                if (i<size) {
                    myWriter.write(System.lineSeparator());
                    i++;
                }
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error");
        }
        System.out.println("Wrote to absolute path: " + new File(fileName).getAbsolutePath());


    }

    public static void deleteLine(String fileName, String targetLine){
        List<String> list = readFile(fileName);
        int size = list.size();
        int i = 1;
        try {
            FileWriter myWriter = new FileWriter(fileName);
            for (String line : list){
                if (!line.equals(targetLine)){
                    myWriter.write(line);
                    if (i<size-1) {
                        myWriter.write(System.lineSeparator());
                        i++;
                    }
                }
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error wrriting to file");
        }
    }
    public static void rewriteFile(String fileName, List<?> objectList) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            for (int i = 0; i < objectList.size(); i++) {
                Object obj = objectList.get(i);
                myWriter.write(obj.toString());
                if (i < objectList.size() - 1) {
                    myWriter.write(System.lineSeparator());
                }
            }
            myWriter.close();
            System.out.println("File successfully rewritten with updated data.");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
    
    //not sure if u guys want to use this, but i prefer not to override to.String() method for text file writing purposes so imma put it here
    //to.String() method is usually used for debugging purposes but if u guys think this is messy then pls lemme know, i will use the toSring()
    //Info about this method: 
    //<E> is used here instead of "?" because I want to ensure Collection<> object type is exactly same as Function<> input type
    //Collection is basically just the "parent" of Lists/ArrayLists or Sets, so later can pass Lists of whatever object types 
    //Function<(input type), (output type)> function, is actually just to store a lambda expression function and execute it on the input to return output.
    //There are other similar ones as well but with different purposes, like Consumer<(input type)> & Supplier<(output type)>
    //Consumer<(input type)> function, also takes in a lambda expression (the funciton) and apply on the input, but returns no output
    //Supplier<(output type)> function, takes in a lambda expression (the funciton) and run the function wihtout an input, then returns an output
    //sry for the long explanations, anyways pls lemme know if this is messy, then i will change the separator character in text file & override toString method
    public static <E> void writeToFile(String filePath, Collection<E> data, Function<E, String> lineFormatter) {
        if (!checkFileExists(filePath)) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (E item : data) {
                writer.write(lineFormatter.apply(item));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
    
    public static void replaceLineByPOId(String fileName, String orderId, String newLine) {
    try {
        List<String> lines = readFile(fileName);
        List<String> updatedLines = new ArrayList<>();
        boolean replaced = false;
        

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith(orderId + ",")) {
                System.out.println("found PO id: " + orderId);
                updatedLines.add(newLine);
                replaced = true;
            } else {
                updatedLines.add(line);
            }
        }

        // Write updated list back to file
        FileWriter writer = new FileWriter(fileName, false);
        for (String line : updatedLines) {
            writer.write(line + System.lineSeparator());
        }
        writer.close();

        if (!replaced) {
            System.out.println("No PO ID: " + orderId);
        } else {
            System.out.println("file updated.");
        }

    } catch (IOException e) {
        System.out.println("file write error:");
        e.printStackTrace();
    }
}
    
}


