package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Changes the input of items to work with the 0-1 knapsack algorithm.
 * Finds the optimal solution by creating a item/weight value table to track the best value
 * solution and a keep table to keep track of the items used in that solution.
 * Does this recursively by having each problem implicitly solve its sub-problems
 * and storing the calculated solution for lookup.
 * <p>
 * Sources:
 * Code source for recursive with value table and keep table:
 * http://www.programminglogic.com/knapsack-problem-dynamic-programming-algorithm/
 * <p>
 * Source for recursive with value table:
 * https://quickgrid.wordpress.com/2015/05/23/0-1-knapsack-iterative-and-recursive-with-code/
 * <p>
 * Source for recursive without table:
 * http://stackoverflow.com/a/14186622
 * http://codereview.stackexchange.com/q/45793
 * http://www.geeksforgeeks.org/dynamic-programming-set-10-0-1-knapsack-problem/
 * <p>
 * Understanding storing solution in keep table:
 * http://www.programminglogic.com/knapsack-problem-dynamic-programming-algorithm/
 * https://sadakurapati.wordpress.com/2013/11/30/algorithm-knapsack/
 * http://www.sanfoundry.com/java-program-knapsack-algorithm/
 * <p>
 * Understanding value table and keep table:
 * https://www.youtube.com/watch?v=EH6h7WA7sDw
 * http://www.es.ele.tue.nl/education/5MC10/Solutions/knapsack.pdf
 * http://cse.unl.edu/~goddard/Courses/CSCE310J/Lectures/Lecture8-DynamicProgramming.pdf
 * http://jhave.org/learner/misc/knapsack/knapsack.shtml
 */
public class Recursive {
    private int maxWeight;
    private int itemCount;
    private int numberOfRecursiveCalls = 0;

    private int K[][];
    private int keep[][];
    private int weight[];
    private int value[];
    private String name[];

    private HashMap<String, Integer> solution;
    private int totalValue;
    private int totalWeight;

    // Set up bounded knapsack input to work with 0-1 knapsack algorithm.
    public int boundedSolve(int maxWeight, ArrayList<Item> itemInput) {
        this.maxWeight = maxWeight;
        itemCount = 0;
        numberOfRecursiveCalls = 0;
        // count the individual solution
        for (int i = 0; i < itemInput.size(); i++) {
            itemCount += itemInput.get(i).getQuantity();
        }
        K = new int[itemCount + 1][this.maxWeight + 1];
        keep = new int[itemCount + 1][this.maxWeight + 1];
        weight = new int[itemCount + 1];
        value = new int[itemCount + 1];
        name = new String[itemCount + 1];
        // initialize value table
        for (int i = 0; i < itemCount + 1; i++) {
            for (int w = 0; w < this.maxWeight + 1; w++) {
                K[i][w] = 0;
            }
        }
        // put one copy of each item per quantity into weight and value arrays
        for (int i = 0, k = 0; i < itemInput.size(); i++) {
            for (int j = 0; j < itemInput.get(i).getQuantity(); j++, k++) {
                weight[k] = itemInput.get(i).getWeight();
                value[k] = itemInput.get(i).getValue();
                name[k] = itemInput.get(i).getName();
            }
        }

        // create solution
        int bestValueForWeight = solve(itemCount - 1, this.maxWeight);
        findItemsInSolution();
        return bestValueForWeight;
    }

    // Return the maximum value that can be put in a knapsack of capacity i + 1.
    private int solve(int i, int w) {
        numberOfRecursiveCalls++;

        if (i < 0 || w == 0) {
            return 0;
        }

        // Note: removing this makes program a purely recursive program.
        // if there is already a solution calculated, return it
        if (K[i][w] != 0) {
            return K[i][w];
        }

        // if weight of current item is more than will fit, don't take item
        if (weight[i] > w) {
            return K[i][w] = solve(i - 1, w);
        }

        // find the value if item i is taken and the value if it is not taken,
        // if it is taken then set the new best solution value in the value table K
        // and mark it as taken in the keep table keep
        else {
            int take = solve(i - 1, w - weight[i]) + value[i];
            int dontTake = solve(i - 1, w);
            if (take > dontTake) {
                keep[i][w] = 1;
                K[i][w] = take;
            } else {
                keep[i][w] = 0;
                K[i][w] = dontTake;
            }
            return K[i][w];
        }
    }

    // Find each item that is marked as kept and part of the optimal solution, add the item
    // to solution map with its quantity and update the total value and weight of solution.
    private void findItemsInSolution() {
        solution = new HashMap<>();
        totalValue = 0;
        totalWeight = 0;

        for (int i = itemCount, w = maxWeight; i >= 0; i--) {
            if (keep[i][w] == 1) {
                if (solution.containsKey(name[i])) {
                    solution.put(name[i], solution.get(name[i]) + 1);
                } else {
                    solution.put(name[i], 1);
                }
                totalValue += value[i];
                totalWeight += weight[i];
                w = w - weight[i];
            }
        }
    }

    // Print total weight, total value, and solution chosen for optimal solution.
    public String printResult() {
        String ret = "----"
                + "\nAs calculated by the recursive algorithm: "
                + "\nTotal weight of solution: " + totalWeight
                + "\nTotal value of solution: " + totalValue
                + "\nNumber of recursive calls: " + numberOfRecursiveCalls
                + "\nSet of items chosen and their quantity: "
                + "\n--"
                + "\n";

        for (Map.Entry<String, Integer> entry : solution.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            ret += key + " : " + value + "\n";
        }
        return ret;
    }
}
