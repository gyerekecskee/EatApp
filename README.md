# EatApp
An application aiding cooking and shopping


The application stores measurements, ingredients, conversions, foods and items. All of this data is stored in sources.txt, when the application isn't running

Measurements are grouped into measurementtypes eg. Volume, Weight and every measurement has a name and a multiplier which tells us by how much we have to multiply the measurement to get the base measurement. For example milliliter's multiplier is 1000 because 1000 ml = 1 l

An ingredient is a specific _thing_ that you can find in recipes, like apples or flour. Every ingredient has a measurement, name, and a weighted average price. An example ingredient: Chickpeas 59.75 euros/7500g

A conversion tells you how to convert an ingredient between two different measurementtypes. For example 1 tbsp of baking powder = 13.812154696 g

A food has different fields, like taste complexity and such. Also a list of ingredients and connected amounts. An ingredient doesn't have an amount so we must have the accompanying amounts.

An item is an object that you can buy from the store, like 1 liter of soy milk for 0.95 euros from aldi



Options:
1. printing the whole database
2. adding a new receipt to the database
3. adding a new food to the database
4. adding a new conversion to the database
5. adding a new measurement to the database
6. adding a new ingredient to the database
7. printing only the meals
8. generating a menu for a week


More detailed descriptions of options:
1. Just prints out the whole database

2.You can add a receipt, by typing in its store, and items. Afterwards the items will get added to the database and the responisive ingredients will also be added, except when they already exist, then their prices will be updated

3.You can add a meal, by typing in its different attributes, then the list of ingredients and accompanying amounts. Then the application calculates the cost of the food, and stores it

4.You can add a new conversion, by typing in the name of the ingredient, the target measurement and by how much you have to multiply the amount to receive an amount in the target measurement

5.You can add a new measurement, by typing in its type, name and multiplier

6.You can add a new ingredient, by tping in all it's relevant values

7.You can print out only the foods in the database

8.You can generate a menu for a week. This happens by randomly selecting from the available foods, based on their propertis. Something that is cheaper has a bigger chance of getting selected. The application also generates a shopping list for the entire week
