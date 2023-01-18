import java.util.List;
import java.util.Objects;

public class Receipt {

    private final String store;
    private final List<Item> items;


    public Receipt(String store, List<Item> items) {
        this.store = store;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(store, receipt.store) && Objects.equals(items, receipt.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, items);
    }

    @Override
    public String toString() {
        return store + ": " +
                "\n" + Catalog.listToString(items).indent(4);
    }
}
