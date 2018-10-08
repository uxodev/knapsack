package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

// Asks for a file of items, then finds the optimal solution to the bounded knapsack problem
// (most valuable items) and the items used, both recursively and dynamically.

// Enter the maximum knapsack weight with system console input.

// Input item format:
// name weight value quantity
// name weight value quantity
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileIO fileIO = new FileIO();

        System.out.println("Finds the optimal solution to the bounded knapsack problem " +
                "and the items used, both recursively and dynamically.");
        System.out.println("Select file with item data:");

        File file = fileIO.chooseFile();
        ArrayList<Item> input = fileIO.createListOfItems();
        fileIO.write("File used: " + file.getName() + "\n");

        System.out.print("Enter the maximum weight you can carry in your knapsack: \n");
        Integer maxWeight = scanner.nextInt();

        fileIO.write("Maximum weight for knapsack: " + maxWeight + "\n");

        Recursive recursive = new Recursive();
        recursive.solveBounded(maxWeight, input);
        fileIO.write(recursive.printResult());

        Dynamic dynamic = new Dynamic();
        dynamic.solveBounded(maxWeight, input);
        fileIO.write(dynamic.printResult());

        fileIO.close();
    }
}
