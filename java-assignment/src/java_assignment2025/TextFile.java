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
}
