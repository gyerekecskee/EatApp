import java.util.Objects;

/**
 * A class that can be used to specify the conversion rate between two measurements,
 * that are not of the same type.
 * @param ingredient the ingredient in which we specify the conversion
 * @param measurement the measurement that we convert to
 * @param multiplier the number which we have to divide the measurement of the ingredient
 *                   to get to the measurement parameter
 */
public record Conversion(Ingredient ingredient, Measurement measurement, double multiplier)
    implements Comparable<Conversion>{

    /**
     * Equals method for Conversion
     * @param o   the reference object with which to compare.
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversion that = (Conversion) o;
        return Objects.equals(ingredient, that.ingredient) &&
            Objects.equals(measurement, that.measurement);
    }

    /**
     * Hashcode method
     * @return a hashed int
     */
    @Override
    public int hashCode() {
        return Objects.hash(ingredient, measurement);
    }

    /**
     * toStrong method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return multiplier + " " + measurement +
            " = 1 " + ingredient.getMeasurement() + " of " + ingredient.getName();
    }

    /**
     * Compares two Conversions putting them in abc order
     * @param o the object to be compared.
     * @return an integer, negative when this is "smaller" than o
     */
    @Override
    public int compareTo(Conversion o) {
        return this.ingredient.compareTo(o.ingredient);
    }
}
