package simu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WaiterTest {
    private Waiter waiter;

    @BeforeEach
    void setUp() {
        waiter = new Waiter();
        Waiter.getWaiterList().clear();
    }

    @Test
    void getId() {
        assertTrue(waiter.getId() > 0);
    }

    @Test
    void isOccupied() {
        assertFalse(waiter.isOccupied());
        waiter.setOccupied(true);
        assertTrue(waiter.isOccupied());
    }

    @Test
    void addToWaiterList() {
        Waiter.addToWaiterList(waiter);
        assertTrue(Waiter.getWaiterList().contains(waiter));
    }

    @Test
    void setOccupied() {
        waiter.setOccupied(true);
        assertTrue(waiter.isOccupied());
        waiter.setOccupied(false);
        assertFalse(waiter.isOccupied());
    }

    @Test
    void getWaiterList() {
        Waiter.addToWaiterList(waiter);
        ArrayList<Waiter> waiterList = Waiter.getWaiterList();
        assertNotNull(waiterList);
        assertEquals(1, waiterList.size());
        assertEquals(waiter, waiterList.get(0));
    }

    @Test
    void getAvailableWaiter() {
        Waiter.addToWaiterList(waiter);
        Waiter availableWaiter = Waiter.getAvailableWaiter(Waiter.getWaiterList());
        assertNotNull(availableWaiter);
        assertEquals(waiter, availableWaiter);

        waiter.setOccupied(true);
        availableWaiter = Waiter.getAvailableWaiter(Waiter.getWaiterList());
        assertNull(availableWaiter);
    }
}