package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Finds the optimal solution to the bounded knapsack problem by keeping
 * track of the best solution for the current weight and recording the
 * items used for this solution.
 * It does this dynamically by recording the best solution for
 * previous weights to be used to find the solution for subsequent weights.
 * <p>
 * Sources:
 * Main source for code:
 * https://www.codeproject.com/Articles/706838/Bounded-Knapsack-Algorithm
 * <p>
 * Understanding bottom-up solution to 0-1 knapsack problem:
 * https://www.youtube.com/watch?v=EH6h7WA7sDw
 * http://www.es.ele.tue.nl/education/5MC10/Solutions/knapsack.pdf
 * http://cse.unl.edu/~goddard/Courses/CSCE310J/Lectures/Lecture8-DynamicProgramming.pdf
 * http://jhave.org/learner/misc/knapsack/knapsack.shtml
 */
public class Dynamic {
    private ItemCollection[] bestValueForWeight;
    private int maxWeight;

    public ItemCollection solveBounded(int maxWeight, ArrayList<Item> itemInput) {
        this.maxWeight = maxWeight;
        bestValueForWeight = new ItemCollection[this.maxWeight + 1];
        for (int i = 0; i < this.maxWeight + 1; i++) {
            bestValueForWeight[i] = new ItemCollection();
        }
        ItemCollection remainingWeight = new ItemCollection();

        // for each item i, for each weight from the maximal weight down w
        for (int i = 0; i < itemInput.size(); i++) {
            for (int w = this.maxWeight; w >= 0; w--) {
                // if at least one if the item i will fit in the weight w
                if (w >= itemInput.get(i).getWeight()) {
                    // the most that can be added is either all of the quantity of the item
                    // or as many as will fit, whichever is lower
                    int maxQuantityFit = Math.min(itemInput.get(i).getQuantity(), w / itemInput.get(i).getWeight());
                    // check each quantity from 1 to the max quantity that would fit
                    for (int q = 1; q <= maxQuantityFit; q++) {
                        // bestValueForWeight[w - q * items.get(i).weight] is the optimal
                        // solution to the remaining weight after item i is added q times
                        remainingWeight = bestValueForWeight[w - q * itemInput.get(i).getWeight()];
                        // find the value of the optimal solution to the remaining weight
                        // after item i is added q times plus the value of adding i q times.
                        // this is the solution to the current subproblem.
                        int testValue = remainingWeight.getTotalValue() + q * itemInput.get(i).getValue();
                        // if the the value of the current solution is more than the
                        // value of the highest so far for this weight w,
                        // then this solution is the new best solution for this weight w
                        if (testValue > bestValueForWeight[w].getTotalValue()) {
                            // clone list of best solution for the remaining weight
                            // and add the item and quantity being considered
                            // into the record as the current best this weight w
                            bestValueForWeight[w] = remainingWeight.copy();
                            bestValueForWeight[w].addItem(itemInput.get(i), q);
                        }
                    }
                }
            }
        }
        return bestValueForWeight[this.maxWeight];
    }

    // Print total weight, total value, and solution chosen for optimal solution.
    public String printResult() {
        String ret = "----"
                + "\nAs calculated by the dynamic algorithm: "
                + "\nTotal weight of solution: " + bestValueForWeight[maxWeight].getTotalWeight()
                + "\nTotal value of solution: " + bestValueForWeight[maxWeight].getTotalValue()
                + "\nSet of items chosen and their quantity: "
                + "\n--"
                + "\n";

        for (Map.Entry<String, Integer> entry : bestValueForWeight[maxWeight].getItems().entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            ret += key + " : " + value + "\n";
        }
        return ret;
    }
}

// Keeps a list of items and their quantity, total weight, and total value.
// For keeping track of what items are in the best solution.
class ItemCollection {
    private HashMap<String, Integer> items = new HashMap<>();
    private int totalWeight = 0;
    private int totalValue = 0;

    void addItem(Item item, int quantity) {
        if (items.containsKey(item.getName())) {
            items.put(item.getName(), items.get(item.getName()) + quantity);
        } else {
            items.put(item.getName(), quantity);
        }
        totalWeight += quantity * item.getWeight();
        totalValue += quantity * item.getValue();
    }

    HashMap<String, Integer> getItems() {
        return items;
    }

    int getTotalWeight() {
        return totalWeight;
    }

    int getTotalValue() {
        return totalValue;
    }

    ItemCollection copy() {
        ItemCollection copy = new ItemCollection();
        copy.items = new HashMap<>(this.items);
        copy.totalValue = this.totalValue;
        copy.totalWeight = this.totalWeight;
        return copy;
    }
}
