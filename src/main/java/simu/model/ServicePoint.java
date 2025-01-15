package simu.model;

import simu.framework.*;
import java.util.ArrayList;
import java.util.LinkedList;
import eduni.distributions.ContinuousGenerator;
import simu.model.customerTypes.AbstractCustomer;

/**
 * The ServicePoint class represents a service point in the simulation, where customers wait in a queue
 * to be served. It tracks the state of the service point, manages the queue of customers, and handles
 * service times using a continuous random generator. This class also integrates with the event list
 * to schedule events once service is completed.
 */
public class ServicePoint {
	/*
	 *in the final version, there may be multiple identical service points (because there are many tables).
	 *whenever a service point (that requires a waiter) is used, a waiter is assigned to it.
	 *the list of waiters is checked to see if there are available waiters, rather than checking if the service point is free.
	 */
	private final LinkedList<AbstractCustomer> queue = new LinkedList<>();
	private final LinkedList<AbstractCustomer> eaters = new LinkedList<>();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType typeToBeScheduled;
	private static final ArrayList<Waiter> waiterList = Waiter.getWaiterList();
	private Waiter waiter;
	protected boolean occupied = false;
	private static int i = 0;
	private final int id;
	private static double totalBusyTime = 0;
	private double busyTime = 0;
	protected double serviceStartTime;

	/**
	 * Constructs a ServicePoint with the given service time generator, event list, and event type.
	 *
	 * @param generator The generator to determine service times.
	 * @param eventList The list of scheduled events.
	 * @param type The type of event to be scheduled after service completion.
	 */
	public ServicePoint(ContinuousGenerator generator, EventList eventList, EventType type) {
		this.eventList = eventList;
		this.generator = generator;
		this.typeToBeScheduled = type;
		id = i++;
	}

	public void addToQueue(AbstractCustomer c, double start){   //queue's first customer is always in service
		queue.add(c);
		c.enterQueue(start);
	}

	public AbstractCustomer pullFromQueue(){  //remove customer from queue
		occupied = false;
		releaseWaiter();
		return queue.poll();
	}

	public int getId() {
		return id;
	}

	/**
	 * Starts the service for the customer at the front of the queue, assigns a waiter, and schedules
	 * a new event for when the service is completed.
	 *
	 * @param i The identifier for the service event.
	 * @param exit The time when the service starts.
	 */
	public void startService(int i, double exit) {
		serviceStartTime = exit;
		assert queue.peek() != null;
		queue.peek().exitQueue(exit);
		Trace.out(Trace.Level.INFO, "Starting service for customer " + queue.peek().getId());
		assignWaiter();
		occupied = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(typeToBeScheduled,Clock.getInstance().getTime()+serviceTime, i));
	}

	public void startEating(AbstractCustomer c) {
		eaters.add(c);
		double serviceTime = generator.sample();
		eventList.add(new Event(typeToBeScheduled,Clock.getInstance().getTime()+serviceTime, -1));
	}

	public AbstractCustomer finishEating() {
		return eaters.poll();
	}

    // check if the service point is occupied
	public boolean isOccupied(){
        return !isWaiterAvailable() || occupied;
    }

	// check if there are customers in the queue
	public boolean isInQueue() {
		return !queue.isEmpty();
	}

	public int getQueueSize() {
		return queue.size();
	}

	public boolean isWaiterAvailable() {
        return Waiter.getAvailableWaiter(waiterList) != null;
    }

	protected void assignWaiter() { //assign waiter
			waiter = Waiter.getAvailableWaiter(waiterList);
			if (waiter != null) {
				waiter.setOccupied(true);
				System.out.println("Assigned waiter " + waiter.getId());
			}
	}

	protected void releaseWaiter() { //release waiter
		if (waiter != null) {
			System.out.println("Released waiter: " + waiter.getId());
			waiter.setOccupied(false);
			waiter = null;
		}
	}

	// report the exit of a customer from the system
	public void report() {
		Trace.out(Trace.Level.INFO, "Customer " + queue.peek().getId() + " exits system.");
	}

	public void addOrderToQueue(Order p) {
	}

	public Order pullOrderFromQueue() {
		return null;
	}

	public void addGroupToQueue(AbstractCustomer[] group, double start){}

	public AbstractCustomer[] pullGroupFromQueue(){
		return null;
	}

	public AbstractCustomer getCustomerFromQueue() {
		return queue.peek();
	}

	public double getBusyTime() {
		return busyTime;
	}

	public void updateBusyTime(double serviceStopTime) {
		busyTime += serviceStopTime - serviceStartTime;
		totalBusyTime += busyTime;
	}

	public static double getTotalBusyTime() {
		return totalBusyTime;
	}

	public static void setTotalBusyTime(double totalBusyTime) {
		ServicePoint.totalBusyTime = totalBusyTime;
	}
}
