import java.util.Objects;

public record Item(String name, double cost, Amount amount, String store)
    implements Comparable<Item>{
    /**
     * toString method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return name + " " + amount + ": " + cost + " € (" + store + ")";
    }

    /**
     * Equals method
     * @param o   the reference object with which to compare.
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Double.compare(item.cost, cost) == 0 &&
            Objects.equals(name, item.name) &&
            Objects.equals(amount, item.amount) &&
            Objects.equals(store, item.store);
    }

    /**
     * Compares two Items based on their name
     * @param o the object to be compared.
     * @return smaller if smaller
     */
    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }
}
