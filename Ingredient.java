import java.util.Objects;

public class Ingredient implements Comparable<Ingredient>{
    //TODO toString should round to two digits
    private final String name;
    private Rational averagePrice;
    private final Measurement measurement;
    private int kiloCalories;

    /**
     * Constructor for ingredient, when kiloCalories is unknown.
     * @param name name of the ingredient
     * @param measurement measurement of the ingredient
     * @param averagePrice the weighted average price of the ingredient
     */
    public Ingredient(String name, Rational averagePrice, Measurement measurement) {
        this.name = name;
        this.measurement = measurement;
        this.averagePrice = averagePrice;
    }

    /**
     * Constructor for ingredient, when kiloCalories are known.
     * @param name name of the ingredient
     * @param averagePrice the weighted average price of the ingredient
     * @param measurement the measurement of the ingredient
     * @param kiloCalories the amount of kiloCalories that 100 of the
     *                     measurement of the ingredient provides
     */
    public Ingredient(String name, Rational averagePrice, Measurement measurement,
                      int kiloCalories) {
        this.name = name;
        this.averagePrice = averagePrice;
        this.measurement = measurement;
        this.kiloCalories = kiloCalories;
    }

    /**
     * Getter for name
     * @return name of the ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for kilocalories
     * @return how many kilocalories ar in 100 grams of the ingredient
     */
    public int getKiloCalories() {
        return kiloCalories;
    }

    /**
     * Getter for average price
     * @return a weighted average
     */
    public Rational getAveragePrice() {
        return averagePrice;
    }

    /**
     * Setter for average price
     * @param averagePrice a weighted average
     */
    public void setAveragePrice(Rational averagePrice) {
        this.averagePrice = averagePrice;
    }

    /**
     * Getter for measurement
     * @return the measurement of the food
     */
    public Measurement getMeasurement() {
        return measurement;
    }

    /**
     * Equals method
     * @param o the other object which we compare with
     * @return whether the two objects re equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name);
    }

    /**
     * Hashcode method
     * @return a hashed int
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, averagePrice, measurement, kiloCalories);
    }

    private static String roundToTwo(Double number){
        if (number == null){
            return "null";
        }
        return String.valueOf((Math.round(number * 100) / 100.0));
    }

    /**
     * ToString method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return name + ", " +
            "average price: " + roundToTwo(averagePrice.value()) + " €/" + measurement + ", "
            + kiloCalories + "kcal/100 " + measurement;
    }

    /**
     * Compares this to o and decides which one is better
     * @param o the object to be compared.
     * @return smaller when worse
     */
    @Override
    public int compareTo(Ingredient o) {
        return this.name.compareTo(o.name);
    }

    public String toSimpleString(){
        return name;
    }

}
