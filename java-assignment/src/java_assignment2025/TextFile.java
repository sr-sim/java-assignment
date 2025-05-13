/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TextFile {
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


    public void rewriteFile(String fileName, List<?> objectList) {
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

}
