import java.util.List;
import java.util.Objects;

public class Food implements Comparable<Food>{
    private static Double ageMultiplier = 1.0;
    private static Double costMultiplier = 1.0;
    private static Double caloriesMultiplier = 1.0;
    private static Double timeMultiplier = 1.0;
    private static Double tasteMultiplier = 1.0;
    private static Double complexityMultiplier = 1.0;
    private final String name;
    private int age;
    private final double cost;
    private final double kiloCalories;
    private final int minutesToMake;
    private final int tastiness;
    private final int complexityToMake;
    private final List<Buyable> ingredients;
    private final List<String> types;

    public Food(String name, int age, double cost, double kiloCalories, int minutesToMake,
                int tastiness, int complexityToMake, List<Buyable> ingredients,
                List<String > types) {
        this.name = name;
        this.age = age;
        this.cost = cost;
        this.kiloCalories = kiloCalories;
        this.minutesToMake = minutesToMake;
        this.tastiness = tastiness;
        this.complexityToMake = complexityToMake;
        this.ingredients = ingredients;
        this.types = types;
    }

    /**
     * Equals method
     *
     * @param o the reference object with which to compare.
     * @return whether the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return age == food.age &&
            Double.compare(food.cost, cost) == 0 &&
            kiloCalories == food.kiloCalories &&
            minutesToMake == food.minutesToMake &&
            tastiness == food.tastiness &&
            complexityToMake == food.complexityToMake &&
            Objects.equals(name, food.name) &&
            Objects.equals(ingredients, food.ingredients) &&
            Objects.equals(types, food.types);
    }

    /**
     * toString method
     *
     * @return a human-readable string
     */
    @Override
    public String toString() {
        String complexity;
        switch (complexityToMake) {
            case 1 -> complexity = "easy";
            case 2 -> complexity = "medium";
            default -> complexity = "hard";
        }
        return name + ", " + tastiness + "/10\n" +
            Math.round(cost * 100) / 100.0 + " €, " +
            complexity + ' ' + minutesToMake + " minutes";
    }

    /**
     * Creates a weight for the food, depending on its variables
     *
     * @return a Double representing the goodness of the food. Bigger better
     */
    public Double getWeight() {
        double weight =  age * age * ageMultiplier +
            Main.costStandardizer(cost) * costMultiplier +
            kiloCalories * caloriesMultiplier +
            Main.timeStandardizer(minutesToMake) * timeMultiplier +
            tastiness * tasteMultiplier +
            Main.complexityStandardizer(complexityToMake) * complexityMultiplier;
        return Math.max(weight, 0);
    }

    /**
     * Setter for ageMultiplier
     *
     * @param ageMultiplier how important is age
     */
    public static void setAgeMultiplier(Double ageMultiplier) {
        Food.ageMultiplier = ageMultiplier;
    }

    public static void setCostMultiplier(Double costMultiplier) {
        Food.costMultiplier = costMultiplier;
    }

    public static void setCaloriesMultiplier(Double caloriesMultiplier) {
        Food.caloriesMultiplier = caloriesMultiplier;
    }

    public static void setTimeMultiplier(Double timeMultiplier) {
        Food.timeMultiplier = timeMultiplier;
    }

    public static void setTasteMultiplier(Double tasteMultiplier) {
        Food.tasteMultiplier = tasteMultiplier;
    }

    public static void setComplexityMultiplier(Double complexityMultiplier) {
        Food.complexityMultiplier = complexityMultiplier;
    }

    public String name() {
        return name;
    }

    public int age() {
        return age;
    }

    public double cost() {
        return cost;
    }

    public double kiloCalories() {
        return kiloCalories;
    }

    public int minutesToMake() {
        return minutesToMake;
    }

    public int tastiness() {
        return tastiness;
    }

    public int complexityToMake() {
        return complexityToMake;
    }

    public List<Buyable> ingredients() {
        return ingredients;
    }

    public List<String> types() {
        return types;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, cost, kiloCalories, minutesToMake, tastiness,
            complexityToMake, ingredients, types);
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Food o) {
        return this.name.compareTo(o.name);
    }

}
