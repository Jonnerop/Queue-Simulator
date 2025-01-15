package simu.model.customerTypes;

import jakarta.persistence.*;
import simu.framework.Clock;
import simu.model.Order;

/**
 * Represents an abstract customer in the simulation system.
 * This class uses inheritance to allow different types of customers.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "Customers")
public abstract class AbstractCustomer {
    protected double arrivalTime;
    protected double exitTime;
    @Id
    protected int id;
    private static int i = 1;
    protected static long sum = 0;
    @Column(name = "`table`")
    private int table;
    @OneToOne
    private Order order;
    private double totalTimeInQueue = 0;
    private double queueStartTime = 0;
    private static double AllCustomerQueueTime = 0;
    private static int customersArrived = 0;
    private static int customersServed = 0;
    private boolean hasEaten;

    /**
     * Constructor to initialize the arrival time and assign a unique ID to the customer.
     */
    public AbstractCustomer(){
        arrivalTime = Clock.getInstance().getTime();
        id = i++;
        customersArrived++;
        hasEaten = false;
    }

    public double getExitTime() {
        return exitTime;
    }

    public void setExitTime(double exitTime) {
        this.exitTime = exitTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public abstract void report();

    public abstract String getType();

    public void setHasEaten(boolean hasEaten) {
        this.hasEaten = hasEaten;
    }

    public boolean isHasEaten() {
        return hasEaten;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public static void setI(int i) {
        AbstractCustomer.i = i;
    }

    /**
     * Records the start time of queuing for the customer.
     *
     * @param start the start time of the queue
     */
    public void enterQueue(double start) {
        queueStartTime = start;
    }

    /**
     * Records the exit time of the queue and updates the total time spent in queue.
     *
     * @param end the end time of the queue
     */
    public void exitQueue(double end) {
        double queueTime = end - queueStartTime;
        totalTimeInQueue += queueTime;
        AbstractCustomer.AllCustomerQueueTime += queueTime;
    }

    public double getTotalTimeInQueue() {
        return totalTimeInQueue;
    }

    /**
     * Gets the total number of customers that have arrived,
     * both takeaway and local.
     *
     * @return the total number of customers that have arrived
     */
    public static int getBothArrived() {
        return customersArrived;
    }

    public static int getBothServed() {
        return customersServed;
    }

    public static void updateBothServed() {
        customersServed++;
    }
}
