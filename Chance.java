import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chance {

    private List<Double> weights;
    private final List<Food> foods;

    /**
     * Constructor for Chance
     * @param foods list of foods to be chosen from
     */
    public Chance(List<Food> foods){
        weights = new ArrayList<>();
        for (Food food : foods){
            Double weight = food.getWeight();
            weights.add(weight);
        }
        this.foods = foods;
    }

    private Double getSum(){
        Double sum = 0.0;
        for (Double weight : weights){
            sum += weight;
        }
        return sum;
    }

    /**
     * Calculates, which weight has been chosen
     * @return the index of the chosen weight
     */
    public int getResult(){
        Double top = getSum();
        Random random = new Random();
        double complexResult = random.nextDouble() * top;
        Double pointer = (double) 0;
        int simpleResult = -1;
        for (Double weight : weights){
            if (complexResult >= pointer){
                simpleResult++;
                pointer += weight;
            }
        }
        return simpleResult;
    }

    /**
     * Recalculates the weights of the foods.
     */
    public void refreshChances(){
        weights = new ArrayList<>();
        for (Food food : foods){
            Double weight = food.getWeight();
            weights.add(weight);
        }
    }

    /**
     * toString method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return "Chance{" +
            "weights=" + weights +
            ", foods=" + foods +
            '}';
    }
}
