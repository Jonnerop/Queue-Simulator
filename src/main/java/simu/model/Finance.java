package simu.model;

import simu.framework.Clock;

import java.util.HashMap;

/**
 * The Finance class handles the financial aspects of the restaurant simulation,
 * including revenues, expenses, and net profit. It manages both the cost of orders
 * and the operational expenses of the restaurant, such as staff wages and property costs.
 */
public class Finance {
    // average hourly wage
    private final double WAITER_WAGE = 12.0;
    private final double CHEF_WAGE = 12.0;
    private final double PROPERTY_EXP = 30; // daily expense

    private static double totalRevenue = 0;
    private static double totalExpenses = 0;

    private boolean discountDay = true;
    private final double DISCOUNT_FACTOR = 0.8;
    private double previousTime = 0;

    private final HashMap<String, DefaultSpecs.Spec> specs;

    // revenue and expense maps
    public static HashMap<PizzaNames, Double> pizzaTypeRev = new HashMap<>();
    public static HashMap<PizzaSizes, Double> pizzaSizeRev = new HashMap<>();
    public static HashMap<DrinkNames, Double> drinkTypeRev = new HashMap<>();

    public static HashMap<PizzaNames, Double> pizzaTypeExp = new HashMap<>();
    public static HashMap<PizzaSizes, Double> pizzaSizeExp = new HashMap<>();
    public static HashMap<DrinkNames, Double> drinkTypeExp = new HashMap<>();

    /**
     * Constructs a Finance object with a set of specifications and a discount day flag.
     *
     * @param specs       A HashMap of specifications that affect financial calculations.
     * @param discountDay A flag indicating whether it is a discount day.
     */
    public Finance(HashMap<String, DefaultSpecs.Spec> specs, boolean discountDay) {
        this.specs = specs;
        this.discountDay = discountDay;
        initializeMaps();
    }

    public Finance() {
        this.specs = DefaultSpecs.setDefaultSpecs();
        initializeMaps();
    }

    private void initializeMaps() {
        initializeRevenues();
        initializeExpenses();
    }

    // cost of customer order (revenue for restaurant)
    private void initializeRevenues() {
        pizzaTypeRev.put(PizzaNames.MARGHERITA, specs.get("margRev").getValue());
        pizzaTypeRev.put(PizzaNames.MEATLOVER, specs.get("meatRev").getValue());
        pizzaTypeRev.put(PizzaNames.VEGAN, specs.get("vegRev").getValue());

        drinkTypeRev.put(DrinkNames.SODA, specs.get("sodaRev").getValue());
        drinkTypeRev.put(DrinkNames.WATER, specs.get("waterRev").getValue());
        drinkTypeRev.put(DrinkNames.BEER, specs.get("beerRev").getValue());

        // hard coded at the moment
        if (discountDay) {
            pizzaSizeRev.put(PizzaSizes.SMALL, 0.7 * DISCOUNT_FACTOR);
            pizzaSizeRev.put(PizzaSizes.MEDIUM, 1.0 * DISCOUNT_FACTOR);
            pizzaSizeRev.put(PizzaSizes.LARGE, 1.5 * DISCOUNT_FACTOR);
        } else {
            pizzaSizeRev.put(PizzaSizes.SMALL, 0.7);
            pizzaSizeRev.put(PizzaSizes.MEDIUM, 1.0);
            pizzaSizeRev.put(PizzaSizes.LARGE, 1.5);
        }
    }

    // cost of preparing the pizza (expense for restaurant)
    private void initializeExpenses() {
        pizzaTypeExp.put(PizzaNames.MARGHERITA, specs.get("margExp").getValue());
        pizzaTypeExp.put(PizzaNames.MEATLOVER, specs.get("meatExp").getValue());
        pizzaTypeExp.put(PizzaNames.VEGAN, specs.get("vegExp").getValue());

        drinkTypeExp.put(DrinkNames.SODA, specs.get("sodaExp").getValue());
        drinkTypeExp.put(DrinkNames.WATER, specs.get("waterExp").getValue());
        drinkTypeExp.put(DrinkNames.BEER, specs.get("beerExp").getValue());

        // hard coded at the moment
        pizzaSizeExp.put(PizzaSizes.SMALL, 0.7);
        pizzaSizeExp.put(PizzaSizes.MEDIUM, 1.0);
        pizzaSizeExp.put(PizzaSizes.LARGE, 1.5);
    }

    public void addRevenue(double amount) {
        totalRevenue += amount;
    }

    public void addExpense(double amount) {
        totalExpenses += amount;
    }

    public void addOtherExpense(int numWaiters, int numChefs, double time) {
        totalExpenses += updateOtherExpenses(numWaiters, numChefs, time);
    }

    public double getNetProfit() {
        return totalRevenue - totalExpenses;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void addOrderRev(Order order) {
        double revenue = order.getOrderRevenue();
        addRevenue(revenue);
    }

    public void addOrderExp(Order order) {
        double expense = order.getOrderExpense();
        addExpense(expense);
    }

    public void setPreviousTime(double time) {
        previousTime = time;
    }
    private double updateOtherExpenses(int numWaiters, int numChefs, double time) {
        double result = (time - previousTime) * (PROPERTY_EXP / 720 + (WAITER_WAGE * numWaiters + CHEF_WAGE * numChefs) / 60);
        setPreviousTime(time);
        return result;
    }

    public void reset() {
        totalRevenue = 0;
        totalExpenses = 0;
    }

    public double getPreviousTime() {
        return previousTime;
    }
}
