import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    //TODO side dishes
    //TODO milk soy milk etc.
    //TODO bread
    //TODO add kilo calories to ingredient
    //TODO make kilo calories null
    //TODO whole rework of kilocalories, disable it temporarily
    //TODO make options cancellable
    //TODO when inputting a new ingredient print out the details
    //TODO cleanup project
    //TODO rotting food
    //TODO modify menu
    //TODO larger selection of foods
    //TODO change multipliers
    //TODO more complex multiplierFunctions
    //TODO make complexityFunction flexible
    //TODO add portions
    //TODO best shops
    //TODO edit stuff
    //TODO cleanup reader
    //TODO debug conversionAdder

    private static final Scanner input = new Scanner(System.in);
    private static Scanner file = fileInitializer();
    private static final Catalog catalog = necessaryReader();

    public static void main(String[] args){
        restReader(file);
        file.close();

        Food.setAgeMultiplier(4.0);
        Food.setCaloriesMultiplier(0.0);
        Food.setTasteMultiplier(5.0);
        Food.setComplexityMultiplier(2.0);
        Food.setCostMultiplier(5.0);
        Food.setTimeMultiplier(1.0);

        String command;
        do{
            System.out.println("Please make a choice:");
            System.out.println("0 - Quit application.");
            System.out.println("1 - Print the whole database.");
            System.out.println("2 - Add new receipt to the database.");
            System.out.println("3 - Add new food to the database.");
            System.out.println("4 - Add new conversion.");
            System.out.println("5 - Add new measurement.");
            System.out.println("6 - Add new ingredient to the database.");
            System.out.println("7 - Print meals.");
            System.out.println("8 - Generate menu.");
            command = input.nextLine();
            switch (command){
                case "0" -> System.out.println();
                case "1" -> System.out.println(catalog);
                case "2" -> takeReceipt();
                case "3" -> addRecipe(input);
                case "4" -> takeConversion(input, catalog);
                case "5" -> takeMeasurement(catalog, input);
                case "6" -> catalog.getIngredients().add(addIngredient(input, catalog));
                case "7" -> System.out.println(catalog.foodsToString());
                case "8" -> {
                    try {
                        generateMenu();
                    } catch (NoSuchElementException e) {
                        System.out.println("You can't generate a menu while you have no " +
                            "breakfasts or mains in the database.\n" +
                            "Please use option 3 to add a new food.");
                        e.printStackTrace();
                    }
                }
                case "9" -> printWeights();
                default -> System.out.println("Not yet implemented.");
            }
        }while (!command.equals("0"));
        input.close();
        try {
            writeToFile(catalog, "source.txt");
            writeToFile(catalog, "/home/abel/IntellIJProjects/EatApp-20221109T144916Z-001/backups/" + LocalDateTime.now());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Scanner fileInitializer() {
        Scanner file;
        try {
            file = new Scanner(new File("source.txt"));
        } catch (FileNotFoundException e) {

            try {
                System.out.println("There was no source.txt file found so we have created one");
                File newFile = new File("source.txt");
                PrintWriter writer = new PrintWriter("source.txt");
                writer.println("Measurements");
                writer.println("Ingredients");
                writer.println("Conversions");
                writer.println("Foods");
                writer.println("Items");
                writer.close();
                if(newFile.createNewFile()){
                    System.out.println("The new file was created");
                }
                file = new Scanner(new File("source.txt"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return file;
    }


    public static void restReader(Scanner file) {

        foodReader(file, catalog, input);
        itemReader(file, catalog, input);

    }

    public static Catalog necessaryReader(){
        Catalog catalog = new Catalog();

        List<MeasurementType> measurements = measurementReader(file);
        catalog.setMeasurementTypes(measurements);
        ingredientReader(file, catalog, input);


        conversionReader(file, catalog, input);

        return catalog;
    }

    public static void ingredientReader(Scanner file, Catalog catalog, Scanner input){
        List<Ingredient> ingredients = new ArrayList<>();
        String s = file.nextLine();
        Scanner line;
        while (!s.equals("Conversions")){
            line = new Scanner(s).useDelimiter("; ");
            String name = line.next().toLowerCase();
            Rational rational;
            s = line.next();
            String t = line.next();
            if(s.equals("null") || t.equals("null")){
                rational = null;
            }else {
                rational = new Rational(Double.parseDouble(s), Double.parseDouble(t));
            }
            int kCals = Integer.parseInt(line.next());
            String measName = line.next();
            line.close();
            if(catalog.notInMeasurements(new Measurement(measName, 0))){
                System.out.println("The database doesn't contain " + measName +
                        " yet, would you like to add it" +
                    "or lose " + name + " or change the measurement of it[a/l/c]");
                switch (input.nextLine()){
                    case "a" -> takeMeasurement(catalog, input);
                    case "c" -> {
                        System.out.println("What should the new measurement be?");
                        measName = input.nextLine();
                    }
                    default -> {
                        s = file.nextLine();
                        continue;
                    }
                }
            }
            Ingredient ingredient = new Ingredient(name, rational, catalog.getMeasurement(measName),
                kCals);
            ingredients.add(ingredient);
            s = file.nextLine();
        }
        catalog.setIngredients(ingredients);
    }

    public static void foodReader(Scanner file, Catalog catalog, Scanner input){
        String s = file.nextLine();
        List<String> types = new ArrayList<>();
        String name, ingName, measName;
        int time, taste, complexity, age;
        double number, cost, kCals;
        Buyable ingredient;
        Amount amount;
        List<Buyable> ingredients = new ArrayList<>();
        Food food;
        while (!s.equals("Items")){
            Scanner line = new Scanner(s).useDelimiter("; ");
            while (line.hasNext()){
                types.add(line.next());
            }
            line = new Scanner(file.nextLine()).useDelimiter("; ");
            name = line.next();
            age = Integer.parseInt(line.next());
            line.next();
            kCals = Double.parseDouble(line.next());
            time = Integer.parseInt(line.next());
            taste = Integer.parseInt(line.next());
            complexity = Integer.parseInt(line.next());
            s = file.nextLine();
            while (!FoodType.combinations().contains(parseToStrings(s)) && !s.equals("Items")){
                line = new Scanner(s).useDelimiter("; ");
                number = Double.parseDouble(line.next());
                measName = line.next();
                if(catalog.notInMeasurements(new Measurement(measName, 0))){
                    takeMeasurement(catalog, input);
                }
                amount = new Amount(number, catalog.getMeasurement(measName));
                ingName = line.next();
                ingredient = new Buyable(catalog.getIngredient(ingName), amount);
                ingredients.add(ingredient);
                s = file.nextLine();
            }
            cost = getCost(catalog, ingredients);
            food = new Food(name, age, cost, kCals, time, taste, complexity, ingredients, types);
            catalog.addFood(food);
            ingredients = new ArrayList<>();
            types = new ArrayList<>();
            line.close();
        }
    }

    public static void itemReader(Scanner file, Catalog catalog, Scanner input){
        String store, name;
        Double amountNumber;
        double cost;
        Item item;
        Amount amount;
        Measurement amountMeas;
        while (file.hasNextLine()){
            Scanner line = new Scanner(file.nextLine()).useDelimiter("; ");
            name = line.next().toLowerCase();
            cost = Double.parseDouble(line.next());
            String s = line.next();
            if (s.equals("null")){
                amountNumber = null;
            }else
                amountNumber = Double.parseDouble(s);
            amountMeas = Measurement.parseMeasurement(line.next());
            store = line.next().toLowerCase();
            amount = new Amount(amountNumber, amountMeas);
            item = new Item(name, cost, amount, store);
            catalog.getItems().add(item);
            Collections.sort(catalog.getItems());
            line.close();
        }
    }

    public static List<MeasurementType> measurementReader(Scanner file){
        //TODO make measurementType flexible
        List<MeasurementType> measurementTypes = new ArrayList<>();
        String type, name;
        double multiplier;
        Measurement measurement;
        file.nextLine();

        String s = file.nextLine();
        MeasurementType measurementType;
        while (!s.equals("Ingredients")){
            type = s;
            s = file.nextLine();
            measurementType = new MeasurementType(type);
            while (!(s.equals("Volume") ||
                s.equals("Weight") ||
                s.equals("Unit") ||
                s.equals("Ingredients"))){
                Scanner line = new Scanner(s).useDelimiter("; ");
                name = line.next();
                multiplier = Double.parseDouble(line.next());
                measurement = new Measurement(name, multiplier);
                measurementType.addMeasurement(measurement);
                s = file.nextLine();
                line.close();
            }
            measurementTypes.add(measurementType);
        }
        return measurementTypes;
    }

    public static void conversionReader(Scanner file, Catalog catalog, Scanner input){
        String s = file.nextLine();
        while (!s.equals("Foods")){
            Scanner line = new Scanner(s).useDelimiter("; ");
            Ingredient ingredient = catalog.getIngredient(line.next());
            double multiplier = Double.parseDouble(line.next());
            String measName = line.next();
            if(catalog.notInMeasurements(new Measurement(measName, 0))){
                System.out.println("We don't have " + measName + " in the database yet.");
                takeMeasurement(catalog, input);
            }
            Measurement measurement = catalog.getMeasurement(measName);
            catalog.addConversion(new Conversion(ingredient, measurement, multiplier));
            s = file.nextLine();
            line.close();
        }
    }

    public static Double costStandardizer(double cost){
        double minCost = Double.MAX_VALUE;
        double maxCost = Double.MIN_VALUE;
        for (Food food : catalog.getFoods()){
            if (food.cost() < minCost)
                minCost = food.cost();
            if (food.cost() > maxCost)
                maxCost = food.cost();
        }
        return 10 - ((cost - minCost) / (maxCost - minCost) * 9);
    }

    public static Double timeStandardizer(double time){
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;
        for (Food food : catalog.getFoods()){
            if (food.minutesToMake() < minTime)
                minTime = food.minutesToMake();
            if (food.minutesToMake() > maxTime)
                maxTime = food.minutesToMake();
        }
        return 10 - ((time - minTime) / (maxTime - minTime) * 9);
    }

    public static Double complexityStandardizer(int complexity){
        return switch (complexity) {
            case 1 -> 10.0;
            case 2 -> 5.0;
            case 3 -> 1.0;
            default -> throw new IllegalArgumentException();
        };
    }

    private static void printWeights(){
        for (Food food : catalog.getFoods()){
            System.out.println(food.name() + food.getWeight());
        }
    }

    public static void takeMeasurement(Catalog catalog, Scanner input){
        try {
            System.out.println("""
                Please input a measurement like this:
                Weight: g = 0.001 kg
                <Measurement type>: <base measurement> = <multiplier> <new measurement>
                """);
            String[] line = input.nextLine().split(" ");
            Measurement measurement = new Measurement(line[4], Double.parseDouble(line[3]));
            String type = line[0].substring(0, line[0].length()-1);
            catalog.addMeasurement(type, measurement);
        }catch (NumberFormatException e){
            System.out.println("You have probably typed a ',' instead of a '.'. Please try again!");
        }
    }

    public static Ingredient addIngredient(Scanner input, Catalog catalog){
        System.out.println("""
                Please input an ingredient like this:
                Mayonnaise; 2.61; 750; ml; 100
                <name>; <cost>; <amount>; <measurement>; <kilocalories per 100 measurement>
                """);
        Scanner line = new Scanner(input.nextLine()).useDelimiter("; ");
        String name = line.next().toLowerCase();
        double cost = Double.parseDouble(line.next());
        double amount = Double.parseDouble(line.next());
        String measName = line.next();
        int kCals = Integer.parseInt(line.next());
        if(catalog.notInMeasurements(new Measurement(measName, 0))){
            takeMeasurement(catalog, input);
        }
        Measurement measurement = catalog.getMeasurement(measName);
        Ingredient ingredient = new Ingredient(name, new Rational(cost, amount), measurement, kCals);
        if(!catalog.getIngredients().contains(ingredient)){
            catalog.getIngredients().add(ingredient);
        }else {
            System.out.println("That ingredient is already in the database.");
        }
        return ingredient;
    }

    public static Buyable takeIngredient(String s, Catalog catalog, Scanner input){
        //TODO make it flexible so when more categories are added it also works
        Scanner line = new Scanner(s).useDelimiter("; ");
        try {
            double number = Double.parseDouble(line.next());
            String measName = line.next();
            String ingName = line.next();
            if(catalog.notInMeasurements(new Measurement(measName, 0))){
                System.out.println("There is no " + measName + " in the database. " +
                    "Would you like to add it now, or retype it?[a/r]");
                s = input.nextLine();
                if(s.equals("a")){
                    takeMeasurement(catalog, input);
                }else {
                    return takeIngredient(s, catalog, input);
                }

            }
            Buyable buyable = null;
            Amount amount = new Amount(number, catalog.getMeasurement(measName));
            try {
                Ingredient ingredient = catalog.getIngredient(ingName);
                buyable = new Buyable(ingredient , amount);
            } catch (IllegalArgumentException e){
                System.out.println("There is no " + ingName + " in the database. " +
                    "Would you like to add it now, or retype it?[a/r]");
                s = input.nextLine();
                if(s.equals("a")){
                    buyable = new Buyable(addIngredient(input, catalog), amount);
                }else if (s.equals("r")){
                    s = input.nextLine();
                    buyable = takeIngredient(s, catalog, input);
                }else {
                    System.out.println("The ingredient was skipped, " +
                        "and will be missing from the list of ingredients.");
                }
            }
//        System.out.println(pair.getIngredient().getName() + "was taken");
            return buyable;
        }catch (Exception e){
            System.out.println("You have messed something up, please try again.");
            e.printStackTrace();
            return takeIngredient(s, catalog, input);
        }
    }

    public static void addRecipe(Scanner input){
        System.out.println("""
                Please input a food like this:
                Breakfast, Lunch
                French Toast; 30; 9; 1
                1; st; egg
                1; tsp; vanilla powder
                0.5; tsp; cinnamon
                0.25; cup; milk
                4; slices; bread
                """);
        List<String> types = new ArrayList<>();
        Scanner line = new Scanner(input.nextLine()).useDelimiter(", ");
        while (line.hasNext()){
            types.add(line.next());
        }
        line = new Scanner(input.nextLine()).useDelimiter("; ");
        int time = 0, taste = 0, complexity = 0;
        String name = null;
        try {
            name = line.next();
            time = Integer.parseInt(line.next());
            taste = Integer.parseInt(line.next());
            complexity = Integer.parseInt(line.next());
        }catch (NoSuchElementException e){
            System.out.println("You have messed something up in, please try again.");
            addRecipe(input);
        }catch (NumberFormatException e){
            System.out.println("You might have forgot to add the details of the meal, " +
                "please try again.");
            e.printStackTrace();
            addRecipe(input);
        }

        List<Buyable> ingredients = new ArrayList<>();
        String s = input.nextLine();
        while (!s.equals("") && !s.equals("e") && !s.equals("end")){
            Buyable ingredient = takeIngredient(s, catalog, input);
            MeasurementType ingMeasType =
                catalog.getMeasurementType(ingredient.getIngredient().getMeasurement());
            MeasurementType amountMeasType =
                catalog.getMeasurementType(ingredient.getAmount().measurement());
            if(!ingMeasType.equals(amountMeasType) &&
                    !catalog.containsConversion(ingredient.getIngredient(), amountMeasType)){
                takeConversion(input, catalog);
            }

            s = input.nextLine();
            if(ingredient.getIngredient()!=null){
                ingredients.add(ingredient);
            }
        }
        double cost = getCost(catalog, ingredients);

        //TODO needs some work for calculating the nutrition
        double kCals = 0;
        for (Buyable ingredient : ingredients) {
            kCals = kCals + (ingredient.getIngredient().getKiloCalories() *
                ingredient.getAmount().number());
        }
        kCals = kCals / 100;
        Food food = new Food(name, 0, cost, kCals, time, taste, complexity, ingredients, types);
        catalog.addFood(food);
    }

    private static double getCost(Catalog catalog, List<Buyable> ingredients) {
        double cost = 0;
        Amount amount;
        for (Buyable ingredient : ingredients) {

            Measurement measOfIngInCatalog = ingredient.getIngredient().getMeasurement();
            MeasurementType measTypeOfIngInCatalog =
                catalog.getMeasurementType(measOfIngInCatalog);

            MeasurementType measTypeOfIngInRecipe =
                catalog.getMeasurementType(ingredient.getAmount().measurement());
            if(!measTypeOfIngInRecipe.equals(measTypeOfIngInCatalog)){
                Conversion conversion =
                    catalog.getConversion(ingredient.getIngredient(), measTypeOfIngInRecipe);
                Amount amountBeforeConverting =
                    ingredient.getAmount().convertTo(conversion.measurement());
                amount = new Amount(amountBeforeConverting.number() /
                        conversion.multiplier(), measOfIngInCatalog);
            }else{
                amount = ingredient.getAmount().convertTo(measOfIngInCatalog);
            }
            double averagePrice = ingredient.getIngredient().getAveragePrice().value();
            double costOfThis = averagePrice * amount.number();
            cost += costOfThis;
        }
        return cost;
    }

    public static void takeReceipt(){
        String s;
        String store;
        try {
            System.out.println("""
                    Please type in a receipt like the format below:
                    \tAldi
                    \tMayonnaise; 1.61; 650ml
                    \tMustard; 0.89; 370g
                    \tParsley; 0.89; 20g
                    \tFusilli Pasta; 1.09; 500g
                    \tTuna; 1.69; 150g
                    \tSpaghetti; 0.89; 500g""");
            s = input.nextLine();
            store = s;
            Item item;
            double cost;
            Amount amount;
            String name;
            List<Item> items = new ArrayList<>();
            s = input.nextLine();
            while(!s.equals("")&&!s.equals("e")&&!s.equals("end")){
                Scanner line = new Scanner(s).useDelimiter("; ");
                name = line.next().toLowerCase();
                cost = Double.parseDouble(line.next());
                amount = Amount.parseAmount(line.next(), catalog, input);
                item = new Item(name, cost, amount, store);
                items.add(item);
                line.close();
                s = input.nextLine();
            }
            catalog.addReceipt(new Receipt(store, items));
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Probably you have forgot a ';', or the price, please try again.");
            takeReceipt();
        }
    }

    public static void takeConversion(Scanner input, Catalog catalog){
        System.out.println("Please type in a conversion like this:\n" +
                "cinnamon: 1 g = 0.362 tsp");
        String line = input.nextLine();
        String[] name = line.split(": ");
        try {
            convEx(input, catalog, name);
        }catch (IllegalArgumentException e){
            System.out.println("This Ingredient is not in the the database. Would you like to skip, retry, or add new ingredient[s/r/a]");
            e.printStackTrace();
            switch (input.nextLine()){
                case "r" -> takeConversion(input, catalog);
                case "a" ->{
                    addIngredient(input, catalog);
                    convEx(input, catalog, name);

                }
                default -> System.out.println("The conversion wasn't taken.");
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("You have probably mistyped something, do it again!");
            takeConversion(input, catalog);
        }
    }

    private static void convEx(Scanner input, Catalog catalog, String[] name) throws ArrayIndexOutOfBoundsException{
        Ingredient ingredient = catalog.getIngredient(name[0]);
        double multiplier = Double.parseDouble(name[1].split(" ")[3]);
        String measName = name[1].split(" ")[4];
        if(catalog.notInMeasurements(new Measurement(measName, 0))){
            takeMeasurement(catalog, input);
        }
        Measurement measurement = catalog.getMeasurement(measName);
        catalog.addConversion(new Conversion(ingredient, measurement, multiplier));
    }

    public static void generateMenu(){
        String command;
        do {
            Menu menu = new Menu();

            List<Food> breakfasts = catalog.getBreakfasts();
            List<Food> lunches = catalog.getLunches();
            List<Food> dinners = catalog.getDinners();

            if (breakfasts.isEmpty() || lunches.isEmpty() || dinners.isEmpty()){
                throw new NoSuchElementException();
            }


            Chance breakfastChance = new Chance(breakfasts);
            Chance lunchChance = new Chance(lunches);
            Chance dinnerChance = new Chance(dinners);
            for(int i = 1; i <= 7; i++) {

                menu.addFood(dinners, dinnerChance);
                Food tempDinner = menu.getLast();

                if (tempDinner.types().contains("Breakfast"))
                    menu.addFood(tempDinner, breakfastChance);

                else
                    menu.addFood(breakfasts, breakfastChance);

                Food tempBreakfast = menu.getLast();

                if (tempDinner.types().contains("Lunch"))
                    menu.addFood(tempDinner, lunchChance);
                else if (tempBreakfast.types().contains("Lunch"))
                    menu.addFood(tempBreakfast, lunchChance);
                else
                    menu.addFood(lunches, lunchChance);

            }
            menu.generalizeBuyables();
            System.out.println(menu);
            System.out.println("Do you want to save the menu or generate a " +
                "new on or cancel[s/n/c]?");
            command = input.nextLine();
            switch (command){
                case "s" -> {
                    try {
                        menu.save(new File("menu.txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Saved the menu");
                }
                case "n" -> System.out.println("Generating a new menu");
            }
        } while (command.equals("n"));

    }

    public static Catalog getCatalog() {
        return catalog;
    }

    public static List<String> parseToStrings(String s){
        Scanner line = new Scanner(s).useDelimiter("; ");
        List<String> result = new ArrayList<>();
        while (line.hasNext()){
            result.add(line.next());
        }
        return result;
    }

    public static void writeToFile(Catalog catalog, String filename) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filename);
        writer.println("Measurements");
        for(MeasurementType measurementType : catalog.getMeasurementTypes()) {
            writer.println(measurementType.getType());
            for (Measurement measurement : measurementType.getMeasurements()){
                writer.println(measurement.name() + "; " +
                    measurement.multiplier());
            }
        }
        writer.println("Ingredients");
        for (Ingredient ingredient : catalog.getIngredients()){
            writer.print(ingredient.getName() + "; ");
            try {
                writer.print(ingredient.getAveragePrice().getNominator() + "; ");
            }catch (NullPointerException e){
                writer.print("null" + "; ");
            }
            try {
                writer.print(ingredient.getAveragePrice().getDenominator() + "; ");
            }catch (NullPointerException e){
                writer.print("null" + "; ");
            }
            writer.print(ingredient.getKiloCalories() + "; ");
            writer.println(ingredient.getMeasurement());
        }
        writer.println("Conversions");
        for(int i = 0; i < catalog.getConversions().size(); i++){
            writer.println(catalog.getConversions().get(i).ingredient().getName() + "; " +
                catalog.getConversions().get(i).multiplier() + "; " +
                catalog.getConversions().get(i).measurement().name());
        }
        writer.println("Foods");
        for (Food food : catalog.getFoods()){
            for (int i = 0; i < food.types().size(); i++){
                writer.print(food.types().get(i));
                if (i != food.types().size()-1)
                    writer.print("; ");
            }
            writer.println();
            writer.println(food.name() + "; " +
                food.age() + "; " +
                food.cost() + "; " +
                food.kiloCalories() + "; " +
                food.minutesToMake() + "; " +
                food.tastiness() + "; " +
                food.complexityToMake());
            for (Buyable ingredient : food.ingredients()){
                writer.println(ingredient.getAmount().number() + "; " +
                    ingredient.getAmount().measurement() + "; " +
                    ingredient.getIngredient().getName());
            }
        }
        writer.println("Items");
        for(int i = 0; i < catalog.getItems().size(); i++){
            writer.println(catalog.getItems().get(i).name() + "; " +
                catalog.getItems().get(i).cost() + "; " +
                catalog.getItems().get(i).amount().number() + "; " +
                catalog.getItems().get(i).amount().measurement() + "; " +
                catalog.getItems().get(i).store());
        }
        writer.close();
    }

}
