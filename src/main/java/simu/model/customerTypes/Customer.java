package simu.model.customerTypes;

import jakarta.persistence.*;
import simu.framework.*;
import simu.model.customerTypes.AbstractCustomer;

/**
 * Represents a customer in the simulation system.
 * Extends the AbstractCustomer class to provide specific customer behavior.
 */
@Entity
@Table(name = "customers")
public class Customer extends AbstractCustomer {
	private static int customerAmount = 0;
	private static int customersServed = 0;
	private static double averageThroughputTime = 0;
	private static double totalThroughputTime = 0;
	private String type;

	/**
	 * Constructor to initialize a new customer, assign an ID,
	 * and record their arrival time.
	 */
	public Customer(){
		super();
		type = "Customer";
		customerAmount++;
		arrivalTime = Clock.getInstance().getTime();
		Trace.out(Trace.Level.INFO, "New customer " + id + " arrived at "+arrivalTime);
	}

	@Override
	public double getExitTime() {
		return exitTime;
	}
    @Override
	public void setExitTime(double exitTime) {
		this.exitTime = exitTime;
	}
    @Override
	public double getArrivalTime() {
		return arrivalTime;
	}
    @Override
	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	@Override
	public void report(){
		Trace.out(Trace.Level.INFO, "\nCustomer "+id+ " ready! ");
		Trace.out(Trace.Level.INFO, "Customer "+id+ " arrived: " +arrivalTime);
		Trace.out(Trace.Level.INFO,"Customer "+id+ " exited: " +exitTime);
		Trace.out(Trace.Level.INFO,"Customer "+id+ " stayed for: " +(exitTime-arrivalTime));
		totalThroughputTime += (exitTime-arrivalTime);
		averageThroughputTime = totalThroughputTime/customerAmount;
		System.out.println("The average throughput time of customers so far "+ averageThroughputTime);
	}

	public String getType() {
		return type;
	}

	public int getId() {
		return super.getId();
	}

	public static int getCustomerAmount() {
		return customerAmount;
	}

	public static void setCustomerAmount(int customerAmount) {
		Customer.customerAmount = customerAmount;
	}

	public static int getCustomersServed() {
		return customersServed;
	}

	public static void setCustomersServed(int customersServed) {
		Customer.customersServed = customersServed;
	}

	public static void updateCustomersServed() {
		customersServed++;
	}

	public static double getAverageThroughputTime() {
		return averageThroughputTime;
	}

	public static double getTotalThroughputTime() {
		return totalThroughputTime;
	}

	public static void setTotalThroughputTime(double totalThroughputTime) {
		Customer.totalThroughputTime = totalThroughputTime;
	}


}