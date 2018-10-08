package main;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

// Gets items data out of the file and creates result file.
public class FileIO {
    private File inputFile;
    private File outputFile;
    private Scanner input = null;
    private PrintWriter output = null;

    // ask for a file from directory program is executed from
    public File chooseFile() {
        String executedDirectory = System.getProperty("user.dir");
        JFileChooser jFileChooser = new JFileChooser(executedDirectory);
        String selectedFilePath;
        int returnValue = jFileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            inputFile = jFileChooser.getSelectedFile();
            selectedFilePath = inputFile.getAbsolutePath();
            outputFile = new File(selectedFilePath.substring(0, selectedFilePath.length() - 4) + "_result.txt");
        } else {
            System.out.println("\nNo file selected.\n");
            System.exit(0);
        }
        try {
            input = new Scanner(inputFile);
            output = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputFile;
    }

    // write string to both output file and console
    public void write(String writeThisToFile) {
        System.out.print(writeThisToFile);
        output.write(writeThisToFile);
    }

    public void close() {
        output.close();
    }

    // parse and return an ArrayList of Items from the chosen file
    public ArrayList<Item> createListOfItems() {
        String currentLine;
        String[] splitLine;
        String name;
        ArrayList<Item> arrayList = new ArrayList<>();
        Item item;

        if (input != null) {
            while (input.hasNextLine()) {
                currentLine = input.nextLine();
                // split by any whitespace
                splitLine = currentLine.trim().split("\\s+");

                // set fields reading right to left
                int quantity = Integer.parseInt(splitLine[splitLine.length - 1]);
                int value = Integer.parseInt(splitLine[splitLine.length - 2]);
                int weight = Integer.parseInt(splitLine[splitLine.length - 3]);
                // reconstruct the name field that got split by whitespace
                name = "";
                for (int i = 0; i < splitLine.length - 3; i++) {
                    name += splitLine[i] + " ";
                }

                item = new Item(name.trim(), weight, value, quantity);

                arrayList.add(item);
            }
        } else {
            write("\nNo file selected.\n");
            System.exit(0);
        }
        return arrayList;
    }
}
