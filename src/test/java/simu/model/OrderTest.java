package simu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.Trace;
import simu.model.customerTypes.AbstractCustomer;
import simu.model.customerTypes.Customer;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Order order;
    private Finance finance;
    private AbstractCustomer customer;
    private PizzaNames pizzaType;
    private PizzaSizes pizzaSize;
    private DrinkNames drinkType;


    @BeforeEach
    void setUp() {
        Trace.setTraceLevel(Trace.Level.INFO);
        customer = new Customer() {};
        pizzaType = PizzaNames.MARGHERITA;
        pizzaSize = PizzaSizes.MEDIUM;
        drinkType = DrinkNames.WATER;
        finance = new Finance();
        order = new Order(pizzaType, pizzaSize, drinkType, customer);
    }

    @Test
    void getDrink() {
        assertEquals(drinkType, order.getDrink());
    }

    @Test
    void getId() {
        assertTrue(order.getId() > 0);
    }

    @Test
    void getCustomer() {
        assertEquals(customer, order.getCustomer());
    }

    @Test
    void getPizza() {
        assertNotNull(order.getPizza());
        assertEquals(pizzaType, order.getPizza().getName());
        assertEquals(pizzaSize, order.getPizza().getSize());
    }

    @Test
    void getDrinkExpense() {
        assertEquals(Finance.drinkTypeExp.get(drinkType), order.getDrinkExpense());
    }

    @Test
    void getDrinkPrice() {
        assertEquals(Finance.drinkTypeRev.get(drinkType), order.getDrinkPrice());
    }

    @Test
    void getOrderRevenue() {
        double expectedRevenue = order.getPizza().getPrice() + order.getDrinkPrice();
        assertEquals(expectedRevenue, order.getOrderRevenue());
    }

    @Test
    void getOrderExpense() {
        double expectedExpense = order.getPizza().getExpense() + order.getDrinkExpense();
        assertEquals(expectedExpense, order.getOrderExpense());
    }

    @Test
    void getSize() {
        assertEquals(pizzaSize, order.getSize());
    }

    @Test
    void setOrderRevenue() {
        double newRevenue = 100.0;
        order.setOrderRevenue(newRevenue);
        assertEquals(newRevenue, order.getOrderRevenue());
    }

    @Test
    void setOrderExpense() {
        double newExpense = 50.0;
        order.setOrderExpense(newExpense);
        assertEquals(newExpense, order.getOrderExpense());
    }
}