package simu.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.id.factory.spi.GenerationTypeStrategy;

/**
 * The Pizza class represents a pizza in the simulation. It contains details
 * about the pizza's name, size, price, and associated expenses. Each pizza
 * is tied to an order with a unique ID.
 */
@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private PizzaNames name;
    private PizzaSizes size;
    private double price;
    private double expense;

    /**
     * Constructs a Pizza object with the specified name, size, and ID.
     * The price and expense of the pizza are calculated based on the specifications.
     *
     * @param name The name of the pizza.
     * @param size The size of the pizza.
     * @param i The ID of the pizza, typically corresponding to the order ID.
     */
    public Pizza(PizzaNames name, PizzaSizes size, int i) {
        this.name = name;
        this.size = size;
        this.price = Finance.pizzaTypeRev.get(this.name) * Finance.pizzaSizeRev.get(this.size);
        this.expense = Finance.pizzaTypeExp.get(this.name) * Finance.pizzaSizeExp.get(this.size);
        this.id = i;
    }

    public Pizza() {
    }

    public PizzaNames getName() {
        return name;
    }

    public PizzaSizes getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public double getExpense() {
        return expense;
    }
}
