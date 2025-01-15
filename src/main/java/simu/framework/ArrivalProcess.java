package simu.framework;

import eduni.distributions.*;
import simu.model.EventType;


/**
 * The ArrivalProcess class manages the generation of customer arrivals
 * at a pizzeria based on a normal distribution. The arrival rate is adjusted
 * based on whether it is a discount day and during rush hours.
 */
public class ArrivalProcess {

	private ContinuousGenerator generator;
	private final EventList eventList;
	private double mean;
	private double variance;
	private boolean discountDay;

	/**
	 * Constructs an ArrivalProcess with the specified parameters.
	 *
	 * @param mean         The mean time between arrivals.
	 * @param variance     The variance in the time between arrivals.
	 * @param el           The event list to which generated events are added.
	 * @param discountDay  Flag indicating if it is a discount day.
	 */
	public ArrivalProcess(double mean, double variance, EventList el, boolean discountDay) {
		this.mean = mean;
		this.variance = variance;
		this.discountDay = discountDay;
		this.eventList = el;
	}

	public void setMean(int mean) {
		this.mean = mean;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public boolean isDiscountDay() {
		return discountDay;
	}

	// get adjusted mean for the arrival process based on discount day boolean
    protected double getAdjustedMean() {
		if (discountDay) {
			return mean / 2; // mean is halved during discount day
		} else {
			return mean; // normal mean when it is not discount day
		}
	}

	/**
	 * Determines if the current time is during rush hours.
	 * The rush hours are from 11 AM to 2 PM and from 6 PM to 8 PM.
	 *
	 * @return  A multiplier for adjusting arrival frequency during rush hours (0.33).
	 *          Returns 1.0 during non-rush hours.
	 */
	// The pizzeria is open from 10 AM to 10 PM. Rush hours are from 11 AM to 2 PM and from 6 PM to 8 PM. The clock unit is a minute.
    protected double rushHour() {
		double time = Clock.getInstance().getTime();
		if (time % 720 >= 60 && time % 720 <= 240) {
			return 0.33;
		} else if (time % 720 >= 480 && time % 720 <= 600) {
			return 0.33;
		} else {
			return 1;
		}
	}

	/**
	 * Generates the next arrival event. The type of event and the time between arrivals
	 * is influenced by whether it is a discount day or rush hour.
	 */
	public void generateNext() {
		// mean is adjusted on discount day
		generator = new Normal(getAdjustedMean(), variance); // generator is recreated with adjusted mean

		double choice = Math.random() * 100;
		IEventType type;
		if (choice <= 60 * rushHour()) {
			type = EventType.ARR2; // Takeaway order (takeaway orders occur 3 times less frequently compared to customers during rush hours).
		} else {
			type = EventType.ARR1; // Customer arrives
		} // During rush hour there are 3 times more arrivals.

		// Generate event with adjusted arrival time based on discount or rush hour status
		Event e = new Event(type, Clock.getInstance().getTime() + (generator.sample() * rushHour()), 0);
		eventList.add(e);
	}

	public double getMean() {
		return mean;
	}

	public double getVariance() {
		return variance;
	}
}