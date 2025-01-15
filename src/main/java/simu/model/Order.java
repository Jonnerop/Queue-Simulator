package simu.model;

import jakarta.persistence.*;
import simu.model.customerTypes.AbstractCustomer;

/**
 * The Order class represents a customer's order in the simulation.
 * It includes details about the pizza, drink, and associated revenue and expenses.
 * Each order is tied to a customer and contains a unique ID.
 */
@Entity
@Table(name = "orders")
public class Order {
    private static int counter = 0;
    private DrinkNames drinkType;
    private double drinkPrice;
    private double drinkExpense;
    private double orderRevenue;
    private double orderExpense;
    private int id;
    @Id
    @OneToOne
    private AbstractCustomer aC;
    @OneToOne(cascade = CascadeType.ALL)
    private Pizza pizza;

    /**
     * Constructs an Order object with the specified pizza, drink, and customer details.
     * The order revenue and expenses are calculated based on the prices of the items.
     *
     * @param pizzaType The type of pizza ordered.
     * @param pizzaSize The size of the pizza ordered.
     * @param drinkType The type of drink ordered.
     * @param c The customer who placed the order.
     */
    public Order (PizzaNames pizzaType, PizzaSizes pizzaSize, DrinkNames drinkType, AbstractCustomer c) {
        counter++;
        id = counter;
        this.drinkType = drinkType;
        this.drinkPrice = Finance.drinkTypeRev.get(this.drinkType);
        this.drinkExpense = Finance.drinkTypeExp.get(this.drinkType);
        this.pizza = new Pizza(pizzaType, pizzaSize,id);
        this.aC = c;
        this.orderRevenue = pizza.getPrice() + drinkPrice;
        this.orderExpense = pizza.getExpense() + drinkExpense;
        printOrder();
    }

    private void printOrder() {
        System.out.println("Order ID: " + id);
        System.out.println("Pizza: " + pizza.getName() + " " + pizza.getSize());
        System.out.println("Drink: " + drinkType);
        System.out.println("Order Revenue: " + orderRevenue);
        System.out.println("Order Expense: " + orderExpense);
    }

    public Order() {
        super();
    }

    public DrinkNames getDrink() {
        return drinkType;
    }

    public int getId() {
        return id;
    }

    public AbstractCustomer getCustomer() {
        return aC;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public double getDrinkExpense() {
        return drinkExpense;
    }

    public double getDrinkPrice() {
        return drinkPrice;
    }

    public double getOrderRevenue() {
        return orderRevenue;
    }

    public double getOrderExpense() {
        return orderExpense;
    }

    public Object getSize() {
        return pizza.getSize();
    }

    public void setOrderRevenue(double value) {
        orderRevenue = value;
    }

    public void setOrderExpense(double value) {
        orderExpense = value;
    }
}
