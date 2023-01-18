import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Menu {

    //TODO european measurements
    //TODO nice shopping list
    //TODO Corporate chance into menu

    private final List<Food> foods;
    private final List<Buyable> shoppingList;


    /**
     *Initializes all the fields with empty Arraylists
     */
    public Menu(){
        foods = new ArrayList<>();
        shoppingList = new ArrayList<>();
    }

    /**
     * Adds a food object to the menu and its ingredients to the shopping list, updates the chances
     * @param foods list of foods to be drawn from
     * @param chance list of weights
     */
    public void addFood(List<Food> foods, Chance chance){
        Food food = foods.get(chance.getResult());
        addFood(food, chance);
    }

    public void addFood(Food food, Chance chance) {
        this.foods.add(food);
        for (Buyable buyable : food.ingredients()){
            Buyable buyableCopy = buyable.copy();
            if (!buyableCopy.getAmount().measurement().sameTypeAs(buyableCopy.getIngredient()
                .getMeasurement())){
                Conversion conversion = Main.getCatalog().getConversion(buyableCopy.getIngredient(),
                    Main.getCatalog().getMeasurementType(buyableCopy.getAmount().measurement()));
                buyableCopy = new Buyable(buyableCopy.getIngredient(), new Amount(buyableCopy
                    .getAmount().number() / conversion.multiplier(),
                    buyableCopy.getIngredient().getMeasurement()));
            }
            if (contains(buyableCopy.getIngredient())){
                getBuyable(buyableCopy.getIngredient()).getAmount().add(buyableCopy.getAmount());
            }else {
                shoppingList.add(buyableCopy);
            }
        }
        Main.getCatalog().ageFood(food);
        chance.refreshChances();
    }

    public void generalizeBuyables(){
        for (Buyable buyable : shoppingList){
            buyable.generalize();
        }
    }

    /**
     * Returns the length of the foods array
     * @return an int
     */
    public int size(){
        return foods.size();
    }

    /**
     * Getter for Food
     * @param i index of the food in foods
     * @return the chosen food
     */
    public Food getFood(int i){
        return foods.get(i);
    }

    /**
     * toString method.
     * @return a human-readable string
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size(); i++){
            int idOfDay = (i / 3 + 6) % 7 + 1;
            String uglyDayName = DayOfWeek.of(idOfDay).name();
            String lowerCaseDayName = uglyDayName.toLowerCase();
            String niceDayName = lowerCaseDayName.substring(0, 1).toUpperCase() +
                lowerCaseDayName.substring(1);
            String mealType;
            switch (i % 3){
                case 0 -> mealType = "dinner";
                case 1 -> mealType = "breakfast";
                default -> mealType = "lunch";
            }
            s.append(niceDayName).append(' ').append(mealType).append(": ").append(getFood(i)
                .name()).append('\n');
        }
        return s + shoppingListToString();
    }

    /**
     * Checks whether an ingredient is already in the shopping list
     * @param ingredient the ingredient we are looking for
     * @return true if found, false otherwise
     */
    public boolean contains(Ingredient ingredient){
        for (Buyable buyable : shoppingList){
            if (buyable.getIngredient().equals(ingredient))
                return true;
        }
        return false;
    }

    /**
     * Getter for buyable
     * @param ingredient of the buyable
     * @return a buyable object
     */
    public Buyable getBuyable(Ingredient ingredient){
        for (Buyable buyable : shoppingList){
            if (buyable.getIngredient().equals(ingredient))
                return buyable;
        }
        throw new NoSuchElementException();
    }

    /**
     * To string method for shopping list
     * @return a human-readable string
     */
    public String shoppingListToString(){
        StringBuilder s = new StringBuilder();
        for (Buyable buyable : shoppingList){
            s.append(buyable.toSimpleString()).append('\n');
        }
        return s.toString();
    }

    public Food getLast(){
        return foods.get(foods.size()-1);
    }

    public void save(File file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print(this);
        writer.close();
    }

}
