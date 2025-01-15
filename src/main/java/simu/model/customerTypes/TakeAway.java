package simu.model.customerTypes;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import simu.framework.Trace;

/**
 * The TakeAway class represents a takeaway order in the simulation.
 * It extends the AbstractCustomer class and tracks statistics related to takeaways,
 * such as the total number of takeaways, the number of takeaways served, and the average throughput time.
 */
@Entity
@Table(name = "takeaways")
public class TakeAway extends AbstractCustomer {
    private static int takeAwayAmount = 0;
    private static int takeAwaysServed = 0;
    private static double averageThroughputTime = 0;
    private static double totalThroughputTime = 0;
    private final String type;

    public TakeAway() {
        super();
        type = "Takeaway";
        takeAwayAmount++;
        Trace.out(Trace.Level.INFO, "New takeaway order" + id + " arrived at " + arrivalTime);
    }

    /**
     * Generates a report for the takeaway order, including its ID, arrival time, exit time, and total processing time.
     * Updates the total and average throughput times.
     */
    @Override
    public void report(){
        Trace.out(Trace.Level.INFO, "\nTakeaway "+ id + " ready! ");
        Trace.out(Trace.Level.INFO, "Takeaway "+id+ " arrived: " +arrivalTime);
        Trace.out(Trace.Level.INFO,"Takeaway "+id+ " left: " +exitTime);
        Trace.out(Trace.Level.INFO,"Takeaway "+id+ " took to process: " +(exitTime-arrivalTime));
        totalThroughputTime += (exitTime-arrivalTime);
        averageThroughputTime = totalThroughputTime/takeAwayAmount;
        System.out.println("Takeaway throughput average time: "+ averageThroughputTime);
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return super.getId();
    }

    public static int getTakeAwayAmount() {
        return takeAwayAmount;
    }

    public static int getTakeAwaysServed() {
        return takeAwaysServed;
    }

    public static void setTakeAwayAmount(int takeAwayAmount) {
        TakeAway.takeAwayAmount = takeAwayAmount;
        System.out.println("Takeaway amount: "+takeAwayAmount);
    }

    public static void setTakeAwaysServed(int takeAwaysServed) {
        TakeAway.takeAwaysServed = takeAwaysServed;
    }

    public static void updateTakeAwaysServed() {
        takeAwaysServed++;
    }

    public static double getAverageThroughputTime() {
        return averageThroughputTime;
    }

    public static double getTotalThroughputTime() {
        return totalThroughputTime;
    }

    public static void setTotalThroughputTime(double totalThroughputTime) {
        TakeAway.totalThroughputTime = totalThroughputTime;
    }

}