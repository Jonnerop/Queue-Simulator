package simu.model;

import eduni.distributions.RandomGenerator;
import eduni.distributions.Uniform;
import eduni.distributions.Uniform;
import simu.model.customerTypes.AbstractCustomer;

import java.util.HashMap;

/**
 * The OrderRandomizer class generates random orders for customers in the simulation.
 * It uses a uniform random number generator to determine the type of pizza, drink, and size,
 * based on predefined probabilities.
 */
public class OrderRandomizer {
    private final Uniform randomGen;
    private final HashMap<String, DefaultSpecs.Spec> specs;

    /**
     * Constructs an OrderRandomizer object with a given random generator and specifications.
     *
     * @param randomGen The uniform random generator to generate random values.
     * @param specs A HashMap containing the probabilities for different pizzas, drinks, and sizes.
     */
    public OrderRandomizer(Uniform randomGen, HashMap<String, DefaultSpecs.Spec> specs) {
        this.randomGen = randomGen;
        this.specs = specs;
    }

    /**
     * Generates a random pizza based on the probabilities in the specifications.
     *
     * @return The randomly generated pizza name.
     */
    public PizzaNames generateRandomPizza() {
        double rand = randomGen.sample();
        double margProb = specs.get("margProb").getValue();
        double meatProb = specs.get("meatProb").getValue();

        if (rand < margProb) { // probability for margherita
            return PizzaNames.MARGHERITA;
        } else if (rand < (margProb + meatProb)) { // probability for meat lover
            return PizzaNames.MEATLOVER;
        } else { // probability for vegan
            return PizzaNames.VEGAN;
        }
    }

    /**
     * Generates a random drink based on the probabilities in the specifications.
     *
     * @return The randomly generated drink name.
     */
    public DrinkNames generateRandomDrink() {
        double rand = randomGen.sample();
        double sodaProb = specs.get("sodaProb").getValue();
        double waterProb = specs.get("waterProb").getValue();

        if (rand < sodaProb) { // probability for soda
            return DrinkNames.SODA;
        } else if (rand < (sodaProb + waterProb)) { // probability for water
            return DrinkNames.WATER;
        } else { // probability for beer
            return DrinkNames.BEER;
        }
    }

    /**
     * Generates a random pizza size based on the probabilities in the specifications.
     *
     * @return The randomly generated pizza size.
     */
    public PizzaSizes generateRandomSize() {
        double rand = randomGen.sample();
        double smallProb = specs.get("smallProb").getValue();
        double medProb = specs.get("medProb").getValue();

        if (rand < smallProb) { // Probability for small
            return PizzaSizes.SMALL;
        } else if (rand < (smallProb + medProb)) { // Probability for medium
            return PizzaSizes.MEDIUM;
        } else { // Probability for large
            return PizzaSizes.LARGE;
        }
    }

    /**
     * Creates a new order for a given customer by randomly generating a pizza, size, and drink.
     *
     * @param c The customer placing the order.
     * @return A new Order object for the customer.
     */
    public Order createOrder(AbstractCustomer c) {
        return new Order(generateRandomPizza(), generateRandomSize(), generateRandomDrink(), c);
    }
}