import java.util.Objects;
import java.util.Scanner;

import static java.lang.Character.isDigit;

public final class Amount {
    private Double number;
    private final Measurement measurement;

    public Amount(Double number, Measurement measurement) {
        this.number = number;
        this.measurement = measurement;
    }

    /**
     * Parses a text to an amount
     *
     * @param input   the string ti be parsed
     * @param catalog the database
     * @param term    System.in
     * @return a new Amount
     */
    public static Amount parseAmount(String input, Catalog catalog, Scanner term) {
        Pair<String, String> pair = amountBreaker(input);
        if (pair == null) return null;
        String name = pair.getFirst();
        String amount = pair.getSecond();
        if (catalog.notInMeasurements(new Measurement(name, 0))) {
            System.out.println("We don't have " + name + " yet, you will have to add it.");
            Main.takeMeasurement(catalog, term);
        }
        Measurement measurement = catalog.getMeasurement(name);
        Double dAmount;
        if (amount != null) {
            dAmount = Double.parseDouble(amount);
        } else
            dAmount = null;

        return new Amount(dAmount, measurement);
    }

    private static Pair<String, String> amountBreaker(String input) {
        if (input.equals("?") || input.equals("null")) {
            return null;
        }
        StringBuilder amount = new StringBuilder();
        StringBuilder name = new StringBuilder();
        if (input.startsWith("null")) {
            return new Pair<>(input.substring(4), null);
        }
        if (input.startsWith("?")) {
            return new Pair<>(input.substring(1), null);
        }

        for (int i = 0; i < input.length(); i++) {
            if (isDigit(input.charAt(i)) || input.charAt(i) == '.') {
                amount.append(input.charAt(i));
            } else {
                name.append(input.charAt(i));
            }
        }
        return new Pair<>(name.toString(), amount.toString());
    }

    /**
     * toString method
     *
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return number + " " + measurement;
    }

    public String toSimpleString(){
        return Math.round(number * 100) / 100.0 + " " + measurement;
    }

    /**
     * equals method
     *
     * @param o the reference object with which to compare.
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Double.compare(amount.number, number) == 0 &&
            Objects.equals(measurement, amount.measurement);
    }

    /**
     * Converts the amount to another measurement
     *
     * @param measurement the specified measurement
     * @return the converted amount
     */
    public Amount convertTo(Measurement measurement) {
        if (!this.measurement.sameTypeAs(measurement))
            throw new IllegalArgumentException();
        return new Amount(number / this.measurement().multiplier() *
            measurement.multiplier(), measurement);
    }

    public Double number() {
        return number;
    }

    public Measurement measurement() {
        return measurement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, measurement);
    }

    public void add(Amount addable){
        if (!measurement.sameTypeAs(addable.measurement)){
            throw new IllegalArgumentException();
        }
        addable = addable.convertTo(measurement);
        number += addable.number;
    }

    public Amount copy(){
        return new Amount(number, measurement);
    }
}
