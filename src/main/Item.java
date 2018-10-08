package main;

public class Item {
    private String name;
    private int weight;
    private int value;
    private int quantity;

    public Item(String name, int weight, int value, int quantity) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", value=" + value +
                ", quantity=" + quantity +
                '}';
    }
}
