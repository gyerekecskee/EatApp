import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FoodType {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack");

    private final String name;

    FoodType(String name){this.name = name;}

    /**
     * Getter for name
     * @return the name of the foodtype
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether a string is a type of food
     * @param s the string that we check
     * @return true if the string is a type of food, false otherwise
     */
    public static boolean contains(String s){
        for(FoodType type : FoodType.values()){
            if(type.getName().equals(s))
                return true;
        }
        return false;
    }

    /**
     * Makes a list of all the combinations of food-types, except of nothing
     * @return a list, of list of strings
     */
    public static List<List<String>> combinations(){
        List<List<String>> combinations = new ArrayList<>();
        for (int i = 1; i < Math.pow(2, FoodType.values().length); i++){
            List<String> current = new ArrayList<>();
            List<Boolean> code = multiplexer(i, FoodType.values().length);
            for (int j = 0; j < FoodType.values().length; j++){
                if (code.get(j))
                    current.add(FoodType.values()[j].getName());
            }
            combinations.add(current);
        }
        return combinations;
    }

    /**
     * Turns a number into a list with true and false values, like a real world multiplexer
     * @param b an integer representing the value in radix-2
     * @return a list of boolean values
     */
    public static List<Boolean> multiplexer(int b){
        int length = (int) Math.ceil(Math.log(b + 1) / Math.log(2));
        List<Boolean> output = new ArrayList<>();
        for (int i = length - 1; i >= 0; i--){
            if (b % 2 == 1){
                output.add(true);
                b--;
            } else
                output.add(false);
            b /= 2;
        }
        Collections.reverse(output);
        return output;
    }

    /**
     * Same as regular multiplexer just adds, a few falses to the end until outputs length is equal
     * to length
     * @param b an integer representing the value in radix-2
     * @param length how long the output should be
     * @return a list of boolean values
     */
    public static List<Boolean> multiplexer(int b, int length){
        List<Boolean> output = multiplexer(b);
        while (output.size() < length){
            output.add(0, false);
        }
        return output;
    }
}
