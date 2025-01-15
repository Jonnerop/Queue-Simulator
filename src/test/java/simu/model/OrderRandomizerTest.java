package simu.model;

import eduni.distributions.Uniform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.Trace;
import simu.model.customerTypes.Customer;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OrderRandomizerTest {
    private OrderRandomizer orderRandomizer;
    private Finance finance;
    private HashMap<String, DefaultSpecs.Spec> specs = new HashMap<>();

    @BeforeEach
    void setUp() {
        specs = DefaultSpecs.setDefaultSpecs();
        finance = new Finance();
        orderRandomizer = new OrderRandomizer(new Uniform(0, 1), specs);
        Trace.setTraceLevel(Trace.Level.INFO);
    }

    @Test
    void generateRandomPizza() {
        PizzaNames pizza = orderRandomizer.generateRandomPizza();
        assertNotNull(pizza);
        assertTrue(pizza == PizzaNames.MARGHERITA || pizza == PizzaNames.MEATLOVER || pizza == PizzaNames.VEGAN);
    }

    @Test
    void generateRandomDrink() {
        DrinkNames drink = orderRandomizer.generateRandomDrink();
        assertNotNull(drink);
        assertTrue(drink == DrinkNames.SODA || drink == DrinkNames.WATER || drink == DrinkNames.BEER);
    }

    @Test
    void generateRandomSize() {
        PizzaSizes size = orderRandomizer.generateRandomSize();
        assertNotNull(size);
        assertTrue(size == PizzaSizes.SMALL || size == PizzaSizes.MEDIUM || size == PizzaSizes.LARGE);
    }

    @Test
    void createOrder() {
        Customer customer = new Customer();
        Order order = orderRandomizer.createOrder(customer);
        assertNotNull(order);
        assertNotNull(order.getPizza());
        assertNotNull(order.getSize());
        assertNotNull(order.getDrink());
        assertEquals(customer, order.getCustomer());
    }
}