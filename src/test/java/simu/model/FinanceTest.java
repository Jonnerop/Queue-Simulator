package simu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class FinanceTest {
    private Finance finance;
    private HashMap<String, DefaultSpecs.Spec> specs;

    @BeforeEach
    void setUp() {
        specs = DefaultSpecs.setDefaultSpecs();
        finance = new Finance(specs, false);
        finance.reset();
    }

    @Test
    void addRevenue() {
        finance.addRevenue(100.0);
        assertEquals(100.0, finance.getTotalRevenue());
    }

    @Test
    void addExpense() {
        finance.addExpense(50.0);
        assertEquals(50.0, finance.getTotalExpenses());
    }

    @Test
    void addOtherExpense() {
        finance.setPreviousTime(0);
        finance.addOtherExpense(2, 1, 60);
        assertEquals(38.5, finance.getTotalExpenses());
    }

    @Test
    void getNetProfit() {
        finance.addRevenue(200.0);
        finance.addExpense(50.0);
        assertEquals(150.0, finance.getNetProfit());
    }

    @Test
    void getTotalRevenue() {
        finance.addRevenue(100.0);
        finance.addRevenue(200.0);
        assertEquals(300.0, finance.getTotalRevenue());
    }

    @Test
    void getTotalExpenses() {
        finance.addExpense(50.0);
        finance.addExpense(30.0);
        assertEquals(80.0, finance.getTotalExpenses());
    }

    @Test
    void addOrderRev() {
        Order order = new Order();
        order.setOrderRevenue(150.0);
        finance.addOrderRev(order);
        assertEquals(150.0, finance.getTotalRevenue());
    }

    @Test
    void addOrderExp() {
        Order order = new Order();
        order.setOrderExpense(70.0);
        finance.addOrderExp(order);
        assertEquals(70.0, finance.getTotalExpenses());
    }

    @Test
    void setPreviousTime() {
        finance.setPreviousTime(100.0);
        assertEquals(100.0, finance.getPreviousTime());
    }

    @Test
    void reset() {
        finance.addRevenue(200.0);
        finance.addExpense(100.0);
        finance.reset();
        assertEquals(0.0, finance.getTotalRevenue());
        assertEquals(0.0, finance.getTotalExpenses());
    }
}