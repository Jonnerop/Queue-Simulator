package simu.model;

import java.util.ArrayList;

/**
 * The Waiter class represents a waiter in the simulation, tracking their availability and assigning them to service points.
 * Each waiter has a unique ID and a status indicating whether they are occupied or available for service.
 */
public class Waiter {
    private static int i = 0;
    private final int id;
    private boolean isOccupied;
    private static final ArrayList<Waiter> waiterList = new ArrayList<>();

    public Waiter() {
        i++;
        id = i;
        isOccupied = false;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public static void addToWaiterList(Waiter waiter) {
        waiterList.add(waiter);
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public static ArrayList<Waiter> getWaiterList() {
        return waiterList;
    }

    public static Waiter getAvailableWaiter(ArrayList<Waiter> waiters) { //method to get an available waiter
        for (Waiter waiter : waiters) {
            if (!waiter.isOccupied()) {
                return waiter;
            }
        }
        return null; //return null if no waiter is available
    }
}
