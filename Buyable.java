import java.util.NoSuchElementException;
import java.util.Objects;

public class Buyable {

    //TODO convert this to something not bullshit

    private final Ingredient ingredient;
    private Amount amount;

    public Buyable(Ingredient ingredient, Amount amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Amount getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buyable buyable = (Buyable) o;
        return Objects.equals(ingredient, buyable.ingredient) && Objects.equals(amount, buyable.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, amount);
    }

    @Override
    public String toString() {
        return amount + " of " + ingredient;
    }

    public String toSimpleString(){
        return amount.toSimpleString() + " of " + ingredient.toSimpleString();
    }

    public Buyable copy(){
        return new Buyable(ingredient,  amount.copy());
    }

    public void generalize(){
        while (amount.number() < 1){
            try {
                amount = amount.convertTo(amount.measurement().previousMeasurement());
            }catch (NoSuchElementException e){
                return;
            }
        }
    }

}
