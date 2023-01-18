import java.util.*;

public class Catalog {

    private List<Ingredient> ingredients;
    private final List<Food> foods;
    private List<MeasurementType> measurements;
    private final List<Conversion> conversions;
    private final List<Item> items;
    private int numOfBreakfasts;
    private int numOfLunch;
    private int numOfDinners;


    /**
     * Initializes the catalog with empty lists.
     */
    public Catalog(){
        ingredients = new ArrayList<>();
        foods = new ArrayList<>();
        measurements = new ArrayList<>();
        conversions = new ArrayList<>();
        items = new ArrayList<>();
        numOfDinners = 0;
        numOfBreakfasts = 0;
        numOfLunch = 0;
    }

    /**
     * Checks whether a measurement is contained in the catalog.
     * @param measurement1 the measurement which we check
     * @return true, when the measurement is not in the catalog true, otherwise
     */
    public boolean notInMeasurements(Measurement measurement1){
        for(MeasurementType measurementType : measurements){
            for(Measurement measurement2 : measurementType.getMeasurements()){
                if(measurement1.equals(measurement2))
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the catalog contains an ingredient.
     * @param name the name of the ingredient
     * @return whether the ingredient is in the catalog
     */
    public boolean containsIngredient(String name){
        for(Ingredient ingredient : ingredients){
            if (ingredient.getName().equals(name))
                return true;
        }
        return false;
    }

    /**
     * Returns the measurement of an ingredient
     * @param name the of the ingredient
     * @return measurement of the ingredient
     * @throws IllegalArgumentException when the ingredient is not found
     */
    public Measurement getIngredientsMeasurement(String name){
        for(Ingredient ingredient : ingredients){
            if(ingredient.getName().equals(name))
                return ingredient.getMeasurement();
        }
        throw new IllegalArgumentException();
    }

    /**
     * Getter for conversions
     * @return the list of conversions
     */
    public List<Conversion> getConversions() {
        return conversions;
    }

    /**
     * Adds an item, and the responsive ingredient to the database
     * @param item the item, that
     */
    public void addItem(Item item){
        if(!items.contains(item)){
            items.add(item);
        }
        addIngredient(item);
        Collections.sort(items);
    }

    /**
     * Adds an ingredient, based on the item inputted.
     * @param item that the ingredient is based on
     */
    public void addIngredient(Item item){
        //TODO when the user doesn't know the amount of something he/she should still input the
        // measurement
        //TODO solve more elegantly the different cases: case1 ingredient, already in the database,
        // case2 unknown amount number
        Measurement measurement;
        Rational price;
        Amount amount;
        try {
            measurement = getIngredientsMeasurement(item.name());
        }catch (IllegalArgumentException e){
            measurement = item.amount().measurement();
        }
        try {
            if(containsIngredient(item.name())){
                amount = item.amount().convertTo(getIngredient(item.name()).getMeasurement());
                price = new Rational(item.cost(), amount.number());
            }else {
                price = new Rational(item.cost(), item.amount().number());
                amount = item.amount();
            }
            Ingredient newIngredient = new Ingredient(item.name(), price, measurement);
            if(!ingredients.contains(newIngredient)){
                ingredients.add(newIngredient);
            }else{
                Ingredient oldIngredient = ingredients.get(ingredients.indexOf(newIngredient));
                try {
                    oldIngredient.getAveragePrice().addNominator(item.cost());
                    oldIngredient.getAveragePrice().addDenominator(amount.number());
                }catch (NullPointerException e){
                    oldIngredient.setAveragePrice(null);
                }
            }
            Collections.sort(ingredients);
        }catch (NullPointerException e){
            System.out.println("Amount of " + item.name() + " is unknown, so it was skipped.");
        }
    }

    /**
     * Checks whether the catalog contains  a conversion
     * @param ingredient of the conversion
     * @param measurementType destination of teh conversion
     * @return whether the conversion is contained
     */
    public boolean containsConversion(Ingredient ingredient, MeasurementType measurementType){
        for(Conversion conversion : conversions){
            if(conversion.ingredient().equals(ingredient) &&
                    this.getMeasurementType(conversion.measurement()).equals(measurementType)){
                return true;
            }
        }
        return false;
    }

    /**
     * Getter for ingredients
     * @return the list  of ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Getter for the type of measurement
     * @param measurement the measurement
     * @return the measurement type of the measurement
     * @throws NoSuchElementException when there is no such measurement in the catalog
     */
    public MeasurementType getMeasurementType(Measurement measurement){
        for(MeasurementType measurementType : measurements){
            for (Measurement measurement1 : measurementType.getMeasurements()){
                if(measurement1.equals(measurement)){
                    return measurementType;
                }
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Getter for ingredient
     * @param name name of the ingredient
     * @return the ingredient
     */
    public Ingredient getIngredient(String name){
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equals(name.toLowerCase())) {
                return ingredient;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Getter for foods
     * @return list of food
     */
    public List<Food> getFoods() {
        return foods;
    }

    /**
     * Setter for ingredients
     * @param ingredients list of ingredients
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        Collections.sort(ingredients);
    }

    /**
     * Equals method
     * @param o the other object
     * @return whether this equals to o
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catalog catalog = (Catalog) o;
        return Objects.equals(ingredients, catalog.ingredients) &&
                Objects.equals(foods, catalog.foods) &&
                Objects.equals(measurements, catalog.measurements) &&
                Objects.equals(items, catalog.items);
    }

    /**
     * Hashcode method
     * @return a hashed int
     */
    @Override
    public int hashCode() {
        return Objects.hash(ingredients, foods, measurements, items);
    }

    /**
     * To string method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        return "Ingredients:\n" + listToString(ingredients).indent(4) +
            foodsToString() +
            "Items:\n" + listToString(items).indent(4) +
            "Conversions:\n" + listToString(conversions).indent(4) +
            "Measurement Types:\n" + listToString(measurements).indent(4);
    }

    /**
     * Adds the items of a receipt and scans them
     * @param receipt a receipt from a store
     */
    public void addReceipt(Receipt receipt){
        for(int i = 0; i < receipt.getItems().size(); i++){
            if(!items.contains(receipt.getItems().get(i))){
                addItem(receipt.getItems().get(i));
            }
        }
    }

    /**
     * Converts a list to a nicer looking string
     * @param list a list of objects
     * @return a human-readable string
     */
    public static String listToString(List<?> list) {
        StringBuilder result = new StringBuilder();
        for (Object o : list) {
            result.append(o).append("\n");
        }
        if(result.length()==0){
            return "";
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    /**
     * Converts a list to a string in a line
     * @param list a list of objects
     * @return a human-readable string
     */
    public static String listToFlatString(List<?> list){
        StringBuilder s = new StringBuilder();
        for(Object element : list){
            s.append(element);
            s.append(", ");
        }
        return s.substring(0, s.length()-2);
    }

    /**
     * Getter for items
     * @return a list of items
     */
    public List<Item> getItems(){
        return items;
    }

    /**
     * Adds a measurement to the specified measurement type
     * @param type type of the measurement
     * @param measurement to be added
     */
    public void addMeasurement(String type, Measurement measurement){
        MeasurementType measurementType;
        try {
            measurementType = this.getMeasurementType(type);
        }catch (IllegalArgumentException e){
            System.out.println("There was no " + type + " in the database so we have created one.");
            measurementType = new MeasurementType(type);
            measurements.add(measurementType);
        }
        measurementType.addMeasurement(measurement);
    }

    /**
     * Gets a measurement type with the given name
     * @param type the name of the measurement type
     * @return the measurement type
     */
    public MeasurementType getMeasurementType(String type) {
        for(MeasurementType measurementType : measurements){
            if(measurementType.getType().equals(type)){
                return measurementType;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Adds a food object to the catalog and updates the counter of different types
     * @param food a food
     */
    public void addFood(Food food){
        if(!foods.contains(food)){
            foods.add(food);
        }
        if (food.types().contains("Dinner")){
            numOfDinners++;
        }
        if (food.types().contains("Lunch")){
            numOfLunch++;
        }
        if (food.types().contains("Breakfast")){
            numOfBreakfasts++;
        }
        Collections.sort(foods);
    }

    /**
     * Adds a conversion to the catalog
     * @param conversion a conversion between two different measurement types
     */
    public void addConversion(Conversion conversion){
        if(!conversions.contains(conversion)){
            conversions.add(conversion);
        }
        Collections.sort(conversions);
    }

    /**
     * Returns the conversion of the specific ingredient and measurement type.
     * @param ingredient the ingredient from which we convert
     * @param measurementType the measurement type to which we convert
     * @return the conversion
     */
    public Conversion getConversion(Ingredient ingredient, MeasurementType measurementType){
        for(Conversion conversion : this.conversions){
            if(conversion.ingredient().equals(ingredient) &&
                    this.getMeasurementType(conversion.measurement()).equals(measurementType)){
                return conversion;
            }
        }
        throw new NoSuchElementException("There is no Conversion stored of " + ingredient.getName()
        + " to " + measurementType.getType());
    }

    /**
     * Getter for measurement
     * @param name name of the measurement
     * @return the measurement with the name
     */
    public Measurement getMeasurement(String name) {
        for (MeasurementType measurementType : measurements) {
            for(Measurement measurement : measurementType.getMeasurements()){
                if(measurement.name().equals(name)){
                    return measurement;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Setter for measurementTypes
     * @param measurements a list of measurement types
     */
    public void setMeasurementTypes(List<MeasurementType> measurements) {
        this.measurements = measurements;
    }

    /**
     * Getter for measurementTypes.
     * @return a list of measurement types
     */
    public List<MeasurementType> getMeasurementTypes() {
        return measurements;
    }

    /**
     * Clears the catalog
     */
    public void clear(){
        ingredients.clear();
        measurements.clear();
        conversions.clear();
        items.clear();
        foods.clear();
    }

    /**
     * Converts the foods list to a string
     * @return a human-readable string
     */
    public String foodsToString(){
        return numOfBreakfasts + " breakfasts, " +
            numOfLunch + " lunches, " +
            numOfDinners + " dinners." + '\n' +
            listToString(foods).indent(4);
    }

    /**
     * Getter for breakfast
     * @return the meals that are breakfasts
     */
    public List<Food> getBreakfasts(){
        List<Food> breakfasts = new ArrayList<>();
        for (Food food : foods){
            if(food.types().contains("Breakfast"))
                breakfasts.add(food);
        }
        return breakfasts;
    }

    /**
     * Getter for lunches
     * @return the meals that are lunches
     */
    public List<Food> getLunches(){
        List<Food> lunches = new ArrayList<>();
        for (Food food : foods){
            if(food.types().contains("Lunch"))
                lunches.add(food);
        }
        return lunches;
    }

    /**
     * Getter for dinners
     * @return the meals that are dinners
     */
    public List<Food> getDinners(){
        List<Food> dinners = new ArrayList<>();
        for (Food food : foods){
            if(food.types().contains("Dinner"))
                dinners.add(food);
        }
        return dinners;
    }

    /**
     * Increments every food's age by one, and sets chosenFood's to 0.
     * @param chosenFood the food that we have chosen to the menu
     */
    public void ageFood(Food chosenFood){
        for (Food food : foods){
            if (!food.equals(chosenFood))
                food.setAge(food.age() + 1);
            else
                food.setAge(0);
        }
    }

    /**
     * Returns a food object based on its name
     * @param name of the food
     * @return a food object
     */
    public Food getFood(String name){
        name = name.toLowerCase();
        for (Food food : foods){
            if (food.name().toLowerCase().equals(name)){
                return food;
            }
        }
        throw new NoSuchElementException();
    }



}
