package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The PizzaServicePoint class represents a service point where pizzas are processed,
 * including being placed in the oven and taken out after preparation. It handles the queue
 * of pizza orders and tracks the availability of oven slots.
 */

public class PizzaServicePoint extends ServicePoint{

    private final LinkedList<Order> queue = new LinkedList<>();
    private final ContinuousGenerator generator;
    private final EventList eventList;
    private final EventType typeToBeScheduled;
    private static final boolean[] ovenList = new boolean[3];

    /**
     * Constructs a PizzaServicePoint with the given generator, event list, and event type.
     *
     * @param generator The service time generator.
     * @param eventList The event list to add new events after service.
     * @param type The event type to be scheduled after service completion.
     */
    public PizzaServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
        super(generator, eventList, type);
        this.generator = generator;
        this.eventList = eventList;
        this.typeToBeScheduled = type;
    }

    @Override
    public void addOrderToQueue(Order p){
        queue.add(p);
    }

    @Override
    public Order pullOrderFromQueue(){  // Poistetaan pizza
        occupied = false;
        return queue.poll();
    }


    /**
     * Starts the service for the next pizza in the queue and schedules a new event
     * for when the service is completed.
     *
     * @param i The identifier for the service point.
     * @param exit The time when the service starts.
     */
    @Override
    public void startService(int i, double exit){
        serviceStartTime = exit;
        assert queue.peek() != null;
        occupied = true;
        double servicetime = generator.sample();
        eventList.add(new Event(typeToBeScheduled,Clock.getInstance().getTime()+servicetime, i));
    }

    @Override
    public boolean isOccupied(){
        return occupied;
    }

    @Override
    public boolean isInQueue(){
        return !queue.isEmpty();
    }

    public static boolean[] getOvenList() {
        return ovenList;
    }

    public static void putPizzaInOven() { // True if slot is reserved, false if available
        for (int i = 0; i < ovenList.length; i++) {
            if (!ovenList[i]) {
                ovenList[i] = true;
                break;
            }
        }
    }

    public static void removePizzaFromOven() { // True if slot is reserved, false if available
        for (int i = 0; i < ovenList.length; i++) {
            if (ovenList[i]) {
                ovenList[i] = false;
                break;
            }
        }
    }

}
