package simu.model;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.EventList;
import simu.framework.Trace;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServicePointTest {
    private PizzaServicePoint pizzaServicePoint;
    private Order order;

    @BeforeEach
    void setUp() {
        Trace.setTraceLevel(Trace.Level.INFO);
        ContinuousGenerator generator = new Normal(10, 2);
        EventList eventList = new EventList();
        pizzaServicePoint = new PizzaServicePoint(generator, eventList, EventType.ARR1);
        order = new Order();
    }

    @Test
    void addOrderToQueue() {
        pizzaServicePoint.addOrderToQueue(order);
        assertTrue(pizzaServicePoint.isInQueue());
    }

    @Test
    void pullOrderFromQueue() {
        pizzaServicePoint.addOrderToQueue(order);
        Order pulledOrder = pizzaServicePoint.pullOrderFromQueue();
        assertEquals(order, pulledOrder);
        assertFalse(pizzaServicePoint.isInQueue());
    }

    @Test
    void startService() {
        pizzaServicePoint.addOrderToQueue(order);
        pizzaServicePoint.startService(1, 0);
        assertTrue(pizzaServicePoint.isOccupied());
    }

    @Test
    void isOccupied() {
        assertFalse(pizzaServicePoint.isOccupied());
        pizzaServicePoint.addOrderToQueue(order);
        pizzaServicePoint.startService(1, 0);
        assertTrue(pizzaServicePoint.isOccupied());
    }

    @Test
    void isInQueue() {
        assertFalse(pizzaServicePoint.isInQueue());
        pizzaServicePoint.addOrderToQueue(order);
        assertTrue(pizzaServicePoint.isInQueue());
    }

    @Test
    void getOvenList() {
        boolean[] ovenList = PizzaServicePoint.getOvenList();
        assertEquals(3, ovenList.length);
        for (boolean slot : ovenList) {
            assertFalse(slot);
        }
    }

    @Test
    void putPizzaInOven() {
        PizzaServicePoint.putPizzaInOven();
        boolean[] ovenList = PizzaServicePoint.getOvenList();
        assertTrue(ovenList[0]); // tää pysyy falsena, vaikka pitäis olla true
    }

    @Test
    void removePizzaFromOven() {
        PizzaServicePoint.putPizzaInOven();
        PizzaServicePoint.removePizzaFromOven();
        boolean[] ovenList = PizzaServicePoint.getOvenList();
        assertFalse(ovenList[0]);
    }
}